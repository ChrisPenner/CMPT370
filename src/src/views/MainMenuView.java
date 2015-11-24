package views;

import java.awt.Component;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.awt. Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.GameBoard;
import models.Robot;

import javax.swing.BoxLayout;
import javax.swing.Box;


public class MainMenuView extends JPanel{
	
	
	
	public static final long serialVersionUID = 1;
	
	static final int FONT_SIZE = 72;
	static final int BUTTON_SPACER_SIZE = 30;
	static final int BUTTON_WIDTH = 140;
	static final int BUTTON_HEIGHT = 30;

	
	public void MainMenu(int width, int height, ActionListener listener)
	{
		setSize(width, height);
		setBackground(Color.YELLOW);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		add(Box.createRigidArea(new Dimension(0, height/5)));
		
		JLabel label = new JLabel("Robort Sport");
		label.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
		label.setForeground(Color.BLACK);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(label);
		
		add(Box.createRigidArea(new Dimension(0, height/5)));
		
		JButton newGameButton = new JButton("Watch Match");
		newGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		newGameButton.setBackground(Color.YELLOW);
		newGameButton.setForeground(Color.BLACK);
		newGameButton.setActionCommand("WatchMatch");
		newGameButton.addActionListener(listener);
		newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(newGameButton);
		
		add(Box.createRigidArea(new Dimension(0, BUTTON_SPACER_SIZE)));
		
		JButton highScoreButton = new JButton("Instant Result");
		highScoreButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		highScoreButton.setBackground(Color.YELLOW);
	
		highScoreButton.setForeground(Color.BLACK);
		highScoreButton.setActionCommand("InstantResult");
		highScoreButton.addActionListener(listener);
		highScoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(highScoreButton);
		
		add(Box.createRigidArea(new Dimension(0, BUTTON_SPACER_SIZE)));
		
		JButton quitButton = new JButton("Test Bench");
		quitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		quitButton.setBackground(Color.YELLOW);
		quitButton.setForeground(Color.BLACK);
		quitButton.setActionCommand("TestBench");
		quitButton.addActionListener(listener);
		quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(quitButton);
	}
	


}

