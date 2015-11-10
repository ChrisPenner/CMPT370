package parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import models.Robot;

public class Parser{
	protected Stack<Token> executionStack;
	private HashMap<String, Token> variables;
	private HashMap<String, LinkedList<Token>> macros;
	private Robot robot;
	
	
	// Parser flags
	private boolean inQuote = false;
	private boolean stringPrefix = false;
	private boolean inComment = false;
	
	public static void main(String[] args) throws IOException{
	  Parser p = new Parser(new Robot());
	  String content = readFile("/Users/chris/Desktop/370.txt", Charset.defaultCharset());
	  LinkedList<Token> l = p.parse(content);
	  p.executeList(l, "", 0);
	}
	
	static String readFile(String path, Charset encoding) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	
	public Parser(Robot r) {
		robot = r;
		executionStack = new Stack<Token>();
		variables = new HashMap<String, Token>();
		macros = new HashMap<String, LinkedList<Token>>();
	}
	
	public void run(LinkedList<Token> l) {
		executeList(l, "", 0);
	}
	
	public LinkedList<Token> parse(String s) throws IOException{
		LinkedList<Token> l = new LinkedList<Token>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++){
			char c = s.charAt(i);        
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
				if(inQuote){
					sb.append(c);
					continue;
				}
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
			case '\n':
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
		return l;
	}
	
	public void print(){
		for( Token t: executionStack){
			System.out.println(t.svalue);
		}
	}
	
	@SuppressWarnings("unchecked")
	public String executeList(LinkedList<Token> l, String mode, int currentIterator){
		LinkedList<Token> macroList = null;
		String macroName = null;
		int finish = 0;
		LinkedList<Token> listCopy = null;
		boolean ifFlag = false;
		int current = currentIterator;
		// Do setup
		switch(mode){
		case "if":
			ifFlag = executionStack.pop().bvalue;
			break;
		case "macro":
			macroList = new LinkedList<Token>();
			macroName = l.pop().svalue;
			break;
		case "variable":
			String key = l.pop().svalue;
			variables.put(key, new Token(0));
			l.pop(); // Pop the ';'
			return "";
		case "iloop":
			current = executionStack.pop().ivalue;
			finish = executionStack.pop().ivalue;
			listCopy = (LinkedList<Token>) l.clone();
			break;
		case "uloop":
			listCopy = (LinkedList<Token>) l.clone();
			break;
		}
		while (!l.isEmpty()){
			Token t = l.pop();
			switch(mode){
			case "leave":
				if(t.svalue.equals("loop")){
					l.pop(); // Pop off ';'
					return "leave";
				} else {
					continue;
				}
			case "uloop":
				// If true, we loop
//				if(t.svalue.equals(";")){
//					if(uloopCond){
//						l = listCopy;
//						listCopy = (LinkedList<Token>) l.clone();
//					} else {
//						return "";
//					}
//				}
				break;
			case "macro":
				if(t.svalue.equals(";")){
					macros.put(macroName, macroList);
					System.out.println("Done recording macro");
					return "";
				} else {
					macroList.add(t);
					continue;
				}
			case "ignoreif":
				if(t.svalue.equals("then")){
					return "";
				}
				continue;
			case "if":
			case "else":
				if(t.svalue.equals("then")){
					// Jump up out of the 'if' nesting level.
					return "";
				} else if(t.svalue.equals("else")){
					mode = "else";
					continue;
				} 
				// If we're in the section that doesn't match the if flag, just ignore stuff.
				if(mode.equals("if") && !ifFlag){
					if(t.svalue.equals("if")){
						executeList(l, "ignoreif", current);
					}
					continue;
				} else if (mode.equals("else") && ifFlag){
					if(t.svalue.equals("if")){
						executeList(l, "ignoreif", current);
					}
					continue;
				}
				// Otherwise, break and execute the code.
				break;
				
				default:
					break;
			}
			
			switch(t.svalue){
			case ":":
				executeList(l, "macro", current);
				continue;
			case "variable":
				executeList(l, "variable", current);
				continue;
			case "if":
				String innerReturn = executeList(l, "if", current);
				if(!mode.equals("iloop") && innerReturn.equals("leave")){
					return "leave";
				} else if(mode.equals("iloop") && innerReturn.equals("leave")){
					mode = "leave";
				}
				continue;
			case "do":
				executeList(l, "iloop", current);
				continue;
			case "leave":
				mode = "leave";
				continue;
			case "loop":
				if(current < finish){
					l.pop(); // pop trailing ";"
					l = listCopy;
					listCopy = (LinkedList<Token>) l.clone();
					current++;
					continue;
				}
				l.pop(); // Kill ';' 
				return "";
			case "I":
				executionStack.add(new Token(current));
				continue;
			default:
				if(t.type != Token.SYMBOL){
					executionStack.add(t);
					continue;
				} else {
					executeToken(t, current);
				}
			}
		}
		return "";
	}
	
	private void executeToken(Token t, int current){
		switch(t.svalue){
		case "peek":
			peek(executionStack);
			break;

		case "drop":
			drop(executionStack);
			break;

		case "dup":
			dup(executionStack);
			break;

		case "swap":
			swap(executionStack);
			break;

		case "rot":
			rot(executionStack);
			break;

		case "+":
			add(executionStack);
			break;

		case "-":
			minus(executionStack);
			break;

		case "*":
			mult(executionStack);
			break;

		case "/mod":
			mod(executionStack);
			break;

		case "<":
			lt(executionStack);
			break;

		case "<=":
			lte(executionStack);
			break;

		case ">":
			gt(executionStack);
			break;

		case ">=":
			gte(executionStack);
			break;

		case "=":
			eq(executionStack);
			break;

		case "<>":
			neq(executionStack);
			break;

		case "and":
			and(executionStack);
			break;

		case "or":
			or(executionStack);
			break;

		case "?":
			getVariable(executionStack);
			break;

		case "!":
			setVariable(executionStack);
			break;

		case "health":
			executionStack.add(new Token(robot.getHealth()));
			break;

		case "movesLeft":
			executionStack.add(new Token(robot.getMovesLeft()));
			break;

		case "firepower":
			executionStack.add(new Token(robot.getFirepower()));
			break;

		case "team":
			executionStack.add(new Token(robot.getTeam()));
			break;

		case "member":
			executionStack.add(new Token(robot.getMember()));
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
			if(macros.containsKey(t.svalue)){
				@SuppressWarnings("unchecked")
				LinkedList<Token> copy = (LinkedList<Token>) macros.get(t.svalue).clone();
				executeList(copy, "", current);
				System.out.println("Executed macro: " + t.svalue);
			} else {
				executionStack.add(t);
			}
			break;
		}	
	}
	
	
	
			private void drop(Stack<Token> s){
				s.pop();
			}

			private void peek(Stack<Token> s){
				System.out.println("PEEK: " + s.peek().svalue);
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
				variables.put(key, value);
			}

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
