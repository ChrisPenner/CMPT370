package parser;

public class Token {
	String svalue;
	double dvalue;
	int ivalue;
	boolean bvalue;
	int type;
	static final int SYMBOL = 0;
	static final int STRING = 1;
	static final int INT = 2;
	static final int DOUBLE = 3;
	static final int NUMBER = 4;
	static final int BOOLEAN = 5;

	public Token(String s, int type){
//		System.out.println("New token: " + s);
		this.type = type;
		svalue = s;
	}
	
	public Token(String s){
		type = SYMBOL;
		try {
			dvalue = Double.parseDouble(s);
			ivalue = new Double(dvalue).intValue();
			type = NUMBER;
//			System.out.println("New NUMBER: " + s);
			svalue = s;
			return;
		} catch (NumberFormatException nfe) {
		}
		if(s.equals("true") || s.equals("false")){
			bvalue = s.equals("true");
			type = BOOLEAN;
//			System.out.println("New BOOLEAN: " + s);
		} else {
//			System.out.println("New SYMBOL: " + s);
		}
		svalue = s;
	}

	public Token(double d){
		svalue = Double.toString(d);
		dvalue = d; 
		ivalue = new Double(d).intValue();
		this.type = DOUBLE;
//		System.out.println("New DOUBLE: " + svalue);
	}

	public Token(int i){
		svalue = Integer.toString(i);
		ivalue = i; 
		dvalue = i; 
		type = INT;
//		System.out.println("New INT: " + svalue);
	}

	public Token(boolean b){
		svalue = b ? "true" : "false";
		bvalue = b; 
		this.type = BOOLEAN;
//		System.out.println("New BOOL: " + svalue);
	}
}
