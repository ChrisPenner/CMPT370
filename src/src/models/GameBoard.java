package models;

import java.util.LinkedList;
import java.util.Random;

import parser.Token;


public class GameBoard {
	private LinkedList<Robot>[] teams;
	private Cell[][] cells;
	private int diameter;
	private Coord[][] coordinateValues = {
		// team 1
		{new Coord(0, 3), new Coord(1, 3), new Coord(1, 4), new Coord(0, 5)},	
		// team 2
		{new Coord(3, 0), new Coord(3, 1), new Coord(2, 2), new Coord(1, 1)},	
		// team 3
		{new Coord(7, 1), new Coord(6, 2), new Coord(5, 1), new Coord(5, 0)},	
		// team 4
		{new Coord(8, 5), new Coord(7, 4), new Coord(7, 3), new Coord(8, 3)},	
		// team 5
		{new Coord(5, 7), new Coord(5, 6), new Coord(6, 6), new Coord(7, 6)},	
		// team 6
		{new Coord(1, 6), new Coord(2, 6), new Coord(3, 6), new Coord(3, 7)}	
	};
	
//	newGameBoard(teams): Initializes a new GameBoard with the selected teams
//	loaded
	public GameBoard(LinkedList<Robot>[] teams, int sideLength) {
		initializeBoard(sideLength);
//		this.teams = (LinkedList<Robot>[]) new LinkedList<?>[6];
		this.teams = teams;
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
	public boolean addRobot(Robot robot, int team) {
		robot.teamNumber = team;
		int numRobots = teams[team - 1].size();
		if(numRobots < 4){
			teams[team - 1].add(robot);
		}
		else{
			return false;
		}

		robot.c = coordinateValues[team-1][numRobots];
		getCell(coordinateValues[team-1][numRobots]).occupants.add(robot);

		System.out.println(robot.name + " added.");
		return true;
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
	
	public void shoot(Robot caller, int id, int ir) {
		
		System.out.println("Shoot()");
		
//		if(ir > 3){
//			System.out.println("Error: tried to shoot at range > 3");
//			return;
//		}
		Coord coord = getCoordAtDirAndRange(caller, id, ir);
		try{
			Cell cell = getCell(coord);
			for(int i = 0; i < cell.occupants.size(); i++){
				Robot r = cell.occupants.get(i);
				if(r.teamNumber != caller.teamNumber){
					r.health -= caller.firepower;
					if(r.health <= 0){
						cell.occupants.remove(i);
					}
					teams[r.teamNumber - 1].remove(r);
				}
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private Coord getCoordAtDirAndRange(Robot r, int id, int ir){
		Coord c = new Coord(r.c.x, r.c.y);
		int direction = getTeamSpecificDirection(r.teamNumber, id);
		switch(direction){
			case 0:
				for(int i = 0; i < ir; i++){
					c.move1();
				}
				break;
			case 1:
				for(int i = 0; i < ir; i++){
					c.move2();
				}
				break;
			case 2:
				for(int i = 0; i < ir; i++){
					c.move3();
				}
				break;
			case 3:
				for(int i = 0; i < ir; i++){
					c.move4();
				}
				break;
			case 4:
				for(int i = 0; i < ir; i++){
					c.move5();
				}
				break;
			case 5:
				for(int i = 0; i < ir; i++){
					c.move6();
				}
				break;
			default:
		}
		return c;
		
	}
	
	private int getTeamSpecificDirection(int team, int id){
		int offset = Math.abs((team+2) % 6);
		int direction = (id + offset) % 6;
		
		return direction;
	}
	
	public void move(Robot caller, int id, int ir) {
		
		System.out.println("Move()");

		Coord c = getCoordAtDirAndRange(caller, id, ir);
		moveRobotTo(caller, c);
	}
	
	private boolean moveRobotTo(Robot r, Coord c){
		try{
			if(getCell(c).isValid()){
				// move robot
				System.out.println("true");
				getCell(r.c).occupants.remove(r);
				getCell(c).occupants.add(r);
				r.c = c;
				return true;
			}
			else {
				System.out.println("false");
				return false;
			}
		}
		catch(Exception e){
			System.out.println("false");
			return false;
		}

	}
	
	/**
	 * Scans the board for other robots within firing range of 
	 * the current robot
	 * @param caller Robot - robot that wants to scan
	 * @return int - number of robots in range
	 */
	public int scan(Robot caller){
		
		System.out.println("Scan()");
	
		Coord position = new Coord(caller.c.x, caller.c.y);
		int numRobots = 0;
		
		for(int i = 0; i <= 3 && numRobots < 4; i++){
			Cell[] ret = scan(position, i);
			for(Cell c : ret){
				if(c != null){
					numRobots++;
				}
			}
//			numRobots = ret.length;
		}

		return numRobots;
	}
	
	/**
	 * Returns the number of robots on a given hex square relative to the
	 * calling robot.
	 * @param caller Robot - robot that wants to scan
	 * @param id the direction of the hex square
	 * @param il the range of the hex square
	 * @return int - number of robots on ir,id hex.
	 */
	public int hex(Robot caller, int id, int ir){
		
		System.out.println("Hex()");
		
		Coord c = getCoordAtDirAndRange(caller, id, ir);
		return getCell(c).occupants.size();

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
	// TODO: GB.scan(): include robots on own team??
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
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y)
			};
			xy[0].move1();
			xy[1].move2();
			xy[2].move3();
			xy[3].move4();
			xy[4].move5();
			xy[5].move6();
			
			LinkedList<Coord> coordsToSearch = new LinkedList<Coord>();
			// if each coordinate is valid, add it to a linked list
			for(int i = 0; i < xy.length; i++){
				if (isCellInArray(xy[i])){
					coordsToSearch.add(xy[i]);
				}
			}
			
			// randomly choose a coordinate from the list...
			while(numRobots < 4 && coordsToSearch.size() > 0){
				int index = Math.abs(rnd.nextInt()) % coordsToSearch.size();
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
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y)
			};
			xy[0].move1(); xy[0].move1(); 
			xy[1].move2(); xy[1].move2(); 
			xy[2].move3(); xy[2].move3(); 
			xy[3].move4(); xy[3].move4(); 
			xy[4].move5(); xy[4].move5(); 
			xy[5].move6(); xy[5].move6(); 
			
			LinkedList<Coord> coordsToSearch = new LinkedList<Coord>();
			for(int i = 0; i < xy.length; i++){
				if (isCellInArray(xy[i])){
					coordsToSearch.add(xy[i]);
				}
			}
			
			while(numRobots < 4 && coordsToSearch.size() > 0){
				int index = Math.abs(rnd.nextInt()) % coordsToSearch.size();
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
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y),
					new Coord(position.x, position.y)
			};
			xy[0].move1(); xy[0].move1(); xy[0].move1();
			xy[1].move2(); xy[1].move2(); xy[1].move2();
			xy[2].move3(); xy[2].move3(); xy[2].move3();
			xy[3].move4(); xy[3].move4(); xy[3].move4();
			xy[4].move5(); xy[4].move5(); xy[4].move5();
			xy[5].move6(); xy[5].move6(); xy[5].move6();
			
			LinkedList<Coord> coordsToSearch = new LinkedList<Coord>();
			for(int i = 0; i < xy.length; i++){
				if (isCellInArray(xy[i])){
					coordsToSearch.add(xy[i]);
				}
			}
			
			while(numRobots < 4 && coordsToSearch.size() > 0){
				int index = Math.abs(rnd.nextInt()) % coordsToSearch.size();
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
	
	private Robot getNthRobotFromCellsArray(Cell[] cells, int n){
		
		int numRobots = 0;
		for(int i = 0; i < cells.length; i++){
			try{
				for(int j = 0; j < cells[j].occupants.size(); j++){
					if(numRobots == n){
						return cells[i].occupants.get(j);
					}
					numRobots++;
				}
			} catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return null;
	}
	
	private void getDirectionAndRange(Robot caller, Cell target, int id, int ir){
		
//		Cell c = getCell(caller.c);
		Coord start = new Coord(caller.c.x, caller.c.y);
		Coord end = new Coord(target.occupants.getFirst().c.x, target.occupants.getFirst().c.y);
		int team = caller.teamNumber;
		
		// same column
		if(start.x == end.x){
			if(end.y > start.y){
				// up
				id = getTeamSpecificDirection(team, 0);
				ir = Math.abs(start.y - end.y);
			}
			else{
				//down
				id = getTeamSpecificDirection(team, 3);
				ir = Math.abs(start.y - end.y);
			}
		}
		else if(start.x > end.x){
			// left
			if(start.x % 2 == 0){
				if(end.y >= start.y){
					// down-left 
					id = getTeamSpecificDirection(team, 4);
					ir = Math.abs(start.x - end.x);
				}
				else{
					// up-left
					id = getTeamSpecificDirection(team, 5);
					ir = Math.abs(start.x - end.x);
				}
			}
			else{
				// right
				if(end.y <= start.y){
					// up-left
					id = getTeamSpecificDirection(team, 5);
					ir = Math.abs(start.x - end.x);
				}
				else{
					// down-left
					id = getTeamSpecificDirection(team, 4);
					ir = Math.abs(start.x - end.x);
				}
			}
		}
		else{
			// right
			if(start.x % 2 == 0){
				if(end.y >= start.y){
					// down-right
					id = getTeamSpecificDirection(team, 2);
					ir = Math.abs(start.x - end.x);
				}
				else{
					// up-right
					id = getTeamSpecificDirection(team, 1);
					ir = Math.abs(start.x - end.x);
				}
			}
			else{
				// right
				if(end.y <= start.y){
					// up-right
					id = getTeamSpecificDirection(team, 1);
					ir = Math.abs(start.x - end.x);
				}
				else{
					// down-right
					id = getTeamSpecificDirection(team, 2);
					ir = Math.abs(start.x - end.x);
				}
			}
		}

	}
	
	public RobotIdentityData identify(Robot caller, int identity) {

		System.out.println("Identify()");
		
		if(identity > 4){
			System.out.println("Error: trying to identify robot with identity > 4");
		}
		Cell[] totalCells = new Cell[4];
		int numCells = 0;
		for(int i = 0; i < 4; i++){
			Cell[] cells = scan(caller.c, i);
			for(int j = 0; j < cells.length; j++){
				if(cells[j] != null){
					totalCells[numCells] = cells[j];
				}
			}
		}
		
		Robot r = getNthRobotFromCellsArray(totalCells, identity);
		Cell c = null;
		for(int i = 0; i < totalCells.length; i++){
			try{
				if(totalCells[i].occupants.contains(r)){
					c = totalCells[i];
				}
			} catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		if(r == null){
			System.out.println("Error in identify(): robot == null");
			return null;
		}
		if(c == null){
			System.out.println("Error in identify(): cell == null");
			return null;
		}
		Integer ir = new Integer(0);
		Integer id = new Integer(0);
		getDirectionAndRange(r, c, id, ir);
		
		// teamNumber, range, direction, health
		return new RobotIdentityData(r.getTeam(), ir, id, r.getHealth());
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
