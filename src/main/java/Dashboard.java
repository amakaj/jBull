package main.java;

import javax.swing.*;
import java.awt.*;

public class Dashboard {
    private void changeButtonStyle(JButton button) {
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBackground(Color.WHITE);
    }

    /*
     * Method invoked from the event-dispatching thread for thread-safety,
     * since oftentimes Java Swing elements are not thread-safe
     * This method will create the GUI and show it.
     */
    private void createAndShowGUI() {
        // Frame creation
        JFrame frame = new JFrame("jBull");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setResizable(false);

        // Display the window
        frame.setVisible(true);

        // Set the menu bar
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);
        menubar.setBackground(Color.WHITE); // Menu bar will be white

        // Instantiate the image objects
        ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("bull.png")).getImage()
                .getScaledInstance(25, 25, Image.SCALE_DEFAULT));

        ImageIcon profileIcon = new ImageIcon(new ImageIcon(getClass().getResource("profile.png")).getImage()
                .getScaledInstance(25, 25, Image.SCALE_DEFAULT));

        // Set the icon image for the frame
        frame.setIconImage(bullIcon.getImage());

        // Create labels that just contain icons without text
        JLabel bullIconLabel = new JLabel(bullIcon);
        JLabel profileIconLabel = new JLabel(profileIcon);

        // Menu items aligned on the left
        JButton buySell = new JButton("Buy/Sell");
        JButton news = new JButton("News");
        JButton help = new JButton("Help");

        changeButtonStyle(buySell);
        changeButtonStyle(news);
        changeButtonStyle(help);

        menubar.add(bullIconLabel);
        menubar.add(buySell);
        menubar.add(news);
        menubar.add(help);

        // Menu items aligned on the right
        menubar.add(Box.createHorizontalGlue()); // Separates right and left
        JMenu profileName = new JMenu("John Doe");

        menubar.add(profileName);
        menubar.add(profileIconLabel);
    }

    // Main function
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Dashboard d1 = new Dashboard();
                d1.createAndShowGUI();
            }
        });
    }
}