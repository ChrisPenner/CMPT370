package parser;

public class StringToken extends Token {
	private String value;
	public StringToken(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}

}
