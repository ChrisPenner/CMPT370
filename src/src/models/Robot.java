package src.models;

public class Robot {
	public Coord c;
	private int health;
	
	public Robot() {
		// TODO Auto-generated constructor stub
	}
	
	public int getHealth(){
		return health;
	}
	
	protected void setHealth(int amount) {
		health = amount;
	}

	public int getMovesLeft() {
		return 0;
	}

	public int getFirepower() {
		return 0;
	}

	public int getMember() {
		return 0;
	}

	public int getTeam() {
		return 0;
	}
}
