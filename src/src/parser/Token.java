package src.parser;

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
		this.type = type;
		svalue = s;
	}
	
	public Token(String s){
		type = SYMBOL;
		try {
			dvalue = Double.parseDouble(s);
			type = DOUBLE;
			if ((dvalue == Math.floor(dvalue)) && !Double.isInfinite(dvalue)) {
			    type = INT;
			}
			ivalue = new Double(dvalue).intValue();
			svalue = s;
			return;
		} catch (NumberFormatException nfe) {
		}
		if(s.equals("true") || s.equals("false")){
			bvalue = s.equals("true");
			type = BOOLEAN;
		} else {
		}
		svalue = s;
	}

	public Token(double d){
		svalue = Double.toString(d);
		dvalue = d; 
		ivalue = new Double(d).intValue();
		this.type = DOUBLE;
	}

	public Token(int i){
		svalue = Integer.toString(i);
		ivalue = i; 
		dvalue = i; 
		type = INT;
	}

	public Token(boolean b){
		svalue = b ? "true" : "false";
		bvalue = b; 
		this.type = BOOLEAN;
	}
}
