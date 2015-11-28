package controller;

import models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.hamcrest.Condition.Step;

import parser.Token;
import views.*;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {
	static public LinkedList<Robot>[] teams;
	static int edgeLength = 5;
	static Timer gameTimer;
	static boolean gameIsRunning = false;
	static int timerLoopCount = 0;
	static int gameRate = 50;
	static int currentTurn = 1;
	static JPanel view;
	static JFrame frame;
	static GameBoard gb;
	static AtomicBoolean loopInUse = new AtomicBoolean();

	static int teamNum = 0;
	static int robotNum = 0;
	static boolean step = false;
	
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
		view = new TeamSelectView(frame);
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
		
		for(int i = 0; i < teams.length; i++){
			for(Robot r : teams[i]){
				r.init();
			}
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param team
	 * @return the team list the robot was added to, null if failure
	 */
	public static LinkedList<Robot>[] loadRobot(File file, int team) {
		System.out.print("Adding " + file.getName() + " to team " + team + "...");
		checkGB();

		// Read the whole file.
		try {
			Scanner sn = new Scanner( file );
			String fileString = sn.useDelimiter("\\A").next();
			Robot robot = new Robot();
			robot = Robot.fromJson(fileString);
			if(!gb.addRobot(robot, team)){
				System.out.println("failed.  Team already at max capacity\n");
			}
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
	
	public static void pause() {
		if(gameIsRunning){
			System.out.println("Controller says: Stop was pressed");
			gameIsRunning = false;
		}
	}
	
	public static void step() {
		System.out.println("Controller says: Step was pressed");
		if(gameIsRunning){
			gameIsRunning = false;
//			gameTimer.cancel();
		}
		else{
			step = true;
			gameIsRunning = true;
			gameTimer.schedule(new GameLoop(), 0, 1000/60);
		}
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
		return gb.scan(caller);
	}
	
	public static int hex(Robot caller, int id, int ir){
		return gb.hex(caller, id, ir);
	}

	public static RobotIdentityData identify(Robot caller, int identifier){
		return gb.identify(caller, identifier);
	}
	
	public static boolean send(Robot caller, int teamMember, Token value){
		return gb.send(caller, teamMember, value);
	}
	
	public static boolean mesg(Robot caller, int fromTeamMember){
		return gb.mesg(caller, fromTeamMember);
	}
	
	public static Token recv(Robot caller, int fromTeamMember){
		return gb.recv(caller, fromTeamMember);
	}
	
	private static class GameLoop extends TimerTask {
		
		public void run() {
			if(loopInUse.compareAndSet(false, true)) {
				if(!gameIsRunning){
					this.cancel();
				}
				
				Robot r;
				while(true){
					if(robotNum == 4) {
						teamNum++;
						teamNum = teamNum % 6;
						robotNum = 0;
					}

					try{
						r = teams[teamNum].get(robotNum);
					} catch(Exception e){
						robotNum++;
						continue;
					}
					break;
				}

				
				if(timerLoopCount % (101-gameRate) == 0) {
					System.out.println(teamNum);
//				Robot r = teams[0].getFirst();
//				System.out.println("Scan: " + scan(r));
//				r.turn();
//				move(r, 0, 1);
//				System.out.println("Team 1 Robot 1 (" + r.c.x + ", " + r.c.y + "): " + scan(r));
					r.turn();
					((View) view).updateDisplay();
					System.out.println("Current turn: " + currentTurn);
					currentTurn++;
					timerLoopCount = 0;
					robotNum++;
					if(step){
						step = false;
						gameIsRunning = false;
					}
				}
				timerLoopCount++;
				loopInUse.set(false);
			}
		}
		
	}

}
