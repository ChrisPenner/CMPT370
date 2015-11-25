package models;

import java.util.LinkedList;


public class GameBoard {
	private LinkedList<Robot>[] teams;
	private Cell[][] cells;
	private int diameter;
	
//	newGameBoard(teams): Initializes a new GameBoard with the selected teams
//	loaded
	@SuppressWarnings("unchecked")
	public GameBoard(LinkedList<Robot>[] teams, int sideLength) {
		initializeBoard(sideLength);
		this.teams = (LinkedList<Robot>[]) new LinkedList<?>[6];
		for(int i = 0; i < teams.length; i++) {
			if(teams[i] == null) {
				this.teams[i] = new LinkedList<Robot>();
			} else {
				this.teams[i] = teams[i];
			}
		}
	}
	
	private boolean isCellInArray(Coord c) {
		if(c.x < 0 || c.x >= diameter || c.y < 0 || c.y >= diameter) {
			return false;
		}
		if(cells[c.x][c.y] == null) {
			cells[c.x][c.y] = new Cell();
		}
		
		return true;
	}
	
	/**
	 * Initializes the board to have a specified side length
	 * All empty spaces are left null
	 * Pattern used: row 0. 1: up. 2: down. 3: up. 4: down...
	 */
	private void initializeBoard(int sideLength) {
		diameter = sideLength * 2 - 1;
		cells = new Cell[diameter][diameter];
		
		for(int x = 0; x < diameter; x++) {
			for(int y = 0; y < diameter; y++) {
				cells[x][y] = new Cell();
			}
		}
		
		// Start from middle row top, go downleft and downright
		init5(new Coord(sideLength - 1, 0));
		init3(new Coord(sideLength - 1, 0));
		// Start from middle row bottom, go upleft and upright
		init6(new Coord(sideLength - 1, diameter-1));
		init2(new Coord(sideLength - 1, diameter-1));
		
		
		// Fill in the center of the grid
		for(int x = 0; x < diameter; x++) {
			boolean reached = false;
			for(int y = 0; y < diameter; y++) {
				if(cells[x][y] == null) {
					System.out.println(x + " " + y);
				}
				if(cells[x][y].valid) {
					if(!reached) { // If it has not yet started filling in
						reached = true;
					} else {
						break;
					}
				} else if (reached) { // If currently filling in
					cells[x][y].valid = true;
				}
			}
		}
	}
	
	private void init2(Coord c) {
		if(isCellInArray(c)){
			cells[c.x][c.y].valid = true;
			c.move2();
			init2(c);
		}
	}
	
	private void init3(Coord c) {
		if(isCellInArray(c)){
			cells[c.x][c.y].valid = true;
			c.move3();
			init3(c);
		}
	}
	
	private void init5(Coord c) {
		if(isCellInArray(c)){
			cells[c.x][c.y].valid = true;
			c.move5();
			init5(c);
		}
	}
	
	private void init6(Coord c) {
		if(isCellInArray(c)){
			cells[c.x][c.y].valid = true;
			c.move6();
			init6(c);
		}
	}
	
//	GB.addRobot(file): Adds a robot to the set of loaded robots by reading in a file.
	public void addRobot(String file, int team) {
		Robot robot = Robot.fromJson(file);
		robot.teamNumber = team;
		teams[team - 1].add(robot);
		System.out.println(robot.name + " added.");
	}
	
//	GB.addTeam(team): Adds all robots specified in a team file.
	public void addTeam(String file, int team) {
		
	}
	
//	GB.setPosition(robot, coord): Updates the position of a robot to the given
//	coordinate.
	public void setPosition(Robot r, Coord c) throws Exception {
		if(!isCellInArray(c)) throw new Exception("Cell not in array");
		cells[r.c.x][r.c.y].occupants.remove(r);
		cells[c.x][c.y].occupants.add(r);
	}
	
//	GB.setHealth(robot, int): Updates the health of a robot to the given level.
	public void setHealth(Robot r, int health) {
		r.setHealth(health);
	}
	
//	GB.getCells(): Returns an matrix such that matrix[x][y] contains the cell at
//	coords(x, y)
	public Cell[][] getCells() {
		return cells;
	}
	
//	GB.getCell(coord): Returns the cell object located at coord.
	public Cell getCell(Coord c) {
		return cells[c.x][c.y];
	}
}
