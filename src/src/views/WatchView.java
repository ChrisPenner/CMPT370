package views;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JTextArea;

import models.Cell;

public class WatchView extends View {
	private static final long serialVersionUID = 1L;
	Canvas canvas;
	HexGridDisplay hex;
	int diameter;
	
	public WatchView(int diameter, Cell[][] cells) {
		this.diameter = diameter;
		
		Dimension d = new Dimension(800,400);
		this.setPreferredSize(d);
		setLayout(null);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(288, 11, 55, 23);
		add(btnStop);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(223, 11, 55, 23);
		add(btnPlay);
		
		JButton btnStep = new JButton("Step");
		btnStep.setBounds(353, 11, 55, 23);
		add(btnStep);
		
		JTextArea txtrLogdisplay = new JTextArea();
		txtrLogdisplay.setBounds(223, 62, 187, 186);
		add(txtrLogdisplay);
		
		hex = new HexGridDisplay(200, diameter, cells);
		hex.setBounds(10, 10, 200, 200);
		add(hex);
	}
	
	/**
	 * Call this whenever something happens that should update the display!
	 */
	public void updateDisplay() {
		
	}
	
	/**
	 * Call this whenever a message should be passed to the log
	 * @param message the message to pass
	 */
	public void updateLog(String message) {
		
	}
	
}
