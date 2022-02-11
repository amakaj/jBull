package main.java;

import javax.swing.*;
import java.awt.*;

import java.util.Date;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;

public class Dashboard {
	// Menu bar must be accessible to all methods
    JMenuBar menubar = new JMenuBar();
    private JTable table;

    // Takes button as parameter to match the style of the menu bar
    private void changeButtonStyle(JButton button) {
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
    }
    
    // Clock method that will run on its own thread
    private void startClock() {
    	Thread startClockThread = new Thread() {
    		public void run() {
    			JLabel clockLabel = new JLabel("null");
    			menubar.add(clockLabel);
    			while (true) {
    				try {
    					Date date = new Date();
    					clockLabel.setText(String.format("    %tr    ", date));
    					
    					// update every second (usually 1 second behind system clock)
    					sleep(1000L); 
    				} catch (InterruptedException e)
    				{
    					JOptionPane.showMessageDialog(null,  "ERROR: Time is broken!");
    					continue;
    				}
    			}
    		}
    	};
    	startClockThread.start();
    }

    /*
     * Method invoked from the event-dispatching thread (EDT) for thread-safety,
     * since oftentimes Java Swing elements are not thread-safe
     * This method will create the GUI and show it.
     */
    public void createAndShowGUI() {
        // Frame creation
        JFrame frame = new JFrame("jBull");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setResizable(false);
        frame.getContentPane().setLayout(null);

        // Graph panel, which will display the graph
        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.WHITE);
        panel1.setBounds(0, 0, 550, 600);
        frame.getContentPane().add(panel1);
        panel1.setLayout(null);

        // List panel, which will display the stocks owned and the watchlist
        JPanel panel2 = new JPanel();
        panel2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel2.setBackground(Color.WHITE);
        panel2.setLayout(null);
        panel2.setBounds(550, 0, 450, 600);
        frame.getContentPane().add(panel2);
        
        JLabel lblNewLabel = new JLabel("Stocks");
        lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 23));
        lblNewLabel.setBounds(6, 6, 118, 38);
        panel2.add(lblNewLabel);
        
        String[] columnName = {"Stocks","Price"};
        Object[][] data = {{"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}};
        table = new JTable(data, columnName);
        table.setPreferredScrollableViewportSize(new Dimension(304,249));
        table.setFillsViewportHeight(true);
        table.setBounds(16, 56, 304, 249);
        panel2.add(table);
        
        // JScrollPane scrollpane = new JScrollPane(table);

        // Display the window
        frame.setVisible(true);

        // Set the menu bar
        frame.setJMenuBar(menubar);
        menubar.setBackground(Color.WHITE); // Menu bar will be white

        // Instantiate the image objects
        ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
                .getScaledInstance(30, 30, Image.SCALE_DEFAULT));

        ImageIcon profileIcon = new ImageIcon(
                new ImageIcon(getClass().getResource("/main/resources/profile.png")).getImage()
                        .getScaledInstance(30, 30, Image.SCALE_DEFAULT));

        // Set the icon image for the frame
        frame.setIconImage(bullIcon.getImage());

        // Create labels that just contain icons without text
        JLabel bullIconLabel = new JLabel(bullIcon);
        JLabel profileIconLabel = new JLabel(profileIcon);

        // Menus aligned on the left
        JButton buySell = new JButton("Buy/Sell");
        JButton news = new JButton("News");
        JButton help = new JButton("Help");

        changeButtonStyle(buySell);
        changeButtonStyle(news);
        changeButtonStyle(help);

        JLabel bullWhiteSpace = new JLabel("   ");
        menubar.add(bullWhiteSpace);

        menubar.add(bullIconLabel);
        menubar.add(buySell);
        menubar.add(news);
        menubar.add(help);

        // Menus aligned on the right
        menubar.add(Box.createHorizontalGlue()); // Separates right and left
        JMenu profileName = new JMenu("John Doe");
        profileName.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // items in the menu will open aligned
                                                                                 // to the right
        // Menu items to be appended to profile menu
        JMenuItem editProfile = new JMenuItem("Edit Profile");
        JMenuItem logout = new JMenuItem("Logout");
        
        profileName.add(editProfile);
        profileName.add(logout);

        menubar.add(profileName);
        menubar.add(profileIconLabel);
    }

    // Main function
    public static void main(String[] args) {
        // Schedule jobs for the event-dispatching thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Dashboard d1 = new Dashboard();
                d1.createAndShowGUI();
                d1.startClock();
            }
        });
    }
}