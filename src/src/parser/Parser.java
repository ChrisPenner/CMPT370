package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Stack;

public class Parser{
	private StreamTokenizer st;
	public Parser(String filename) {
		filename = "/Users/chris/Desktop/370.txt";
		FileReader fr;
		try {
			fr = new FileReader(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		BufferedReader br = new BufferedReader(fr);
		st = new StreamTokenizer(br);
		st.quoteChar('"');
	}

	public void parse() throws IOException{
		while(parseLine(st));
	}

	private boolean parseLine(StreamTokenizer st) throws IOException {
		Stack<Token> s = new Stack<Token>();
		st.nextToken();
		while(st.ttype != StreamTokenizer.TT_EOF 
				&& st.ttype != StreamTokenizer.TT_EOL){
			Token token = tokenize(st);
			s.add(token);
			st.nextToken();
		}
		if(st.ttype == StreamTokenizer.TT_EOF){
			return false;
		} else {
			return true;
		}
	}

	private Token tokenize(StreamTokenizer st) throws IOException {
		switch(st.ttype){
		case StreamTokenizer.TT_WORD:
			System.out.println(st.sval);
			return new StringToken(st.sval);
		case StreamTokenizer.TT_NUMBER:
			System.out.println(st.nval);
			return new NumberToken((int)st.nval);
		default:
			// Hit other symbol
			System.out.println("\"" + st.sval + "\"");
			return new SymbolToken((char)st.ttype);
		}
	}
}
