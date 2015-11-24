package views;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.Controller;

public class MainMenuView extends JPanel {
	private static final long serialVersionUID = 1L;
	Canvas canvas;
	HexGridDisplay hex;
	int diameter;
	final int winHeight = 500;
	final int winWidth = 800;
	final int buttonWidth = 175;
	final int buttonHeight = 50;
	JTextArea txtrLogdisplay;
	
	public MainMenuView() {
		
		Dimension d = new Dimension(winWidth,winHeight);
		this.setPreferredSize(d);
		setLayout(null);
		
		JLabel labelTitle = new JLabel("RoboSport 360");
		labelTitle.setHorizontalAlignment(JLabel.CENTER);
		labelTitle.setFont(labelTitle.getFont().deriveFont(50f));
		labelTitle.setBounds(0, d.height/4-d.height/8, d.width, 50);
		add(labelTitle);
		
		JLabel labelGroup = new JLabel("Group A3");
		labelGroup.setHorizontalAlignment(JLabel.CENTER);
		labelGroup.setFont(labelGroup.getFont().deriveFont(25f));
		labelGroup.setBounds(0, d.height/4, d.width, 50);
		add(labelGroup);

		JButton btnWatchMatch = new JButton("Watch Match");
		btnWatchMatch.setBounds((d.width/2)-(buttonWidth/2), d.height/2, buttonWidth, buttonHeight);
		btnWatchMatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                // Continue execution
            	Controller.watchMatchButtonPressed();
            }
        });
		add(btnWatchMatch);

		JButton btnInstantMode = new JButton("Instant Mode");
		btnInstantMode.setBounds((d.width/2)-(buttonWidth/2), (d.height/2)+buttonHeight, buttonWidth, buttonHeight);
		btnInstantMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                // Continue execution
            	Controller.instantModeButtonPressed();
            }
        });
		add(btnInstantMode);

		JButton btnTestBench = new JButton("Test Bench");
		btnTestBench.setBounds((d.width/2)-(buttonWidth/2), (d.height/2)+(2*buttonHeight), buttonWidth, buttonHeight);
		btnTestBench.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                // Continue execution
            	Controller.testBenchButtonPressed();
            }
        });
		add(btnTestBench);

	}
	
}

