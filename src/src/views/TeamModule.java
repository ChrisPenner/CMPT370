package views;

import javax.swing.JPanel;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;

public class TeamModule extends JPanel {
	private static final long serialVersionUID = 1L;
	protected int teamNumber;

	/**
	 * Create the panel.
	 */
	public TeamModule(int teamNumber) {
		Dimension d = new Dimension(325,100);
		this.setPreferredSize(d);
		setLayout(null);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setBounds(0, 5, 70, 25);
		add(btnLoad);
		
		JTextArea txtrLoadeddisplay = new JTextArea();
		txtrLoadeddisplay.setText("");
		txtrLoadeddisplay.setBounds(75, 5, 250, 95);
		txtrLoadeddisplay.setEditable(false);
		add(txtrLoadeddisplay);
		
		JLabel lblLoaded = new JLabel("Loaded");
		lblLoaded.setBounds(20, 40, 46, 14);
		add(lblLoaded);
		
		JLabel lblRobots = new JLabel("Robots:");
		lblRobots.setBounds(20, 60, 46, 14);
		add(lblRobots);

	}
}
