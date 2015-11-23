package views;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
		btnPlay.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e)
            {
                // Continue exection
                System.out.println("You clicked play");
            }
        });
		add(btnPlay);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(555, 10, 60, 23);
		btnStop.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
                // Stop execution
                System.out.println("You clicked stop");
            }
        });
		add(btnStop);
		
		JButton btnStep = new JButton("Step");
		btnStep.setBounds(620, 10, 60, 23);
		btnStep.addActionListener(new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
                // Take one step
                System.out.println("You clicked step");
            }
        });
		add(btnStep);
		
		JTextArea txtrLogdisplay = new JTextArea();
		txtrLogdisplay.setBounds(480, 50, 291, 300);
		add(txtrLogdisplay);
		
		hex = new HexGridDisplay(470, diameter, cells);
		hex.setBounds(10, 10, 470, winHeight-10);
		add(hex);
		
		JSlider slider = new JSlider();
		slider.setBounds(680, 10, 110, 30);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            int rate = (int)source.getValue();
		            System.out.println("Rate is set to " + rate);
		        }    
			}
		});
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
