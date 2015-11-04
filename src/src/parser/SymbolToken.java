package parser;

public class SymbolToken extends Token {
	private char value;
	public SymbolToken(char symbol) {
		this.value = symbol;
	}
	
	public char getValue(){
		return this.value;
	}

}
