package main.java;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.opencsv.exceptions.CsvException;

import yahoofinance.*;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;
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
	private static JScrollPane stockTableScrollPane;

	private static User currentUser;

	private void updatePrice() {
		Thread startPriceThread = new Thread() {
			public void run() {
				while (true) {
					try {
						if ((numOfSharesField.getText() != null && !numOfSharesField.getText().equals("") && Integer.parseInt(numOfSharesField.getText()) > 0)
								&& (stockSearchField.getText() != null && !stockSearchField.getText().equals(""))) {
							try {
								Stock s = YahooFinance.get(stockSearchField.getText());
								if (s != null) {
									priceOfFieldShare = s.getQuote().getPrice().doubleValue() * Double.parseDouble(numOfSharesField.getText());
									priceLabel.setText("Price: $" + String.format("%.2f", priceOfFieldShare));
								} else {
									priceLabel.setText("Price: $---");
								}
							} catch (IOException|NullPointerException e) {
								e.printStackTrace();
							}
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

					try {
						sleep(1000L);// sleep for 1 second
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
		};
		startPriceThread.start();
	}

	private static void updateBalances() {
		HashMap<String, Integer> stockDataNew = currentUser.getStockData();

		if (!stockDataNew.isEmpty()) {
			Double portfolioBalance = 0.0;
			// Iterates through each pair in the hashmap
			Iterator hmIterator = (currentUser.getStockData()).entrySet().iterator();

			//Loops for each pair in the hashmap
			while (hmIterator.hasNext()) {
				//Gets an individual element
				Map.Entry mapElement = (Map.Entry)hmIterator.next();

				try {
					//Retrieves stock price for the current stock symbol (i.e., AAPL, TSLA, AMZN, etc.)
					Stock s = YahooFinance.get(mapElement.getKey().toString());

					//Take the stock symbol, multiply it by # of shares, and add to portfolioBalance
					portfolioBalance += (s.getQuote().getPrice()).doubleValue() * Integer.parseInt(mapElement.getValue().toString());

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//Assign portfolioBalance to user object
			currentUser.setPortfolioBalance(portfolioBalance);
		} else if (stockDataNew.isEmpty()) {
			currentUser.setPortfolioBalance(0.0);
		}

		portfolioBalanceLabel.setText("Portfolio Balance: $" + String.format("%.2f", currentUser.getPortfolioBalance()));
		cashBalanceLabel.setText("Cash Balance: $" + String.format("%.2f", currentUser.getCashBalance()));
		totalBalanceLabel.setText("Total Balance: $" + String.format("%.2f", (currentUser.getPortfolioBalance() + currentUser.getCashBalance())));
	}

	private static void updateTable() {
		HashMap<String, Integer> stockDataNew = currentUser.getStockData();
		DefaultTableModel stockTableModel;

		if (stockTable != null) { // if stocktable was found already
			// get the model from stocktable
			stockTableModel = (DefaultTableModel) stockTable.getModel();
		} else {
			//create a new model for stocktable
			stockTableModel = new DefaultTableModel();

			//add columns
			stockTableModel.addColumn("Symbol");
			stockTableModel.addColumn("# of Shares");
			stockTableModel.addColumn("Price/Share ($)");
		}

		stockTableModel.getDataVector().removeAllElements();

		if (!stockDataNew.isEmpty()) {
			//PARSING HASHMAP DATA
			Iterator hashMapIterator = stockDataNew.entrySet().iterator();

			while (hashMapIterator.hasNext()) {
				Map.Entry hmElement = (Map.Entry) hashMapIterator.next();

				try {
					Stock s = YahooFinance.get(hmElement.getKey().toString());
					String stockPrice = (s.getQuote().getPrice()).toString();
					stockTableModel.addRow(new Object[] {hmElement.getKey(), hmElement.getValue(), stockPrice});
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		stockTable = new JTable(stockTableModel);
		stockTableModel.fireTableDataChanged();

		stockTable.revalidate();
		stockTable.repaint();
	}

	public BuySell(User inputUser) {
		currentUser = inputUser;

		// buySellFrame creation
		JFrame buySellFrame = new JFrame("Buy/Sell");
		buySellFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		buySellFrame.setSize(460, 361);
		buySellFrame.setResizable(false);
		buySellFrame.getContentPane().setLayout(null);

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 460, 341);
		buySellFrame.getContentPane().add(panel);
		panel.setLayout(null);

		// Instantiate the image objects
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));

		// Set the icon image for the buySellFrame
		buySellFrame.setIconImage(bullIcon.getImage());

		HashMap<String, Integer> stockData = currentUser.getStockData();

		stockTableScrollPane = new JScrollPane();
		stockTableScrollPane.setBounds(180, 70, 270, 251);
		panel.add(stockTableScrollPane);

		updateTable();

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
		stocksOwnedLabel.setBounds(180, 48, 145, 20);
		stocksOwnedLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(stocksOwnedLabel);

		JLabel nameLabel = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
		nameLabel.setBounds(10, 11, 212, 20);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(nameLabel);

		stockSearchField = new JTextField();
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
		numOfSharesField.setColumns(10);
		panel.add(numOfSharesField);

		priceLabel = new JLabel("Price: $---");
		priceLabel.setBounds(20, 121, 145, 20);
		priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(priceLabel);

		JButton buyBtn = new JButton("Buy");
		buyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (stockSearchField.getText() != null && !stockSearchField.getText().equals("") && numOfSharesField.getText() != null && !numOfSharesField.getText().equals("") && (currentUser.getCashBalance() > priceOfFieldShare)) {
					int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to buy " + numOfSharesField.getText() + " share(s) of " + stockSearchField.getText() + "?","Buy Share",JOptionPane.YES_NO_OPTION);
					if(confirmed == JOptionPane.YES_OPTION)
					{
						if (stockData.containsKey(stockSearchField.getText())) {

							int numOfStocks = stockData.get(stockSearchField.getText());
							numOfStocks += Integer.parseInt(numOfSharesField.getText());

							stockData.put(stockSearchField.getText(), numOfStocks);
							currentUser.setCashBalance(currentUser.getCashBalance()-priceOfFieldShare);
							currentUser.setStockData(stockData);

							updateBalances();
							updateTable();

						} else if (!stockData.containsKey(stockSearchField.getText())) {
							stockData.put(stockSearchField.getText(), Integer.parseInt(numOfSharesField.getText()));
							currentUser.setCashBalance(currentUser.getCashBalance()-priceOfFieldShare);
							currentUser.setStockData(stockData);

							updateBalances();
							updateTable();
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
				if (stockData.get(stockSearchField.getText()) != null && stockData.get(stockSearchField.getText()) >= Integer.parseInt(numOfSharesField.getText()) && priceOfFieldShare != null)  {
					int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to sell " + numOfSharesField.getText() + " share(s) of " + stockSearchField.getText() + "?","Buy Share",JOptionPane.YES_NO_OPTION);
					if(confirmed == JOptionPane.YES_OPTION)
					{
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

						updateBalances();
						updateTable();
					}
				}
			}
		});
		sellBtn.setBounds(101, 152, 64, 23);
		panel.add(sellBtn);

		portfolioBalanceLabel = new JLabel("Portfolio Balance: $---");
		portfolioBalanceLabel.setBounds(20, 251, 181, 20);
		portfolioBalanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(portfolioBalanceLabel);

		cashBalanceLabel = new JLabel("Cash Balance: $---");
		cashBalanceLabel.setBounds(20, 274, 181, 20);
		cashBalanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(cashBalanceLabel);

		totalBalanceLabel = new JLabel("Total Balance: $---");
		totalBalanceLabel.setBounds(20, 298, 181, 20);
		totalBalanceLabel.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(totalBalanceLabel);

		updateBalances();

		// Display the window
		buySellFrame.setVisible(true);
		updatePrice();

		buySellFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//Actions to take when window is closed
				try {
					fileIO fio = new fileIO("add_user.txt");
					fio.addStockData(currentUser, stockData);
				} catch (IOException | CsvException e1) {
					e1.printStackTrace();
				}

				buySellFrame.setVisible(false);
				Dashboard d = new Dashboard(currentUser);
				buySellFrame.dispose();
			}
		});
	}
}
