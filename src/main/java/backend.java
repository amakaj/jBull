package main.java;

import java.awt.*;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.net.*;
import java.io.*;


public class Backend
{
	JMenuBar servermenubar = new JMenuBar();
	static JTextArea responseConsole = new JTextArea();
	static JTextArea connectionsList = new JTextArea();
	static String connectionInformation;
	static String stringOfMessages = "";
	static int numOfConnections = 0;
	static String serverIP;
	static String portAndIPInfo;
	static boolean found = false;
	static HashMap<Integer, String> listOfConnections = new HashMap<Integer, String>();
	static String stringOfConnections = "";


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
				String receivedString = "";
				try {
					BufferedReader in = new BufferedReader(new InputStreamReader(inputSocket.getInputStream()));
					String clientNumber = (currentThread().getName()).substring((currentThread().getName()).length()-1);
					listOfConnections.put(Integer.valueOf(clientNumber), (inputSocket.getInetAddress()).toString().substring(1));

					updateConnectionList();

					while (true) {
						receivedString = in.readLine();

						/* Checks if the socket has closed, or if the client sends a "QUIT" which will prompt the server to
						 * disconnect from the server side
						 * 
						 * read() returns -1
						 * readLine() returns null
						 * readXXX() throws EOFException for any other XXX.
						 * A write will throw an IOException: 'connection reset by peer', eventually, subject to buffering delays.
						 */
						if (receivedString == null || inputSocket.isClosed() || receivedString.equals("QUIT")) {
							listOfConnections.remove(Integer.valueOf(clientNumber));
							inputSocket.close();
							
							updateConnectionList();
							return; // we can exit thread by returning the run function
						} else { //if the socket has not closed, then we allow for messages to come in
							stringOfMessages += "\nClient #" + clientNumber + ": " + receivedString;
							responseConsole.setText(portAndIPInfo + stringOfMessages);
						}
					}
				} catch (IOException e) {
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
	
	public Backend()
	{
		//Frame Creation
		JFrame frame = new JFrame("jBull Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
		frame.setResizable(false);
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

		JList watchlist = new JList();
		watchlist.setEnabled(false);
		watchlist.setModel(new AbstractListModel() {
			String[] values = new String[] {"No Stocks"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		watchlist.setRequestFocusEnabled(false);
		watchlist.setFont(new Font("Calibri", Font.PLAIN, 16));
		watchlist.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		watchlist.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		watchlist.setBackground(Color.WHITE);
		watchlist.setBounds(23, 25, 214, 124);
		rightPanel.add(watchlist);

		JList stocklist = new JList();
		stocklist.setEnabled(false);
		stocklist.setModel(new AbstractListModel() {
			String[] values = new String[] {"No Stocks"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		stocklist.setRequestFocusEnabled(false);
		stocklist.setFont(new Font("Calibri", Font.PLAIN, 16));
		stocklist.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		stocklist.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		stocklist.setBackground(Color.WHITE);
		stocklist.setBounds(247, 25, 214, 124);
		rightPanel.add(stocklist);

		JLabel watchlistLabel = new JLabel("Watchlist");
		watchlistLabel.setFont(new Font("Arial", Font.BOLD, 16));
		watchlistLabel.setBounds(23, 8, 107, 20);
		rightPanel.add(watchlistLabel);

		JLabel stocklistLabel = new JLabel("Stocks");
		stocklistLabel.setFont(new Font("Arial", Font.BOLD, 16));
		stocklistLabel.setBounds(247, 8, 88, 20);
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
		ImageIcon blueSwooshIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/blueswoosh.png")).getImage()
				/*.getScaledInstance(30, 30, Image.SCALE_DEFAULT)*/);
		frame.setIconImage(bullIcon.getImage());

		//Add to menu bar
		JLabel bullIconLabel = new JLabel(bullIcon);
		JLabel bullWhiteSpace = new JLabel("   ");
		servermenubar.add(bullWhiteSpace);
		servermenubar.add(bullIconLabel);
		servermenubar.add(Box.createHorizontalGlue()); // Separates right and left
		leftPanel.setLayout(null);

		JLabel blueSwooshLabel = new JLabel(blueSwooshIcon);
		leftPanel.add(blueSwooshLabel);
		blueSwooshLabel.setBounds(-5, 300, 290, 149);

		JList userList = new JList();
		userList.setFont(new Font("Arial", Font.PLAIN, 14));
		userList.setRequestFocusEnabled(false);
		userList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		userList.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		userList.setBackground(Color.WHITE);
		userList.setModel(new AbstractListModel() {
			String[] values = new String[] {"Anthony Makaj", "Syeda Islam", "Ibrahim Nabid", "Imu Islam", "John Doe", "Jane Doe", "Some Name"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		userList.setBounds(30, 47, 240, 218);
		leftPanel.add(userList);

		JLabel usersLabel = new JLabel("Users");
		usersLabel.setFont(new Font("Arial", Font.BOLD, 16));
		usersLabel.setBounds(30, 29, 74, 20);
		leftPanel.add(usersLabel);

		JLabel lblTotal = new JLabel("Total: 7");
		lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
		lblTotal.setBounds(179, 29, 91, 20);
		leftPanel.add(lblTotal);


		frame.setVisible(true);

		startClock();
		startServer(3333);
	}
	public static void main(String[] args) {
		Backend b = new Backend();
	}
}