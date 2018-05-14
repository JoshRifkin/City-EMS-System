package rifkin.Algorithms.Spring2017.HW2;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BreadthFirstSearch {
	
	private HashMap<Vertex, Boolean> marked;
	private HashMap<Vertex, Vertex> lastAddress;
	private HashMap<Vertex, Integer> distTo;
	private final Vertex source;
	
	public BreadthFirstSearch(Vertex v) {
		this.source = v;
		
		this.marked = new HashMap<Vertex, Boolean>();
		this.lastAddress = new HashMap<Vertex, Vertex>();
		this.distTo = new HashMap<Vertex, Integer>();
		
		bfs(v);
	}

	private void bfs(Vertex source) {
		Queue<Vertex> toVisit = new LinkedList<Vertex>();
		marked.put(source, true);
		distTo.put(source, 0);
		toVisit.add(source);
		
		while(!toVisit.isEmpty()) { //While there are still nodes to visit
			Vertex curr = toVisit.poll();
			for(Edge edge : curr.getAdjVertices()) {
				if(edge.isBroken()) continue;
				Vertex next = edge.getNextVertex(curr);
				if(!marked.containsKey(next)) {
					lastAddress.put(next, curr);
					marked.put(next, true);
					distTo.put(next, distTo.get(curr) + 1);
					toVisit.offer(next);
				}
			}
		}
		
	}
	
	
	public boolean hasPathTo(Vertex destination) {
		return marked.get(destination);
	}
	
	public Iterable<Vertex> pathTo(Vertex destination) {
		if (!hasPathTo(destination)) return null;
		Stack<Vertex> pathTo = new Stack<Vertex>();
		for(Vertex last = destination; !last.equals(source); last = lastAddress.get(last)) {
			pathTo.push(last);
		}
		pathTo.push(source);
		
		return pathTo;
		
	}
	
	public Integer getDistTo(Vertex v) {
		return distTo.get(v);
	}


	public Integer getDistTo(HashSet<Vertex> vertices) {
		Integer minDist = (int) Double.POSITIVE_INFINITY;
		for(Vertex v : vertices) {
			if(distTo.containsKey(v) && distTo.get(v) < minDist) minDist = distTo.get(v);
		}
		return minDist;
	}
	
	public Vertex getClosestHelp(Vertex start) {
		Queue<Vertex> toVisit = new LinkedList<Vertex>();
		this.marked = new HashMap<Vertex, Boolean>();
		this.distTo = new HashMap<Vertex, Integer>();
		marked.put(start, true);
		distTo.put(start, 0);
		toVisit.add(start);
		while(!toVisit.isEmpty()) {
			Vertex curr = toVisit.poll();
			for(Edge edge : curr.getAdjVertices()) {
				Vertex next = edge.getNextVertex(curr);
				if((next.hasEMS() || next.isHospital()) &&
						(!next.getComponent().equals(start.getComponent()))) {
					return next;
				}
				if(!marked.containsKey(next)) {
					lastAddress.put(next, curr);
					marked.put(next, true);
					distTo.put(next, distTo.get(curr) + 1);
					toVisit.offer(next);
				}
			}
		}
		return start;
		
	}
	
}
