package main.java;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.ImageIcon;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JSeparator;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class ProfileScreen {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;



	/**
	 * Create the application.
	 */
	public ProfileScreen() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel pfp_label = new JLabel();
		pfp_label.setBounds(39, 6, 80, 78);
		pfp_label.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/profile.png")).getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(pfp_label);
		frame.getContentPane().add(pfp_label);
		
		JLabel lblNewLabel_1 = new JLabel("Portfolio Value:");
		lblNewLabel_1.setBounds(39, 92, 114, 16);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Buying Power: ");
		lblNewLabel_1_1.setBounds(39, 120, 114, 16);
		frame.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_2 = new JLabel("$0.00");
		lblNewLabel_2.setBounds(141, 92, 61, 16);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_2_1 = new JLabel("$0.00");
		lblNewLabel_2_1.setBounds(141, 120, 61, 16);
		frame.getContentPane().add(lblNewLabel_2_1);
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBorder(new LineBorder(new Color(0, 0, 0)));
		verticalBox.setBounds(39, 148, 420, 245);
		frame.getContentPane().add(verticalBox);
		
		JLabel lblNewLabel_3 = new JLabel("Contact Information");
		lblNewLabel_3.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		verticalBox.add(lblNewLabel_3);
		
		Component verticalStrut_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1);
		
		JPanel panel = new JPanel();
		panel.setMaximumSize(new Dimension(800,25));
		verticalBox.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_6 = new JLabel("Name:");
		lblNewLabel_6.setBounds(6, 6, 61, 16);
		panel.add(lblNewLabel_6);
		
		textField = new JTextField();
		textField.setBounds(79, 1, 315, 26);
		panel.add(textField);
		textField.setColumns(10);
		
		Component verticalStrut_1_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1_1);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setMaximumSize(new Dimension(800, 25));
		verticalBox.add(panel_1);
		
		JLabel lblNewLabel_6_1 = new JLabel("Email:");
		lblNewLabel_6_1.setBounds(6, 6, 61, 16);
		panel_1.add(lblNewLabel_6_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(79, 1, 315, 26);
		panel_1.add(textField_1);
		
		Component verticalStrut_1_1_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1_1_1);
		
		JPanel panel_1_1 = new JPanel();
		panel_1_1.setLayout(null);
		panel_1_1.setMaximumSize(new Dimension(800, 25));
		verticalBox.add(panel_1_1);
		
		JLabel lblNewLabel_6_1_1 = new JLabel("Phone:");
		lblNewLabel_6_1_1.setBounds(6, 6, 61, 16);
		panel_1_1.add(lblNewLabel_6_1_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(79, 1, 315, 26);
		panel_1_1.add(textField_2);
		
		Component verticalStrut_1_1_1_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1_1_1_1);
		
		JPanel panel_1_1_1 = new JPanel();
		panel_1_1_1.setLayout(null);
		panel_1_1_1.setMaximumSize(new Dimension(800, 25));
		verticalBox.add(panel_1_1_1);
		
		JLabel lblNewLabel_6_1_1_1 = new JLabel("Address");
		lblNewLabel_6_1_1_1.setBounds(6, 6, 61, 16);
		panel_1_1_1.add(lblNewLabel_6_1_1_1);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(79, 1, 315, 26);
		panel_1_1_1.add(textField_3);
		
		Component verticalStrut_1_1_1_1_1 = Box.createVerticalStrut(10);
		verticalBox.add(verticalStrut_1_1_1_1_1);
		
		JLabel lblNewLabel_3_1 = new JLabel("Other");
		lblNewLabel_3_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		verticalBox.add(lblNewLabel_3_1);
		
		JPanel panel_1_1_2 = new JPanel();
		panel_1_1_2.setLayout(null);
		panel_1_1_2.setMaximumSize(new Dimension(800, 25));
		verticalBox.add(panel_1_1_2);
		
		JLabel lblNewLabel_6_1_1_2 = new JLabel("Account Number:");
		lblNewLabel_6_1_1_2.setBounds(6, 6, 119, 16);
		panel_1_1_2.add(lblNewLabel_6_1_1_2);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(137, 1, 257, 26);
		panel_1_1_2.add(textField_4);
		
		Box verticalBox_1 = Box.createVerticalBox();
		verticalBox_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		verticalBox_1.setBounds(319, 18, 140, 118);
		frame.getContentPane().add(verticalBox_1);
		
		JLabel lblNewLabel = new JLabel("Linked Account");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		verticalBox_1.add(lblNewLabel);
		
		Component verticalStrut_1_1_1_1_2 = Box.createVerticalStrut(10);
		verticalBox_1.add(verticalStrut_1_1_1_1_2);
		
		JLabel lblNewLabel_5 = new JLabel("TD Bank");
		verticalBox_1.add(lblNewLabel_5);
		
		JButton btnNewButton = new JButton("Remove");
		verticalBox_1.add(btnNewButton);
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBounds(213, 50, 1, 1);
		frame.getContentPane().add(horizontalBox);
		
		frame.setVisible(true);
		frame.setResizable(false);
	}
}