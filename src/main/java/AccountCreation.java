// jBull | AccountCreation class to provide functionality for creating a user object
package main.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class AccountCreation {

	//Declaration of Swing objects
	private JFrame creationFrame;
	private JSeparator separator;
	private JLabel bullIcon;
	private JLabel lblNewLabel;
	private JLabel userLabel;
	private JTextField userField;
	private JLabel passLabel;
	private JLabel emailLabel;
	private JTextField emailField;
	private JButton backToLogin;
	private JButton createButton;
	private JPasswordField passField;
	private JTextField firstNameField;
	private JTextField lastNameField;

	//AccountCreation constructor
	public AccountCreation() {
		//Creation of JFrame object and modification of JFrame properties
		creationFrame = new JFrame("Account Creation");
		creationFrame.setBounds(100, 100, 450, 300);
		creationFrame.setSize(500, 500);
		creationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		creationFrame.setResizable(false);
		creationFrame.getContentPane().setLayout(null);

		//Retrieval of bull image to append to menu bar and set as creationFrame icon image
		ImageIcon bullToolbarIcon = new ImageIcon(
				new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		creationFrame.setIconImage(bullToolbarIcon.getImage());

		//Creation and modification of new JPanel
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 500, 450);
		panel.setSize(500, 500);
		creationFrame.getContentPane().add(panel);
		panel.setLayout(null);

		//JSeparator to separate fields from top icon and description
		separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBackground(Color.BLACK);
		separator.setBounds(0, 59, 500, 12);
		panel.add(separator);

		//New JLabel for bullIcon to append to panel
		bullIcon = new JLabel();
		bullIcon.setBounds(20, 6, 49, 50);
		bullIcon.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png"))
				.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		panel.add(bullIcon);

		//Account Creation label
		lblNewLabel = new JLabel("Account Creation");
		lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblNewLabel.setBounds(290, 18, 175, 30);
		panel.add(lblNewLabel);
		
		//Label for first name field
		JLabel firstNameLabel = new JLabel("First Name");
		firstNameLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		firstNameLabel.setBounds(59, 83, 107, 16);
		panel.add(firstNameLabel);
		
		//Label for last name field
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblLastName.setBounds(322, 83, 107, 16);
		panel.add(lblLastName);

		//Label for username field
		userLabel = new JLabel("Username");
		userLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		userLabel.setBounds(20, 157, 107, 16);
		panel.add(userLabel);

		//Label for password field
		passLabel = new JLabel("Password");
		passLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		passLabel.setBounds(20, 220, 107, 16);
		panel.add(passLabel);

		//Label for email field
		emailLabel = new JLabel("Email");
		emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
		emailLabel.setBounds(20, 282, 82, 16);
		panel.add(emailLabel);
		
		//Field to retrieve first name from user
		firstNameField = new JTextField();
		firstNameField.setColumns(10);
		firstNameField.setBackground(Color.WHITE);
		firstNameField.setBounds(20, 104, 184, 20);
		panel.add(firstNameField);


		//Field to retrieve last name from user
		lastNameField = new JTextField();
		lastNameField.setColumns(10);
		lastNameField.setBackground(Color.WHITE);
		lastNameField.setBounds(281, 104, 184, 20);
		panel.add(lastNameField);
		creationFrame.setVisible(true);
		
		//Username field to retrieve desired username from user
		userField = new JTextField();
		userField.setBackground(Color.WHITE);
		userField.setBounds(20, 175, 445, 20);
		panel.add(userField);
		userField.setColumns(10);
		
		//Password field to retrieve desired password from user
		passField = new JPasswordField();
		passField.setBackground(Color.WHITE);
		passField.setBounds(20, 240, 445, 20);
		panel.add(passField);

		//Email field to retrieve email from user
		emailField = new JTextField();
		emailField.setColumns(10);
		emailField.setBackground(Color.WHITE);
		emailField.setBounds(20, 300, 445, 20);
		panel.add(emailField);

		//Button to bring user back to login screen
		backToLogin = new JButton("Back to Login");
		backToLogin.setBackground(Color.WHITE);
		backToLogin.setFont(new Font("SansSerif", Font.PLAIN, 18));
		backToLogin.setBounds(59, 343, 150, 50);
		panel.add(backToLogin);
		
		backToLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creationFrame.setVisible(false);
				LoginScreen l1 = new LoginScreen();
				creationFrame.dispose();
			}
		});

		//Button to start the action of creating a user and writing to CSV
		createButton = new JButton("Create");
		createButton.setBackground(Color.WHITE);
		createButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
		createButton.setBounds(267, 343, 150, 50);
		panel.add(createButton);
		
		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Retrieves all values from fields and stores them in their respective variables
				String firstName = firstNameField.getText().trim();
				String lastName = lastNameField.getText().trim();
				String username_string = userField.getText().trim();
				String password_string = passField.getText().trim();
				String email_string = emailField.getText().trim();
				boolean valid_email = false;
				boolean valid_info = false;

				//If the email doesn't contain an @, indicate to user that it's not a valid email
				if(email_string.contains("@") == false) {
					JOptionPane.showMessageDialog(null, "ERROR! Not a valid email! Try Again!", "Account Creation",JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					valid_email = true;
				}
				
				//If any of the fields are empty, throw a warning message, otherwise, indicate that the info is valid and that the account was created
				if((username_string == "" || username_string == null || username_string.length() == 0)
						|| (password_string == "" || password_string == null || password_string.length() == 0)
						|| (email_string == "" || email_string == null || email_string.length() == 0 || valid_email == false)
						|| (firstName == "" || firstName == null || firstName.length() == 0)
						|| (lastName == "" || lastName == null || lastName.length() == 0)) 
				{
					JOptionPane.showMessageDialog(null, "ERROR! All fields are required and need to be valid!", "Account Creation",JOptionPane.WARNING_MESSAGE);
				} else
				{
					JOptionPane.showMessageDialog(null, "SUCCESS! Account was successfully created!");
					valid_info = true;
				}

				//Clear all text fields once account is created
				firstNameField.setText("");
				lastNameField.setText("");
				userField.setText("");
				passField.setText("");
				emailField.setText("");

				//Write account data to CSV as a user object
				if(valid_email == true & valid_info == true)
				{
					User userObj = new User(firstName, lastName, username_string,password_string, email_string, null);
					try {
						fileIO fio = new fileIO("add_user.txt");
						fio.addtoCSV(userObj);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
}


