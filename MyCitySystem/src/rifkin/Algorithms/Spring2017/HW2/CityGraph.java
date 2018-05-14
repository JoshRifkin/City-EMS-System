package rifkin.Algorithms.Spring2017.HW2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.TreeSet;

public class CityGraph {
	
	private HashMap<Integer, Vertex> idMap; //Maps IDs to their matching Vertex
	private HashMap<String, Vertex> addressMap; //Maps address string to its matching vertex, used for broken roads and 911 calls
	private HashMap<String, TreeSet<Vertex>> roads; //Maps street names to a TreeSet of all Vertices on that street, used for Broken Roads
	private HashSet<RepairTeam> repairTeams;
	private Queue<EMSUnit> emsUnits;
	private Queue<BrokenRoad> brPath;
	private ArrayList<Vertex> hospitals;
	private ArrayList<BrokenRoad> brokenRoads;
	private CallPriorityQueue<Call> calls;
	private CallPriorityQueue<Call> unreachables;
	private boolean callGroup;
	private Comparator<Call> callComp;
	
	class CallComparator implements Comparator<Call> {

		@Override
		public int compare(Call c1, Call c2) {
			return -(c1.compareTo(c2)); //The negative will switch the order of the queue to descending.
		}
		
	}

	
	public CityGraph() {
		this.idMap = new HashMap<Integer, Vertex>();
		this.addressMap = new HashMap<String, Vertex>();
		this.roads = new HashMap<String, TreeSet<Vertex>>();
		this.repairTeams = new HashSet<RepairTeam>();
		this.emsUnits = new LinkedList<EMSUnit>();
		this.brPath = new LinkedList<BrokenRoad>();
		this.hospitals = new ArrayList<Vertex>();
		this.brokenRoads = new ArrayList<BrokenRoad>();
		this.callComp = new CallComparator();
		this.calls = new CallPriorityQueue<Call>(callComp);
		this.unreachables = new CallPriorityQueue<Call>(callComp);
		this.callGroup = false;
	}

	public void addNewAddress(int ID, int houseNum, String streetName) {
		System.out.println("Adding " + houseNum + " " + streetName + " to our city!");
		Vertex vertex = new Vertex(ID, houseNum, streetName);
		
		idMap.put(ID, vertex);
		addressMap.put(vertex.toString(), vertex);
		
		//Check to see if we already have a TS to store this street and all of its house numbers
		if(roads.containsKey(streetName)){ 
			TreeSet<Vertex> ourRoad = roads.get(streetName); //gets the TS that stores our road
			ourRoad.add(vertex); //adds the house number we just parsed to our road
			roads.put(streetName, ourRoad); //adds the road back to our road map
		}
		//We don't have an TS of this street and must make a new one
		else {
			TreeSet<Vertex> ourRoad = new TreeSet<Vertex>(new Comparator<Vertex>() {

				@Override
				public int compare(Vertex v1, Vertex v2) {
					return v1.getHouseNumber().compareTo(v2.getHouseNumber());
				}
							
			}); //makes a new TS representing our street, sorted by vertex's houseNum
			ourRoad.add(vertex); //adds the house number we just parsed to our road
			roads.put(streetName, ourRoad); //adds our new road back to our road map
		}
		
	}

	public void addIntersection(String[] csvString) {
		for(int i = 0; i < csvString.length; i++) {
			for(int j = i + 1; j < csvString.length; i++) {
				createEdge(idMap.get(Integer.parseInt(csvString[i])), idMap.get(Integer.parseInt(csvString[j]))); 
			}
		}
	}

	public void addEdge(String[] csvString) {
		Vertex v1 = idMap.get(Integer.parseInt(csvString[0]));
		Vertex v2 = idMap.get(Integer.parseInt(csvString[1]));
		createEdge(v1, v2);
	}

	private void createEdge(Vertex prev, Vertex curr) {
		System.out.println("Adding an edge between " + prev.toString() + " and " + curr.toString());
		Edge prevEdge = new Edge(prev, curr);
		Edge currEdge = new Edge(curr, prev);
		prev.addEdge(curr, currEdge);
		curr.addEdge(prev, prevEdge);
	}
	
	public void addEMSUnit(int ID) {
		EMSUnit ems = new EMSUnit(idMap.get(ID));
		emsUnits.add(ems);
		ems.getLocation().placeEMS();
	}

	public void addHospital(int ID) {
		Vertex v = idMap.get(ID);
		System.out.println("Building a hospital at " + v.toString());
		hospitals.add(v);
		v.buildHospital();
	}


	public void connectRoads() {
		
		for(String street : roads.keySet()) {
			TreeSet<Vertex> houses = roads.get(street);
			Iterator<Vertex> it = houses.iterator();
			Vertex currHouse = it.next();
			Vertex nextHouse;
			while(it.hasNext()) {
				nextHouse = it.next();
				createEdge(currHouse, nextHouse);
				currHouse = nextHouse;
			}
		}
	}
	
	
	public void addBrokenRoad(String start, String end, String street) {
		Vertex first = addressMap.get(start + " " + street);
		Vertex last = addressMap.get(end + " " + street);
		
		NavigableSet<Vertex> stretchOfBrokenRoad = roads.get(street).subSet(first, true, last, true);
		Iterator<Vertex> it = stretchOfBrokenRoad.iterator();
		BrokenRoad br = new BrokenRoad(first, last);
		Vertex curr = it.next();
		while(it.hasNext()) {
			Vertex next = it.next();
			br.addBrokenAddress(curr);
			Edge e = curr.getEdgeTo(next);
			e.breakRoad();
			br.addBrokenEdge(e);
			curr = next;
			next = it.next();
		}
		
		brokenRoads.add(br);
		
		checkForRepairTeams();
	}
	
	public void addRepairTeam() {
		RepairTeam rt = new RepairTeam();
		System.out.println("New repair team has arrived!");
		repairTeams.add(rt);
		checkForRepairTeams();
	}
	


	public void startCallGroup() { callGroup = true; }

	public void endCallGroup() { callGroup = false; }
	
	public void addCall(String street, int severity) {
		Vertex location = addressMap.get(street);
		Call call = new Call(location, severity);
		calls.add(call);
		
		if(callGroup) return;
		
		dispatch();
	}

	
	public void dispatch() {
		Call filler = new Call(new Vertex(-1, -1, "Empty"), (int) Double.POSITIVE_INFINITY);  //Create filler Call with distance of positive infinity
		int numUnits = emsUnits.size();
		do {
			for(EMSUnit ems : emsUnits) {
				ArrayList<Call> topKCalls = calls.peek(emsUnits.size());
				BreadthFirstSearch bfs = new BreadthFirstSearch(ems.getLocation());
				Call closest = filler;
				for(Call call : topKCalls) {
					if(bfs.hasPathTo(call.getLocation()) && //check if our ems unit has a path to the call -and-
							bfs.getDistTo(closest.getLocation()) > bfs.getDistTo(call.getLocation())) { //check if the distance of that path is shorter than the path to our current closest call
						closest = call; //If it's closer, set it as the closest
					}
				}
				//At this point we have found the closest call to our current EMS Unit
				
				if(!filler.equals(closest)) {
					System.out.println("EMS Unit dispatched from " + ems.getLocation().toString() 
							+ " a call at " + closest.getLocation().toString());
					boolean success = tryRoute(bfs, closest, ems);
					
					if(!success) {
						if(closest.getNumAttempts() >= numUnits) {
							unreachables.add(closest);
							System.out.println("All available EMS units have been unable to reach the 911 call at " + closest.getLocation().toString());
							System.out.println("When more repair teams are available or broken roads are repaired an attempt to reach the call will be made again.");
							calls.remove(closest);
						}
						else {
							closest.addAttempt();
						}
					}
				}
			}
		} while(!calls.isEmpty());
	}

	private boolean tryRoute(BreadthFirstSearch bfs, Call closest, EMSUnit ems) {
		for(Vertex curr : bfs.pathTo(closest.getLocation())) {
			for(BrokenRoad br : brokenRoads) {
				if(br.containsAddress(curr)) { //We've encountered a broken road! EMS unit could not reach the patient.
					Vertex stopped = curr; //Location where the ems unit hit a broken road
					System.out.println("EMS Unit encountered a broken road at " + stopped.toString());
					System.out.println("The broken road spans from " + br.getStart().toString() + " to " + br.getEnd().toString());
					br.setDiscovered();
					ems.move(stopped); 
					emsUnits.add(ems); //Add the EMS Unit back onto the Queue
					checkForRepairTeams();
					return false;
				}
			}
		}
		//The EMS Unit was able to reach the patient!
		ems.move(closest.getLocation());
		System.out.println("Call was resolved successfully!");
		calls.remove(closest);
		emsUnits.add(ems); //Add our ems unit back onto the list
		incrementTime();
		return true;
		
	}

	private void checkForRepairTeams() {
		for(RepairTeam rt : repairTeams) {
			if(!rt.isBusy()) assignRepairs(rt);
		}
	}


	private void assignRepairs(RepairTeam rt) {
		DepthFirstSearchCC dfs = new DepthFirstSearchCC(idMap.values());
		
		BrokenRoad closest = null;
		if(dfs.getCCCount() == 1) { //The entire graph is connected
			Integer closeDist = -1;
			for(Vertex hospital : hospitals) {
				BreadthFirstSearch bfs = new BreadthFirstSearch(hospital);
				for (BrokenRoad br : brokenRoads) {
					Integer distToBR = bfs.getDistTo(br.getVertices());
					if((distToBR > closeDist) && (br.isDiscovered() && !br.isAssigned())) { //Only set closest if this broken road is both discovered, not assigned a repair team, and the closest
						closest = br;
						closeDist = distToBR;
					}
				}
			}
			System.out.println("A repair team has been sent to repair the broken road between " +
					closest.getStart().toString() + " and " + closest.getEnd().toString() + ".");
			closest.assign();
			rt.assignTeam(closest);
		}
		else { //There are multiple components comprising our graph
			ArrayList<ConnectedComponent> components = dfs.getComponents();
			components.sort(new Comparator<ConnectedComponent>() {
				@Override
				public int compare(ConnectedComponent cc1, ConnectedComponent cc2) {
					return cc1.compareTo(cc2);
				}
			});
			ConnectedComponent leastHelp = components.get(0);
			Vertex component = ccAsVertex(leastHelp);
			BreadthFirstSearch ccBFS = new BreadthFirstSearch(component);
			Vertex help = ccBFS.getClosestHelp(component);
			Iterable<Vertex> path = ccBFS.pathTo(help);
			for(BrokenRoad br : brokenRoads) {
				if(br.isDiscovered() && !br.isAssigned()) {
					for(Vertex v : path) {
						if(br.containsAddress(v)) {
							brPath.add(br);
						}
					}
				}
			}
			BrokenRoad curr = brPath.poll();
			System.out.println("A repair team has been sent to repair the broken road between " +
					curr.getStart().toString() + " and " + curr.getEnd().toString() + ".");
			curr.assign();
			rt.assignTeam(curr);
		}
	}
	
	private Vertex ccAsVertex(ConnectedComponent cc) {
		Vertex c = new Vertex();
		for(Vertex v : cc.getV()){
			for(Vertex v2 : v.getConnectedVertices()) {
				if(!cc.containsV(v2)){
					c.addEdge(v2, v.getEdgeTo(v2));
				}
			}
		}
		//We have now created a single vertex representing the entire CC
		return c;
		
	}

	private void incrementTime() {
		if(!repairTeams.isEmpty()) {
			for(RepairTeam rt : repairTeams) {
				if(rt.isBusy()) rt.incrementTime();
				if(rt.getTime() >= emsUnits.size()) {
					BrokenRoad fixed = rt.getAssignment();
					for(Edge e : fixed.getEdges()) {
						e.fixRoad();
					}
					System.out.println("The broken road spanning from " + fixed.getStart().toString() +
							" to " + fixed.getEnd().toString() + " has been repaired!");
					brokenRoads.remove(fixed);
					retryCalls();
				}
			}
		}
	}

	private void retryCalls() {
		System.out.println("Retrying previously unreachable 911 calls.");
		for(Call call : unreachables) {
			calls.add(call);
		}
		dispatch();
	}
	
}
