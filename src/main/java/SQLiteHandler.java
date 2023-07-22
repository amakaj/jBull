package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SQLiteHandler {
	final static String databaseName = "jBullDB.db";
	static String url = "jdbc:sqlite:" + databaseName;
	static Connection conn = null;

	public static void connectToDatabase() {
		try {
			conn = DriverManager.getConnection(url);

			System.out.println("Connection to SQLite database engine has been established.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void createSchema() {
			try {
				File file = new File("src/main/sql/CreateSchema.sql");
				Scanner sc = new Scanner(file);
				
				StringBuilder sb = new StringBuilder("");
				
				while (sc.hasNextLine() == true)
				{
					sb.append(sc.nextLine());
				}
				
				Statement statement = conn.createStatement();
				statement.executeUpdate(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	

	
	public static void deleteRecord(String tableName, String name) {
		String query = "DELETE FROM " + tableName + " WHERE name='" + name + "'";
		
		try {
			Statement statement = conn.createStatement();
			boolean result = statement.execute(query);
			
			if (result == false) {
				System.out.println("Record deleted successfully");
			} else {
				System.out.println("Error deleting record");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printAllRecords(String tableName) {
		String query = "SELECT * FROM " + tableName;
		
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				System.out.println(resultSet.getString("name") + " " + resultSet.getInt("age"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		connectToDatabase();
		//createTable("Table1");
		createSchema();
		System.out.println();
		
	}
}
