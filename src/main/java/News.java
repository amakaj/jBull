package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class News extends Dashboard {
	public void showNewsScreen() {
		//Create menu bar
		JFrame newsFrame = new JFrame("News");
		newsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		newsFrame.setSize(800, 500);
		newsFrame.setResizable(false);
		newsFrame.getContentPane().setLayout(null);
		newsFrame.setVisible(true);

		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		newsFrame.setIconImage(bullIcon.getImage());

		/*newsFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				d1.setVisible(true);
			}
		});*/
	}

	public static void main(String[] args) {
		// Schedule jobs for the event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				News n1 = new News();
				n1.showNewsScreen();
			}
		});
	}
}
