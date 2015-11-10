package parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.LinkedList;

import models.Robot;

import org.junit.Test;

public class ParserTest {
  @Test
  public void parseTrue() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> actual =  p.parse("true");
	  Token first = actual.pop();
	  assertEquals(first.bvalue, true);
	  assertEquals(first.svalue, "true");
	  assertEquals(first.type, Token.BOOLEAN);
  }

  @Test
  public void parseFalse() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> actual =  p.parse("false");
	  Token first = actual.pop();
	  assertEquals(first.bvalue, false);
	  assertEquals(first.svalue, "false");
	  assertEquals(first.type, Token.BOOLEAN);
  }

  @Test
  public void parseInt() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> actual =  p.parse("1");
	  Token first = actual.pop();
	  assertEquals(first.ivalue, 1);
	  assertEquals(first.dvalue, 1.0, 0.001);
	  assertEquals(first.svalue, "1");
	  assertEquals(first.type, Token.INT);
  }

  @Test
  public void parseDouble() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> actual =  p.parse("3.14");
	  Token first = actual.pop();
	  assertEquals(first.ivalue, 3);
	  assertEquals(first.dvalue, 3.14, 0.0001);
	  assertEquals(first.svalue, "3.14");
	  assertEquals(first.type, Token.DOUBLE);
  }

  @Test
  public void parseSymbol() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> actual =  p.parse("someSymbol");
	  Token first = actual.pop();
	  assertEquals(first.svalue, "someSymbol");
	  assertEquals(first.type, Token.SYMBOL);
  }
  
  @Test
  public void parseStrings() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> actual =  p.parse(".\"Testing this out\"");
	  Token first = actual.pop();
	  assertEquals(first.svalue, "Testing this out");
	  assertEquals(first.type, Token.STRING);
  }
  
  @Test
  public void parseMultipleTokens() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> actual =  p.parse("1 3 + .\"A String\" drop");
	  Token first = actual.pop();
	  Token second = actual.pop();
	  Token third = actual.pop();
	  Token fourth = actual.pop();
	  Token fifth = actual.pop();
	  assertEquals(first.svalue, "1");
	  assertEquals(second.svalue, "3");
	  assertEquals(third.svalue, "+");
	  assertEquals(fourth.svalue, "A String");
	  assertEquals(fifth.svalue, "drop");
  }
  
  
  //Test running a lists of tokens.
  @Test
  public void runAddition() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("1 3 +");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.ivalue, 4);
  } 

  @Test
  public void runMultiplication() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("2 3 *");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.ivalue, 6);
  } 
  
  @Test
  public void runOr() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("true false or");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);

	  list =  p.parse("true true or");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);

	  list =  p.parse("false false or");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);
  } 
  
  @Test
  public void runAnd() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("true false and");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("true true and");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);

	  list =  p.parse("false false and");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);
  } 

  @Test
  public void runInvert() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("true invert");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("false invert");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);
  } 

  @Test
  public void runDrop() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("1 2 3 drop");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.ivalue, 2);
  } 

  @Test
  public void runDup() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("2 dup");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  assertEquals(first.ivalue, 2);
	  assertEquals(second.ivalue, 2);
  } 

  @Test
  public void runSwap() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("1 2 swap");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  assertEquals(first.ivalue, 1);
	  assertEquals(second.ivalue, 2);
  }

  @Test
  public void runRot() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("3 2 1 rot");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  Token third = p.executionStack.pop();
	  assertEquals(3, first.ivalue);
	  assertEquals(1, second.ivalue);
	  assertEquals(2, third.ivalue);
  }

  @Test
  public void runMod() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("9 4 /mod");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  assertEquals(first.ivalue, 2);
	  assertEquals(second.ivalue, 1);
  }

  @Test
  public void runGt() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("1 2 >");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("2 1 >");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);

	  list =  p.parse("1 1 >");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);
  }

  @Test
  public void runGte() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("1 2 >=");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("2 1 >=");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);

	  list =  p.parse("1 1 >=");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);
  }

  @Test
  public void runLt() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("1 2 <");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);

	  list =  p.parse("2 1 <");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("1 1 <");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);
  }

  @Test
  public void runLte() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("1 2 <=");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);

	  list =  p.parse("2 1 <=");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("1 1 <=");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);
  }

  @Test
  public void runEq() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("1 2 =");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("2 2 =");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);
  }

  @Test
  public void runVariables() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("variable test ; 5 test ! test ?");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.ivalue, 5);
  }

  @Test
  public void runIf() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse("true if .\"yup\" else .\"nope\" then");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals("yup", first.svalue);

	  list =  p.parse("false if .\"yup\" else .\"nope\" then");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals("nope", first.svalue);
  }
  
  @Test
  public void nestedIf() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse(
			 "true if true if .\"True, True\" else .\"True, False\" then else true if .\"False, True\" else .\"False, False\" then then" 
			  );
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals("True, True", first.svalue);

	  list =  p.parse(
			 "false if true if .\"True, True\" else .\"True, False\" then else true if .\"False, True\" else .\"False, False\" then then" 
			  );
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals("False, True", first.svalue);

	  list =  p.parse(
			 "true if false if .\"True, True\" else .\"True, False\" then else true if .\"False, True\" else .\"False, False\" then then" 
			  );
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals("True, False", first.svalue);

	  list =  p.parse(
			 "false if false if .\"True, True\" else .\"True, False\" then else false if .\"False, True\" else .\"False, False\" then then" 
			  );
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals("False, False", first.svalue);
  }
  
  @Test
  public void runMacro() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse(": testmacro 1 25 + ; testmacro testmacro +");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(52, first.ivalue);

	  list =  p.parse(": negative? 0 >= if .\"Nonnegative\" else .\"Negative\" then ; -5 negative?");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals("Negative", first.svalue);

	  // Macro with 'if'
	  list =  p.parse(": equal? = if .\"equal\" else .\"unequal\" then ; 1 2 equal?");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals("unequal", first.svalue);
  }
  
  @Test
  public void runDoLoop() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse(
			  "5 0 do "
			  + "I "
			  + "I 3 = if leave then "
			  + "loop ;"
			  );
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(3, first.ivalue);

  }
  
  @Test
  public void runNestedDoLoop() throws IOException {
	  Parser p = new Parser(new Robot());
	  // Make sure 'I' is maintained separately on the inside and the outside.
	  LinkedList<Token> list =  p.parse(
			  "variable outer ; "
			  + "6 4 do "
			  +   "I outer ! "
			  +   "3 2 do "
			  +     "I outer ? * "
			  +   "loop ; "
			  + "loop ;"
			  );
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  Token third = p.executionStack.pop();
	  Token fourth = p.executionStack.pop();
	  Token fifth = p.executionStack.pop();
	  Token sixth = p.executionStack.pop();
	  assertEquals(8, sixth.ivalue);
	  assertEquals(12, fifth.ivalue);
	  assertEquals(10, fourth.ivalue);
	  assertEquals(15, third.ivalue);
	  assertEquals(12, second.ivalue);
	  assertEquals(18, first.ivalue);
  }
  
  @Test
  public void runUntilLoop() throws IOException {
	  Parser p = new Parser(new Robot());
	  // Loop until iter is 5.
	  LinkedList<Token> list =  p.parse(
			  "variable iter ; "
			  + "0 iter ! "
			  + "begin iter ? iter ? 1 + iter ! iter ? 5 >= until ;"
			  );
	  
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  Token third = p.executionStack.pop();
	  Token fourth = p.executionStack.pop();
	  Token fifth = p.executionStack.pop();
	  assertEquals(0, fifth.ivalue);
	  assertEquals(1, fourth.ivalue);
	  assertEquals(2, third.ivalue);
	  assertEquals(3, second.ivalue);
	  assertEquals(4, first.ivalue);
  }

  @Test
  public void runRandom() throws IOException {
	  Parser p = new Parser(new Robot());
	  LinkedList<Token> list =  p.parse(
			  "25 random 25 random 25 random 25 random 25 random 25 random"
			  );
	  
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  Token third = p.executionStack.pop();
	  Token fourth = p.executionStack.pop();
	  Token fifth = p.executionStack.pop();
	  Token sixth = p.executionStack.pop();
	  assertEquals(Token.INT, first.type);
	  assert(first.ivalue >= 0 && first.ivalue <= 25);
	  assert(second.ivalue >= 0 && first.ivalue <= 25);
	  assert(third.ivalue >= 0 && first.ivalue <= 25);
	  assert(fourth.ivalue >= 0 && first.ivalue <= 25);
	  assert(fifth.ivalue >= 0 && first.ivalue <= 25);
	  assert(sixth.ivalue >= 0 && first.ivalue <= 25);
  }

}