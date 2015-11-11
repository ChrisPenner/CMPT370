package models;

public class Coord {
	public int x, y;
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move1() {
		y--;
	}
	
	public void move2() {
		x++;
	}
	
	public void move3() {
		y--;
		x++;
	}
	
	public void move4() {
		y++;
	}
	
	public void move5() {
		y--;
		x--;
	}
	
	public void move6() {
		x--;
	}
}
