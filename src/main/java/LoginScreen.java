package main.java;

import javax.swing.*;
import java.awt.*;

public class LoginScreen {
	public void showLoginScreen() {
		JFrame loginFrame = new JFrame("jBull Login");
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setSize(350, 500);
		loginFrame.setResizable(false);
		loginFrame.getContentPane().setLayout(null);
		loginFrame.setVisible(true);
		
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		loginFrame.setIconImage(bullIcon.getImage());
	}
	
	public static void main(String[] args) {
		// Schedule jobs for the event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginScreen l1 = new LoginScreen();
				l1.showLoginScreen();
			}
		});
	}
}
