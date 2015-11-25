package controller;

import models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import views.*;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
	
	static LinkedList<Robot>[] teams;
	static int edgeLength = 4;
	static Timer gameTimer;
	static boolean gameIsRunning = false;
	static int timerLoopCount = 0;
	static int gameRate = 50;
	static int currentTurn = 1;
	static JPanel view;
	static JFrame frame;
	static GameBoard gb;
	
	
	@SuppressWarnings("unchecked")
	public Controller() {
		// initialize the controller
		teams = (LinkedList<Robot>[]) new LinkedList<?>[6];
		gameTimer = new Timer();
	}
	
	/*
	 * Checks to ensure that gb is initialized
	 */
	private static void checkGB() {
		if(gb == null) {
			gb = new GameBoard(teams, edgeLength);
		}
	}
	
	public static void watchMatchButtonPressed() {
		view = new TeamSelectView();
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void confirmRobotsButtonPressed() {
		checkGB();
		view = new WatchView(edgeLength, gb.getCells());
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * 
	 * @param file
	 * @param team
	 * @return the team list the robot was added to, null if failure
	 */
	public static LinkedList<Robot>[] loadRobot(File file, int team) {
		System.out.println("Adding " + file.getName() + " to team " + team + ".\n");
		checkGB();

		// Read the whole file.
		try {
			Scanner sn = new Scanner( file );
			String fileString = sn.useDelimiter("\\A").next();
			Robot robot = Robot.fromJson(fileString);
			gb.addRobot(robot, team);
			sn.close();
			return gb.getTeams();
		} catch (FileNotFoundException e) {
			// Crash
			e.printStackTrace();
			return null;
		}
	}
	
	public static void instantModeButtonPressed() {
		System.out.println("Controller says: Instant Mode Button Pressed");
	}

	public static void testBenchButtonPressed() {
		System.out.println("Controller says: Test Bench Button Pressed");
	}

	public void start() {
		// starts the actual game program
		
		// Will want to display the main menu after it is implemented
		
		frame = new JFrame("RobotSport");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view = new MainMenuView();
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void play() {
		if(!gameIsRunning){
			System.out.println("Controller says: Play was pressed");
			
			gameIsRunning = true;
			gameTimer.schedule(new GameLoop(), 0, 1000/60);
		}
		
	}
	
	public static void stop() {
		if(gameIsRunning){
			System.out.println("Controller says: Stop was pressed");
			gameIsRunning = false;
		}
	}
	
	public static void step() {
		System.out.println("Controller says: Step was pressed");
	}
	
	public static void changeRate(int rate) {
		System.out.println("Controller says: Rate was changed to " + rate);
		gameRate = rate;
	}

	public static void shoot(Robot caller, int id, int ir) {
		gb.shoot(caller, id, ir);
	}
	
	public static void move(Robot caller, int id, int ir) {
		gb.move(caller, id, ir);
	}
	
	public static int scan(Robot caller){
		gb.scan(caller);
		return 0;
	}
	
	public static RobotIdentityData identify(Robot caller, int identifier){
		return gb.identify(caller, identifier);
	}
	
	static boolean send(Robot caller, Robot target, String value){
		gb.send(caller, target, value);
		return true;
	}
	
	static boolean mesg(Robot caller){
		gb.mesg(caller);
		return true;
	}
	
	static String recv(Robot caller){
		gb.recv(caller);
		return "Message contents";
	}
	
	private static class GameLoop extends TimerTask {
		
		public void run() {
			if(!gameIsRunning){
				this.cancel();
			}
			
			if(timerLoopCount % (101-gameRate) == 0) {
				((View) view).updateDisplay();
				System.out.println("Current turn: " + currentTurn);
				currentTurn++;
				timerLoopCount = 0;
			}
			timerLoopCount++;
		}
		
	}

}
