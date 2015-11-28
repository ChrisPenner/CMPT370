package parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import controller.Controller;
import models.Robot;
import models.RobotIdentityData;

public class Parser{
	protected Stack<Token> executionStack;
	private HashMap<String, Token> variables;
	private HashMap<String, LinkedList<Token>> macros;
	private Stack<ExpressionState> expressionStack = new Stack<ExpressionState>();
	private Robot robot;
	
	
	// Parser flags
	private boolean inQuote = false;
	private boolean stringPrefix = false;
	private boolean inComment = false;
	
	public static void main(String[] args) throws IOException{
	  Parser p = new Parser(new Robot());
	  String content = readFile("/Users/chris/Desktop/370.txt", Charset.defaultCharset());
	  LinkedList<Token> l = p.parse(content);
	  p.executeList(l);
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
		for(String s : robot.code.variables){
			defineVariable(s);
		}
		for(Word w : robot.code.words){
			defineWord(w.name, w.body);
		}
	}
	
	public void defineVariable(String var){
		variables.put(var, new Token(0));
	}
	
	public void defineWord(String name, String body){
		LinkedList<Token> macro = parse(body);
		macros.put(name, macro);
	}
	
	public void run(LinkedList<Token> l) {
		executeList(l);
	}
	
	public LinkedList<Token> parse(String s){
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
	
	public String executeList(LinkedList<Token> l){
		ExpressionState expression = new ExpressionState(ExpressionState.NORMAL, l);
		expressionStack.add(expression);
		while (true){
			expression = expressionStack.peek();
			if(expression.list.isEmpty()){
				break;
			}
			Token t = expression.next();
			boolean ifIgnore = false;
			
			if(expression.type == ExpressionState.MACRO){
				if(t.svalue.equals(";")){
					macros.put(expression.name, expression.macroList);
					expressionStack.pop();
					continue;
				} else {
					expression.addToMacro(t);
					continue;
				}
			}
			
			if(!expression.ifStack.empty()){
				ifIgnore = expression.ifStack.peek().ignore();
			}
			// We're entering a new if block, add a new state.
			if(t.svalue.equals("if")){
				boolean ifFlag;
				if(!ifIgnore){
					ifFlag = executionStack.pop().bvalue;
				} else {
					// This doesn't matter, since we're ignoring anyways.
					ifFlag = true;
				}
				expression.ifStack.add(new IfState(ifFlag, ifIgnore));
				continue;
			} 
			
			if(t.svalue.equals("else")){
				expression.ifStack.peek().isIf = false;
				continue;
			} else if(t.svalue.equals("then")){
				// Move up one ifStack level.
				expression.ifStack.pop();
				continue;
			}

			// If we're in the section that doesn't match the if flag, just ignore stuff.
			if(!expression.ifStack.empty() && expression.ifStack.peek().ignore()){
				continue;
			}
			
			// Finish current expression context here.
			if(t.svalue.equals(";")){
				switch(expression.type){
				case ExpressionState.ILOOP:
				case ExpressionState.ULOOP:
					if(expression.loop()){
						continue;
					} else {
						expressionStack.pop();
						continue;
					}
				}
			}
			
			switch(t.svalue){
			// Macro:
			case ":":
				ExpressionState macroExpression = new ExpressionState(ExpressionState.MACRO, expression.list);
				macroExpression.name = expression.next().svalue;
				expressionStack.add(macroExpression);
				continue;
			case "variable":
				String key = expression.list.pop().svalue;
				defineVariable(key);
				expression.list.pop(); // Pop the ';'
				continue;
			case "do":
				int start = executionStack.pop().ivalue;
				int finish = executionStack.pop().ivalue;
				ExpressionState iloopExpression = new ExpressionState(ExpressionState.ILOOP, expression.list, start, finish);
				expressionStack.add(iloopExpression);
				continue;
			case "begin":
				ExpressionState uloopExpression = new ExpressionState(ExpressionState.ULOOP, expression.list);
				expressionStack.add(uloopExpression);
				continue;
			case "leave":
				expression.leave();
				expressionStack.pop();
				continue;
			case "until":
				expression.cond = executionStack.pop().bvalue;
				continue;
			case "loop":
				// This is handled when we hit ';' in a loop.
				continue;
			case "I":
				executionStack.add(new Token(expression.index));
				continue;
			default:
				if(t.type != Token.SYMBOL){
					executionStack.add(t);
					continue;
				} else if(macros.containsKey(t.svalue)){
					// Add macro into execution context.
					expression.list.addAll(0, macros.get(t.svalue));
				} else {
					executeToken(t);
				}
			}
		}
		return "";
	}
	
	public void init(){
		LinkedList<Token> initCommands = macros.get("init");
		executeList(initCommands);
	}
	
	@SuppressWarnings("unchecked")
	public void turn(){
		LinkedList<Token> turnCommands = (LinkedList<Token>) macros.get("turn").clone();
		executeList(turnCommands);
	}
	
	private void executeToken(Token t){
		switch(t.svalue){
		case ".":
			peek(executionStack);
			break;

		case "random":
			random(executionStack);
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

		case "invert":
			invert(executionStack);
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
			shoot(executionStack);
			break;

		case "move!":
			move(executionStack);
			break;

		case "scan!":
			scan(executionStack);
			break;

		case "identify!":
			identify(executionStack);
			break;

		case "send!":
			send(executionStack);
			break;

		case "mesg?":
			mesg(executionStack);
			break;

		case "recv!":
			recv(executionStack);
			break;

		default:
			executionStack.add(t);
			break;
		}	
	}
	
	private void shoot(Stack<Token> s){
			int shootIr = s.pop().ivalue;
			int shootId = s.pop().ivalue;
			Controller.shoot(robot, shootIr, shootId);
	}

	private void move(Stack<Token> s){
			int moveIr = s.pop().ivalue;
			int moveId = s.pop().ivalue;
			Controller.move(robot, moveId, moveIr);
	}

	private void scan(Stack<Token> s){
			int nearby = Controller.scan(robot);
			s.add(new Token(nearby));
	}
	
	private void mesg(Stack<Token> s){
		int fromTeamMember = s.pop().ivalue;
		boolean hasMessage = Controller.mesg(robot, fromTeamMember);
		s.add(new Token(hasMessage));
	}

	private void recv(Stack<Token> s){
		int fromTeamMember = s.pop().ivalue;
		Token value = Controller.recv(robot, fromTeamMember);
		s.add(value);
	}

	private void send(Stack<Token> s){
			Token sendValue = s.pop();
			int teamMember = s.pop().ivalue;
			boolean sendSuccess = Controller.send(robot, teamMember, sendValue);
			s.add(new Token(sendSuccess));
	}
	
	private void identify(Stack<Token> s){
		int identifier = s.pop().ivalue;
		RobotIdentityData data = Controller.identify(robot, identifier);
		s.add(new Token(data.getHealth()));
		s.add(new Token(data.getDirection()));
		s.add(new Token(data.getRange()));
		s.add(new Token(data.getTeamNumber()));
	}
	
	private void drop(Stack<Token> s){
		s.pop();
	}

	private void peek(Stack<Token> s){
		System.out.println(s.peek().svalue);
	}

	private void random(Stack<Token> s){
		int maximum = s.pop().ivalue;
		int num = (int)(Math.random() * (maximum + 1)); // Need plus 1 for INCLUSIVE
		s.add(new Token(num));
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
		Token t1 = s.pop();
		Token t2 = s.pop();
		Token t3 = s.pop();
		s.add(t2);
		s.add(t1);
		s.add(t3);
	}

	private void add(Stack<Token> s){
		Token a1 = s.pop();
		Token a2 = s.pop();
		if(a1.type == Token.DOUBLE || a2.type == Token.DOUBLE){
			s.add(new Token(a2.dvalue + a1.dvalue));
		} else {
			s.add(new Token(a2.ivalue + a1.ivalue));
		}
	}

	private void mult(Stack<Token> s){
		Token a1 = s.pop();
		Token a2 = s.pop();
		if(a1.type == Token.DOUBLE || a2.type == Token.DOUBLE){
			s.add(new Token(a2.dvalue * a1.dvalue));
		} else {
			s.add(new Token(a2.ivalue * a1.ivalue));
		}
	}

	private void minus(Stack<Token> s){
		Token a1 = s.pop();
		Token a2 = s.pop();
		if(a1.type == Token.DOUBLE || a2.type == Token.DOUBLE){
			s.add(new Token(a2.dvalue - a1.dvalue));
		} else {
			s.add(new Token(a2.ivalue - a1.ivalue));
		}
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
		Token a1 = s.pop();
		Token a2 = s.pop();
		if(a1.type == Token.DOUBLE || a2.type == Token.DOUBLE){
			s.add(new Token(Double.compare(a1.dvalue, a2.dvalue) == 0));
		} else {
			s.add(new Token(a2.ivalue == a1.ivalue));
		}
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

	private void invert(Stack<Token> s){
		boolean a1 = s.pop().bvalue;
		s.add(new Token(!a1));
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
	
	private class IfState{
		boolean flag;
		boolean isIf;
		boolean ignore;
		
		public IfState(boolean flag, boolean ignore){
			this.flag = flag;
			this.isIf = true;
			this.ignore = ignore;
		}
		
		public boolean ignore(){
			return ignore || (flag != isIf);
		}
	}

	private class ExpressionState{
		public int type;
		public static final int MACRO = 1;
		public static final int ILOOP = 2;
		public static final int ULOOP = 3;
		public static final int NORMAL = 4;
		public Stack<IfState> ifStack;
		public String name = null;
		public int index, finish;
		public LinkedList<Token> macroList;
		public LinkedList<Token> startLoopList;
		public LinkedList<Token> list = null;
		public boolean cond;
		@SuppressWarnings("unchecked")
		public ExpressionState(int type, LinkedList<Token> tokenList){
			this.type = type;
			this.list = tokenList;
			this.ifStack = new Stack<IfState>();
			switch(type){
			case MACRO:
				this.macroList = new LinkedList<Token>();
				break;
			case ULOOP:
				this.startLoopList = (LinkedList<Token>)list.clone();
				break;
			default:
				break;
			}
		}
		
		@SuppressWarnings("unchecked")
		public ExpressionState(int type, LinkedList<Token> tokenList, int start, int finish){
			this.type = type;
			this.list = tokenList;
			this.ifStack = new Stack<IfState>();
			this.startLoopList = (LinkedList<Token>)list.clone();
			this.index = start;
			this.finish = finish;
		}
		
		public void addToMacro(Token t){
			macroList.add(t);
		}
		
		@SuppressWarnings("unchecked")
		public boolean loop(){
			this.list = startLoopList;
			this.startLoopList = (LinkedList<Token>)list.clone();
			if(type == ILOOP){
				index++;
				return index <= finish;
			} else {
				// ULoop, go unless true;
				return !cond;
			}
		}
		
		public Token next(){
			return list.pop();
		}

		public void leave(){
			// Cycle through the rest of the junk in the loop, ignoring it.
			while(!next().svalue.equals(";"));
		}
	}
}
