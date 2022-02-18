package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginScreen {
	private JTextField userField;
	private JPasswordField passField;
	
	public LoginScreen() {
		JFrame loginFrame = new JFrame("jBull Login");
		
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setSize(350, 500);
		loginFrame.setResizable(false);
		loginFrame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setFont(new Font("SansSerif", Font.PLAIN, 35));
		panel.setBounds(0, 0, 334, 461);
		loginFrame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel bulliconlabel = new JLabel();
		bulliconlabel.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png")).getImage().getScaledInstance(125, 125, Image.SCALE_DEFAULT)));
		bulliconlabel.setBounds(99, 23, 128, 158);
		panel.add(bulliconlabel);
		
		JLabel jbullLabel = new JLabel("jBull");
		jbullLabel.setBackground(Color.WHITE);
		jbullLabel.setFont(new Font("SansSerif", Font.PLAIN, 30));
		jbullLabel.setBounds(133, 182, 58, 33);
		panel.add(jbullLabel);
		
		JLabel userLabel = new JLabel("Username");
		userLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		userLabel.setBounds(37, 246, 114, 16);
		panel.add(userLabel);
		
		userField = new JTextField();
		userField.setBackground(Color.WHITE);
		userField.setBounds(37, 265, 263, 20);
		panel.add(userField);
		userField.setColumns(10);
		
		JLabel passLabel = new JLabel("Password");
		passLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		passLabel.setBounds(37, 296, 114, 16);
		panel.add(passLabel);
		
		JButton loginButton = new JButton("Login");

		loginButton.setForeground(Color.BLACK);
		loginButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
		loginButton.setBackground(Color.WHITE);
		loginButton.setBounds(37, 364, 114, 41);
		panel.add(loginButton);
		
		JButton createButton = new JButton("Create");
		createButton.setForeground(Color.BLACK);
		createButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
		createButton.setBackground(Color.WHITE);
		createButton.setBounds(186, 364, 114, 41);
		panel.add(createButton);
		
		passField = new JPasswordField();
		passField.setBounds(37, 314, 263, 20);
		panel.add(passField);
	
		loginFrame.setVisible(true);
		
		ImageIcon bullToolbarIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		loginFrame.setIconImage(bullToolbarIcon.getImage());
		
		//ACTION LISTENERS
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loginFrame.setVisible(false);
				Dashboard d1 = new Dashboard();
				loginFrame.dispose();
			}
		});
		
		createButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				loginFrame.setVisible(false);
				AccountCreation a1 = new AccountCreation();
				loginFrame.dispose();
			}
		});
	}
	
	public static void main(String[] args) {
		// Schedule jobs for the event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginScreen l1 = new LoginScreen();
			}
		});
	}
}

