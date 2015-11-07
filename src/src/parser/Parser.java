package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import models.Robot;

public class Parser{
	private String filename;
	private Stack<Token> s;
	private HashMap<String, Token> variables;
	private HashMap<String, Token> macros;
	private Robot robot;
	private LinkedList<Token> l;
	
	
	public static void main(String[] args) throws IOException{
	  Parser p = new Parser("/Users/chris/Desktop/370.txt", new Robot());
	  p.parse();
	  p.run();
	}
	
	
	public Parser(String filename, Robot r) {
		this.filename = "/Users/chris/Desktop/370.txt";
		robot = r;
		s = new Stack<Token>();
		l = new LinkedList<Token>();
		variables = new HashMap<String, Token>();
		macros = new HashMap<String, Token>();
	}

	public void parse() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));  
		String line = null;  
		boolean inQuote = false;
		boolean stringPrefix = false;
		boolean inComment = false;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null)  
		{  
			for (int i = 0; i < line.length(); i++){
				char c = line.charAt(i);        
				if(inComment && c != ')'){
					continue;
				} else if (c == ')'){
					inComment = false;
					continue;
				}
				if(stringPrefix && c != '"'){
					stringPrefix = false;
					sb.append('.');
				}
				switch(c){
				case '(':
					inComment = true;
					break;
				case '.':
					// hit string prefix.
					stringPrefix = true;
					break;
				case '"':
					if(inQuote){
						inQuote = false;
						l.add(new Token(sb.toString(), Token.STRING));
						// Reset StringBuilder.
						sb.setLength(0);
					} else if (stringPrefix){
						stringPrefix = false;
						inQuote = true;
					} else {
						sb.append('"');
					}
					break;
				case ' ':
					if(inQuote){
						sb.append(c);
						break;
					}
					if(sb.length() != 0){
						l.add(new Token(sb.toString()));
					}
					// Reset StringBuilder.
					sb.setLength(0);
					break;
				default:
					sb.append(c);
					break;
				}
			}
			if(sb.length() != 0){
				l.add(new Token(sb.toString()));
			}
			sb.setLength(0);
		} 
		br.close();
	}
	
	public void print(){
		for( Token t: s){
			System.out.println(t.svalue);
		}
	}
	
	public void run(){
		String mode = "ex";
		String ifMode = "";
		boolean ifFlag = false;
		while (!l.isEmpty()){
			Token t = l.pop();
			if(!ifMode.equals("")){
				if(t.svalue.equals("then")){
					ifMode = "";
					continue;
				} else if(t.svalue.equals("else")){
					ifMode = "else";
				}
				// If we're in the section that doesn't match the if flag, just ignore stuff.
				// This doesn't work with nested ifs.
				if(ifMode.equals("if") && !ifFlag){
					continue;
				} else if (ifMode.equals("else") && ifFlag){
					continue;
				}
			}
			if(t.type != Token.SYMBOL){
				System.out.println("Addstack: " + t.svalue);
				s.add(t);
				continue;
			}

			System.out.println("Ex: " + t.svalue);
			switch(t.svalue){
			case ";":
				if(mode.equals("variable")){
					variable(s);
					mode = "ex";
				}
				break;

			case "drop":
				drop(s);
				break;

			case "dup":
				dup(s);
				break;
				
			case "swap":
				swap(s);
				break;
				
			case "rot":
				rot(s);
				break;
				
			case "+":
				add(s);
				break;
				
			case "-":
				minus(s);
				break;
				
			case "*":
				mult(s);
				break;
				
			case "/mod":
				mod(s);
				break;

			case "<":
				lt(s);
				break;

			case "<=":
				lte(s);
				break;
				
			case ">":
				gt(s);
				break;
				
			case ">=":
				gte(s);
				break;
				
			case "=":
				eq(s);
				break;
				
			case "<>":
				neq(s);
				break;
				
			case "and":
				and(s);
				break;
				
			case "or":
				or(s);
				break;
				
			case "variable":
				mode = "variable";
				break;

			case "?":
				getVariable(s);
				break;

			case "!":
				setVariable(s);
				break;

			case "if":
				ifMode = "if";
				ifFlag = s.pop().bvalue;
				break;
				
			case "health":
				s.add(new Token(robot.getHealth()));
				break;
				
			case "movesLeft":
				s.add(new Token(robot.getMovesLeft()));
				break;
				
			case "firepower":
				s.add(new Token(robot.getFirepower()));
				break;
				
			case "team":
				s.add(new Token(robot.getTeam()));
				break;
				
			case "member":
				s.add(new Token(robot.getMember()));
				break;
				
			case "shoot!":
				break;
				
			case "move!":
				break;
				
			case "scan!":
				break;
				
			case "identify!":
				break;
				
			case "send!":
				break;
				
			case "mesg?":
				break;
				
			case "recv!":
				break;
				
			 default:
				 s.add(t);
				break;
			}
		}
	}
	
	
			private void drop(Stack<Token> s){
				System.out.println("Dropped: " + s.pop().svalue);
			}

			private void dup(Stack<Token> s){
				s.add(s.peek());
			}

			private void swap(Stack<Token> s){
				Token t = s.pop();
				Token t2 = s.pop();
				s.add(t);
				s.add(t2);
			}

			private void rot(Stack<Token> s){
				Token t3 = s.pop();
				Token t2 = s.pop();
				Token t1 = s.pop();
				s.add(t3);
				s.add(t1);
				s.add(t2);
			}

			private void add(Stack<Token> s){
				double a1 = s.pop().dvalue;
				double a2 = s.pop().dvalue;
				s.add(new Token(a2 + a1));
			}

			private void mult(Stack<Token> s){
				int a1 = s.pop().ivalue;
				int a2 = s.pop().ivalue;
				s.add(new Token(a2 * a1));
			}

			private void minus(Stack<Token> s){
				double a1 = s.pop().dvalue;
				double a2 = s.pop().dvalue;
				s.add(new Token(a2 - a1));
			}

			private void mod(Stack<Token> s){
				int a1 = s.pop().ivalue;
				int a2 = s.pop().ivalue;
				int mod = a2 % a1;
				int divisor = a2 / a1;
				s.add(new Token(mod));
				s.add(new Token(divisor));
			}

			private void lt(Stack<Token> s){
				double a1 = s.pop().dvalue;
				double a2 = s.pop().dvalue;
				s.add(new Token(a2 < a1));
			}

			private void lte(Stack<Token> s){
				double a1 = s.pop().dvalue;
				double a2 = s.pop().dvalue;
				s.add(new Token(a2 <= a1));
			}

			private void gt(Stack<Token> s){
				double a1 = s.pop().dvalue;
				double a2 = s.pop().dvalue;
				s.add(new Token(a2 > a1));
			}

			private void gte(Stack<Token> s){
				double a1 = s.pop().dvalue;
				double a2 = s.pop().dvalue;
				s.add(new Token(a2 >= a1));
			}

			private void eq(Stack<Token> s){
				double a1 = s.pop().ivalue;
				double a2 = s.pop().ivalue;
				s.add(new Token(a2 == a1));
			}

			private void neq(Stack<Token> s){
				double a1 = s.pop().ivalue;
				double a2 = s.pop().ivalue;
				s.add(new Token(a2 != a1));
			}

			private void and(Stack<Token> s){
				boolean a1 = s.pop().bvalue;
				boolean a2 = s.pop().bvalue;
				s.add(new Token(a2 && a1));
			}

			private void or(Stack<Token> s){
				boolean a1 = s.pop().bvalue;
				boolean a2 = s.pop().bvalue;
				s.add(new Token(a2 || a1));
			}

			private void variable(Stack<Token> s){
				String key = s.pop().svalue;
				variables.put(key, new Token(0));
			}

			private void getVariable(Stack<Token> s){
				String key = s.pop().svalue;
				Token t = variables.get(key);
				s.add(t);
			}

			private void setVariable(Stack<Token> s){
				String key = s.pop().svalue;
				Token value = s.pop();
				Token t = variables.put(key, value);
			}

//			case "if":
//				break;
//			case "else":
//				break;
//			case "then":
//				break;
//			case "health":
//				break;
//			case "movesLeft":
//				break;
//			case "firepower":
//				break;
//			case "team":
//				break;
//			case "member":
//				break;
//			case "shoot!":
//				break;
//			case "move!":
//				break;
//			case "scan!":
//				break;
//			case "identify!":
//				break;
//			case "send!":
//				break;
//			case "mesg?":
//				break;
//			case "recv!":
//				break;
	
	
}
