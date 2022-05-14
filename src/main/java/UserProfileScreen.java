// jBull | Displays data about the currently logged in user
package main.java;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UserProfileScreen {

	private JFrame frame;
	private JTextField first_name_field;
	private JTextField last_name_field;
	private JTextField email_field;

	public UserProfileScreen(User currentuser) {
		//Frame creation
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 400, 400);
		frame.getContentPane().setLayout(null);
		
		JLabel profile_icon = new JLabel("");
		profile_icon.setBounds(17, 24, 97, 71);
		profile_icon.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/profile.png")).getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
		ImageIcon bullIcon = new ImageIcon(new ImageIcon(getClass().getResource("/main/resources/bull.png")).getImage()
				.getScaledInstance(30, 30, Image.SCALE_DEFAULT));
		frame.getContentPane().add(profile_icon);
		
		frame.setIconImage(bullIcon.getImage());
		
		//Show all data for user by using getter methods on user object
		JLabel username_field = new JLabel("New label");
		username_field.setText(currentuser.getUsername());
		username_field.setBounds(126, 55, 109, 16);
		frame.getContentPane().add(username_field);
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(17, 120, 350, 200);
		frame.getContentPane().add(verticalBox);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		verticalBox.add(panel);
		panel.setLayout(null);
		
		JLabel first_name_label = new JLabel("First Name");
		first_name_label.setBounds(5, 17, 75, 16);
		panel.add(first_name_label);
		
		first_name_field = new JTextField();
		first_name_field.setEditable(false);
		first_name_field.setText(currentuser.getFirstName());
		first_name_field.setBounds(92, 12, 186, 26);
		panel.add(first_name_field);
		first_name_field.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		verticalBox.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel last_name_label = new JLabel("Last Name");
		last_name_label.setBounds(6, 16, 77, 16);
		panel_1.add(last_name_label);
		
		last_name_field = new JTextField();
		last_name_field.setColumns(10);
		last_name_field.setEditable(false);
		last_name_field.setText(currentuser.getLastName());
		last_name_field.setBounds(95, 11, 186, 26);
		panel_1.add(last_name_field);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalBox.add(verticalStrut_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		verticalBox.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel email_label = new JLabel("Email");
		email_label.setBounds(41, 18, 61, 16);
		panel_2.add(email_label);
		
		email_field = new JTextField();
		email_field.setColumns(10);
		email_field.setEditable(false);
		email_field.setText(currentuser.getEmail());
		email_field.setBounds(95, 13, 186, 26);
		panel_2.add(email_field);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		
		//Create window listener to pass user object back over to dashboard
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				Dashboard h1 = new Dashboard(currentuser);
				frame.dispose();
			}
		});
	}
}
