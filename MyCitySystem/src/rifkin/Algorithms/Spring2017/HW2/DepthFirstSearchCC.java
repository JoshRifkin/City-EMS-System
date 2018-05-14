package rifkin.Algorithms.Spring2017.HW2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DepthFirstSearchCC {
	
	private Integer componentCount;
	private HashMap<Vertex, Boolean> marked;
	private HashMap<Vertex, ConnectedComponent> ccIDMap;
//	private HashMap<Integer, Integer> hospitals; //Maps the CC ID number to the number of hospitals in that CC
//	private HashMap<Integer, Integer> emsUnits; //Maps the CC ID number to the number of ems units in that CC
	private ConnectedComponent curr;
	private ArrayList<ConnectedComponent> components;
	
	public DepthFirstSearchCC(Collection<Vertex> collection) {
		this.componentCount = 1; //There is at least one component in every Graph
		this.marked = new HashMap<Vertex, Boolean>();
		this.ccIDMap = new HashMap<Vertex, ConnectedComponent>();
//		this.hospitals = new HashMap<Integer, Integer>();
		this.components = new ArrayList<ConnectedComponent>();
		curr = new ConnectedComponent();
//		hospitals.put(componentCount, 0);
//		emsUnits.put(componentCount, 0);
		
		for(Vertex address : collection) {
			if(!marked.containsKey(address)){
				dfs(address);
				components.add(curr);
				this.componentCount++;
				curr = new ConnectedComponent();
//				hospitals.put(componentCount, 0);
//				emsUnits.put(componentCount, 0);
			}
		}
	}

	private void dfs(Vertex address) {
		marked.put(address, true);
		ccIDMap.put(address, curr);
		curr.addVertex(address);
		if(address.isHospital()) {
//			Integer count = hospitals.get(componentCount);
//			hospitals.put(componentCount, count+1);
			curr.addHospital();
		}
		if(address.hasEMS()) {
//			Integer count = emsUnits.get(componentCount);
//			emsUnits.put(componentCount, count+1);
			curr.addEMS();
		}
		for(Edge edge : address.getAdjVertices()) {
			if(!edge.isBroken()) {
				Vertex next = edge.getNextVertex(address);
				dfs(next);
			}
		}
	}
	
	public Integer getCCCount() { return this.componentCount; }
	
//	public Integer getTotal(Integer CC) {
//		return hospitals.get(CC) + emsUnits.get(CC);
//	}
	
	public ArrayList<ConnectedComponent> getComponents() {
		return this.components;
	}
}
