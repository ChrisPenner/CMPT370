package views;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import controller.Controller;

import javax.swing.JLabel;

public class TeamModule extends JPanel {
	private static final long serialVersionUID = 1L;
	protected int teamNumber;
	final JFileChooser fc = new JFileChooser();

	/**
	 * Create the panel.
	 */
	public TeamModule(int teamNumber) {
		Dimension d = new Dimension(325,100);
		this.setPreferredSize(d);
		setLayout(null);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.setBounds(0, 5, 70, 25);
		btnLoad.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() == btnLoad) {
					int returnVal = fc.showOpenDialog(btnLoad);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = fc.getSelectedFile();
			            Controller.loadRobot(file, teamNumber);
			        }
				}
			}
			
		});
		add(btnLoad);
		
		JTextArea txtrLoadeddisplay = new JTextArea();
		txtrLoadeddisplay.setText("< No robots loaded >");
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
