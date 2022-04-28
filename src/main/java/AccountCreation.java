package main.java;

import java.awt.*;
import javax.swing.*;

import main.java.fileIO;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

//Account Creation
public class AccountCreation {

	private JFrame creationFrame;
	private JSeparator separator;
	private JLabel bullIcon;
	private JLabel lblNewLabel;
	private JLabel userLabel;
	private JTextField userField;
	private JLabel passLabel;
	private JTextField phoneField;
	private JLabel emailLabel;
	private JLabel phoneLabel;
	private JTextField emailField;
	private JButton backToLogin;
	private JButton createButton;
	private JPasswordField passField;

	public AccountCreation() {
		creationFrame = new JFrame("Account Creation");
		creationFrame.setBounds(100, 100, 450, 300);
		creationFrame.setSize(500, 500);
		creationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		creationFrame.setResizable(false);
		creationFrame.getContentPane().setLayout(null);

		ImageIcon bullToolbarIcon = new ImageIcon(
				new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
						.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		creationFrame.setIconImage(bullToolbarIcon.getImage());

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 500, 450);
		panel.setSize(500, 500);

		creationFrame.getContentPane().add(panel);
		panel.setLayout(null);

		separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBackground(Color.BLACK);
		separator.setBounds(0, 59, 500, 12);
		panel.add(separator);

		bullIcon = new JLabel();
		bullIcon.setBounds(20, 6, 49, 50);
		bullIcon.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png"))
				.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		panel.add(bullIcon);

		lblNewLabel = new JLabel("Account Creation");
		lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblNewLabel.setBounds(290, 18, 175, 30);
		panel.add(lblNewLabel);

		userLabel = new JLabel("Username");
		userLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		userLabel.setBounds(20, 82, 107, 16);
		panel.add(userLabel);

		userField = new JTextField();
		userField.setBackground(Color.WHITE);
		userField.setBounds(20, 100, 445, 20);
		panel.add(userField);
		userField.setColumns(10);

		passLabel = new JLabel("Password");
		passLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		passLabel.setBounds(20, 142, 107, 16);
		panel.add(passLabel);

		phoneField = new JTextField();
		phoneField.setColumns(10);
		phoneField.setBackground(Color.WHITE);
		phoneField.setBounds(20, 287, 445, 20);
		panel.add(phoneField);

		emailLabel = new JLabel("Email");
		emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		emailLabel.setBounds(20, 207, 82, 16);
		panel.add(emailLabel);

		phoneLabel = new JLabel("Phone");
		phoneLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		phoneLabel.setBounds(20, 268, 107, 16);
		panel.add(phoneLabel);

		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBackground(Color.WHITE);
		emailField.setBounds(20, 225, 445, 20);
		panel.add(emailField);

		backToLogin = new JButton("Back to Login");

		backToLogin.setBackground(Color.WHITE);
		backToLogin.setFont(new Font("SansSerif", Font.PLAIN, 18));
		backToLogin.setBounds(59, 343, 150, 50);
		panel.add(backToLogin);

		createButton = new JButton("Create");
		createButton.setBackground(Color.WHITE);
		createButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
		createButton.setBounds(267, 343, 150, 50);

		panel.add(createButton);

		passField = new JPasswordField();
		passField.setBackground(Color.WHITE);
		passField.setBounds(20, 162, 445, 20);
		panel.add(passField);
		creationFrame.setVisible(true);

		// ACTION LISTENERS
		backToLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creationFrame.setVisible(false);
				LoginScreen l1 = new LoginScreen();
				creationFrame.dispose();
			}
		});
		
		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String username_string = userField.getText().trim();
				String password_string = passField.getText().trim();
				String email_string = emailField.getText().trim();
				String phone_string = phoneField.getText().trim();
				
				if((username_string == "" || username_string == null || username_string.length() == 0) || (password_string == "" || password_string == null || password_string.length() == 0)
				   || (email_string == "" || email_string == null || email_string.length() == 0) || ((phone_string == "" || phone_string == null || phone_string.length() == 0))) {
					JOptionPane.showMessageDialog(null, "ERROR! Empty Fields!", "Account Creation",JOptionPane.WARNING_MESSAGE);
				
				}
				
				userField.setText("");
				passField.setText("");
				emailField.setText("");
				phoneField.setText("");
				
				User s = new User(username_string,password_string,email_string,phone_string);
	
				try {
					fileIO fio = new fileIO("add_user.txt");
					fio.addtoCSV(s);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
			
		});
	}
}
