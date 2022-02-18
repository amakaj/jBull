package main.java;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;

//Account Creatio
public class AccountCreation {

	private JFrame frame;
	private JSeparator separator;
	private JLabel bullIcon;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTextField textField;
	private JLabel lblNewLabel_2;
	private JTextField textField_1;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JTextField textField_3;
	private JButton btnNewButton;
	private JButton btnCreate;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AccountCreation window = new AccountCreation();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AccountCreation() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 450, 272);
		panel.setSize(500, 500);
		
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		separator = new JSeparator();
		separator.setBackground(Color.BLACK);
		separator.setBounds(0, 59, 500, 12);
		panel.add(separator);
		
		bullIcon = new JLabel();
		bullIcon.setBounds(20, 6, 61, 50);
		bullIcon.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png")).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
		panel.add(bullIcon);
		
		lblNewLabel = new JLabel("Account Creation");
		lblNewLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblNewLabel.setBounds(292, 17, 186, 30);
		panel.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(20, 107, 107, 16);
		panel.add(lblNewLabel_1);
		
		textField = new JTextField();
		textField.setBackground(Color.LIGHT_GRAY);
		textField.setBounds(20, 126, 442, 37);
		panel.add(textField);
		textField.setColumns(10);
		
		lblNewLabel_2 = new JLabel("Password");
		lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblNewLabel_2.setBounds(20, 175, 107, 16);
		panel.add(lblNewLabel_2);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBackground(Color.LIGHT_GRAY);
		textField_1.setBounds(20, 272, 442, 37);
		panel.add(textField_1);
		
		lblNewLabel_3 = new JLabel("Email");
		lblNewLabel_3.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblNewLabel_3.setBounds(20, 244, 107, 16);
		panel.add(lblNewLabel_3);
		
		lblNewLabel_4 = new JLabel("Phone");
		lblNewLabel_4.setFont(new Font("SansSerif", Font.PLAIN, 18));
		lblNewLabel_4.setBounds(20, 321, 107, 16);
		panel.add(lblNewLabel_4);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBackground(Color.LIGHT_GRAY);
		textField_3.setBounds(20, 339, 442, 37);
		panel.add(textField_3);
		
		btnNewButton = new JButton("Back to Login");
		btnNewButton.setFont(new Font("SansSerif", Font.PLAIN, 18));
		btnNewButton.setBounds(62, 396, 150, 50);
		panel.add(btnNewButton);
		
		btnCreate = new JButton("Create");
		btnCreate.setFont(new Font("SansSerif", Font.PLAIN, 18));
		btnCreate.setBounds(274, 396, 150, 50);
		panel.add(btnCreate);
		
		passwordField = new JPasswordField();
		passwordField.setBackground(Color.LIGHT_GRAY);
		passwordField.setBounds(20, 195, 442, 37);
		panel.add(passwordField);
		frame.setVisible(true);
	}
}
