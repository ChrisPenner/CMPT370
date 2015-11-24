package models;

import java.util.LinkedList;

public class Cell {
	protected LinkedList<Robot> occupants = new LinkedList<Robot>();
	protected boolean valid = false; // Cells are initially just placeholders
	
	public Cell() {
		
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public LinkedList<Robot> getOccupants() {
		return occupants;
	}
}
