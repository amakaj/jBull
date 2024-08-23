// jBull | Defines the archetype for a User object
package main.java;

import java.io.Serializable;
import java.util.HashMap;

//User Class implements Serializable in order to be sent from client to server
//All constructors automatically provide the user with a $1000.00 cash balance, unless another cash balance is passed through the constructor

public class User implements Serializable {

	String firstName, lastName, username, password;
	Double cashBalance, portfolioBalance;
	HashMap<String, Integer> stockData;
	
	//Takes only user data to create a user object
	public User(String u, String pw, String fn, String ln, Double cB)
	{
		this.firstName = fn;
		this.lastName = ln;
		this.username = u;
		this.password = pw;
		if (cB == null) {
			this.cashBalance = (Double) 1000.0;
		} else {
			this.cashBalance = cB;
		}
		this.portfolioBalance = (Double) 0.0;
	}
	
	//Takes parameters to construct a full user object
	public User (String u, String pw, String fn, String ln, Double cB, HashMap<String, Integer> stockData) {
		this.firstName = fn;
		this.lastName = ln;
		this.username = u;
		this.password = pw;
		if (cB == null) {
			this.cashBalance = (Double) 1000.0;
		} else {
			this.cashBalance = cB;
		}
		this.portfolioBalance = (Double) 0.0;
		this.stockData = stockData;
	}
	
	//Takes string array to create user object
	public User (String[] inputUserData) {
		if (inputUserData.length == 5) {
			this.firstName = inputUserData[0];
			this.lastName = inputUserData[1];	
			this.username = inputUserData[2];
			this.password = inputUserData[3];

			this.portfolioBalance = (Double) 0.0;
			this.cashBalance = (Double) Double.parseDouble(inputUserData[4]);
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