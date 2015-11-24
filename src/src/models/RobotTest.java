package models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
public class RobotTest {
	String json = "{ "
			+ "\"serial\" : 12345"
			+ ", \"team\" : \"XX\""
			+ ", \"name\" : \"random sniper\""
			+ ", \"version\" : 2"
			+ ", \"matches\" : 43"
			+ ", \"wins\" : 12"
			+ ", \"losses\" : 38"
			+ ", \"executions\" : 55"
			+ ", \"lived\" : 10"
			+ ", \"died\" : 45"
			+ ", \"health\" : 2"
			+ ", \"absorbed\" : 93	"
			+ ", \"firepower\" : 3"
			+ ", \"damages\" : 17"
			+ ", \"kills\" : 12"
			+ ", \"movement\" : 1"
			+ ", \"distance\" : 0"
			+ ", \"code\" : [ {\"variable\" : \"maxRange\"}"
			+ ", {\"word\" : { \"name\" : \"init\", \"comment\" : \"( -- )\", \"body\" : \"3 maxRange !\" }}"
			+ ", {\"word\" : { \"name\" : \"hexesPerRange\" , \"comment\" : \"( -- i)\" , \"body\" : \"6\" }}"
			+ ", {\"word\" : { \"name\" : \"turn\" , \"comment\" : \"( -- )\" , \"body\" : \"maxRange ? 1 - random 1 + dup hexesPerRange * 1 - random swap shoot!\" }}"
			+ "]"
			+ " }";

  @Test
  public void testRobotFromJson() {
	  Robot r = Robot.fromJson(json);
	  assertEquals(12345, r.serial);
	  assertEquals("XX", r.team);
	  assertEquals(2, r.version);
	  assertEquals(43, r.matches);
	  assertEquals(12, r.wins);
	  assertEquals(38, r.losses);
	  assertEquals(55, r.executions);
	  assertEquals(10, r.lived);
	  assertEquals(45, r.died);
	  assertEquals(2, r.health);
	  assertEquals(93, r.absorbed);
	  assertEquals(3, r.firepower);
	  assertEquals(17, r.damages);
	  assertEquals(12, r.kills);
	  assertEquals(1, r.movement);
	  assertEquals(0, r.distance);
	  assertEquals(3, r.code.words.size());
	  assertEquals("hexesPerRange", r.code.words.get(1).name);
	  assertEquals("6", r.code.words.get(1).body);
	  assertEquals(3, r.code.words.size());

	  assertEquals(1, r.code.variables.size());
	  assertEquals("maxRange", r.code.variables.get(0));
  }
}
