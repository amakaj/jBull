package main.java;

import javax.swing.*;
import java.awt.*;

public class LoginScreen {
	private JTextField textField;
	private JTextField textField_2;
	
	
	
	public void showLoginScreen() {
		JFrame loginFrame = new JFrame("jBull Login");
		
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.setSize(350, 500);
		loginFrame.setResizable(false);
		loginFrame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setFont(new Font("SansSerif", Font.PLAIN, 35));
		panel.setBounds(0, 0, 350, 472);
		loginFrame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png")).getImage().getScaledInstance(125, 125, Image.SCALE_DEFAULT)));
		lblNewLabel.setBounds(103, 25, 128, 158);
		panel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("jBull");
		lblNewLabel_1.setBackground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("SansSerif", Font.PLAIN, 30));
		lblNewLabel_1.setBounds(137, 184, 67, 25);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("UserName");
		lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(43, 248, 114, 16);
		panel.add(lblNewLabel_2);
		
		textField = new JTextField();
		textField.setBackground(Color.LIGHT_GRAY);
		textField.setBounds(43, 268, 263, 41);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_2_1 = new JLabel("Password");
		lblNewLabel_2_1.setFont(new Font("SansSerif", Font.PLAIN, 20));
		lblNewLabel_2_1.setBounds(43, 321, 114, 16);
		panel.add(lblNewLabel_2_1);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setForeground(Color.BLACK);
		btnNewButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
		btnNewButton.setBackground(new Color(0, 0, 0));
		btnNewButton.setBounds(43, 401, 114, 41);
		panel.add(btnNewButton);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBackground(Color.LIGHT_GRAY);
		textField_2.setBounds(43, 334, 263, 41);
		panel.add(textField_2);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setForeground(Color.BLACK);
		btnCreate.setFont(new Font("SansSerif", Font.PLAIN, 20));
		btnCreate.setBackground(Color.BLACK);
		btnCreate.setBounds(181, 401, 114, 41);
		panel.add(btnCreate);
	
		loginFrame.setVisible(true);
		
		/*ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		loginFrame.setIconImage(bullIcon.getImage());*/
	}
	
	public static void main(String[] args) {
		// Schedule jobs for the event-dispatching thread
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoginScreen l1 = new LoginScreen();
				l1.showLoginScreen();
			}
		});
	}
}
