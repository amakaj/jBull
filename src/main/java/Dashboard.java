package main.java;

import javax.swing.*;

public class Dashboard {
    /*
     * /**
     * Create the GUI and show it. For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     *
     *
     */
    public void createAndShowGUI() {
        // Frame creation
        JFrame frame = new JFrame("jBull");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setResizable(false);

        // Display the window.
        frame.setVisible(true);

        /* Set the menu bar */
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        JLabel label1 = new JLabel();
        ImageIcon image1 = new ImageIcon(getClass().getResource("./resources/bull.png"));
        // .getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT)));

        label1.setIcon(image1);
        menubar.add(label1);
    }

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