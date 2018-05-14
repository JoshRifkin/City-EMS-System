package rifkin.Algorithms.Spring2017.HW2;

public class EMSUnit {

	private Vertex currLocation;
	
	public EMSUnit(Vertex location) {
		this.currLocation = location;
	}
	
	public void move(Vertex newLocation) {
		this.currLocation.moveEMS();
		this.currLocation = newLocation;
		newLocation.placeEMS();
	}
	
	public Vertex getLocation() {
		return currLocation;
	}
}
