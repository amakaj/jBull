package main.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileIOSQL {
	final String databaseName;
	static String url;
	static Connection conn;
	
	public FileIOSQL(String databaseName) {
		this.databaseName = databaseName;
		url = "jdbc:sqlite:" + databaseName;
		conn = null;
	}

	private static void connectToDatabase() {
		try {
			conn = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite database engine has been established.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void closeConnectionToDatabase() {
		try {
			if (conn.isClosed())
			{
				System.out.println("Connection to SQLite database engine is already closed.");
			} else {
				conn.close();
				System.out.println("Connection to SQLite database engine has been closed.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getUserId(User u) throws SQLException
	{
		connectToDatabase();
		
		String query = "SELECT u.user_id FROM USERS u "
				+ "WHERE u.username = '" + u.username + "'";

		String userId = null;

		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);

		userId = rs.getString("user_id");

		closeConnectionToDatabase();
		return userId;
	}

	public void insertUserRecord(User u) throws SQLException {
		String username = u.username;
		String password = u.password;
		String first_name = u.firstName;
		String last_name = u.lastName;
		Double cash_balance = u.cashBalance;
		Double portfolio_balance = u.portfolioBalance;
		
		connectToDatabase();
		
		String query =
				"INSERT INTO Users (username, password, first_name, last_name, cash_balance, portfolio_balance) " +
						" VALUES('"+username+"','"+password+"', '"+first_name+"','"+last_name+"',"+cash_balance+","+portfolio_balance+");";
		
		System.out.println(query);

		Statement statement = conn.createStatement();
		statement.executeUpdate(query);
		
		closeConnectionToDatabase();
	}

	public ArrayList<User> readAllUserData() throws SQLException {
		connectToDatabase();
		
		String query = "SELECT * FROM Users";
		ArrayList<User> listOfUsers = null;

		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);

		listOfUsers = new ArrayList<User>();

		while (rs.next())
		{
			HashMap<String, Integer> userMap;

			String username = rs.getString("username");
			String password = rs.getString("password");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			Double cashBalance = (Double) rs.getDouble("cash_balance");
			Double portfolioBalance = (Double) rs.getDouble("portfolio_balance");

			String stockQuery = "SELECT *"
					+ "FROM Stocks s, Users u"
					+ "WHERE u.user_id = s.user_id";

			statement = conn.createStatement();
			ResultSet rsStocks = statement.executeQuery(stockQuery);

			userMap = new HashMap<String, Integer>();

			while (rsStocks.next())
			{
				userMap.put(rs.getString("stock_ticker"), Integer.valueOf(rs.getInt("quantity")));
			}

			listOfUsers.add(new User(username, password, firstName, lastName, cashBalance, userMap));
		}
		
		closeConnectionToDatabase();
		return listOfUsers;
	}

	public HashMap<String, Integer> readStockData(User s)
	{
		connectToDatabase();
		
		String query = "SELECT s.stock_ticker, s.quantity "
				+ "FROM Stocks s, Users u "
				+ "WHERE s.user_id = u.user_id";

		HashMap<String, Integer> stockData = null;

		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);

			stockData = new HashMap<String, Integer>();

			while (rs.next())
			{
				stockData.put(rs.getString("stock_ticker"), Integer.valueOf(rs.getInt("quantity")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		closeConnectionToDatabase();
		return stockData;
	}

	public void addStockData(String userId, HashMap<String, Integer> h) throws SQLException {
		connectToDatabase();

		String selectQuery = "SELECT stock_ticker FROM Stocks";
		ArrayList<String> existingStocks = new ArrayList<String>();

		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(selectQuery);

		while (rs.next()) {
			existingStocks.add(rs.getString("stock_ticker"));
		}

		for (Map.Entry<String, Integer> mapEntry : h.entrySet()) {
			//If the map entry's stock ticker is already in the list of the user's stock tickers, just update
			if (existingStocks.contains(mapEntry.getKey())) {
				String updateQuery = "UPDATE Stocks "
						+ "SET quantity = " + mapEntry.getValue()
						+ " WHERE user_id = '" + userId + "' AND stock_ticker = '" + mapEntry.getKey() + "'";

				statement.executeUpdate(updateQuery);
			} else { //else, create a new record
				String insertQuery = "INSERT INTO Stocks(user_id, stock_ticker, quantity) "
						+ "VALUES(" + userId + ", '" + mapEntry.getKey() + "', " + mapEntry.getValue() + ")";

				statement.executeUpdate(insertQuery);
			}
		}

		closeConnectionToDatabase();
	}
	
	public User authenticate(String username, String password) throws SQLException
	{
		connectToDatabase();
		
		String query = "SELECT * FROM Users u "
				+ "WHERE u.username = '" + username + "' AND u.password = '" + password + "'";
		
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		if (rs.next())
		{
			String stockQuery = "SELECT s.stock_ticker, s.quantity "
					+ "FROM Users u, Stocks s "
					+ "WHERE u.user_id = s.user_id ";
			
			HashMap<String, Integer> stockData = new HashMap<String, Integer>();
			
			statement = conn.createStatement();
			ResultSet stocksRs = statement.executeQuery(stockQuery);
			
			while (stocksRs.next())
			{
				stockData.put(stocksRs.getString("stock_ticker"), Integer.valueOf(stocksRs.getInt("quantity")));
			}
			
			User returnedUser = new User(username, password, rs.getString("first_name"), rs.getString("last_name"), (Double) rs.getDouble("cash_balance"), stockData);
			
			closeConnectionToDatabase();
			return returnedUser;
		} else {
			closeConnectionToDatabase();
			return null;
		}
	}
	
	private void printAllRecordsFromTable(String table) throws SQLException {
		connectToDatabase();
		String query = "SELECT * FROM " + table + ";";
		
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		int counter = 1;
		while (rs.next())
		{
			System.out.println(rs.getString(counter));
			counter++;
		}
		closeConnectionToDatabase();
	}
}