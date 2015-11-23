package main;

import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import models.GameBoard;
import views.WatchView;
import models.Robot;

public class Main {	
	public static void main(String[] args) {
		@SuppressWarnings("unchecked")
		LinkedList<Robot>[] teams = (LinkedList<Robot>[]) new LinkedList<?>[6];
		
		int edgeLength = 4;
		JFrame frame = new JFrame("Robot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GameBoard gb = new GameBoard(teams, edgeLength);
		JPanel view = new WatchView(edgeLength, gb.getCells());
		frame.setContentPane(view);
		frame.pack();
		frame.setVisible(true);
	}
}
