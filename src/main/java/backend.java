package main.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;

public class backend extends JFrame
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;


	// global variables
	public static JTextArea top;
	public static JTextArea leftTop;
	public static JTextArea center;
	public static JTextArea leftBottom;


	//
	// constructor
	//
	public backend()
	{
		InetAddress ipAddress = null;
		try
		{
			ipAddress = InetAddress.getLocalHost();
		}
		catch (UnknownHostException el)
		{
			el.printStackTrace();
		}

		String titleString = "JBull Backend   :   Rel Date  :  Mar 3, 2022       " +
				"IP : " + ipAddress.getHostAddress() + "     Port# : 3333";
		setTitle(titleString);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		//
		// set the Frame size
		//
		setSize(1020, 900);

		//
		// panel title
		//
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(new EtchedBorder(), "JBull Rev.1 JAVA Swing"));


		setContentPane(contentPane);
		//
		// TOP - available text area - has the real-time clock display for now
		//
		top = new JTextArea();
		top.setEditable(false);
		top.setBounds(15, 20, 303, 58);
		top.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		top.setBackground(Color.WHITE);
		contentPane.add(top);

		//
		// LEFT - available text area
		//
		leftTop = new JTextArea();
		leftTop.setAlignmentX(LEFT_ALIGNMENT);
		leftTop.setEditable(false);
		leftTop.setBounds(15, 120, 261, 251);
		leftTop.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		leftTop.setBackground(Color.LIGHT_GRAY);
		contentPane.add(leftTop);
		leftTop.setEditable(false);
		leftTop.setText("REVENUE: \n");
		leftTop.append("STOCK PRICES"); 


		//
		// main area for socket server to display messages
		//
		center = new JTextArea();
		center.setEditable(false);
		center.setBounds(288, 120, 416, 555);
		center.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		center.setBackground(Color.LIGHT_GRAY);
		contentPane.add(center);
		center.setEditable(false);
		center.setText("BUY/SELL INFORMATION \n");
		center.append("USERS: username, ssn, type, amount"); 


		//
		// RIGHT - available text
		//
		leftBottom = new JTextArea();
		leftBottom.setEditable(false);
		leftBottom.setBounds(15, 383, 261, 292);
		leftBottom.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		leftBottom.setBackground(Color.LIGHT_GRAY);
		contentPane.add(leftBottom);
		leftBottom.setText("USERS: \n");
		leftBottom.append("First Name Last Name username");

		//
		// define all BUTTONS
		//
		JButton exitButton = new JButton("EXIT");
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		exitButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int result = JOptionPane.showConfirmDialog(null,
						"Do you really want to exit the Socket Server?",
						"JBull Socket Server Backend",
						JOptionPane.INFORMATION_MESSAGE);

				if (result == JOptionPane.OK_OPTION)
				{
					// socketServer.writeHashTableData();

					dispose();
					System.exit(0);
				}		
			}
		});
		exitButton.setBounds(6, 687, 187, 58);;
		contentPane.add(exitButton);;





		//JButton clients = new JButton("Clients");
		//clients.setOnAction(new EventHandler<ActionEvent>()
		//{
		//	@Override
		// 	public void handle(ActionEvent e)
		// 	{
		//
		//	}
		//});



		//JButton showLog = new JButton("Show Log");
		//showLog.setOnAction(new EventHandler<ActionEvent>()
		//{
		//	@Override
		// 	public void handle(ActionEvent e)
		// 	{
		//		Platform.runLater(new Runnable()
		//		 {
		//			    String logString = "";
		//			    
		//		        public void run()
		//		        {
		//		        	try
		//		            {
		//		        	      File f = new File("transactionLog.txt");
		//		        	      if (f.exists())
		//		        	      {
		//		                    FileReader reader = new FileReader("transactionLog.txt");
		//		                    BufferedReader br = new BufferedReader(reader);
		//		                  
		//		                    String line = br.readLine();
		//		                    while (line != null)
		//		                    {
		//		                    	logString = logString + line + "\r\n";
		//		                    	line = br.readLine();
		//		                    }
		//		                    
		//		                    br.close();
		//		        	      }
		//		        	      else
		//		        	      {
		//		        	    	  logString = "No log File Found!";
		//		        	      }
		//		        	 }
		//		             catch(Exception e2)
		//		             {   
		// e2.printStackTrace();
		//		             }		
		//		        	
		//		             Alert alert = new Alert(Alert.AlertType.INFORMATION);
		//		             alert.setTitle("--- Ticket Kiosk ---");
		//		             alert.setHeaderText("Transaction Log File");
		//		          
		//		             alert.setContentText(logString);
		//		             alert.setWidth(300);
		//		             alert.setHeight(600);
		//		             alert.showAndWait();
		//		        }
		//		    });	
		//	}
		//});




		//JButton summary = new JButton("Summary");
		//summary.setOnAction(new EventHandler<ActionEvent>()
		//{
		//	@Override
		//	public void handle(ActionEvent e)
		// 	{
		//
		//	}
		//});





		//JButton newKiosk = new JButton("New Kiosk");
		//newKiosk.setOnAction(new EventHandler<ActionEvent>()
		//{
		//	@Override
		//	public void handle(ActionEvent e)
		// 	{
		//		Platform.runLater(new Runnable()
		//		 {
		//		        public void run()
		//		        {
		//		          sockServer.createNewKiosk();
		//		          
		//		          Alert alert = new Alert(Alert.AlertType.INFORMATION);
		//		          alert.setTitle("--- Ticket Kiosk ---");
		//		          alert.setHeaderText("Total Number of Transactions");
		//		          
		//		          alert.setContentText(sockServer.getAllTransactions());
		//		          
		//		          alert.showAndWait();
		//		        }
		//		    });	
		//	}
		//});



		//JButton query1 = new JButton("Query #1");
		//query1.setOnAction(new EventHandler<ActionEvent>()
		//{
		//	@Override
		// 	public void handle(ActionEvent e)
		// 	{
		//
		//	}
		//});




		//JButton query2 = new JButton("Query #2");
		//query2.setOnAction(new EventHandler<ActionEvent>()
		//{
		//	@Override
		// 	public void handle(ActionEvent e)
		// 	{
		//
		//	}
		//});








		//JButton helpButton = new JButton("HELP");
		//helpButton.setOnAction(new EventHandler<ActionEvent>()
		//{
		//	@Override
		// 	public void handle(ActionEvent e)
		// 	{
		//		 Platform.runLater(new Runnable()
		//		 {
		//		        public void run()
		//		        {
		//		          Alert alert = new Alert(Alert.AlertType.INFORMATION);
		//		          alert.setTitle("--- Ticket Kiosk Help Window ---");
		//		          alert.setHeaderText("Help Screen");
		//		          
		//		          String hStr="- Click on   EXIT   button to quit the socket server.\r\n" +
		//		        		      "- Click on   Show Log   to display current transaction log file.\r\n" +
		//		        		      "- Click on   New Kiosk   to create the next ticket kiosk station.\r\n" +
		//		                      "- Click on   LIST KIOSKS to display current status of kiosks.\r\n";
		//		          
		//		          alert.setContentText(hStr);
		//		          alert.showAndWait();
		//		        }
		//		    });
		//	}
		//});		



		// start all threads  for the GUI screen here
		startRealTimeClock();





		// start the socket server thread - will start to accept client connections
		startSockServer();





		//
		// lights, camera, action
		//
		contentPane.setLayout (null);

		JScrollPane scrollPane = new JScrollPane(leftTop);
		scrollPane.setBounds(15, 120, 261, 251);
		contentPane.add(scrollPane);

		this.setLocationRelativeTo(leftTop);

		JTextArea textArea = new JTextArea();
		textArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		textArea.setBackground(Color.LIGHT_GRAY);
		textArea.setBounds(716, 120, 285, 251);
		contentPane.add(textArea);
		textArea.setEditable(false);
		textArea.setText("USER GROWTH \n");
		textArea.append("USERS: username"); 

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		textArea_1.setBackground(Color.LIGHT_GRAY);
		textArea_1.setBounds(716, 382, 285, 292);
		contentPane.add(textArea_1);
		textArea_1.setEditable(false);
		textArea_1.setText("POPULAR STOCKS OF THE DAY: \n");
		textArea_1.append("STOCK SYMBOLS, PRICES"); 
	}


	/*
	 * Thread to update weather info for NYC and Boston    
	 */     
	private void startSockServer()
	{	
		Thread refreshWeatherThread = new Thread()
		{
			public void run()
			{ 	
				//  socketServer.runSockServer();
			}
		};

		refreshWeatherThread.start();
	}


	/*
	 * Thread to update real-time clock
	 */     
	private void startRealTimeClock()
	{	
		Thread refreshClock = new Thread()
		{
			public void run()
			{  
				while (true)
				{	 			      
					Date   date = new Date();
					String str = String.format("\n    %tc", date);

					top.setText("");
					top.setText(str);

					try
					{
						sleep(1000L);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace ();
					}
				} // end while true
			}
		};

		refreshClock.start();
	}

	//
	// main method starts here
	//
	public static void main(String[] args)
	{
		backend b1 = new backend();
	}
}
