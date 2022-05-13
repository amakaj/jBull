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
	JMenuBar servermenubar = new JMenuBar();
	private static JTextArea responseConsole = new JTextArea();
	private static JTextArea connectionsList = new JTextArea();
	private static String stringOfMessages = "";
	private static String serverIP;
	private static String portAndIPInfo;
	private static boolean found = false;
	private static String stringOfConnections = "";
	private static int totalNumOfUsers = 0;
	private static JScrollPane stockTableScrollPane;
	private static JScrollPane usersListScrollPane;
	private static JList userList;
	private static ArrayList<User> listOfUserObjs = new ArrayList<User>();
	private static JLabel userDataLabel;
	private static JLabel usernameLabel;
	private static JLabel emailLabel;
	private static HashMap<Integer, String> listOfConnections = new HashMap<Integer, String>();	
	private static JTable stockTable;
	private static JLabel lblTotal = new JLabel("Total: " + totalNumOfUsers);

	private static void updateConnectionList() {
		listOfConnections.forEach((clientnum, ip) -> stringOfConnections += "\nClient #: " + clientnum + "\nIP: " + ip);
		if (listOfConnections.size() == 0) {
			connectionsList.setText("No Connections");
		} else {
			connectionsList.setText("Number of Connections: " + listOfConnections.size() + stringOfConnections);
		}
		stringOfConnections = "";
	}

	public static void startServerThread(Socket inputSocket) {
		Thread serverThread = new Thread() {
			public void run() {
				User receivedUser = null;
				String clientNumber;
				String threadName = currentThread().getName();

				try {

					//					BufferedReader in = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));
					ObjectInputStream in = new ObjectInputStream(inputSocket.getInputStream());

					if ((currentThread().getName()).substring((currentThread().getName()).length()-2).contains("-")) {
						clientNumber = threadName.substring(threadName.length()-1);
					} else {
						clientNumber = threadName.substring(threadName.length()-2);
					}
					//inserts into hashtable
					listOfConnections.put(Integer.valueOf(clientNumber), (inputSocket.getInetAddress()).toString().substring(1) /*to get rid of the slash at the beginning of the IP*/);

					updateConnectionList();

					while (true) {

						try {
							receivedUser = (User) in.readObject();
							listOfUserObjs.add(receivedUser);
							updateUserList();

							/* Checks if the socket has closed, or if the client sends a "QUIT" which will prompt the server to
							 * disconnect from the server side
							 * 
							 * read() returns -1
							 * readLine() returns null
							 * readXXX() throws EOFException for any other XXX.
							 * A write will throw an IOException: 'connection reset by peer', eventually, subject to buffering delays.
							 */
							if (receivedUser == null || inputSocket.isClosed()) {
								listOfConnections.remove(Integer.valueOf(clientNumber));
								listOfUserObjs.remove(receivedUser);
								inputSocket.close();

								updateConnectionList();
								updateUserList();
								return; // we can exit thread by returning the run function
							} else { //if the socket has not closed, then we allow for messages to come in
								stringOfMessages += "\nUser" + clientNumber + ": " + receivedUser.getUsername();
								responseConsole.setText(portAndIPInfo + stringOfMessages);
							}
						} catch (EOFException e) {
							listOfConnections.remove(Integer.valueOf(clientNumber));
							listOfUserObjs.remove(receivedUser);
							inputSocket.close();

							updateConnectionList();
							updateUserList();
							return; // we can exit thread by returning the run function
						}
					}
				} catch (IOException|ClassNotFoundException e) {
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

	public static void startServer(int portNum) {
		Socket clientSocket;
		ServerSocket servSocket;
		InetAddress socketAddr;

		//Enumeration and Network interfaces code
		Enumeration<NetworkInterface> nets = null;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		for (NetworkInterface netint: Collections.list(nets)) {
			String dname = netint.getName();
			if (dname.startsWith("en") && !found) {
				Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

				int counter = 0;
				for (InetAddress inetAddress: Collections.list(inetAddresses)) {
					String myAddr = inetAddress.toString();
					myAddr = myAddr.replaceFirst("/", "");
					if (counter != 0) {
						serverIP = myAddr;
						found = true;
					}
					counter++;
				}
			}
		}


		try {
			if (serverIP == null) {
				serverIP = InetAddress.getLocalHost().getHostAddress();
			}
			socketAddr = InetAddress.getByName(serverIP);
			servSocket = new ServerSocket(portNum, 50, socketAddr);

			System.out.println("Socket address: " + socketAddr);
			System.out.println("Server socket: " + servSocket);

			portAndIPInfo = ("IP Address is: " + socketAddr.getHostAddress() + "\nListening on port " + servSocket.getLocalPort());
			responseConsole.setText(portAndIPInfo);

			// Code to listen for client connections
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

	public static void updateStockList(User currentUser) {
		HashMap<String, Integer> stockDataNew = currentUser.getStockData();
		DefaultTableModel stockTableModel;

		if (stockTable != null) { // if stocktable was found already
			// get the model from stocktable
			stockTableModel = (DefaultTableModel) stockTable.getModel();
		} else {
			//create a new model for stocktable
			stockTableModel = new DefaultTableModel();

			//add columns
			stockTableModel.addColumn("Symbol");
			stockTableModel.addColumn("# of Shares");
		}

		if (stockTableModel != null) {
			stockTableModel.getDataVector().removeAllElements();
		}

		if (!stockDataNew.isEmpty()) {
			//PARSING HASHMAP DATA
			Iterator hashMapIterator = stockDataNew.entrySet().iterator();

			while (hashMapIterator.hasNext()) {
				Map.Entry hmElement = (Map.Entry) hashMapIterator.next();

				stockTableModel.addRow(new Object[] {hmElement.getKey(), hmElement.getValue()});
			}
		}
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
		stockTableModel.fireTableDataChanged();

		stockTable.revalidate();
		stockTable.repaint();
	}

	private static void updateUserList() {
		DefaultListModel userListModel;

		if (userList != null) {
			userListModel = (DefaultListModel) userList.getModel();
		} else {
			userListModel = new DefaultListModel();
		}

		if (userListModel != null) {
			userListModel.removeAllElements();
			totalNumOfUsers = 0;
		}

		for (User u : listOfUserObjs) {
			userListModel.addElement(u.getFirstName() + " " + u.getLastName());
			totalNumOfUsers++;
		}

		if (listOfUserObjs.size() == 0) {
			totalNumOfUsers = 0;
		}
		lblTotal.setText("Total: " + totalNumOfUsers);

		userList = new JList(userListModel);
		userList.setFont(new Font("Arial", Font.PLAIN, 14));
		userList.setRequestFocusEnabled(false);
		userList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		userList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		userList.setBackground(Color.WHITE);
		userList.setModel(userListModel);
		userList.setBounds(30, 47, 240, 218);
		usersListScrollPane.setViewportView(userList);

		userList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("SELECTED!");
				if (userList.getSelectedValue() != null) {
					String userFirstNameLastName = userList.getSelectedValue().toString();
					String[] userNameArr = userFirstNameLastName.split(" ");
					for (User u : listOfUserObjs) {
						if (userNameArr[0].equals(u.getFirstName()) && userNameArr[1].equals(u.getLastName())) {
							updateStockList(u);
							userDataLabel.setVisible(true);
							usernameLabel.setText("Username: " + u.getUsername());
							emailLabel.setText("Email: " + u.getEmail());
						}
					}
				}
			}
		});

		userList.revalidate();
		userList.repaint();
	}

	public Backend()
	{
		//Frame Creation
		JFrame frame = new JFrame("jBull Server");
		frame.setSize(800, 500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setForeground(Color.WHITE);
		leftPanel.setBounds(0, 0, 300, 500);
		frame.getContentPane().add(leftPanel);

		JPanel rightPanel = new JPanel();
		rightPanel.setForeground(Color.WHITE);
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setBounds(300, 0, 500, 500);
		frame.getContentPane().add(rightPanel);
		rightPanel.setLayout(null);

		JLabel stocklistLabel = new JLabel("Stocks");
		stocklistLabel.setFont(new Font("Arial", Font.BOLD, 16));
		stocklistLabel.setBounds(23, 15, 88, 20);
		rightPanel.add(stocklistLabel);

		responseConsole.setEditable(false);
		responseConsole.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		responseConsole.setBounds(23, 186, 250, 217);
		rightPanel.add(responseConsole);

		JScrollPane responseConsoleScroll = new JScrollPane (responseConsole);
		responseConsoleScroll.setLocation(23, 186);
		responseConsoleScroll.setSize(250, 217);
		responseConsoleScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		rightPanel.add(responseConsoleScroll);

		JLabel responseConsoleLabel = new JLabel("Response Console");
		responseConsoleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		responseConsoleLabel.setBounds(23, 167, 197, 20);
		rightPanel.add(responseConsoleLabel);

		JLabel connectionsLabel = new JLabel("Connections");
		connectionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
		connectionsLabel.setBounds(283, 167, 126, 20);
		rightPanel.add(connectionsLabel);

		JScrollPane connectionsListScroll = new JScrollPane((Component) null);
		connectionsListScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		connectionsListScroll.setBounds(283, 186, 178, 217);
		rightPanel.add(connectionsListScroll);

		connectionsList = new JTextArea();
		connectionsList.setText("No Connections");
		connectionsList.setEditable(false);
		connectionsList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		connectionsListScroll.setViewportView(connectionsList);

		// Set the menu bar
		frame.setJMenuBar(servermenubar);
		servermenubar.setBackground(Color.WHITE);

		// Instantiate the image objects
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
//		ImageIcon blueSwooshIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/blueswoosh.png")).getImage()
//				/*.getScaledInstance(30, 30, Image.SCALE_DEFAULT)*/);
		frame.setIconImage(bullIcon.getImage());

		//Add to menu bar
		JLabel bullIconLabel = new JLabel(bullIcon);
		JLabel bullWhiteSpace = new JLabel("   ");
		servermenubar.add(bullWhiteSpace);
		servermenubar.add(bullIconLabel);
		servermenubar.add(Box.createHorizontalGlue()); // Separates right and left
		leftPanel.setLayout(null);

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

//		JLabel blueSwooshLabel = new JLabel(blueSwooshIcon);
//		leftPanel.add(blueSwooshLabel);
//		blueSwooshLabel.setBounds(-5, 300, 290, 149);

		usersListScrollPane = new JScrollPane();
		usersListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		usersListScrollPane.setBounds(30, 47, 240, 218);
		leftPanel.add(usersListScrollPane);

		updateUserList();

		JLabel usersLabel = new JLabel("Users");
		usersLabel.setFont(new Font("Arial", Font.BOLD, 16));
		usersLabel.setBounds(30, 29, 74, 20);
		leftPanel.add(usersLabel);


		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
		lblTotal.setBounds(179, 29, 91, 20);
		leftPanel.add(lblTotal);

		stockTableScrollPane = new JScrollPane();
		stockTableScrollPane.setBounds(23, 33, 248, 123);

		rightPanel.add(stockTableScrollPane);

		frame.setVisible(true);
		
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

		startClock();
		startServer(3333);
		

	}
	public static void main(String[] args) {
		Backend b = new Backend();
	}
}