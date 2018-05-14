package rifkin.Algorithms.Spring2017.HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Parser {

	private CityGraph myCity;
	private Scanner sc;
	private String[] currLine;
	private boolean buildingCity, events;
	
	public Parser(File file) throws FileNotFoundException {
		buildingCity = true;
		events = false;
		myCity = new CityGraph();
		sc = new Scanner(file);
	}
	
	protected void parse() {
		while(sc.hasNext()) {
			parseLine(sc.nextLine());
		}
		sc.close();
	}
	
	private void parseLine(String line) {
		currLine = line.split("\\s+");
		
		if(buildingCity) {
			if(line.contains(",")) parseCSV(currLine); //Case 1: Our line is an edge or intersection.
			else if (line.toLowerCase().startsWith("ems")) parseEMS(currLine); //Case 2: Our line is an EMS Unit.
			else if (line.toLowerCase().startsWith("hospital")) parseHospital(currLine); //Case 3: Our line is a hospital
			else if(isInteger(currLine[0]) && isInteger(currLine[1])) parseAddress(currLine); //Case 4: Our line must be an address
			else {
				buildingCity = false;
				events = true;
				myCity.connectRoads();
			}
		}
		if(events) {
			if(currLine[0].equalsIgnoreCase("Begin911CallGroup")) parseCallGroup(); //Event 1: Our line is a 911 call group
			else if(currLine[0].equalsIgnoreCase("brokenRoad")) parseBrokenRoad(currLine); //Event 2: Our line is a broken road
			else if(line.equalsIgnoreCase("repair team")) parseRepairTeam(); //Event 3: Our line is a repair team
			else parseSingleCall(currLine); //Event 4: Our line must be a single 911 call
		}
		
	}

	private void parseCSV(String[] line) {
		String[] csvString = line;
		if(csvString.length == 4){
			//must be an intersection
			myCity.addIntersection(csvString);
		}
		else if (csvString.length == 2){
			//must be a new edge
			myCity.addEdge(csvString);
		}
	}

	private void parseEMS(String[] currLine) {
		myCity.addEMSUnit(Integer.parseInt(currLine[1]));
	}
	
	private void parseHospital(String[] currLine) {
		myCity.addHospital(Integer.parseInt(currLine[1]));
	}
	
	private void parseAddress(String[] line) {
		int ID = Integer.parseInt(line[0]);
		int houseNum = Integer.parseInt(line[1]);
		String streetName = line[2];
		for(int i = 3; i < line.length; i++) {
			streetName += " " + line[i];
		}
		
		myCity.addNewAddress(ID, houseNum, streetName);
		
	}
		
	private void parseCallGroup() {
		myCity.startCallGroup();
		
		while(!currLine[0].toLowerCase().contains("end911callgroup")) {
			parseSingleCall(currLine);
			currLine = this.sc.nextLine().split("\\s+");
		}
		
		myCity.endCallGroup();
		myCity.dispatch();
	}
	
	private void parseSingleCall(String[] currLine) {
		String street = currLine[0];
		for(int i = 1; i < currLine.length-1; i++) {
			street += " " + currLine[i];
		}
		int severity = Integer.parseInt(currLine[currLine.length-1]);
		
		myCity.addCall(street, severity);
	}
	
	private void parseBrokenRoad(String[] currLine) {
		String street = currLine[3];
		for (int i = 4; i < currLine.length; i++) {
			street += currLine[i];
		}
		String start = currLine[1] + " " + street;
		String end = currLine[2] + " " + street;
		
		myCity.addBrokenRoad(start, end, street);
	}
		
	private void parseRepairTeam() {
		myCity.addRepairTeam();
	}
	
	/**
	 * Checks to see if a given string contains an int value.
	 * Source: http://stackoverflow.com/a/5439547
	 * (Based on my research, the method I employed is not a very efficient one,
	 * 	however, for simplicity and readability, I used this method. Faster
	 * 	methods may have included regex or Apache's Number Utils Package.)
	 * @param string
	 * @return boolean value representing whether the string given contains an int
	 */
	private boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
		} catch(NumberFormatException nfe) {
			return false;
		} catch(NullPointerException npe) {
			return false;
		}
		return true;
	}
}
