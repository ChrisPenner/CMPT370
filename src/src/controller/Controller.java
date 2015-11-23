package controller;

import models.*;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import views.*;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
	
	LinkedList<Robot>[] teams;
	int edgeLength;
	static Timer gameTimer;
	
	static boolean gameIsRunning = false;
	
	static int timerLoopCount = 0;
	static int gameRate = 50;
	static int currentTurn = 1;
	
	@SuppressWarnings("unchecked")
	public Controller() {
		// initialize the controller
		teams = (LinkedList<Robot>[]) new LinkedList<?>[6];
		gameTimer = new Timer();
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
		
		gameIsRunning = true;
		gameTimer.schedule(new GameLoop(), 0, 1000/60);
		
	}
	
	public static void stop() {
		System.out.println("Controller: Stop was pressed");
		gameIsRunning = false;
	}
	
	public static void step() {
		System.out.println("Controller: Step was pressed");
	}
	
	public static void changeRate(int rate) {
		System.out.println("Controller: Rate was changed to " + rate);
		gameRate = rate;
	}
	
	private static class GameLoop extends TimerTask {
		
		public void run() {
			
			if(timerLoopCount % (101-gameRate) == 0) {
				System.out.println("Current turn: " + currentTurn);
				currentTurn++;
				timerLoopCount = 0;
			}
			timerLoopCount++;
			
			if(!gameIsRunning){
				this.cancel();
			}
		}
		
	}

}
