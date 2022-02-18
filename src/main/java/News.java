package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class News extends Dashboard {
	public News() {
		//Create menu bar
		JFrame newsFrame = new JFrame("News");
		newsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newsFrame.setSize(800, 500);
		newsFrame.setResizable(false);
		newsFrame.getContentPane().setLayout(null);
		newsFrame.setVisible(true);

		ImageIcon bullToolbarIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		newsFrame.setIconImage(bullToolbarIcon.getImage());
	}
}
