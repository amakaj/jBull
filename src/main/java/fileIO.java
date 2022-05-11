package main.java;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.CSVReader;	

public class fileIO {
	String fileName = null;
	CSVWriter writer = null;
	ArrayList<String[]> data = new ArrayList<String[]>();

	public fileIO() throws IOException
	{
		fileName="transactionLog.csv"; 
		writer = new CSVWriter(new FileWriter(fileName,true));
	}

	public fileIO(String fn) throws IOException
	{
		fileName = fn;
		writer = new CSVWriter(new FileWriter(fileName,true));
	}

	public void addtoCSV(User s) throws IOException
	{
		String[] a = {s.getFirstName(), s.getLastName(), s.getUsername(),s.getPassword(),s.getEmail(), (s.getCashBalance()).toString()};
		data.add(a);
		writer.writeAll(data);
		writer.flush();
	}
	
	public HashMap<String, Integer> readStockData(User s) throws IOException, FileNotFoundException, CsvValidationException
	{
		HashMap<String, Integer> outputMap = new HashMap<String, Integer>();
		
		FileReader fileReader = new FileReader(fileName);
		CSVReader csvReader = new CSVReader(fileReader);
		String[] nextRecord, stockData;
		
		while ((nextRecord = csvReader.readNext()) != null) {
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
	
	public void addStockData(User s, HashMap<String, Integer> h) throws IOException, CsvException
	{
		String[] convertedMap = new String[h.size()*2];
		String[] userData = new String[6];
		int i = 0, j = 1;
		int currentIndex = 0;
		
		
		for (Map.Entry<String, Integer> pair : h.entrySet()) {
			convertedMap[i] = pair.getKey();
			convertedMap[j] = (pair.getValue()).toString();
			i+=2;
			j+=2;
		}
		
		FileReader fileReaderInitial = new FileReader(fileName);
		CSVReader csvReaderInitial = new CSVReader(fileReaderInitial);
		String[] nextRecord;
		
		List<String[]> tempList = csvReaderInitial.readAll();
		csvReaderInitial.close();
		fileReaderInitial.close();
		
		FileReader fileReader = new FileReader(fileName);
		CSVReader csvReader = new CSVReader(fileReader);
		
		while ((nextRecord = csvReader.readNext()) != null) {
			if ((s.getUsername()).equals(nextRecord[2]) && ((s.getPassword()).equals(nextRecord[3]))) {
				/*We're going to take all of the items out of the file, put it into a List<String[]>,
				 * remove the user for whom the stock data is being changed, modify it, append it into a
				 * new String[], and then reinsert it back into the list, then rewrite all of the data
				 * back to the file
				 */
				userData[0] = s.getFirstName();
				userData[1] = s.getLastName();
				userData[2] = s.getUsername();
				userData[3] = s.getPassword();
				userData[4] = s.getEmail();
				userData[5] = s.getCashBalance().toString();
				
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
			currentIndex++;
		}
	}

	public void readFromCSV() throws IOException, CsvValidationException {
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

	public User authenticate(String username, String password) throws IOException, CsvValidationException {
		FileReader fr = new FileReader(fileName);

		CSVReader csvReader = new CSVReader(fr);
		String[] nextRecord = null;
		String returnString = null;
		int currentIndex = 0;

		while (returnString == null) {
			if ((nextRecord = csvReader.readNext()) == null) {
				return null;
			}

			//check for username or email
			if (nextRecord != null && ((nextRecord[2].equals(username) || nextRecord[4].equals(username)) && nextRecord[3].equals(password))) {
				return (new User(nextRecord[0], nextRecord[1], nextRecord[2], nextRecord[3], nextRecord[4]));
			}
			currentIndex++;
		}
		return null;
	}
}
