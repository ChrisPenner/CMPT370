package models;

public class RobotIdentityData {
	int teamNumber;
	int range;
	public int getTeamNumber() {
		return teamNumber;
	}

	public void setTeamNumber(int teamNumber) {
		this.teamNumber = teamNumber;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	int direction;
	int health;

	public RobotIdentityData(int teamNumber, int range, int direction, int health) {
		this.teamNumber = teamNumber;
		this.range = range;
		this.direction = direction;
		this.health = health;
	}

}
