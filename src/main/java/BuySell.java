package main.java;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Image;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import yahoofinance.*;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BuySell {
	private JTextField textField;
	private JTextField textField_1;
	public BuySell() {
		// Frame creation
		JFrame frame = new JFrame("Buy/Sell");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 380);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 384, 341);
		frame.getContentPane().add(panel);
		
		// Instantiate the image objects
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		
		// Set the icon image for the frame
		frame.setIconImage(bullIcon.getImage());
		
		//Show current list of User Stocks
		JList userList = new JList();
		userList.setFont(new Font("Arial", Font.PLAIN, 14));
		userList.setRequestFocusEnabled(false);
		userList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		userList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		userList.setBackground(Color.WHITE);
		userList.setModel(new AbstractListModel() {
			String[] values = new String[] {"AAPL", "MSFT", "GOOG", "YHOO", "AMC"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		userList.setBounds(211, 68, 145, 250);
		panel.add(userList);
		
		JLabel stocksOwnedLabel = new JLabel("Stocks Owned");
		stocksOwnedLabel.setFont(new Font("Arial", Font.BOLD, 16));
		stocksOwnedLabel.setBounds(211, 48, 145, 20);
		panel.add(stocksOwnedLabel);
		
		JLabel nameLabel = new JLabel("nullName");
		nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
		nameLabel.setBounds(10, 11, 212, 20);
		panel.add(nameLabel);
		
		textField = new JTextField();
		textField.setBounds(20, 70, 145, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel searchStockLabel = new JLabel("Stock Search");
		searchStockLabel.setFont(new Font("Arial", Font.BOLD, 16));
		searchStockLabel.setBounds(20, 48, 145, 20);
		panel.add(searchStockLabel);
		
		JLabel numSharesLabel = new JLabel("# of Shares");
		numSharesLabel.setFont(new Font("Arial", Font.BOLD, 16));
		numSharesLabel.setBounds(20, 95, 102, 20);
		panel.add(numSharesLabel);
		
		textField_1 = new JTextField();
		textField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
		});
		textField_1.setColumns(10);
		textField_1.setBounds(123, 95, 42, 20);
		panel.add(textField_1);
		
		JLabel priceLabel = new JLabel("Price: $---");
		priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
		priceLabel.setBounds(20, 121, 145, 20);
		panel.add(priceLabel);
		
		JButton btnNewButton = new JButton("Buy");
		btnNewButton.setBounds(20, 152, 64, 23);
		panel.add(btnNewButton);
		
		JButton btnSell = new JButton("Sell");
		btnSell.setBounds(101, 152, 64, 23);
		panel.add(btnSell);
		
		JLabel lblPortfolioBalance = new JLabel("Portfolio Balance: $---");
		lblPortfolioBalance.setFont(new Font("Arial", Font.BOLD, 12));
		lblPortfolioBalance.setBounds(20, 251, 181, 20);
		panel.add(lblPortfolioBalance);
		
		JLabel lblCashBalance = new JLabel("Cash Balance: $---");
		lblCashBalance.setFont(new Font("Arial", Font.BOLD, 12));
		lblCashBalance.setBounds(20, 274, 181, 20);
		panel.add(lblCashBalance);
		
		JLabel lblTotalBalance = new JLabel("Total Balance: $---");
		lblTotalBalance.setFont(new Font("Arial", Font.BOLD, 12));
		lblTotalBalance.setBounds(20, 298, 181, 20);
		panel.add(lblTotalBalance);
		
		JButton addWatchlistBtn = new JButton("Add to Watchlist");
		addWatchlistBtn.setBounds(20, 181, 145, 23);
		panel.add(addWatchlistBtn);
		
		// Display the window
		frame.setVisible(true);
	}
	public static void main(String[] args) {
		BuySell bs = new BuySell();
	}
}
