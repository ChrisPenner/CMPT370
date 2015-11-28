package views;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOverView extends JPanel {
	
	private static final long serialVersionUID = 1L;
	Canvas canvas;
	final int winHeight = 500;
	final int winWidth = 800;
	final int buttonWidth = 175;
	final int buttonHeight = 50;
	
	public GameOverView(int winningTeam) {
		
		Dimension d = new Dimension(winWidth,winHeight);
		this.setPreferredSize(d);
		setLayout(null);
		
		JLabel winLabel = new JLabel("Team " + winningTeam + " wins!");
		winLabel.setBounds(0, (winHeight/2)-25, winWidth, 50);
		winLabel.setFont(getFont().deriveFont(50f));
		winLabel.setHorizontalAlignment(JLabel.CENTER);
		add(winLabel);
		
		setVisible(true);

	}

}
