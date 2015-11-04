package parser;

public class NumberToken extends Token {
	private int value;
	public NumberToken(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}

}
