// jBull Server | Accepts client connections and user objects to display such data all in one place
package main.java;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Backend
{
	//Create the server's menu bar, to store jBull icon, realtime clock, and "Server" label
	JMenuBar servermenubar = new JMenuBar();

	//Declaration of static variables, to allow for easy access between methods
	private static JTextArea responseConsole = new JTextArea();
	private static JTextArea connectionsList = new JTextArea();

	private static String stringOfMessages = "";
	private static String serverIP;
	private static String portAndIPInfo;
	private static String stringOfConnections = "";

	private static JScrollPane stockTableScrollPane;
	private static JScrollPane usersListScrollPane;

	private static boolean found = false;
	private static int totalNumOfUsers = 0;

	private static HashMap<Integer, String> listOfConnections = new HashMap<Integer, String>();	
	private static ArrayList<User> listOfUserObjs = new ArrayList<User>();
	private static JList userList;

	private static JLabel userDataLabel;
	private static JLabel usernameLabel;
	private static JLabel emailLabel;
	private static JTable stockTable;
	private static JLabel lblTotal = new JLabel("Total: " + totalNumOfUsers);

	//Updates the list of connections toward the right, to allow the server operator to keep track of the list of connected clients
	private static void updateConnectionList() {

		//For each client in the list of connections, it will store the client information in stringOfConnections, for all clients
		listOfConnections.forEach((clientnum, ip) -> stringOfConnections += "\nClient #: " + clientnum + "\nIP: " + ip);

		//If there are no connections, display no connections
		if (listOfConnections.size() == 0) {
			connectionsList.setText("No Connections");
		} else { //Otherwise, also display the number of connections
			connectionsList.setText("Number of Connections: " + listOfConnections.size() + stringOfConnections);
		}

		//Clear the stringOfConnections after method is called, so that when this method is called next time, it will already be clear and ready for overwriting
		stringOfConnections = "";
	}

	//Starts a server thread for a particular client using a given inputSocket
	public static void startServerThread(Socket inputSocket) {
		Thread serverThread = new Thread() {
			public void run() {

				//Declaration of variables
				User receivedUser = null; // User object that will be received from the client
				String clientNumber; // This is the clientNumber itself
				String threadName = currentThread().getName(); // Will be used to assign a number to the client

				try {
					//ObjectInputStream allows for an object to be passed into it, which it will get from the client using inputSocket.getInputStream()
					ObjectInputStream in = new ObjectInputStream(inputSocket.getInputStream());

					//Thread number will be trimmed and used to identify each individual client (giving each client it's own client number)
					if ((currentThread().getName()).substring((currentThread().getName()).length()-2).contains("-")) {
						clientNumber = threadName.substring(threadName.length()-1);
					} else {
						clientNumber = threadName.substring(threadName.length()-2);
					}

					//Inserts the client number into a HashMap, along with it's IP Address
					listOfConnections.put(Integer.valueOf(clientNumber), (inputSocket.getInetAddress()).toString().substring(1) /*to get rid of the slash at the beginning of the IP*/);
					updateConnectionList();

					//Allows for the client to send objects over and over, for each time that the client decides to send it
					while (true) {
						try {
							//Retrieve the object that was sent from the client to the server and insert it into the receivedUser object
							receivedUser = (User) in.readObject();

							listOfUserObjs.add(receivedUser);
							updateUserList();

							/* Checks if the socket has closed, or if the client sends a "QUIT" which will prompt the server to
							 * disconnect from the server side
							 * 
							 * Just for reference, these are the conditions that indicate the client has disconnected from the server:
							 * read() returns -1
							 * readLine() returns null
							 * readXXX() throws EOFException for any other XXX. (i.e., for readObject())
							 * A write will throw an IOException: 'connection reset by peer', eventually, subject to buffering delays.
							 */
							if (receivedUser == null || inputSocket.isClosed()) {
								//Clean up! Remove the client from the list of connections, close the socket, and update the necessary lists
								listOfConnections.remove(Integer.valueOf(clientNumber));
								listOfUserObjs.remove(receivedUser);
								inputSocket.close();

								updateConnectionList();
								updateUserList();
								return; // we can exit thread by returning the run function
							} else { //if the socket has not closed, then continue to allow for objects to come in
								stringOfMessages += "\nUser" + clientNumber + ": " + receivedUser.getUsername();
								responseConsole.setText(portAndIPInfo + stringOfMessages);
							}
						} catch (EOFException e) { //When the client has caused the server to throw an EOFException (i.e., when the client disconnects)
							//Clean up
							listOfConnections.remove(Integer.valueOf(clientNumber));
							listOfUserObjs.remove(receivedUser);
							inputSocket.close();

							updateConnectionList();
							updateUserList();
							return; // we can exit thread by returning the run function
						}
					}
				} catch (IOException|ClassNotFoundException e) {
					//For any other relevant exceptions that can be thrown, print it directly to the responseConsole to allow the server operator to diagnose the issue
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);

					//Print stack trace onto response console
					responseConsole.setText("ERROR:\n" + sw.toString());
				}
			}
		};
		serverThread.start();
	}

	//Starts the server when passed in a given portNum
	public static void startServer(int portNum) {
		Socket clientSocket;
		ServerSocket servSocket;
		InetAddress socketAddr;

		//Creates an Enumeration object to retrieve all of the host system's network interfaces, to find an IP address to open a socket with
		Enumeration<NetworkInterface> nets = null;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		for (NetworkInterface netint: Collections.list(nets)) {
			String dname = netint.getName();

			//Network interface cards in a system are typically denoted by "en" when retrieving network interfaces
			if (dname.startsWith("en") && !found) {
				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

				int counter = 0;
				for (InetAddress inetAddress: Collections.list(inetAddresses)) {
					String myAddr = inetAddress.toString();
					myAddr = myAddr.replaceFirst("/", "");

					//Assign the server IP to open the socket with, once the appropriate network interface is found
					if (counter != 0) {
						serverIP = myAddr;
						found = true;
					}
					counter++;
				}
			}
		}

		//Creates a server socket and binds the input port number to the server IP that was found using the network interface loop
		try {
			if (serverIP == null) {
				serverIP = InetAddress.getLocalHost().getHostAddress();
			}
			socketAddr = InetAddress.getByName(serverIP);

			// Create a new server socket and bind the port number with the IP address of the machine
			servSocket = new ServerSocket(portNum, 50, socketAddr); 

			//Outputs the address and socket data to console, for debugging purposes mostly
			System.out.println("Socket address: " + socketAddr);
			System.out.println("Server socket: " + servSocket);

			portAndIPInfo = ("IP Address is: " + socketAddr.getHostAddress() + "\nListening on port " + servSocket.getLocalPort());
			responseConsole.setText(portAndIPInfo);

			//Listens for client connections
			while (true) {
				clientSocket = servSocket.accept(); //Accepts connection
				responseConsole.setText(portAndIPInfo + stringOfMessages);
				startServerThread(clientSocket);
			}
		} catch (IOException e) {
			//Takes stack trace and pushes it to StringWriter
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			//Print stack trace onto response console
			responseConsole.setText("ERROR:\n" + sw.toString());
		}
	}

	// Starts a real-time clock thread
	private void startClock() {
		Thread startClockThread = new Thread() {
			public void run() {
				JLabel serverName = new JLabel("Server     ");
				JLabel clockLabel = new JLabel("nulltime");
				servermenubar.add(clockLabel);
				servermenubar.add(serverName);
				
				while (true) {
					try {
						Date date = new Date();
						clockLabel.setText(String.format("    %tr    ", date));
						// update every second (usually 1 second behind system clock)
						sleep(1000L);
					} catch (InterruptedException e) {
						JOptionPane.showMessageDialog(null, "ERROR: Time is broken!");
						continue;
					}
				}
			}
		};
		startClockThread.start();
	}

	//Updates the stock list of the selected user in the Users list
	public static void updateStockList(User currentUser) {
		//Retrieve the stock data from the currentUser object, for manipulation throughout the method
		HashMap<String, Integer> stockDataNew = currentUser.getStockData();
		DefaultTableModel stockTableModel;

		// If stock table was found already
		if (stockTable != null) { 
			//Get the model from stock table 
			stockTableModel = (DefaultTableModel) stockTable.getModel();
		} else {
			//Create a new model for stock table
			stockTableModel = new DefaultTableModel();

			//Add columns to the model
			stockTableModel.addColumn("Symbol");
			stockTableModel.addColumn("# of Shares");
		}

		//If there exists a stock table model, clear it of all data
		if (stockTableModel != null) {
			stockTableModel.getDataVector().removeAllElements();
		}

		//If the currentUser's stock data is not empty
		if (!stockDataNew.isEmpty()) {
			
			//Create an iterator and parse through the hashmap to add each field as an entry in the stock table
			Iterator hashMapIterator = stockDataNew.entrySet().iterator();

			while (hashMapIterator.hasNext()) {
				Map.Entry hmElement = (Map.Entry) hashMapIterator.next();
				stockTableModel.addRow(new Object[] {hmElement.getKey(), hmElement.getValue()});
			}
		}
		
		//Create the stock table using the stockTableModel and make the necessary modifications
		stockTable = new JTable(stockTableModel);
		stockTable.setFont(new Font("Arial", Font.PLAIN, 14));
		stockTable.setBorder(new LineBorder(new Color(0, 0, 0)));
		stockTable.setPreferredScrollableViewportSize(new Dimension(304, 249));
		stockTable.setFillsViewportHeight(true);
		stockTable.setBounds(16, 56, 304, 191);
		stockTableScrollPane.setViewportView(stockTable);
		stockTable.setFocusable(false);
		stockTable.setRowSelectionAllowed(false);
		stockTable.setColumnSelectionAllowed(false);
		stockTable.setDragEnabled(false);
		
		//Notify the model, and therefore the table, that data has changed
		stockTableModel.fireTableDataChanged();

		//Revalidate the data and redraw the stock table on the JPanel
		stockTable.revalidate();
		stockTable.repaint();
	}

	//Updates the list of users
	private static void updateUserList() {
		DefaultListModel userListModel;

		//If there exists a user list, retrieve the model from the list, otherwise create a new one
		if (userList != null) {
			userListModel = (DefaultListModel) userList.getModel();
		} else {
			userListModel = new DefaultListModel();
		}

		//If there exists a user list model, remove all the elements
		if (userListModel != null) {
			userListModel.removeAllElements();
			totalNumOfUsers = 0;
		}

		//For each user in the list of user objects, retrieve the first name and last name
		for (User u : listOfUserObjs) {
			userListModel.addElement(u.getFirstName() + " " + u.getLastName());
			totalNumOfUsers++;
		}

		//If there is no user objects in the list, indicate that there are 0 users
		if (listOfUserObjs.size() == 0) {
			totalNumOfUsers = 0;
		}
		
		//Set the label for the total # of users
		lblTotal.setText("Total: " + totalNumOfUsers);

		//Creation and modification of userList object
		userList = new JList(userListModel);
		userList.setFont(new Font("Arial", Font.PLAIN, 14));
		userList.setRequestFocusEnabled(false);
		userList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		userList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		userList.setBackground(Color.WHITE);
		userList.setModel(userListModel);
		userList.setBounds(30, 47, 240, 218);
		
		//Sets the userList inside the scroll pane, making the columns visible and the list scrollable
		usersListScrollPane.setViewportView(userList);

		//Allows for the list to update when a value is selected
		userList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (userList.getSelectedValue() != null) {
					
					//Retrieve the name and split it into firstName and lastName
					String userFirstNameLastName = userList.getSelectedValue().toString();
					String[] userNameArr = userFirstNameLastName.split(" ");
				
					for (User u : listOfUserObjs) {
						//If the retrieved first name and last name matches the name of the user object
						if (userNameArr[0].equals(u.getFirstName()) && userNameArr[1].equals(u.getLastName())) {
							//Update the stock list for that user and show the user data next to the stock list
							updateStockList(u);
							userDataLabel.setVisible(true);
							usernameLabel.setText("Username: " + u.getUsername());
							emailLabel.setText("Email: " + u.getEmail());
						}
					}
				}
			}
		});

		//Revalidate the data and redraw the userList on the panel
		userList.revalidate();
		userList.repaint();
	}

	public Backend()
	{
		//Frame Creation
		JFrame frame = new JFrame("jBull Server");
		frame.setSize(800, 500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //to allow for a warning message to display before exiting
		frame.getContentPane().setLayout(null);

		//Left panel and right panel creation
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setForeground(Color.WHITE);
		leftPanel.setBounds(0, 0, 300, 500);
		frame.getContentPane().add(leftPanel);
		leftPanel.setLayout(null);

		JPanel rightPanel = new JPanel();
		rightPanel.setForeground(Color.WHITE);
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setBounds(300, 0, 500, 500);
		frame.getContentPane().add(rightPanel);
		rightPanel.setLayout(null);

		//Creation of labels in the window
		JLabel stocklistLabel = new JLabel("Stocks");
		stocklistLabel.setFont(new Font("Arial", Font.BOLD, 16));
		stocklistLabel.setBounds(23, 15, 88, 20);
		rightPanel.add(stocklistLabel);
		
		JLabel usersLabel = new JLabel("Users");
		usersLabel.setFont(new Font("Arial", Font.BOLD, 16));
		usersLabel.setBounds(30, 29, 74, 20);
		leftPanel.add(usersLabel);
		
		JLabel responseConsoleLabel = new JLabel("Response Console");
		responseConsoleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		responseConsoleLabel.setBounds(23, 167, 197, 20);
		rightPanel.add(responseConsoleLabel);

		JLabel connectionsLabel = new JLabel("Connections");
		connectionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
		connectionsLabel.setBounds(283, 167, 126, 20);
		rightPanel.add(connectionsLabel);
		
		userDataLabel = new JLabel("User Data");
		userDataLabel.setVisible(false);
		userDataLabel.setFont(new Font("Arial", Font.BOLD, 16));
		userDataLabel.setBounds(283, 15, 124, 20);
		rightPanel.add(userDataLabel);

		usernameLabel = new JLabel("");
		usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		usernameLabel.setBounds(283, 41, 185, 20);
		rightPanel.add(usernameLabel);

		emailLabel = new JLabel("");
		emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		emailLabel.setBounds(283, 63, 185, 20);
		rightPanel.add(emailLabel);
		
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
		lblTotal.setBounds(179, 29, 91, 20);
		leftPanel.add(lblTotal);
		
		//Creation of scroll panes
		JScrollPane responseConsoleScroll = new JScrollPane (responseConsole);
		responseConsoleScroll.setLocation(23, 186);
		responseConsoleScroll.setSize(250, 217);
		responseConsoleScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		rightPanel.add(responseConsoleScroll);

		JScrollPane connectionsListScroll = new JScrollPane((Component) null);
		connectionsListScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		connectionsListScroll.setBounds(283, 186, 178, 217);
		rightPanel.add(connectionsListScroll);
		
		usersListScrollPane = new JScrollPane();
		usersListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		usersListScrollPane.setBounds(30, 47, 240, 218);
		leftPanel.add(usersListScrollPane);
		
		//Creation of console and list text areas
		responseConsole.setEditable(false);
		responseConsole.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		responseConsole.setBounds(23, 186, 250, 217);
		rightPanel.add(responseConsole);

		connectionsList = new JTextArea();
		connectionsList.setText("No Connections");
		connectionsList.setEditable(false);
		connectionsList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		connectionsListScroll.setViewportView(connectionsList);

		stockTableScrollPane = new JScrollPane();
		stockTableScrollPane.setBounds(23, 33, 248, 123);
		rightPanel.add(stockTableScrollPane);

		// Instantiate the image objects
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		frame.setIconImage(bullIcon.getImage());
		
		// This is commented out because this application in the form of a JAR file is not able to find the file
		//		ImageIcon blueSwooshIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/blueswoosh.png")).getImage()
		//				/*.getScaledInstance(30, 30, Image.SCALE_DEFAULT)*/);
		//		JLabel blueSwooshLabel = new JLabel(blueSwooshIcon);
		//		leftPanel.add(blueSwooshLabel);
		//		blueSwooshLabel.setBounds(-5, 300, 290, 149);
		
		// Set the menu bar
		frame.setJMenuBar(servermenubar);
		servermenubar.setBackground(Color.WHITE);
		JLabel bullIconLabel = new JLabel(bullIcon);
		JLabel bullWhiteSpace = new JLabel("   ");
		servermenubar.add(bullWhiteSpace);
		servermenubar.add(bullIconLabel);
		servermenubar.add(Box.createHorizontalGlue()); // Separates right and left

		frame.setVisible(true);
		updateUserList();

		//Allows for an exit confirmation to show before exiting
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?","EXIT",JOptionPane.YES_NO_OPTION);
				if(confirmed == JOptionPane.YES_OPTION)
				{
					frame.dispose();
					System.exit(0);
				} else {
					frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});

		//Start the real-time clock and the server functionalities
		startClock();
		startServer(3333);
	}
	
	public static void main(String[] args) {
		Backend b = new Backend();
	}
}