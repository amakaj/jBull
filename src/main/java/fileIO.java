package main.java;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.opencsv.CSVWriter;
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
		String[] a = {s.getUsername(),s.getPassword(),s.getEmail(),s.getPhone()};
		data.add(a);
		writer.writeAll(data);
		writer.flush();
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

	public boolean authenticate(String username, String password) throws IOException, CsvValidationException {
		FileReader fr = new FileReader(fileName);

		CSVReader csvReader = new CSVReader(fr);
		String[] nextRecord = null;
		boolean userFound = false;

		while (!userFound) {
			if ((nextRecord = csvReader.readNext()) == null) {
				return false;
			}

			if (nextRecord != null && (nextRecord[0].equals(username) && nextRecord[1].equals(password))) {
				userFound = true;
			}
		}
		return userFound;
	}
}
