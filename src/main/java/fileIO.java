package main.java;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.opencsv.CSVWriter;

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
	


}
