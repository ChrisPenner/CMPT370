package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import controller.Controller;
import models.Cell;

public class TestBenchView extends WatchView {
	private static final long serialVersionUID = 1L;
	
	public TestBenchView(int diameter, Cell[][] cells) {
		super(diameter, cells);
		
		JTextField terminal = new JTextField();
		terminal.setBounds(480, 360, 291, 20);
		terminal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.commandEntered("");
			}
			
		});
		add(terminal);
	}


}
