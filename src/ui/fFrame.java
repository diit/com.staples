package ui;

import java.awt.BorderLayout;

import javax.swing.*;

public class fFrame extends JFrame{
	public fFrame(String name){
		super(name);
		setSize(880,620);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

		setLayout(new BorderLayout());
	}
}
