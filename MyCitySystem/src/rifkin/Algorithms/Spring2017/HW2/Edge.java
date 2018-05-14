package rifkin.Algorithms.Spring2017.HW2;

public class Edge {
	
	private Vertex v1, v2;
	private boolean broken;
	
	public Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
		broken = false;
	}
	
	public Vertex getNextVertex(Vertex curr) {
		if(v1.equals(curr)) return v2;
		else return v1;
	}
	
	public void breakRoad(){
		broken = true;
	}
	
	public void fixRoad() {
		broken = false;
	}
	
	public boolean isBroken() {
		return broken;
	}
	
	
}
