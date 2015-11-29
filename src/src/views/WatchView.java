package views;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controller.Controller;
import models.Cell;

public class WatchView extends View {
	private static final long serialVersionUID = 1L;
	HexGridDisplay hex;
	int diameter;
	final int winHeight = 500;
	final int winWidth = 800;
	JTextArea txtrLogdisplay;
	JScrollPane txtrScrollPane;
	
	public WatchView(int diameter, Cell[][] cells) {
		this.diameter = diameter;
		
		Dimension d = new Dimension(winWidth,winHeight);
		this.setPreferredSize(d);
		setLayout(null);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBounds(480, 10, 60, 25);
		btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                // Continue execution
//                System.out.println("You clicked play");
            	Controller.play();
            }
        });
		add(btnPlay);
		
		JButton btnPause = new JButton("Pause");
		btnPause.setBounds(545, 10, 70, 25);
		btnPause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                // Stop execution
//                System.out.println("You clicked stop");
            	Controller.pause();
            }
        });
		add(btnPause);
		
		JButton btnStep = new JButton("Step");
		btnStep.setBounds(620, 10, 60, 25);
		btnStep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                // Take one step
//                System.out.println("You clicked step");
            	Controller.step();
            }
        });
		add(btnStep);
		
		txtrLogdisplay = new JTextArea();
		txtrLogdisplay.setBounds(480, 50, 291, 300);
		txtrLogdisplay.setWrapStyleWord(true);
		txtrLogdisplay.setLineWrap(true);
		txtrLogdisplay.setEditable(false);
		
		txtrScrollPane = new JScrollPane(txtrLogdisplay);
		txtrScrollPane.setBounds(txtrLogdisplay.getBounds());
		txtrScrollPane.setAutoscrolls(true);
		txtrScrollPane.setVisible(true);
		add(txtrScrollPane);
		
		hex = new HexGridDisplay(470, diameter, cells);
		hex.setBounds(10, 10, 470, winHeight-10);
		add(hex);
		
		JSlider slider = new JSlider();
		slider.setBounds(680, 10, 110, 30);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            int rate = (int)source.getValue();
//		            System.out.println("Rate is set to " + rate);
		            Controller.changeRate(rate);
		        }    
			}
		});
		add(slider);
	}
	
	/**
	 * Call this whenever something happens that should update the display!
	 */
	public void updateDisplay() {
		hex.repaint();
	}
	
	/**
	 * Call this whenever a message should be passed to the log
	 * @param message the message to pass
	 */
	public void updateLog(String message) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
		this.txtrLogdisplay.insert(sdf.format(cal.getTime()) + " --> " + message + "\n-----------------------\n", 0);
	}
	
}
