package main;

import java.io.IOException;

import parser.Parser;

public class Main {
	public static void main(String[] args) throws IOException{
	  Parser p = new Parser("/Users/chris/Desktop/370.txt");
	  p.parse();
	}
}
