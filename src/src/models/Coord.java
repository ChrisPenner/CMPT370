package models;

public class Coord {
	public int x, y;
	
	/**
	 * Creates a new coord with the specified x and y
	 */
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Moves up
	 */
	public void move1() {
		y--;
	}
	
	/**
	 * Moves right-up
	 */
	public void move2() {
		x++;
	}

	/**
	 * Moves right-down
	 */
	public void move3() {
		y--;
		x++;
	}

	/**
	 * Moves down
	 */
	public void move4() {
		y++;
	}

	/**
	 * Moves left-down
	 */
	public void move5() {
		y--;
		x--;
	}

	/**
	 * Moves left-up
	 */
	public void move6() {
		x--;
	}
}
