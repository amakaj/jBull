package main.java;

import java.awt.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Backend
{
	JMenuBar servermenubar = new JMenuBar();
	
	private void startClock() {
		Thread startClockThread = new Thread() {
			public void run() {
				JLabel serverName = new JLabel("Server     ");
				JLabel clockLabel = new JLabel("nulltime");
				servermenubar.add(clockLabel);
				servermenubar.add(serverName);
				while (true) {
					try {
						Date date = new Date();
						clockLabel.setText(String.format("    %tr    ", date));
						// update every second (usually 1 second behind system clock)
						sleep(1000L);
					} catch (InterruptedException e) {
						JOptionPane.showMessageDialog(null, "ERROR: Time is broken!");
						continue;
					}
				}
			}
		};
		startClockThread.start();
	}
	
	private void startServer() {
		Thread startServerThread = new Thread() {
			public void run() {
				
			}
		};
	}
	
	public Backend()
	{
		//Frame Creation
		JFrame frame = new JFrame("jBull Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setForeground(Color.WHITE);
		leftPanel.setBounds(0, 0, 300, 500);
		frame.getContentPane().add(leftPanel);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setForeground(Color.WHITE);
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setBounds(300, 0, 500, 500);
		frame.getContentPane().add(rightPanel);
		rightPanel.setLayout(null);
		
		JList watchlist = new JList();
		watchlist.setEnabled(false);
		watchlist.setModel(new AbstractListModel() {
			String[] values = new String[] {"No Stocks"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		watchlist.setRequestFocusEnabled(false);
		watchlist.setFont(new Font("Calibri", Font.PLAIN, 16));
		watchlist.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		watchlist.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		watchlist.setBackground(Color.WHITE);
		watchlist.setBounds(23, 25, 214, 124);
		rightPanel.add(watchlist);
		
		JList stocklist = new JList();
		stocklist.setEnabled(false);
		stocklist.setModel(new AbstractListModel() {
			String[] values = new String[] {"No Stocks"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		stocklist.setRequestFocusEnabled(false);
		stocklist.setFont(new Font("Calibri", Font.PLAIN, 16));
		stocklist.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		stocklist.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		stocklist.setBackground(Color.WHITE);
		stocklist.setBounds(247, 25, 214, 124);
		rightPanel.add(stocklist);
		
		JLabel watchlistLabel = new JLabel("Watchlist");
		watchlistLabel.setFont(new Font("Arial", Font.BOLD, 16));
		watchlistLabel.setBounds(23, 8, 107, 20);
		rightPanel.add(watchlistLabel);
		
		JLabel stocklistLabel = new JLabel("Stocks");
		stocklistLabel.setFont(new Font("Arial", Font.BOLD, 16));
		stocklistLabel.setBounds(247, 8, 88, 20);
		rightPanel.add(stocklistLabel);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		textArea.setBounds(23, 186, 434, 217);
		rightPanel.add(textArea);
		
		JLabel responseConsoleLabel = new JLabel("Response Console");
		responseConsoleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		responseConsoleLabel.setBounds(23, 170, 197, 20);
		rightPanel.add(responseConsoleLabel);
		
		// Set the menu bar
		frame.setJMenuBar(servermenubar);
		servermenubar.setBackground(Color.WHITE);

		// Instantiate the image objects
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		ImageIcon blueSwooshIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/blueswoosh.png")).getImage()
				/*.getScaledInstance(30, 30, Image.SCALE_DEFAULT)*/);
		frame.setIconImage(bullIcon.getImage());

		//Add to menu bar
		JLabel bullIconLabel = new JLabel(bullIcon);
		JLabel bullWhiteSpace = new JLabel("   ");
		servermenubar.add(bullWhiteSpace);
		servermenubar.add(bullIconLabel);
		servermenubar.add(Box.createHorizontalGlue()); // Separates right and left
		leftPanel.setLayout(null);
		
		JLabel blueSwooshLabel = new JLabel(blueSwooshIcon);
		leftPanel.add(blueSwooshLabel);
		blueSwooshLabel.setBounds(-5, 300, 290, 149);
		
		JList userList = new JList();
		userList.setFont(new Font("Arial", Font.PLAIN, 14));
		userList.setRequestFocusEnabled(false);
		userList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		userList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		userList.setBackground(Color.WHITE);
		userList.setModel(new AbstractListModel() {
			String[] values = new String[] {"Anthony Makaj", "Syeda Islam", "Ibrahim Nabid", "Imu Islam", "John Doe", "Jane Doe", "Some Name"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		userList.setBounds(30, 47, 240, 230);
		leftPanel.add(userList);
		
		JLabel usersLabel = new JLabel("Users");
		usersLabel.setFont(new Font("Arial", Font.BOLD, 16));
		usersLabel.setBounds(30, 29, 74, 20);
		leftPanel.add(usersLabel);
		
		JLabel lblTotal = new JLabel("Total: 7");
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
		lblTotal.setBounds(179, 29, 91, 20);
		leftPanel.add(lblTotal);
		
		startClock();
	
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		Backend b = new Backend();
	}
}
