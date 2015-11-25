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


	public static Robot fromJson(String json){
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Code.class, new CodeDeserializer());
		Gson gson = gsonBuilder.create();
		Robot r = (Robot) gson.fromJson(json, Robot.class);
		r.parser = new Parser(r);
		return r;
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
