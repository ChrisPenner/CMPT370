package models;

import java.util.LinkedList;
import java.util.Random;

import parser.Token;


public class GameBoard {
	private LinkedList<Robot>[] teams;
	private Cell[][] cells;
	private int diameter;
	private Coord[] teamStart = new Coord[6];
	
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
						if(x == (sideLength - 1) - sideLength / 2) {
							// Halfway between middle and left edge
							// Team 2
							teamStart[1] = new Coord(x,y);
						}
						if(x == (sideLength - 1) + sideLength / 2) {
							// Halfway between middle and right edge
							// Team 3
							teamStart[2] = new Coord(x,y);
						}
					} else {
						break;
					}
				} else if (reached) { // If currently filling in
					cells[x][y].valid = true;
				}
			}
		}
		
		teamStart[0] = new Coord(0, (diameter / 2) + 1);
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
	public void addRobot(Robot robot, int team) {
		robot.teamNumber = team;
		teams[team - 1].add(robot);
		System.out.println(robot.name + " added.");
	}
	
//	GB.addTeam(team): Adds all robots specified in a team file.
	public void addTeam(String file, int team) {
		
	}
	
	public LinkedList<Robot>[] getTeams() {
		return teams;
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
	
	// TODO: Implement GB.shoot()
	public void shoot(Robot caller, int id, int ir) {

	}
	
	// TODO: Implement GB.move()
	public void move(Robot caller, int id, int ir) {
		
	}
	
	/**
	 * Scans the board for other robots within firing range of 
	 * the current robot
	 * @param caller Robot - robot that wants to scan
	 * @return int - number of robots in range
	 */
	public int scan(Robot caller){
	
		Coord position = new Coord(caller.c.x, caller.c.y);
		int numRobots = 0;
		
		for(int i = 0; i <= 3 && numRobots < 4; i++){
			Cell[] ret = scan(position, i);
			numRobots = ret.length;
		}

		return numRobots;
	}
	
	/**
	 * Helper function
	 * 	Takes the current position of a robot, and a range.
	 * 	Scans the board for robots in the given level
	 * @param position Coord - current position of the robot in question
	 * @param range int - level to scan (i.e. 0 is robot, 1 is tiles 
	 * 		adjacent to robot, 2 is tiles adjacent to 1, etc.)
	 * @return Cell[] - array of cells containing robots in range
	 */
	private Cell[] scan(Coord position, int range){
	
		// for random number generation
		Random rnd = new Random();
		Cell[] returnCells = new Cell[4];
		// number of robots found
		int numRobots = 0;
		Cell thisCell = this.getCell(position);

		// if range is 0, count number of robots on current tile
		if(range == 0) {
			// subtract so you don't include yourself
			numRobots = thisCell.occupants.size()-1;
			for(int i = 0; i < numRobots; i++){
				returnCells[0] = thisCell; 
			}
		}
		// look in the 6 tiles adjacent to the origin
		else if(range == 1) {
			
			// create an array of possible coordinates to search
			Coord[] xy = {
					new Coord(position.x, position.y+1),
					new Coord(position.x, position.y-1),
					new Coord(position.x-1, position.y-1),
					new Coord(position.x-1, position.y),
					new Coord(position.x+1, position.y-1),
					new Coord(position.x+1, position.y)
			};
			
			LinkedList<Coord> coordsToSearch = new LinkedList<Coord>();
			// if each coordinate is valid, add it to a linked list
			for(int i = 0; i < xy.length; i++){
				if (isCellInArray(xy[i])){
					coordsToSearch.add(xy[i]);
				}
			}
			
			// randomly choose a coordinate from the list...
			while(numRobots < 4 && coordsToSearch.size() > 0){
				int index = rnd.nextInt() % coordsToSearch.size();
				Cell c = this.getCell(coordsToSearch.remove(index));
				// ...and for each occupant, add the cell to the array until 
				// there are 4 cells or all occupants have been added
				for(int i = 0; i < c.occupants.size() && numRobots < 4; i++){
					returnCells[numRobots] = c;
					numRobots++;
				}
			}
			
		}
		// same as range 1
		else if(range == 2) {
			
			Coord[] xy = {
					new Coord(position.x, position.y-2),
					new Coord(position.x, position.y+2),
					new Coord(position.x+1, position.y-2),
					new Coord(position.x+1, position.y+1),
					new Coord(position.x+2, position.y-2),
					new Coord(position.x+2, position.y-1),
					new Coord(position.x+2, position.y),
					new Coord(position.x-1, position.y-2),
					new Coord(position.x-1, position.y+1),
					new Coord(position.x-2, position.y-2),
					new Coord(position.x-2, position.y-1),
					new Coord(position.x-2, position.y)
			};
			
			LinkedList<Coord> coordsToSearch = new LinkedList<Coord>();
			for(int i = 0; i < xy.length; i++){
				if (isCellInArray(xy[i])){
					coordsToSearch.add(xy[i]);
				}
			}
			
			while(numRobots < 4 && coordsToSearch.size() > 0){
				int index = rnd.nextInt() % coordsToSearch.size();
				Cell c = this.getCell(coordsToSearch.remove(index));
				for(int i = 0; i < c.occupants.size() && numRobots < 4; i++){
					returnCells[numRobots] = c;
					numRobots++;
				}
			}
		}
		// same as range 1 and 2
		else if(range == 3) {
			
			Coord[] xy = {
					new Coord(position.x, position.y-3),
					new Coord(position.x, position.y+3),
					new Coord(position.x+1, position.y-3),
					new Coord(position.x+1, position.y+2),
					new Coord(position.x+2, position.y-3),
					new Coord(position.x+2, position.y+1),
					new Coord(position.x+3, position.y-3),
					new Coord(position.x+3, position.y-2),
					new Coord(position.x+3, position.y-1),
					new Coord(position.x+3, position.y),
					new Coord(position.x-1, position.y-3),
					new Coord(position.x-1, position.y+2),
					new Coord(position.x-2, position.y-3),
					new Coord(position.x-2, position.y+1),
					new Coord(position.x-3, position.y-3),
					new Coord(position.x-3, position.y-2),
					new Coord(position.x-3, position.y-1),
					new Coord(position.x-3, position.y),
			};
			
			LinkedList<Coord> coordsToSearch = new LinkedList<Coord>();
			for(int i = 0; i < xy.length; i++){
				if (isCellInArray(xy[i])){
					coordsToSearch.add(xy[i]);
				}
			}
			
			while(numRobots < 4 && coordsToSearch.size() > 0){
				int index = rnd.nextInt() % coordsToSearch.size();
				Cell c = this.getCell(coordsToSearch.remove(index));
				for(int i = 0; i < c.occupants.size() && numRobots < 4; i++){
					returnCells[numRobots] = c;
					numRobots++;
				}
			}
		}
		
		// return the cells
		return returnCells;
		
	}
	
	// TODO: Implement GB.identify()
	public RobotIdentityData identify(Robot caller, int identity) {
		return new RobotIdentityData(0,0,0,0);
	}
	
	// TODO: Implement GB.send()
	public boolean send(Robot caller, int teamMember, Token value){
		return true;
	}
	
	// TODO: Implement GB.mesg()
	public boolean mesg(Robot caller, int fromTeamMember){
		return true;
	}

	// TODO: Implement GB.recv()
	public Token recv(Robot caller, int fromTeamMember){
		return new Token("Message contents");
	}
	
}
