package views;

import javax.swing.JPanel;

import controller.Controller;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class TeamSelectView extends JPanel {
	private static final long serialVersionUID = 1L;
	final int winHeight = 500;
	final int winWidth = 800;

	/**
	 * Create the panel.
	 */
	public TeamSelectView() {
		Dimension d = new Dimension(winWidth,winHeight);
		this.setPreferredSize(d);
		setLayout(null);
		
		JLabel lblTeam1 = new JLabel("1");
		lblTeam1.setBounds(35, 100, 46, 14);
		add(lblTeam1);
		
		JLabel lblTeam2 = new JLabel("2");
		lblTeam2.setBounds(35, 240, 46, 14);
		add(lblTeam2);
		
		JLabel lblTeam3 = new JLabel("3");
		lblTeam3.setBounds(35, 390, 46, 14);
		add(lblTeam3);
		
		JLabel lblTeam4 = new JLabel("4");
		lblTeam4.setBounds(425, 100, 46, 14);
		add(lblTeam4);
		
		JLabel lblTeam5 = new JLabel("5");
		lblTeam5.setBounds(425, 240, 46, 14);
		add(lblTeam5);
		
		JLabel lblTeam6 = new JLabel("6");
		lblTeam6.setBounds(425, 390, 46, 14);
		add(lblTeam6);
		
		JPanel panel1 = new TeamModule(1);
		panel1.setBounds(70, 60, 325, 100);
		add(panel1);
		
		JPanel panel2 = new TeamModule(2);
		panel2.setBounds(70, 200, 325, 100);
		add(panel2);
		
		JPanel panel3 = new TeamModule(3);
		panel3.setBounds(70, 350, 325, 100);
		add(panel3);
		
		JPanel panel4 = new TeamModule(4);
		panel4.setBounds(460, 60, 325, 100);
		add(panel4);
		
		JPanel panel5 = new TeamModule(5);
		panel5.setBounds(460, 200, 325, 100);
		add(panel5);
		
		JPanel panel6 = new TeamModule(6);
		panel6.setBounds(460, 350, 325, 100);
		add(panel6);
		
		JLabel lblTeamSelection = new JLabel("Team Selection");
		lblTeamSelection.setBounds(380, 14, 106, 27);
		add(lblTeamSelection);
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.setBounds(550, 20, 89, 23);
		btnConfirm.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent a) {
				Controller.confirmRobotsButtonPressed();
			}
			
		});
		add(btnConfirm);

	}
}
