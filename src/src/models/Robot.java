package models;

import java.lang.reflect.Type;

import parser.Code;
import parser.Parser;
import parser.Word;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

/**
 * A Class representing a Robot, it's stats and its code.
 *
 */
public class Robot {
	public Coord c;
	Parser parser;

	int serial;
	String team;
	int teamNumber;
	public String name;
	int version;
	int matches;
	int wins;
	int losses;
	int executions;
	int lived;
	int died;
	int health;
	int absorbed;
	int firepower;
	int damages;
	int kills;
	int movement;
	int distance;
	public Code code = new Code();
	
	/**
	 * Utility class for decoding a robot's Gson.
	 */
	private static class CodeDeserializer implements JsonDeserializer<Code> {
		/**
		 * This is handled by the Gson library.
		 */
		@Override
		public Code deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			JsonArray jArray = (JsonArray) json;
			Code code = new Code();
			for(int i = 0; i < jArray.size(); i++){
				JsonElement element = jArray.get(i);
				JsonObject object = (JsonObject) element;
				if(object.has("variable")){
					code.variables.add(object.get("variable").getAsString());
				} else if (object.has("word")){
					JsonObject word = (JsonObject) object.get("word");
					code.words.add(new Word(word.get("name").getAsString(), word.get("body").getAsString()));
				} 
			}
			return code;
		}
	}

	/**
	 * A utility class to help with Gson deserialization.
	 */
	private static class RobotDeserializer implements JsonDeserializer<Robot> {
		/**
		 * This is handled by the Gson library.
		 */
		@Override
		public Robot deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {

			JsonObject jobject = json.getAsJsonObject();
			if(jobject.has("script")){
				jobject = jobject.get("script").getAsJsonObject();
			} else if(jobject.has("robot")){
				jobject = jobject.get("robot").getAsJsonObject();
			}
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Code.class, new CodeDeserializer());
			Gson gson = gsonBuilder.create();
			Robot r = (Robot) gson.fromJson(jobject, Robot.class);
			return r;
		}
	}

	/**
	 * Return the Robot class specified by a Robot JSON file.
	 * @param json The JSON string representing the robot.
	 * @param teamNumber The team number to assign to the robot.
	 * @return The constructed robot.
	 */
	public static Robot fromJson(String json, int teamNumber){
		Robot r = fromJson(json);
		r.teamNumber = teamNumber;
		return r;
	}
	
	/**
	 * Return the Robot class specified by a Robot JSON file.
	 * @param json The JSON string representing the robot.
	 * @return The constructed robot.
	 */
	public static Robot fromJson(String json){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Robot.class, new RobotDeserializer());
		Gson gson = gsonBuilder.create();
		Robot r = (Robot) gson.fromJson(json, Robot.class);
		r.parser = new Parser(r);
		return r;
	}
	
	/**
	 * Initialized the robot as specified by its code.
	 */
	public void init(){
		parser.init();
	}

	/**
	 * Run one turn for the robot.
	 */
	public void turn(){
		parser.turn();
	}
	
	/**
	 * Returns the health of the robot.
	 * @return health
	 */
	public int getHealth(){
		return health;
	}
	
	/**
	 * Sets the health of the robot
	 * @param amount The value to set health to.
	 * @return Returns false if robot dies
	 */
	protected boolean setHealth(int amount) {
		health = amount;
		if(health == 0) {
			died++;
			return false;
		}
		return true;
	}

	/**
	 * Return the number of movements remaining to make.
	 * @return moves left
	 */
	public int getMovesLeft() {
		return 0;
	}

	/**
	 * Return the Robot's firepower.
	 * @return firepower level
	 */
	public int getFirepower() {
		return 0;
	}

	/**
	 * Return the Robot's member number.
	 * @return member number
	 */
	public int getMember() {
		return 0;
	}

	/**
	 * Return the Robot's team number.
	 * @return team number
	 */
	public int getTeam() {
		return teamNumber;
	}
	
	/**
	 * Kill the robot.
	 */
	public void kill() {
		setHealth(0);
	}
}
