package views;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import controller.Controller;
import models.Robot;

import javax.swing.JLabel;

public class TeamModule extends JPanel {
	private static final long serialVersionUID = 1L;
	protected int teamNumber;
	static final JFileChooser fc = new JFileChooser();

	/**
	 * Create the panel.
	 */
	public TeamModule(int teamNumber) {
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
		
		Dimension d = new Dimension(325,100);
		this.setPreferredSize(d);
		setLayout(null);

		JTextArea txtrLoadeddisplay = new JTextArea();
		txtrLoadeddisplay.setText("< No robots loaded >");
		txtrLoadeddisplay.setBounds(75, 5, 250, 95);
		txtrLoadeddisplay.setEditable(false);
		add(txtrLoadeddisplay);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setBounds(0, 5, 70, 25);
		btnLoad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnLoad) {
					int returnVal = fc.showOpenDialog(btnLoad);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            LinkedList<Robot>[] teams = Controller.loadRobot(file, teamNumber);
			            if(teams != null) {
			            	String s = "";
			            	int i = 1;
			            	for(Robot r : teams[teamNumber - 1]) {
			            		s = s + i + ": " + r.name + '\n';
			            		i++;
			            	}
			            	txtrLoadeddisplay.setText(s);
			            }
			            if(teams[teamNumber - 1].size() == 4){
			            	btnLoad.setEnabled(false);
			            }
			        }
				}
			}
			
		});
		add(btnLoad);
		
		JLabel lblLoaded = new JLabel("Loaded");
		lblLoaded.setHorizontalAlignment(JLabel.RIGHT);
		lblLoaded.setBounds(0, 40, 60, 14);
		add(lblLoaded);
		
		JLabel lblRobots = new JLabel("Robots:");
		lblRobots.setHorizontalAlignment(JLabel.RIGHT);
		lblRobots.setBounds(0, 60, 60, 14);
		add(lblRobots);
	}
}
