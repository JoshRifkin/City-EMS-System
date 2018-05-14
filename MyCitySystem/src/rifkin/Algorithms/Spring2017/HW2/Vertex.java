package rifkin.Algorithms.Spring2017.HW2;


import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class Vertex {

	private int ID, houseNum;
	private String streetName;
	private boolean hospital, hasEMS;
	private ConnectedComponent component;
	private HashMap<Vertex, Edge> adjVertices;

	public Vertex() {
		this.adjVertices = new HashMap<Vertex, Edge>();
	}
	
	public Vertex(int ID, int houseNum, String streetName) {
		this.ID = ID;
		this.houseNum = houseNum;
		this.streetName = streetName;
		this.hospital = false;
		this.hasEMS = false;
		this.component = null;
		this.adjVertices = new HashMap<Vertex, Edge>();
	}
	
	public Integer getHouseNumber() {
		return this.houseNum;
	}
	
	public boolean hasEMS() {
		return this.hasEMS;
	}
	
	public void placeEMS() {
		this.hasEMS = true;
	}
	
	public void moveEMS() {
		this.hasEMS = false;
	}
	
	public void addEdge(Vertex v, Edge e) {
		adjVertices.put(v, e);
	}
	
	public Edge getEdgeTo(Vertex next) {
		return adjVertices.get(next);
	}
	
	public Integer getID() {
		return this.ID;
	}
	
	public void buildHospital() {
		this.hospital = true;
	}
	
	public boolean isHospital() {
		return hospital;
	}
	
	public Collection<Edge> getAdjVertices() {
		return adjVertices.values();
	}
	
	public Set<Vertex> getConnectedVertices() {
		return adjVertices.keySet();
	}
	
	
	@Override
	public String toString() {
		return houseNum + " " + streetName;
	}

	public void setComponent(ConnectedComponent cc) {
		this.component = cc;
	}
	
	public ConnectedComponent getComponent() {
		return this.component;
	}
	

}
