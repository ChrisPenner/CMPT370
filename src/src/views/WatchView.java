package views;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import models.Cell;

public class WatchView extends View {
	private static final long serialVersionUID = 1L;
	Canvas canvas;
	HexGridDisplay hex;
	int diameter;
	final int winHeight = 500;
	final int winWidth = 800;
	
	public WatchView(int diameter, Cell[][] cells) {
		this.diameter = diameter;
		
		Dimension d = new Dimension(winWidth,winHeight);
		this.setPreferredSize(d);
		setLayout(null);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(490, 10, 60, 23);
		add(btnPlay);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(555, 10, 60, 23);
		add(btnStop);
		
		JButton btnStep = new JButton("Step");
		btnStep.setBounds(620, 10, 60, 23);
		add(btnStep);
		
		JTextArea txtrLogdisplay = new JTextArea();
		txtrLogdisplay.setBounds(480, 50, 291, 300);
		add(txtrLogdisplay);
		
		hex = new HexGridDisplay(470, diameter, cells);
		hex.setBounds(10, 10, 470, winHeight-10);
		add(hex);
		
		JSlider slider = new JSlider();
		slider.setBounds(682, 8, 96, 26);
		add(slider);
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
