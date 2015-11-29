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
	
	private static class CodeDeserializer implements JsonDeserializer<Code> {
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

	private static class RobotDeserializer implements JsonDeserializer<Robot> {
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

	public static Robot fromJson(String json, int teamNumber){
		Robot r = fromJson(json);
		r.teamNumber = teamNumber;
		return r;
	}
	
	public static Robot fromJson(String json){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Robot.class, new RobotDeserializer());
		Gson gson = gsonBuilder.create();
		Robot r = (Robot) gson.fromJson(json, Robot.class);
		r.parser = new Parser(r);
		return r;
	}
	
	public void init(){
		parser.init();
	}

	public void turn(){
		parser.turn();
	}
	
	public int getHealth(){
		return health;
	}
	
	/**
	 * Returns false if robot dies
	 */
	protected boolean setHealth(int amount) {
		health = amount;
		if(health == 0) {
			died++;
			return false;
		}
		return true;
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
		return teamNumber;
	}
	
	public void kill() {
		setHealth(0);
	}
}
