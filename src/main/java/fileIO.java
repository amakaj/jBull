// jBull | Handles the operations for reading and writing data for a user to a file
package main.java;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

public class fileIO {
	String fileName = null;
	CSVWriter writer = null;
	ArrayList<String[]> data = new ArrayList<String[]>();

	//Default constructor - should not be used
	public fileIO() throws IOException
	{
		fileName="transactionLog.csv"; 
		writer = new CSVWriter(new FileWriter(fileName,true));
	}

	//Constructor to write a string to a new file - should not be used
	public fileIO(String fn) throws IOException
	{
		fileName = fn;
		writer = new CSVWriter(new FileWriter(fileName,true));
	}

	//Add initial user data to the CSV
	public void addtoCSV(User s) throws IOException
	{
		String[] a = {s.getFirstName(), s.getLastName(), s.getUsername(),s.getPassword(),s.getEmail(), (s.getCashBalance()).toString()};
		data.add(a);
		writer.writeAll(data);
		writer.flush();
	}
	
	//Reads the entire CSV file and returns an arraylist of all fields
	public ArrayList<User> readAllUserData() throws CsvValidationException, IOException {
		//Instantiates objects for storing temporary file data and reading file
		FileReader fileReader = new FileReader(fileName);
		CSVReader csvReader = new CSVReader(fileReader);
		String[] nextRecord;
		
		//Creates listOfUsers object to return towards the end
		ArrayList<User> listOfUsers = new ArrayList<User>();
		
		while ((nextRecord = csvReader.readNext()) != null) {
			HashMap<String, Integer> userMap = new HashMap<String, Integer>();
			
			String firstName = nextRecord[0];
			String lastName = nextRecord[1];
			String username = nextRecord[2];
			String password = nextRecord[3];
			String email = nextRecord[4];
			Double cashBalance = Double.parseDouble(nextRecord[5]);
				
			int j = 7;
			for (int i = 6; i < nextRecord.length; i+=2) {
				userMap.put(nextRecord[i], Integer.parseInt(nextRecord[j]));
				j+=2;
			}
			listOfUsers.add(new User(firstName, lastName, username, password, email, cashBalance, userMap));
		}
		return listOfUsers;
	}
	
	//Reads the stock data from the CSV for a given user and returns a HashMap<String, Integer>
	public HashMap<String, Integer> readStockData(User s) throws IOException, FileNotFoundException, CsvValidationException
	{
		HashMap<String, Integer> outputMap = new HashMap<String, Integer>();
		
		//Instantiates objects for storing temporary file data and reading file
		FileReader fileReader = new FileReader(fileName);
		CSVReader csvReader = new CSVReader(fileReader);
		String[] nextRecord, stockData;
		
		while ((nextRecord = csvReader.readNext()) != null) {
			//If the username and password match the data from the user object "s", put all stock data onto outputMap, and return outputMap
			if ((s.getUsername()).equals(nextRecord[2]) && ((s.getPassword()).equals(nextRecord[3]))) {
				int j = 7;
				for (int i = 6; i < nextRecord.length; i+=2) {
					outputMap.put(nextRecord[i], Integer.parseInt(nextRecord[j]));
					j+=2;
				}
			}
		}
		return outputMap;
	}
	
	//Adds stock data to a given user object, using a HashMap
	public void addStockData(User s, HashMap<String, Integer> h) throws IOException, CsvException
	{
		//Converts hashtable to String[] in order to write to CSV
		String[] convertedMap = new String[h.size()*2];
		String[] userData = new String[6];
		int i = 0, j = 1;
		
		for (Map.Entry<String, Integer> pair : h.entrySet()) {
			convertedMap[i] = pair.getKey();
			convertedMap[j] = (pair.getValue()).toString();
			i+=2;
			j+=2;
		}
		
		/*We're going to take all of the items out of the file, put it into a List<String[]>,
		 * remove the user for whom the stock data is being changed, modify it, append it into a
		 * new String[], and then reinsert it back into the list, then rewrite all of the data
		 * back to the file
		 */
		
		//Open the file for reading the entire file
		FileReader fileReaderInitial = new FileReader(fileName);
		CSVReader csvReaderInitial = new CSVReader(fileReaderInitial);
		List<String[]> tempList = csvReaderInitial.readAll();
		
		csvReaderInitial.close();
		fileReaderInitial.close();
		
		//Open the file for reading each line
		String[] nextRecord;
		FileReader fileReader = new FileReader(fileName);
		CSVReader csvReader = new CSVReader(fileReader);
		
		//Will iterate through the CSV for each line, as long as it exists
		while ((nextRecord = csvReader.readNext()) != null) {
			if ((s.getUsername()).equals(nextRecord[2]) && ((s.getPassword()).equals(nextRecord[3]))) {

				userData[0] = s.getFirstName();
				userData[1] = s.getLastName();
				userData[2] = s.getUsername();
				userData[3] = s.getPassword();
				userData[4] = s.getEmail();
				userData[5] = s.getCashBalance().toString();
				
				//Puts user data and stock data together on one String[]
				String[] appendedStringArr = Arrays.copyOf(userData, userData.length + convertedMap.length);
				System.arraycopy(convertedMap, 0, appendedStringArr, userData.length, convertedMap.length);
				
				
				tempList.removeIf(element -> (element[2].equals(s.getUsername()) && element[3].equals(s.getPassword())));
				tempList.add(appendedStringArr);
				
				//clear all data
				data.clear();
				FileWriter fw = new FileWriter(fileName, false);
				fw.close();
				
				//write all data
				data.addAll(tempList);
				writer.writeAll(data);
				writer.close();
				return;
			}
		}
	}

	//Prints the CSV to System.out
	public void printCSV() throws IOException, CsvValidationException {
		// Create an object of filereader
		// class with CSV file as a parameter.
		FileReader filereader = new FileReader(fileName);

		// create csvReader object passing
		// file reader as a parameter
		CSVReader csvReader = new CSVReader(filereader);
		String[] nextRecord;

		// we are going to read data line by line
		while ((nextRecord = csvReader.readNext()) != null) {
			for (String cell : nextRecord) {
				System.out.print(cell + "\t");
			}
			System.out.println();
		}
	}

	//Authenticates user and returns a User object
	public User authenticate(String username, String password) throws IOException, CsvValidationException {
		//Opens file
		FileReader fr = new FileReader(fileName);

		//Instantiates objects for storing temporary file data and reading file
		CSVReader csvReader = new CSVReader(fr);
		String[] nextRecord = null;
		String returnString = null;
		int currentIndex = 0;

		while (returnString == null) {
			//If there is no data in the field
			if ((nextRecord = csvReader.readNext()) == null) {
				return null;
			}

			//check for username or email
			if (nextRecord != null && ((nextRecord[2].equals(username) || nextRecord[4].equals(username)) && nextRecord[3].equals(password))) {
				return (new User(nextRecord[0], nextRecord[1], nextRecord[2], nextRecord[3], nextRecord[4], Double.parseDouble(nextRecord[5])));
			}
			currentIndex++;
		}
		return null;
	}
	
	//Returns a user object given a firstname and lastname
	public User retrieveUserFromName(String firstName, String lastName) throws CsvValidationException, IOException {
		FileReader fr = new FileReader(fileName);
		CSVReader csvReader = new CSVReader(fr);
		String[] nextRecord = null;
		String returnString = null;
		int currentIndex = 0;
		
		while (returnString == null) {
			//If there is no data in the field
			if ((nextRecord = csvReader.readNext()) == null) {
				return null;
			}

			//check for username or email
			if (nextRecord != null && ((nextRecord[0].equals(firstName) && nextRecord[1].equals(lastName)))) {
				return (new User(nextRecord[0], nextRecord[1], nextRecord[2], nextRecord[3], nextRecord[4], Double.parseDouble(nextRecord[5])));
			}
			currentIndex++;
		}
		return null;
	}
}
