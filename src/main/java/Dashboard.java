package main.java;

import javax.swing.*;
import java.awt.*;

import java.util.Date;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;
//Hello
//Whats up
public class Dashboard {
	// Menu bar must be accessible to all methods
    JMenuBar menubar = new JMenuBar();
    private JTable table;
    private JTable table_1;
    private JTable table_2;

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
        
        ImageIcon stock = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/Stock_Graph.jpeg")).getImage()
                .getScaledInstance(500, 400, Image.SCALE_DEFAULT));
        JLabel portfolio = new JLabel("Portfolio: ");
        JLabel amount = new JLabel("$298.34");
        Font font = new Font("Courier", Font.BOLD,15);

        JLabel stock_image = new JLabel(stock);
        stock_image.setBounds(0, 0, 550, 500);
        portfolio.setBounds(25,15,100,50);
        portfolio.setFont(font);
        amount.setBounds(460,15,125,50);
        amount.setFont(font);
        JPanel panel1 = new JPanel();
        panel1.setLayout(null);
        panel1.setBackground(Color.WHITE);
        panel1.setBounds(0,0,550,600);
        panel1.add(stock_image);
        panel1.add(portfolio);
        panel1.add(amount);
        frame.getContentPane().add(panel1);
        
        

        // List panel, which will display the stocks owned and the watchlist
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.WHITE);
        panel2.setLayout(null);
        panel2.setBounds(550, 0, 450, 600);
        frame.getContentPane().add(panel2);
        
        JLabel lblNewLabel = new JLabel("Stocks");
        lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 23));
        lblNewLabel.setBounds(6, 6, 118, 38);
        panel2.add(lblNewLabel);
        
        String[] columnName = {"Stocks","Price"};
        Object[][] data = {{"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}, {"AAPL","$172.16"}, {"DDOG","$174.51"}, {"QMCO","$3.02"}, {"TSLA","$904.55"}, {"NVDA","$258.24"}, {"AFRM","$58.82"}, {"SOFI","$12.40"}, {"MSFT","$56.78"}, {"AMD","$125.77"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}};
        table = new JTable(data, columnName) {
        	public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
             }
        };
        
        table.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        table.setPreferredScrollableViewportSize(new Dimension(304,249));
        table.setFillsViewportHeight(true);
        table.setBounds(16, 56, 304, 191);
        panel2.add(table);
        
        JLabel lblNewLabel_1 = new JLabel("Watchlist");
        lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 23));
        lblNewLabel_1.setBounds(6, 259, 118, 38);
        panel2.add(lblNewLabel_1);
        
        String[] columnName2 = {"Stocks","Price"};
        Object[][] data2 = {{"DDOG","$174.51"}, {"QMCO","$3.02"}, {"TSLA","$904.55"}, {"NVDA","$258.24"}, {"AFRM","$58.82"}, {"SOFI","$12.40"}, {"MSFT","$56.78"}, {"AMD","$125.77"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}, {"GOOG","$107.06"}, {"AAPL","$134.50"}, {"MSFT","$56.78"}};
        table_2 = new JTable(data2, columnName2);
        table_2.setPreferredScrollableViewportSize(new Dimension(304,249));
        table_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        table_2.setBounds(16, 309, 304, 181);
        table_2.setFillsViewportHeight(true);
        panel2.add(table_2);
        
       
        
        

        
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