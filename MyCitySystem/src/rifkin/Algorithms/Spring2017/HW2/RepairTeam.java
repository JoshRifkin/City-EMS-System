package rifkin.Algorithms.Spring2017.HW2;

public class RepairTeam {
	
	private Integer time;
	private boolean onAssignment;
	private BrokenRoad assignment;
	
	public RepairTeam() {
		this.time = 0;
		this.onAssignment = false;
	}
	
	public void assignTeam(BrokenRoad br) {
		this.assignment = br;
		this.onAssignment = true;
	}
	
	public void finishRepair() {
		this.assignment = null;
		this.onAssignment = false;
	}
	
	public boolean isBusy() {
		return this.onAssignment;
	}
	
	public BrokenRoad getAssignment() {
		return this.assignment;
	}
	
	public void incrementTime() {
		this.time++;
	}
	
	public Integer getTime() {
		return this.time;
	}
	
	
}
