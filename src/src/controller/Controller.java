package controller;

import models.*;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import views.*;

public class Controller {
	
	LinkedList<Robot>[] teams;
	
	@SuppressWarnings("unchecked")
	public Controller() {
		// initialize the controller
		teams = (LinkedList<Robot>[]) new LinkedList<?>[6];
		
		int edgeLength = 4;
		JFrame frame = new JFrame("RobotSport");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameBoard gb = new GameBoard(teams, edgeLength);
		JPanel view = new WatchView(edgeLength, gb.getCells());
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public void start() {
		// starts the actual game program
	}

}
