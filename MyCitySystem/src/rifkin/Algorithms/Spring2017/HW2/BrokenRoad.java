package rifkin.Algorithms.Spring2017.HW2;

import java.util.HashSet;

public class BrokenRoad {
	
	private Vertex start, end;
	private boolean discovered, assigned;
	private HashSet<Vertex> brokenAddresses;
	private HashSet<Edge> brokenEdges;
	
	public BrokenRoad(Vertex start, Vertex end) {
		this.start = start;
		this.end = end;
		this.discovered = false;
		this.assigned = false;
		this.brokenAddresses = new HashSet<Vertex>();
		this.brokenEdges = new HashSet<Edge>();
	}
	
	public boolean isDiscovered() {
		return discovered;
	}
	
	public void setDiscovered() {
		discovered = true;
	}
	
	public void assign() {
		this.assigned = true;
	}
	
	public boolean isAssigned() {
		return this.assigned;
	}
	
	public void addBrokenAddress(Vertex v) {
		brokenAddresses.add(v);
	}
	
	public void addBrokenEdge(Edge e) {
		brokenEdges.add(e);
	}
	
	public boolean containsAddress(Vertex v) {
		return brokenAddresses.contains(v);
	}
	
	public boolean containsEdge(Edge e) {
		return brokenEdges.contains(e);
	}
	
	public HashSet<Vertex> getVertices() {
		return this.brokenAddresses;
	}
	
	public HashSet<Edge> getEdges() {
		return this.brokenEdges;
	}
	
	public Vertex getStart() { return this.start; }
	
	public Vertex getEnd() { return this.end; }
}
