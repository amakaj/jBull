package main.java;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import yahoofinance.*;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BuySell {
	private JTextField stockSearchField;
	private JTextField numOfSharesField;
	private static JTable stockTable;
	private JLabel priceLabel;
	private static JPanel panel;
	static Double priceOfFieldShare;
	private static JLabel portfolioBalanceLabel;
	private static JLabel cashBalanceLabel;
	private static JLabel totalBalanceLabel;
	public static User currentUser;

	public static void updateBalances() throws IOException {
		HashMap<String, Integer> stockDataNew = currentUser.getStockData();

		if (!stockDataNew.isEmpty()) {
			Double portfolioBalance = 0.0;
			// Iterates through each pair in the hashmap
			Iterator hmIterator = (currentUser.getStockData()).entrySet().iterator();

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
			currentUser.setPortfolioBalance(portfolioBalance);
		}

		portfolioBalanceLabel.setText("Portfolio Balance: $" + String.format("%.2f", currentUser.getPortfolioBalance()));
		cashBalanceLabel.setText("Cash Balance: $" + String.format("%.2f", currentUser.getCashBalance()));
		totalBalanceLabel.setText("Total Balance: $" + String.format("%.2f", (currentUser.getPortfolioBalance() + currentUser.getCashBalance())));
	}

	public static void updateTable() {
		HashMap<String, Integer> stockDataNew = currentUser.getStockData();
		//Show current list of User Stocks
		String[] columnName = {"Symbol", "# of Shares","Price/Share ($)"};
		Object[][] data = new Object[stockDataNew.size()][3];

		if (!stockDataNew.isEmpty()) {
			//PARSING HASHMAP DATA INTO TABLE
			Iterator hashMapIterator = stockDataNew.entrySet().iterator();
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

		DefaultTableModel stockTableModel = new DefaultTableModel(data, columnName) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
				return false;
			}

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		stockTable = new JTable(stockTableModel);
		stockTableModel.setDataVector(data, columnName);
		stockTableModel.fireTableDataChanged();
	}

	public BuySell(User inputUser) {
		currentUser = inputUser;
		
		// Frame creation
		JFrame frame = new JFrame("Buy/Sell");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(476, 380);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 460, 341);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		// Instantiate the image objects
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));

		// Set the icon image for the frame
		frame.setIconImage(bullIcon.getImage());

		HashMap<String, Integer> stockData = currentUser.getStockData();

		updateTable();

		JScrollPane stockTableScrollPane = new JScrollPane();
		stockTableScrollPane.setBounds(211, 70, 239, 251);
		panel.add(stockTableScrollPane);

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

		JLabel stocksOwnedLabel = new JLabel("Stocks Owned");
		stocksOwnedLabel.setBounds(211, 48, 145, 20);
		stocksOwnedLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(stocksOwnedLabel);

		JLabel nameLabel = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
		nameLabel.setBounds(10, 11, 212, 20);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(nameLabel);

		stockSearchField = new JTextField();
		//				stockSearchField.addKeyListener(new KeyAdapter() {
		//					@Override
		//					public void keyTyped(KeyEvent e) {
		//						if (Integer.parseInt(numOfSharesField.getText()) > 0 && (stockSearchField.getText() != null && stockSearchField.getText().equals(""))) {
		//							try {
		//								Stock s = YahooFinance.get(stockSearchField.getText());
		//								priceLabel.setText("Price: $" + s.getQuote().getPrice());
		//							} catch (IOException ex) {
		//								ex.printStackTrace();
		//							}
		//						}
		//					}
		//				});
		stockSearchField.setBounds(20, 70, 145, 20);
		panel.add(stockSearchField);
		stockSearchField.setColumns(10);


		JLabel searchStockLabel = new JLabel("Stock Search");
		searchStockLabel.setBounds(20, 48, 145, 20);
		searchStockLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(searchStockLabel);

		JLabel numSharesLabel = new JLabel("# of Shares");
		numSharesLabel.setBounds(20, 95, 102, 20);
		numSharesLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(numSharesLabel);

		numOfSharesField = new JTextField();
		numOfSharesField.setBounds(123, 95, 42, 20);
		numOfSharesField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				try {
					if ((numOfSharesField.getText() != null && !numOfSharesField.getText().equals("") && Integer.parseInt(numOfSharesField.getText()) > 0)
							&& (stockSearchField.getText() != null && !stockSearchField.getText().equals(""))) {
						try {
							Stock s = YahooFinance.get(stockSearchField.getText());
							priceOfFieldShare = s.getQuote().getPrice().doubleValue() * Double.parseDouble(numOfSharesField.getText());

							priceLabel.setText("Price: $" + String.format("%.2f", priceOfFieldShare));
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					} else {
						priceLabel.setText("Price: $---");
						priceOfFieldShare = 0.0;
					}
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		});
		numOfSharesField.setColumns(10);
		panel.add(numOfSharesField);

		priceLabel = new JLabel("Price: $---");
		priceLabel.setBounds(20, 121, 145, 20);
		priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(priceLabel);

		JButton buyBtn = new JButton("Buy");
		buyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (currentUser.getCashBalance() > priceOfFieldShare) {
					if (stockData.containsKey(stockSearchField.getText())) {

						int numOfStocks = stockData.get(stockSearchField.getText());
						numOfStocks++;

						stockData.put(stockSearchField.getText(), numOfStocks);
						currentUser.setCashBalance(currentUser.getCashBalance()-priceOfFieldShare);
						currentUser.setStockData(stockData);

						try {
							updateBalances();
							updateTable();
						} catch (IOException ex) {
							ex.printStackTrace();
						}

					} else if (!stockData.containsKey(stockSearchField.getText())) {

						stockData.put(stockSearchField.getText(), 1);
						currentUser.setCashBalance(currentUser.getCashBalance()-priceOfFieldShare);
						currentUser.setStockData(stockData);

						try {
							updateBalances();
							updateTable();
						} catch (IOException ex) {
							ex.printStackTrace();
						}

					}
				}
			}
		});
		buyBtn.setBounds(20, 152, 64, 23);
		panel.add(buyBtn);

		JButton sellBtn = new JButton("Sell");
		sellBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (stockData.get(stockSearchField.getText()) != null && stockData.get(stockSearchField.getText()) >= Integer.parseInt(numOfSharesField.getText()))  {
					Integer numOfSharesCurrentlyOwned = stockData.get(stockSearchField.getText());
					currentUser.setCashBalance(currentUser.getCashBalance()+priceOfFieldShare);

					Integer numOfSharesInField = Integer.parseInt(numOfSharesField.getText());
					numOfSharesCurrentlyOwned = numOfSharesCurrentlyOwned - numOfSharesInField;

					if (numOfSharesCurrentlyOwned > 0) {
						stockData.put(stockSearchField.getText(), numOfSharesCurrentlyOwned);
					} else if (numOfSharesCurrentlyOwned == 0) {
						stockData.remove(stockSearchField.getText());
					}

					currentUser.setStockData(stockData);

					try {
						updateBalances();
						updateTable();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		sellBtn.setBounds(101, 152, 64, 23);
		panel.add(sellBtn);

		portfolioBalanceLabel = new JLabel("Portfolio Balance: $" + currentUser.getPortfolioBalance());
		portfolioBalanceLabel.setBounds(20, 251, 181, 20);
		portfolioBalanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(portfolioBalanceLabel);

		cashBalanceLabel = new JLabel("Cash Balance: $" + currentUser.getCashBalance());
		cashBalanceLabel.setBounds(20, 274, 181, 20);
		cashBalanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(cashBalanceLabel);

		totalBalanceLabel = new JLabel("Total Balance: $" + (currentUser.getPortfolioBalance() + currentUser.getCashBalance()));
		totalBalanceLabel.setBounds(20, 298, 181, 20);
		totalBalanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(totalBalanceLabel);

		// Display the window
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		BuySell bs = new BuySell(null);
	}
}
