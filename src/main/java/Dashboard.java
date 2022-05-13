package main.java;

import javax.swing.*;
import javax.swing.border.MatteBorder;

import com.opencsv.exceptions.CsvException;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.*;
import java.net.*;

import yahoofinance.YahooFinance;
import yahoofinance.Stock;
import javax.swing.border.LineBorder;
import org.jfree.chart.*;
import org.jfree.data.general.DefaultPieDataset;

public class Dashboard {
	//SOCKET SERVER CODE
	Socket clientSocket = null;
	ObjectOutputStream outToServer = null;
	BufferedReader inFromServer = null;
	public static boolean connectedToSocket;
	
	public static final String SERVER_IP_TO_CONNECT_TO = "172.17.80.1";

	public boolean socketConnect(String inputIPAdr, int inputPort) {
		boolean rc = false;
		try {
			// 127.0.0.1 and localhost aren't working
			clientSocket = new Socket(inputIPAdr, inputPort);
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			//			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			rc = true;
		} catch (ConnectException ex) {
			ex.printStackTrace();
			return false;
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
			return false;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}

		return rc;
	}

	public boolean sendObject(User userObj) {
		boolean rc = false;

		try {
			outToServer.writeObject(userObj);
			rc = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return rc;
	}

	public void createClientThread(String inputAddr, int inputPort) {
		Thread connectThread = new Thread() {
			public void run() {
				if (!connectedToSocket) {
					connectedToSocket = socketConnect(inputAddr, inputPort);
				} else {
					return;
				}
			}
		};
		connectThread.start();
	}

	// Menu bar must be accessible to all methods
	JMenuBar menubar = new JMenuBar();
	private JTable stockTable;
	private static JTable topStocksTable;
	private JTextField serverMessageField;

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
					} catch (InterruptedException e) {
						JOptionPane.showMessageDialog(null, "ERROR: Time is broken!");
						continue;
					}
				}
			}
		};
		startClockThread.start();
	}


	public static void updateTopStocksTable(Object[][] topStocksDataInput) {
		Thread updateTopStocksThread = new Thread() {
			public void run() {
				while(true) {
					for (int i = 0; i < topStocksDataInput.length; i++) {
						try {
							Stock s = YahooFinance.get(topStocksDataInput[i][0].toString());
							Double stockPrice = s.getQuote().getPrice().doubleValue();
							topStocksDataInput[i][1] = stockPrice;
						} catch (Exception e) {
							e.printStackTrace();
						}
						topStocksTable.revalidate();
						topStocksTable.repaint();
					}
					try {
						sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		updateTopStocksThread.start();
	}


	public Dashboard(User currentUser) {
		// Frame creation
		JFrame frame = new JFrame("jBull");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		// Display the window
		frame.setVisible(true);

		HashMap<String, Integer> stockData = currentUser.getStockData();

		// Graph panel, which will display the graph
		ImageIcon stock = new ImageIcon(
				new ImageIcon(getClass().getResource("/main/resources/Stock_Graph.jpeg")).getImage()
				.getScaledInstance(500, 400, Image.SCALE_DEFAULT));
		Font font = new Font("Courier", Font.BOLD, 15);
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.WHITE);
		panel1.setBounds(0, 0, 550, 600);
		frame.getContentPane().add(panel1);

		serverMessageField = new JTextField();
		serverMessageField.setBounds(53, 26, 210, 20);
		panel1.add(serverMessageField);
		serverMessageField.setColumns(10);

		JButton connectBtn = new JButton("Connect and Send Object");
		connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (serverMessageField.getText() != null && !serverMessageField.getText().equals("")) {
					try {
						createClientThread(serverMessageField.getText(), 3333);
						sendObject(currentUser);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		connectBtn.setBounds(273, 25, 195, 23);
		panel1.add(connectBtn);

		JLabel portfolioBalanceLabel = new JLabel("Portfolio Balance: $---");
		portfolioBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		portfolioBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		portfolioBalanceLabel.setBounds(25, 81, 106, 20);
		panel1.add(portfolioBalanceLabel);
		portfolioBalanceLabel.setText(String.format("$%.2f", currentUser.getPortfolioBalance()));

		JLabel cashBalanceLabel = new JLabel("Cash Balance: $---");
		cashBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cashBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		cashBalanceLabel.setBounds(177, 81, 106, 20);
		panel1.add(cashBalanceLabel);
		cashBalanceLabel.setText(String.format("$%.2f", currentUser.getCashBalance()));

		JLabel totalBalanceLabel = new JLabel("Total Balance: $---");
		totalBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		totalBalanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
		totalBalanceLabel.setBounds(398, 80, 106, 20);
		panel1.add(totalBalanceLabel);
		totalBalanceLabel.setText(String.format("$%.2f", (currentUser.getPortfolioBalance() + currentUser.getCashBalance())));


		// List panel, which will display the stocks owned and the watchlist
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		panel2.setLayout(null);
		panel2.setBounds(550, 0, 450, 600);
		frame.getContentPane().add(panel2);

		JLabel stocksOwnedLabel = new JLabel("Stocks Owned");
		stocksOwnedLabel.setFont(new Font("Arial", Font.BOLD, 24));
		stocksOwnedLabel.setBounds(16, 27, 304, 28);
		panel2.add(stocksOwnedLabel);

		String[] columnName = {"Symbol", "# of Shares","Price/Share ($)"};
		Object[][] data = new Object[stockData.size()][3];

		if (!stockData.isEmpty()) {
			//PARSING HASHMAP DATA INTO TABLE
			Iterator hashMapIterator = stockData.entrySet().iterator();
			int tableIndex = 0;

			while (hashMapIterator.hasNext()) {
				Map.Entry hmElement = (Map.Entry) hashMapIterator.next();
				data[tableIndex][0] = hmElement.getKey();
				data[tableIndex][1] = hmElement.getValue();
				try {
					Stock s = YahooFinance.get(hmElement.getKey().toString());
					data[tableIndex][2] = (s.getQuote().getPrice()).toString();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				tableIndex++;
			}
		}

		JScrollPane stockTableScrollPane = new JScrollPane();
		stockTableScrollPane.setBounds(16, 56, 304, 191);
		panel2.add(stockTableScrollPane);
		stockTable = new JTable(data, columnName) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
				return false;
			}
		};
		stockTable.setFont(new Font("Arial", Font.PLAIN, 14));

		stockTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		stockTable.setPreferredScrollableViewportSize(new Dimension(304, 249));
		stockTable.setFillsViewportHeight(true);
		stockTable.setBounds(16, 56, 304, 191);
		stockTableScrollPane.setViewportView(stockTable);		
		stockTable.setFocusable(false);
		stockTable.setRowSelectionAllowed(false);
		stockTable.setColumnSelectionAllowed(false);
		stockTable.setDragEnabled(false);

		JLabel top20StocksLabel = new JLabel("Top 20 Stocks");
		top20StocksLabel.setFont(new Font("Arial", Font.BOLD, 24));
		top20StocksLabel.setBounds(16, 281, 304, 28);
		panel2.add(top20StocksLabel);

		String[] columnName2 = { "Symbol", "Price/Share ($)" };

		//PARSING HASHMAP DATA INTO TABLE
		Object[][] topStocksData = {{"AAPL", 0.0}, {"MSFT", 0.0}, {"AMZN", 0.0}, {"TSLA", 0.0}, {"GOOG", 0.0}, {"NVDA", 0.0}, {"NFLX", 0.0}, {"FB", 0.0}, {"UNH", 0.0}, {"JNJ", 0.0}, {"JPM", 0.0}, {"V", 0.0}, {"PG", 0.0}, {"XOM", 0.0}, {"HD", 0.0}, {"CVX", 0.0}, {"MA", 0.0}, {"BAC", 0.0}, {"ABBV", 0.0}};

		//		code if only to update once
		//		for (int i = 0; i < topStocksData.length; i++) {
		//			try {
		//				Stock s = YahooFinance.get(topStocksData[i][0].toString());
		//				Double stockPrice = s.getQuote().getPrice().doubleValue();
		//				
		//				topStocksData[i][1] = stockPrice;
		//			} catch (IOException e) {
		//				e.printStackTrace();
		//			}
		//		}


		JScrollPane topStocksScrollPane = new JScrollPane();
		topStocksScrollPane.setBounds(16, 309, 304, 199);
		panel2.add(topStocksScrollPane);

		topStocksTable = new JTable(topStocksData, columnName2);
		topStocksTable.setPreferredScrollableViewportSize(new Dimension(304, 249));
		topStocksTable.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		topStocksTable.setBounds(16, 309, 304, 191);
		topStocksTable.setFillsViewportHeight(true);
		topStocksScrollPane.setViewportView(topStocksTable);
		topStocksTable.setFocusable(false);
		topStocksTable.setRowSelectionAllowed(false);
		topStocksTable.setColumnSelectionAllowed(false);
		topStocksTable.setDragEnabled(false);

		updateTopStocksTable(topStocksData);


		// Set the menu bar
		frame.setJMenuBar(menubar);
		menubar.setBackground(Color.WHITE); // Menu bar will be white

		// Instantiate the image objects
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));

		ImageIcon profileIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/profile.png")).getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

		// Set the icon image for the frame
		frame.setIconImage(bullIcon.getImage());

		// Create labels that just contain icons without text
		JLabel bullIconLabel = new JLabel(bullIcon);
		JLabel profileIconLabel = new JLabel(profileIcon);

		// Menus aligned on the left
		JButton buySell = new JButton("Buy/Sell");
		buySell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentUser.setStockData(stockData);

				frame.setVisible(false);
				BuySell bs = new BuySell(currentUser);
				frame.dispose();

			}
		});
		JButton help = new JButton("Help");

		changeButtonStyle(buySell);
		changeButtonStyle(help);

		JLabel bullWhiteSpace = new JLabel("   ");
		menubar.add(bullWhiteSpace);

		menubar.add(bullIconLabel);
		menubar.add(buySell);
		menubar.add(help);

		// Menus aligned on the right
		menubar.add(Box.createHorizontalGlue()); // Separates right and left
		JMenu profileName;
		profileName = new JMenu(currentUser.getFirstName());
		profileName.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // items in the menu will open aligned
		// to the right
		// Menu items to be appended to profile menu
		JMenuItem editProfile = new JMenuItem("Edit Profile");
		JMenuItem logout = new JMenuItem("Logout");

		profileName.add(editProfile);
		profileName.add(logout);

		menubar.add(profileName);
		menubar.add(profileIconLabel);
		startClock();

		//PIE CHART CODE
		DefaultPieDataset pieDataset = new DefaultPieDataset();

		//insert cash balance first, then iterate through hashmap to insert each value
		pieDataset.setValue("Cash Balance", currentUser.getCashBalance());

		if (!stockData.isEmpty()) {
			//PARSING HASHMAP DATA INTO CHART
			Iterator hashMapIterator = stockData.entrySet().iterator();

			while (hashMapIterator.hasNext()) {
				Map.Entry hmElement = (Map.Entry) hashMapIterator.next();

				try {
					Stock s = YahooFinance.get(hmElement.getKey().toString());
					String symbol = hmElement.getKey().toString();
					Integer numOfShares = (Integer) hmElement.getValue();

					Double stockPrice = numOfShares * (s.getQuote().getPrice().doubleValue());

					pieDataset.setValue(symbol, stockPrice);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}	

		JFreeChart pChart = ChartFactory.createPieChart(null, pieDataset, false, false, false);
		ChartPanel cPanel = new ChartPanel(pChart);

		pChart.getPlot().setBackgroundPaint(Color.WHITE);

		cPanel.setSize(534,400);
		cPanel.setLocation(10,112);
		panel1.add(cPanel);

		JLabel portfolioBalanceLabelText = new JLabel("Portfolio Balance");
		portfolioBalanceLabelText.setHorizontalAlignment(SwingConstants.CENTER);
		portfolioBalanceLabelText.setFont(new Font("Arial", Font.BOLD, 16));
		portfolioBalanceLabelText.setBounds(10, 54, 142, 28);
		panel1.add(portfolioBalanceLabelText);

		JSeparator topBalanceSeparator = new JSeparator();
		topBalanceSeparator.setBounds(10, 51, 534, 2);
		panel1.add(topBalanceSeparator);

		JLabel cashBalanceLabelText = new JLabel("Cash Balance");
		cashBalanceLabelText.setHorizontalAlignment(SwingConstants.CENTER);
		cashBalanceLabelText.setFont(new Font("Arial", Font.BOLD, 16));
		cashBalanceLabelText.setBounds(162, 54, 142, 28);
		panel1.add(cashBalanceLabelText);

		JLabel totalBalanceLabelText = new JLabel("Total Balance");
		totalBalanceLabelText.setHorizontalAlignment(SwingConstants.CENTER);
		totalBalanceLabelText.setFont(new Font("Arial", Font.BOLD, 16));
		totalBalanceLabelText.setBounds(378, 54, 142, 28);
		panel1.add(totalBalanceLabelText);

		JSeparator bottomBalanceSeparator = new JSeparator();
		bottomBalanceSeparator.setBounds(10, 109, 534, 2);
		panel1.add(bottomBalanceSeparator);

		JSeparator middleBalanceSeparator = new JSeparator();
		middleBalanceSeparator.setOrientation(SwingConstants.VERTICAL);
		middleBalanceSeparator.setBounds(344, 51, 2, 58);
		panel1.add(middleBalanceSeparator);

		JLabel connectToServerInstruction = new JLabel("Interested in connecting to a server? Enter the IP Address here!");
		connectToServerInstruction.setHorizontalAlignment(SwingConstants.CENTER);
		connectToServerInstruction.setFont(new Font("Arial", Font.PLAIN, 14));
		connectToServerInstruction.setBounds(53, 5, 437, 20);
		panel1.add(connectToServerInstruction);

		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				LoginScreen l1 = new LoginScreen();
				frame.dispose();
			}
		});

		help.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setVisible(false);
				HelpScreen h1 = new HelpScreen(currentUser);
				frame.dispose();

			}

		});

		editProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				UserProfileScreen p = new UserProfileScreen(currentUser);
				frame.dispose();
				
			}
		});

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (clientSocket != null && clientSocket.isConnected()) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				try {
					fileIO fio = new fileIO("add_user.txt");
					fio.addStockData(currentUser, stockData);
				} catch (IOException | CsvException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				frame.dispose();
				System.exit(0);
			}
		});

	}
}