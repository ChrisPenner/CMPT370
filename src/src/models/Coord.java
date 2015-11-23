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
		if(x % 2 == 0) {
			x++;
			y--;
		} else {
			x++;
		}
	}

	/**
	 * Moves right-down
	 */
	public void move3() {
		if(x % 2 == 0) {
			x++;
		} else {
			x++;
			y++;
		}
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
		if(x % 2 == 0) {
			x--;
		} else {
			x--;
			y++;
		}
	}

	/**
	 * Moves left-up
	 */
	public void move6() {
		if(x % 2 == 0) {
			x--;
			y--;
		} else {
			x--;
		}
	}
}
