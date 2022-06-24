// jBull | Displays the user's dashboard
package main.java;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.opencsv.exceptions.CsvException;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

public class Dashboard {
	JMenuBar menubar = new JMenuBar();
	private JTable stockTable;
	private static JTable topStocksTable;
	private JTextField serverMessageField;
	
	/*----------------SERVER CODE----------------*/
	
	//Creation of socket objects/variables
	Socket clientSocket = null;
	ObjectOutputStream outToServer;
	public static boolean connectedToSocket;

	//Specifies the server's IP address for the client to connect to
	public static final String SERVER_IP_TO_CONNECT_TO = "";

	//Creates a client thread to handle the connection to the server
	public void createClientThread(String inputAddr, int inputPort, User clientUser) {
		Thread connectThread = new Thread() {
			public void run() {
				try {
					//Creates a client socket and sends the clientUser object to the server
					clientSocket = new Socket(inputAddr, inputPort);
					outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
					outToServer.writeObject(clientUser);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		connectThread.start();
	}

	/*----------------END SERVER CODE----------------*/

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


	//Updates the Top 20 stocks table on the dashboard every second
	public static void updateTopStocksTable(Object[][] topStocksDataInput) {
		Thread updateTopStocksThread = new Thread() {
			public void run() {
				while(true) {
					//For each stock in the input, get the price and apply it to the table again
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
						// update every second (usually 1 second behind system clock)
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

		//Retrieves stock data from the current user object
		HashMap<String, Integer> stockData = currentUser.getStockData();

		// Creation of panel
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		panel1.setBackground(Color.WHITE);
		panel1.setBounds(0, 0, 550, 600);
		frame.getContentPane().add(panel1);

		//Attempts a reconnection to the server
		JButton connectBtn = new JButton("Attempt Server Connection");
		connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					createClientThread(SERVER_IP_TO_CONNECT_TO, 3333, currentUser);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		connectBtn.setBounds(6, 30, 195, 23);
		panel1.add(connectBtn);

		// List panel, which will display the stocks owned and the watchlist
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
		panel2.setLayout(null);
		panel2.setBounds(550, 0, 450, 600);
		frame.getContentPane().add(panel2);
		
		//Creation of labels
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

		JLabel stocksOwnedLabel = new JLabel("Stocks Owned");
		stocksOwnedLabel.setFont(new Font("Arial", Font.BOLD, 24));
		stocksOwnedLabel.setBounds(16, 27, 304, 28);
		panel2.add(stocksOwnedLabel);

		//Parsing data into top 20 stocks list
		String[] columnName = {"Symbol", "# of Shares","Price/Share ($)"};
		Object[][] data = new Object[stockData.size()][3];

		if (!stockData.isEmpty()) {
			//Creating hash map iterator to go through each field, get the price, and append it to the second column along with each stock
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
				
				if (clientSocket != null && clientSocket.isConnected()) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
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
		profileName.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // items in the menu will open aligned to the right
		// Menu items to be appended to profile menu
		JMenuItem editProfile = new JMenuItem("View Profile");
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

		//Creation of labels and separators above the chart
		JLabel portfolioBalanceLabelText = new JLabel("Portfolio Balance");
		portfolioBalanceLabelText.setHorizontalAlignment(SwingConstants.CENTER);
		portfolioBalanceLabelText.setFont(new Font("Arial", Font.BOLD, 16));
		portfolioBalanceLabelText.setBounds(10, 54, 142, 28);
		panel1.add(portfolioBalanceLabelText);

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
		
		JSeparator topBalanceSeparator = new JSeparator();
		topBalanceSeparator.setBounds(10, 51, 534, 2);
		panel1.add(topBalanceSeparator);

		JSeparator bottomBalanceSeparator = new JSeparator();
		bottomBalanceSeparator.setBounds(10, 109, 534, 2);
		panel1.add(bottomBalanceSeparator);

		JSeparator middleBalanceSeparator = new JSeparator();
		middleBalanceSeparator.setOrientation(SwingConstants.VERTICAL);
		middleBalanceSeparator.setBounds(344, 51, 2, 58);
		panel1.add(middleBalanceSeparator);

		//Logout button action listener
		logout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				LoginScreen l1 = new LoginScreen();
				
				//If the dashboard is connected to the server, close the connection
				if (clientSocket != null && clientSocket.isConnected()) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				frame.dispose();
			}
		});

		//Help button action listener
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				HelpScreen h1 = new HelpScreen(currentUser);
				
				//If the dashboard is connected to the server, close the connection
				if (clientSocket != null && clientSocket.isConnected()) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				frame.dispose();

			}

		});

		//View profile action listener
		editProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				UserProfileScreen p = new UserProfileScreen(currentUser);
				
				//If the dashboard is connected to the server, close the connection
				if (clientSocket != null && clientSocket.isConnected()) {
					try {
						clientSocket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				frame.dispose();

			}
		});

		//Frame window listener to perform actions when user exits
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//Throw exit confirmation
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?","EXIT",JOptionPane.YES_NO_OPTION);
				if(confirmed == JOptionPane.YES_OPTION)
				{
					//If the dashboard is connected to the server, close the connection
					if (clientSocket != null && clientSocket.isConnected()) {
						try {
							clientSocket.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					//Write stock data for user to file
					try {
						fileIO fio = new fileIO("add_user.txt");
						fio.addStockData(currentUser, stockData);
					} catch (IOException | CsvException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					frame.dispose();
					System.exit(0);
				} else {
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}


			}
		});

		//Try to connect to the server
		try {
			createClientThread(SERVER_IP_TO_CONNECT_TO, 3333, currentUser);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
