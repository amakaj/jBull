// jBull | Allows the user to buy and sell stocks
package main.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BuySell {
	private JTextField stockSearchField;
	private JTextField numOfSharesField;
	private JLabel priceLabel;

	//Declaration of static variables
	private static JTable stockTable;
	private static JPanel panel;
	private static Double priceOfFieldShare;
	private static JLabel portfolioBalanceLabel;
	private static JLabel cashBalanceLabel;
	private static JLabel totalBalanceLabel;
	private static JScrollPane stockTableScrollPane;

	private static User currentUser;

	//Retrieves the symbol, specified number of shares, and uses the API to update the price every second
	private void updatePrice() {
		Thread startPriceThread = new Thread() {
			public void run() {
				while (true) {
					try {
						//If the fields are not null and have data in them
						if ((numOfSharesField.getText() != null && !numOfSharesField.getText().equals("") && Integer.parseInt(numOfSharesField.getText()) > 0)
								&& (stockSearchField.getText() != null && !stockSearchField.getText().equals(""))) {
							try {

								//Retrieve the stock object using the symbol specified in the stock search field
//								Stock s = YahooFinance.get();
								StockAPIWrapper stockAPIWrapper = new StockAPIWrapper();
								Double stockPrice = stockAPIWrapper.getStockPrice(stockSearchField.getText());

								//If a stock was found, get the price and multiply it by the specified number of shares
								if (stockPrice != null) {
									priceOfFieldShare = stockPrice * Double.parseDouble(numOfSharesField.getText());
									priceLabel.setText("Price: $" + String.format("%.2f", priceOfFieldShare));
								} else {
									priceLabel.setText("Price: $---");
								}
							} catch (NullPointerException e) {
								e.printStackTrace();
							}
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}

					try {
						//halts the thread for one second so that it makes an API price request every second
						sleep(1000L); 
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
				}
			}
		};
		startPriceThread.start();
	}

	//Updates the user's balances, usually after the user has bought or sold share(s)
	private static void updateBalances() {
		//Passes the user's stock data into a HashMap, so that it could be manipulated
		HashMap<String, Integer> stockDataNew = currentUser.getStockData();

		//If the user has stocks,
		if (!stockDataNew.isEmpty()) {
			//portfolioBalance is not a part of the user object since it changes according to the stock price	
			Double portfolioBalance = 0.0;
			// Iterates through each pair in the HashMap
			Iterator hmIterator = (currentUser.getStockData()).entrySet().iterator();

			//Loops for each pair in the HashMap
			while (hmIterator.hasNext()) {
				//Gets an individual element
				Map.Entry mapElement = (Map.Entry)hmIterator.next();

                //Retrieves stock price for the current stock symbol (i.e., AAPL, TSLA, AMZN, etc.)
                StockAPIWrapper stockAPIWrapper = new StockAPIWrapper();
                Double stockPrice = stockAPIWrapper.getStockPrice(mapElement.getKey().toString());

                //Take the stock symbol, multiply it by # of shares, and add to portfolioBalance
                portfolioBalance += (stockPrice * Integer.parseInt(mapElement.getValue().toString()));
            }

			//Assign portfolioBalance to user object
			currentUser.setPortfolioBalance(portfolioBalance);
		} else if (stockDataNew.isEmpty()) {
			currentUser.setPortfolioBalance(0.0);
		}

		//Set the balance fields using the appropriate data
		portfolioBalanceLabel.setText("Portfolio Balance: $" + String.format("%.2f", currentUser.getPortfolioBalance()));
		cashBalanceLabel.setText("Cash Balance: $" + String.format("%.2f", currentUser.getCashBalance()));
		totalBalanceLabel.setText("Total Balance: $" + String.format("%.2f", (currentUser.getPortfolioBalance() + currentUser.getCashBalance())));
	}

	//Updates the user's table of owned stocks, typically after they have bought/sold share(s)
	private static void updateTable() {
		//Passes the user's stock data into a HashMap, so that it could be manipulated
		HashMap<String, Integer> stockDataNew = currentUser.getStockData();
		DefaultTableModel stockTableModel;

		if (stockTable != null) { // If a stock table exists
			// Get the model from stock table
			stockTableModel = (DefaultTableModel) stockTable.getModel();
		} else {
			// Create a new model for stocktable
			stockTableModel = new DefaultTableModel();

			//Add columns to the model
			stockTableModel.addColumn("Symbol");
			stockTableModel.addColumn("# of Shares");
			stockTableModel.addColumn("Price/Share ($)");
		}

		//Clear the model of all elements
		stockTableModel.getDataVector().removeAllElements();

		//If the user has stock data
		if (!stockDataNew.isEmpty()) {
			//Create an iterator to go through each field of the Hash Map
			Iterator hashMapIterator = stockDataNew.entrySet().iterator();

			while (hashMapIterator.hasNext()) {
				Map.Entry hmElement = (Map.Entry) hashMapIterator.next();

                //Retrieve a stock object and price for the given symbol
                StockAPIWrapper stockAPIWrapper = new StockAPIWrapper();
                String stockPrice = (stockAPIWrapper.getStockPrice((hmElement.getKey()).toString())).toString();

                //Add the data to the stock table model
                stockTableModel.addRow(new Object[] {hmElement.getKey(), hmElement.getValue(), stockPrice});
            }
		}

		//Reinitialize, revalidate, and redraw the stock table with the new model
		stockTable = new JTable(stockTableModel);
		stockTableModel.fireTableDataChanged();
		stockTable.revalidate();
		stockTable.repaint();
	}

	public BuySell(User inputUser) {
		currentUser = inputUser;

		// Creation of Frame
		JFrame buySellFrame = new JFrame("Buy/Sell");
		buySellFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		buySellFrame.setSize(460, 361);
		buySellFrame.setResizable(false);
		buySellFrame.getContentPane().setLayout(null);

		//Creation of panel
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 460, 341);
		panel.setLayout(null);
		buySellFrame.getContentPane().add(panel);

		// Instantiate the image objects
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));

		// Set the icon image for the buySellFrame
		buySellFrame.setIconImage(bullIcon.getImage());

		//Retrieve the user's stock data 
		HashMap<String, Integer> stockData = currentUser.getStockData();

		//Creation of stock table scroll pane, object, and updating stock table
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

		//Label creation
		JLabel stocksOwnedLabel = new JLabel("Stocks Owned");
		stocksOwnedLabel.setBounds(180, 48, 145, 20);
		stocksOwnedLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(stocksOwnedLabel);

		JLabel nameLabel = new JLabel(currentUser.getFirstName() + " " + currentUser.getLastName());
		nameLabel.setBounds(10, 11, 212, 20);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(nameLabel);

		JLabel searchStockLabel = new JLabel("Stock Search");
		searchStockLabel.setBounds(20, 48, 145, 20);
		searchStockLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(searchStockLabel);

		JLabel numSharesLabel = new JLabel("# of Shares");
		numSharesLabel.setBounds(20, 95, 102, 20);
		numSharesLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(numSharesLabel);

		//Field creation
		stockSearchField = new JTextField();
		stockSearchField.setBounds(20, 70, 145, 20);
		panel.add(stockSearchField);
		stockSearchField.setColumns(10);

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
				//If all fields have data and are not null
				if (stockSearchField.getText() != null && !stockSearchField.getText().equals("") && numOfSharesField.getText() != null && !numOfSharesField.getText().equals("") && (currentUser.getCashBalance() > priceOfFieldShare)) {
					
					//Prompt user for confirmation
					int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to buy " + numOfSharesField.getText() + " share(s) of " + stockSearchField.getText() + "?","Buy Share",JOptionPane.YES_NO_OPTION);
					if(confirmed == JOptionPane.YES_OPTION)
					{
						//If the user's stock data contains the stock in the field
						if (stockData.containsKey(stockSearchField.getText())) {

							//Get the number of stocks that the user owns for the stock specified in the field
							int numOfStocks = stockData.get(stockSearchField.getText());
							numOfStocks += Integer.parseInt(numOfSharesField.getText());

							//Reassign the number of shares in the HashMap for the specified stock after buying the share(s)
							stockData.put(stockSearchField.getText(), numOfStocks);
							currentUser.setCashBalance(currentUser.getCashBalance()-priceOfFieldShare);
							currentUser.setStockData(stockData);

							//Update the user's balances and table
							updateBalances();
							updateTable();

							//If the user's stock data does not contain the stock in the field
						} else if (!stockData.containsKey(stockSearchField.getText())) {
							//Put the stock in the HashMap, along with the number of shares the user initially bought
							stockData.put(stockSearchField.getText(), Integer.parseInt(numOfSharesField.getText()));
							currentUser.setCashBalance(currentUser.getCashBalance()-priceOfFieldShare);
							currentUser.setStockData(stockData);

							//Update the user's balances and table
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
				//If all fields have data and are not null
				if (stockData.get(stockSearchField.getText()) != null && stockData.get(stockSearchField.getText()) >= Integer.parseInt(numOfSharesField.getText()) && priceOfFieldShare != null)  {
					
					//Prompt user for confirmation
					int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to sell " + numOfSharesField.getText() + " share(s) of " + stockSearchField.getText() + "?","Buy Share",JOptionPane.YES_NO_OPTION);
					if(confirmed == JOptionPane.YES_OPTION)
					{
						//Get the number of stocks that the user owns for the stock specified in the field
						Integer numOfSharesCurrentlyOwned = stockData.get(stockSearchField.getText());
						currentUser.setCashBalance(currentUser.getCashBalance()+priceOfFieldShare);

						//Deduct the number of shares sold from the number of shares currently owned
						Integer numOfSharesInField = Integer.parseInt(numOfSharesField.getText());
						numOfSharesCurrentlyOwned = numOfSharesCurrentlyOwned - numOfSharesInField;

						//Update the HashMap
						if (numOfSharesCurrentlyOwned > 0) {
							stockData.put(stockSearchField.getText(), numOfSharesCurrentlyOwned);
						} else if (numOfSharesCurrentlyOwned == 0) {
							stockData.remove(stockSearchField.getText());
						}

						//Assign the updated stock data to the current user
						currentUser.setStockData(stockData);

						//Update the user's balances and table
						updateBalances();
						updateTable();
					}
				}
			}
		});
		sellBtn.setBounds(101, 152, 64, 23);
		panel.add(sellBtn);

		//Balance labels
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
		updatePrice();

		// Display the window
		buySellFrame.setVisible(true);

		//When buy/sell frame is closed, save the data to the file
		buySellFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					FileIOSQL fios = new FileIOSQL("jBullDB.db");
					fios.addStockData(fios.getUserId(currentUser), stockData);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				buySellFrame.setVisible(false);
				Dashboard d = new Dashboard(currentUser);
				buySellFrame.dispose();
			}
		});
	}
}
