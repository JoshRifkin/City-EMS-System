package rifkin.Algorithms.Spring2017.HW2;

public class Call implements Comparable<Call> {
	
	private Vertex location;
	private Integer severity, numAttempts;
	
	public Call(Vertex v, int severe) {
		this.location = v;
		this.severity = severe;
		this.numAttempts = 0;
		System.out.println("New 911 call from " + location.toString() + "! Severity is a " + this.severity);
	}
	
	public Vertex getLocation() {
		return location;
	}
	
	public Integer getSeverity() {
		return severity;
	}
	
	public void addAttempt() {
		this.numAttempts++;
	}
	
	public Integer getNumAttempts() {
		return this.numAttempts;
	}
	
	@Override
	public int compareTo(Call call) {
		return this.severity.compareTo(call.severity);
	}
	
}
