package parser;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import models.Robot;

import org.junit.Test;

/**
 * Holder for our unit tests of Parser.
 */
public class ParserTest {
	String baseJson = "{}";

	/**
	 * Test whether the parser correctly handles 'True'
	 */
  @Test
  public void parseTrue() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> actual =  p.parse("true");
	  Token first = actual.pop();
	  assertEquals(first.bvalue, true);
	  assertEquals(first.svalue, "true");
	  assertEquals(first.type, Token.BOOLEAN);
  }

	/**
	 * Test whether the parser correctly handles 'False'
	 */
  @Test
  public void parseFalse() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> actual =  p.parse("false");
	  Token first = actual.pop();
	  assertEquals(first.bvalue, false);
	  assertEquals(first.svalue, "false");
	  assertEquals(first.type, Token.BOOLEAN);
  }

	/**
	 * Test whether the parser correctly handles integers.
	 */
  @Test
  public void parseInt() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> actual =  p.parse("1");
	  Token first = actual.pop();
	  assertEquals(first.ivalue, 1);
	  assertEquals(first.dvalue, 1.0, 0.001);
	  assertEquals(first.svalue, "1");
	  assertEquals(first.type, Token.INT);
  }

	/**
	 * Test whether the parser correctly handles floats
	 */
  @Test
  public void parseDouble() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> actual =  p.parse("3.14");
	  Token first = actual.pop();
	  assertEquals(first.ivalue, 3);
	  assertEquals(first.dvalue, 3.14, 0.0001);
	  assertEquals(first.svalue, "3.14");
	  assertEquals(first.type, Token.DOUBLE);
  }

	/**
	 * Test whether the parser correctly handles arbitrary symbols. 
	 */
  @Test
  public void parseSymbol() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> actual =  p.parse("someSymbol");
	  Token first = actual.pop();
	  assertEquals(first.svalue, "someSymbol");
	  assertEquals(first.type, Token.SYMBOL);
  }
  
	/**
	 * Test whether the parser correctly handles strings.
	 */
  @Test
  public void parseStrings() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> actual =  p.parse(".\"Testing this out\"");
	  Token first = actual.pop();
	  assertEquals(first.svalue, "Testing this out");
	  assertEquals(first.type, Token.STRING);
  }
  
	/**
	 * Test whether the parser correctly handles multiple tokens
	 */
  @Test
  public void parseMultipleTokens() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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
  
  
	/**
	 * Test whether the parser correctly handles addition
	 */
  @Test
  public void runAddition() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("1 3 +");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.ivalue, 4);
  } 

	/**
	 * Test whether the parser correctly handles multiplication
	 */
  @Test
  public void runMultiplication() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("2 3 *");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.ivalue, 6);
  } 
  
	/**
	 * Test whether the parser correctly handles "or"
	 */
  @Test
  public void runOr() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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
  
	/**
	 * Test whether the parser correctly handles 'and'
	 */
  @Test
  public void runAnd() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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

	/**
	 * Test whether the parser correctly handles 'invert'
	 */
  @Test
  public void runInvert() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("true invert");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("false invert");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);
  } 

	/**
	 * Test whether the parser correctly handles 'drop'
	 */
  @Test
  public void runDrop() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("1 2 3 drop");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.ivalue, 2);
  } 

	/**
	 * Test whether the parser correctly handles 'dup'
	 */
  @Test
  public void runDup() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("2 dup");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  assertEquals(first.ivalue, 2);
	  assertEquals(second.ivalue, 2);
  } 

	/**
	 * Test whether the parser correctly handles 'swap'
	 */
  @Test
  public void runSwap() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("1 2 swap");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  assertEquals(first.ivalue, 1);
	  assertEquals(second.ivalue, 2);
  }

	/**
	 * Test whether the parser correctly handles 'rot'
	 */
  @Test
  public void runRot() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("3 2 1 rot");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  Token third = p.executionStack.pop();
	  assertEquals(3, first.ivalue);
	  assertEquals(1, second.ivalue);
	  assertEquals(2, third.ivalue);
  }

	/**
	 * Test whether the parser correctly handles 'mod'
	 */
  @Test
  public void runMod() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("9 4 /mod");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  assertEquals(first.ivalue, 2);
	  assertEquals(second.ivalue, 1);
  }

	/**
	 * Test whether the parser correctly handles '>'
	 */
  @Test
  public void runGt() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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

	/**
	 * Test whether the parser correctly handles '>='
	 */
  @Test
  public void runGte() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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

	/**
	 * Test whether the parser correctly handles '<'
	 */
  @Test
  public void runLt() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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

	/**
	 * Test whether the parser correctly handles '<=
	 */
  @Test
  public void runLte() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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

	/**
	 * Test whether the parser correctly handles '='
	 */
  @Test
  public void runEq() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("1 2 =");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.bvalue, false);

	  list =  p.parse("2 2 =");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.bvalue, true);
  }

	/**
	 * Test whether the parser correctly handles 'variable'
	 */
  @Test
  public void runVariables() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("variable test ; 5 test ! test ?");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(first.ivalue, 5);
	  
	  // Should initialize to 0.
	  list = p.parse("variable other ; other ?");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(first.ivalue, 0);
  }

	/**
	 * Test whether the parser correctly handles 'if'
	 */
  @Test
  public void runIf() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse("true if .\"yup\" else .\"nope\" then");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals("yup", first.svalue);

	  list =  p.parse("false if .\"yup\" else .\"nope\" then");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals("nope", first.svalue);
  }
  
	/**
	 * Test whether the parser correctly handles nested 'if's
	 */
  @Test
  public void nestedIf() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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
  
	/**
	 * Test whether the parser correctly handles macros (ie. ':')
	 */
  @Test
  public void runMacro() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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
	  
	  list =  p.parse(": divides? /mod drop 0 = ; 15 5 divides?");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(true, first.bvalue);

	  list =  p.parse(": increment dup ? 1 + swap ! ; "
	  		+ "variable test ; test increment test increment test ?");
	  p.run(list);
	  first = p.executionStack.pop();
	  assertEquals(2, first.ivalue);
  }
  
	/**
	 * Test whether the defineWord method works
	 */
  @Test
  public void testDefineWord() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  p.defineWord("testmacro", "1 25 +");
	  LinkedList<Token> list =  p.parse("testmacro testmacro +");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(52, first.ivalue);
  }

	/**
	 * Test whether the defineVariable method works
	 */
  @Test
  public void testDefineVariable() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  p.defineVariable("testvar");
	  LinkedList<Token> list =  p.parse("testvar ?");
	  p.run(list);
	  Token first = p.executionStack.pop();
	  assertEquals(0, first.ivalue);
  }

	/**
	 * Test the behaviour of 'do' loops.
	 */
  @Test
  public void runDoLoop() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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
  
	/**
	 * Test the behaviour of nested 'do' loops.
	 */
  @Test
  public void runNestedDoLoop() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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
  
	/**
	 * Test the behaviour of 'until' loops.
	 */
  @Test
  public void runUntilLoop() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  // Loop until iter is 5.
	  LinkedList<Token> list =  p.parse(
			  "variable iter ; "
			  // Store an extra counter for assertions.
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

	/**
	 * Test the behaviour of nested 'until' loops.
	 */
  @Test
  public void nestedUntilLoop() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  // Loop until iter is 5.
	  LinkedList<Token> list =  p.parse(
			  ": divides? /mod drop 0 = ; "
			  + ": increment dup ? 1 + swap ! ; "
			  + "variable iter ; "
			  + "1 iter !  "
			  + "begin "
			  +   "begin "
			  +     "iter ? 5 divides?  "
			  + "until iter increment ; "
			  + "iter ? 21 = iter ? 41 = or if iter ? then "
			  + "iter ? 51 = "
			  + "iter increment until ; "
			  + "iter ?"
			  );
	  
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  Token third = p.executionStack.pop();
	  assertEquals(21, third.ivalue);
	  assertEquals(41, second.ivalue);
	  // Increment comes regardless of whether we loop.
	  assertEquals(52, first.ivalue);
  }
	  
	/**
	 * Test the behaviour of everything together.
	 */
  @Test
  public void integrationTest() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
	  LinkedList<Token> list =  p.parse(
			  // Define some helpers.
			  ": divides? /mod drop 0 = ; "
			  + ": increment dup ? 1 + swap ! ; "
			  // Define a variable, they default to 0.
			  + "variable iter ; "
			  // begin an outer loop.
			  + "begin "
			    // begin an inner loop.
			  +   "4 1 do "
			      // Do a nested for loop only if our current I is even.
			  +     "I 2 divides? "
			        // Make sure if statements work with loops too.
			  +     "if "
			  +       "5 1 do "
			  +       "loop iter increment ; "
			  +     "then "
			  +   "loop ; "
			  // Throw the current counter on the stack for testing after each iteration.
			  + "iter ? "
			  // Quit when we reach exactly 50.
			  + "iter ? 50 = "
			  + "until ; "
			  );
	  
	  p.run(list);
	  Token first = p.executionStack.pop();
	  Token second = p.executionStack.pop();
	  Token third = p.executionStack.pop();
	  Token fourth = p.executionStack.pop();
	  Token fifth = p.executionStack.pop();
	  assertEquals(10, fifth.ivalue);
	  assertEquals(20, fourth.ivalue);
	  assertEquals(30, third.ivalue);
	  assertEquals(40, second.ivalue);
	  assertEquals(50, first.ivalue);
  }

	/**
	 * Test the behaviour of 'random' keyword
	 */
  @Test
  public void runRandom() {
	  Parser p = new Parser(Robot.fromJson(baseJson));
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