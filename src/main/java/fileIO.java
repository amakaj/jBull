package main.java;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class fileIO {
	String fileName = null;
	
	public fileIO()
	{
	    fileName="transactionLog.txt";	
	}
	
	public fileIO(String fn)
	{
		fileName = fn;
	}
	
	public void wrTransactionData(String dataStr)
	{
        FileWriter fwg = null;
        try 
        {
        	// open the file in append write mode
        	fwg = new FileWriter(fileName, true);
        }
        catch (IOException e)
        {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
   	    
        BufferedWriter bwg = new BufferedWriter(fwg);
        PrintWriter outg   = new PrintWriter(bwg);
		
        String timeStamp = new SimpleDateFormat("MM-dd-yyyy HH.mm.ss").format(new Date());
        
        outg.println(timeStamp + " : " + dataStr);
        
        outg.close();
	}

}
