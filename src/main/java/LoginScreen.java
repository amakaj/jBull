package main.java;

import javax.swing.*;

import com.opencsv.exceptions.CsvException;

import yahoofinance.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.awt.event.ActionEvent;

public class LoginScreen {
	private JTextField userField;
	private JPasswordField passField;

	public LoginScreen() {
		JFrame loginFrame = new JFrame("jBull Login");

		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setSize(350, 500);
		loginFrame.setResizable(false);
		loginFrame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setFont(new Font("SansSerif", Font.PLAIN, 35));
		panel.setBounds(0, 0, 350, 472);
		loginFrame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel bulliconlabel = new JLabel();
		bulliconlabel.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png"))
				.getImage().getScaledInstance(125, 125, Image.SCALE_DEFAULT)));
		bulliconlabel.setBounds(99, 23, 128, 158);
		panel.add(bulliconlabel);

		JLabel jbullLabel = new JLabel("jBull");
		jbullLabel.setBackground(Color.WHITE);
		jbullLabel.setFont(new Font("SansSerif", Font.PLAIN, 30));
		jbullLabel.setBounds(133, 182, 73, 33);
		panel.add(jbullLabel);

		JLabel userLabel = new JLabel("Username/Email");
		userLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		userLabel.setBounds(37, 246, 263, 16);
		panel.add(userLabel);

		userField = new JTextField();
		userField.setBackground(Color.WHITE);
		userField.setBounds(37, 265, 263, 20);
		panel.add(userField);
		userField.setColumns(10);

		JLabel passLabel = new JLabel("Password");
		passLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		passLabel.setBounds(37, 296, 263, 16);
		panel.add(passLabel);

		JButton loginButton = new JButton("Login");

		loginButton.setForeground(Color.BLACK);
		loginButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
		loginButton.setBackground(Color.WHITE);
		loginButton.setBounds(37, 364, 114, 41);
		panel.add(loginButton);

		JButton createButton = new JButton("Create");
		createButton.setForeground(Color.BLACK);
		createButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
		createButton.setBackground(Color.WHITE);
		createButton.setBounds(186, 364, 114, 41);
		panel.add(createButton);

		passField = new JPasswordField();
		passField.setBounds(37, 314, 263, 20);
		panel.add(passField);

		loginFrame.setVisible(true);

		ImageIcon bullToolbarIcon = new ImageIcon(
				new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		loginFrame.setIconImage(bullToolbarIcon.getImage());

		// ACTION LISTENERS
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//Access file
					fileIO fio = new fileIO("add_user.txt");

					//Authenticate user using CSV data and put it into a User object
					User authenticatedUser = fio.authenticate(userField.getText(), passField.getText());

					//If there is a user in the list
					if (authenticatedUser != null) {
						Double portfolioBalance = 0.0;

						//Create a stock data instance for the user by reading the CSV
						HashMap<String,Integer> newMap = fio.readStockData(authenticatedUser);
						
						//Assign that stock data to the user
						authenticatedUser.setStockData(newMap);

						try {
							if (!newMap.isEmpty()) {
								// Iterates through each pair in the hashmap
								Iterator hmIterator = (authenticatedUser.getStockData()).entrySet().iterator();

								//Loops for each pair in the hashmap
								while (hmIterator.hasNext()) {
									//Gets an individual element
									Map.Entry mapElement = (Map.Entry)hmIterator.next();
									
									//Retrieves stock price for the current stock symbol (i.e., AAPL, TSLA, AMZN, etc.)
									Stock s = YahooFinance.get(mapElement.getKey().toString());
									
									//Take the stock symbol, multiply it by # of shares, and add to portfolioBalance
									portfolioBalance += (s.getQuote().getPrice()).doubleValue() * Integer.parseInt(mapElement.getValue().toString());
								}
								
								//Assign portfolioBalance to user object
								authenticatedUser.setPortfolioBalance(portfolioBalance);
							}
						} catch (UnknownHostException ukHostEx) {
							ukHostEx.printStackTrace();
							JOptionPane.showMessageDialog(null, "ERROR! Please connect to the internet!", "Login",JOptionPane.WARNING_MESSAGE);
						}

						//Pass user object and create Dashboard instance
						Dashboard d = new Dashboard(authenticatedUser);						
						loginFrame.setVisible(false);
						loginFrame.dispose();
					} else {
						//If user is not found in CSV
						JOptionPane.showMessageDialog(null, "ERROR! User not found", "Login",JOptionPane.WARNING_MESSAGE);
					}
				} catch (IOException | CsvException e1) {
					e1.printStackTrace();
				}
			}
		});

		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginFrame.setVisible(false);
				AccountCreation a1 = new AccountCreation();
				loginFrame.dispose();
			}
		});
	}

	public static void main(String[] args) {
		// Schedule jobs for the event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginScreen l1 = new LoginScreen();
			}
		});
	}
}
