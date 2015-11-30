package models;

import java.util.LinkedList;
import java.util.Random;

public class Cell {
	protected LinkedList<Robot> occupants = new LinkedList<Robot>();
	protected boolean valid = false; // Cells are initially just placeholders
	protected int difficulty;
	
	public Cell() {
		Random r = new Random();
		difficulty = Math.abs(r.nextInt()) % 3;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public LinkedList<Robot> getOccupants() {
		return occupants;
	}
	
	public int getDifficulty(){
		return difficulty;
	}
}
