package rifkin.Algorithms.Spring2017.HW2;

import java.util.ArrayList;

public class ConnectedComponent implements Comparable<ConnectedComponent> {
	
	private Integer numEMS, numHospitals;
	private ArrayList<Vertex> ccVertices;
	
	public ConnectedComponent() {
		this.numEMS = 0;
		this.numHospitals = 0;
		this.ccVertices = new ArrayList<Vertex>();
	}
	
	public void addVertex(Vertex v) {
		ccVertices.add(v);
		v.setComponent(this);
	}
	
	public ArrayList<Vertex> getV() {
		return this.ccVertices;
	}
	
	public boolean containsV(Vertex v) {
		return ccVertices.contains(v);
	}
	
	public Integer totalHelp() {
		return numEMS + numHospitals;
	}
	
	public void addEMS() { numEMS++; }
	
	public void addHospital() { numHospitals++; }
	
	@Override
	public int compareTo(ConnectedComponent cc2) {
		return this.totalHelp() - cc2.totalHelp();
	}
}
