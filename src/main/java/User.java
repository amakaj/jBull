package main.java;

import java.io.Serializable;
import java.util.HashMap;

public class User implements Serializable {

	String firstName, lastName, username, password, email;
	Double cashBalance, portfolioBalance;
	HashMap<String, Integer> stockData;
	
	public User(String fn, String ln, String u, String pw, String e, Double cB)
	{
		this.firstName = fn;
		this.lastName = ln;
		this.username = u;
		this.password = pw;
		this.email = e;
		if (cB == null) {
			this.cashBalance = 1000.0;
		} else {
			this.cashBalance = cB;
		}
		this.portfolioBalance = 0.0;
	}
	
	public User (String fn, String ln, String u, String pw, String e, Double cB, HashMap<String, Integer> stockData) {
		this.firstName = fn;
		this.lastName = ln;
		this.username = u;
		this.password = pw;
		this.email = e;
		if (cB == null) {
			this.cashBalance = 1000.0;
		} else {
			this.cashBalance = cB;
		}
		this.portfolioBalance = 0.0;
		this.stockData = stockData;
	}
	
	public User (String[] inputUserData) {
		if (inputUserData.length == 6) {
			this.firstName = inputUserData[0];
			this.lastName = inputUserData[1];	
			this.username = inputUserData[2];
			this.password = inputUserData[3];

			this.portfolioBalance = 0.0;
			this.email = inputUserData[4];
			this.cashBalance = Double.parseDouble(inputUserData[5]);
		}
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public Double getCashBalance() {
		return cashBalance;
	}
	
	public Double getPortfolioBalance() {
		return portfolioBalance;
	}
	
	public HashMap<String, Integer> getStockData() {
		return stockData;
	}
	
	public void setFirstName(String inputFirstName) {
		firstName = inputFirstName;
	}
	
	public void setLastName(String inputLastName) {
		lastName = inputLastName;
	}
	
	public void setUsername(String inputUsername) {
		username = inputUsername;
	}
	
	public void setPassword(String inputPassword) {
		password = inputPassword;
	}
	
	public void setEmail(String inputEmail) {
		email = inputEmail;
	}
	
	public void setCashBalance(Double inputCashBalance) {
		cashBalance = inputCashBalance;
	}
	
	public void setPortfolioBalance(Double inputPortfolioBalance) {
		portfolioBalance = inputPortfolioBalance;
	}
	
	public void setStockData(HashMap<String, Integer> inputStockData) {
		stockData = inputStockData;
	}
}