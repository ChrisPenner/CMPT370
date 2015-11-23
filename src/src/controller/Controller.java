package controller;

import models.*;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import views.*;

public class Controller {
	
	LinkedList<Robot>[] teams;
	int edgeLength;
	
	@SuppressWarnings("unchecked")
	public Controller() {
		// initialize the controller
		teams = (LinkedList<Robot>[]) new LinkedList<?>[6];
		
	}
	
	public void start() {
		// starts the actual game program
		
		// Will want to display the main menu after it is implemented
		
		edgeLength = 4;
		JFrame frame = new JFrame("RobotSport");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameBoard gb = new GameBoard(teams, edgeLength);
		JPanel view = new WatchView(edgeLength, gb.getCells());
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void play() {
		System.out.println("Controller: Play was pressed");
	}
	
	public static void stop() {
		System.out.println("Controller: Stop was pressed");
	}
	
	public static void step() {
		System.out.println("Controller: Step was pressed");
	}
	
	public static void changeRate(int rate) {
		System.out.println("Controller: Rate was changed to " + rate);
	}

}
