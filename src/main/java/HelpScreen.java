package main.java;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HelpScreen {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public HelpScreen(User u) {
		JFrame frame = new JFrame("Help");
		frame.setSize(900, 600);
		frame.setResizable(false);
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel container = new JPanel();
		
		JScrollPane jScrollPane = new JScrollPane(container);
		jScrollPane.setBounds(0,0,900,600);
		container.setPreferredSize(new Dimension(1000,2000));
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		container.setLayout(null);
		container.setBackground(Color.WHITE);

		
		JLabel jBullIcon = new JLabel("");
		jBullIcon.setBounds(43, 6, 98, 106);
		jBullIcon.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png"))
				.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT)));
		container.add(jBullIcon);
	
		
		JLabel Titled = new JLabel("<html>\n<h1> Developed By Anthony, Lamia, Ibrahim and Imu</h1>\n<hr/>\nSoftware Engineering <br/>\nSpring 2022\n</html>");
		Titled.setVerticalAlignment(SwingConstants.TOP);
		Titled.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		Titled.setBounds(153, 6, 588, 106);
		container.add(Titled);
		
		JLabel help_login = new JLabel("");
		help_login.setBounds(19, 124, 500, 400);
		help_login.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/help_login.jpeg"))
				.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT)));
		
		JLabel help_register = new JLabel("");
		help_register.setBounds(19,550,500,400);
		help_register.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/help_register_account.jpeg"))
				.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT)));
		
		container.add(help_login);
		container.add(help_register);
		
		JLabel help_desc = new JLabel("<html>\n <h2>Login Screen</h2> \n<h3> <b>Desc </b>: The login screen is where the "
				+ "user will login to the application  </h3>\n<figure>\n<figcaption> Feautures </figcaption>\n <ul> \n    "
				+ "<li>Username/Email: The user will type their username/email here to login</li> \n    "
				+ "<li>Password: The user will type their password here to login </li> \n   <li>Login Button: "
				+ "This is what the user will press after entering their username and password</li>\n   "
				+ "<li>Create Button: If the user does not have an account, the user will press this button to create one </li> "
				+ "\n </ul>\n</figure>\n</html> \n\n");
		help_desc.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		help_desc.setVerticalAlignment(SwingConstants.TOP);
		help_desc.setBounds(531, 124, 329, 300);
		container.add(help_desc);
		
		JLabel register_desc = new JLabel("<html>\n <h2>Register Screen</h2> \n<h3> <b>Desc </b>: The register screen is where the user will create their account if they dont have one already</h3>\n<figure>\n<figcaption> Feautures </figcaption>\n <ul> \n    <li>First Name: The user will type their first name</li> \n    <li>Last Name: The user will type their last name </li> \n   <li>Username:  The user will create their username</li>\n   <li>Password: The user will create their password </li> \n   <li>Email: The user will create their email</li>\n   <li>Back to Login: The user will press this button to return to login screen</li>\n   <li>Create: The user will press this button to register their account</li> \n </ul>\n</figure>\n</html> \n\n");
		register_desc.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		register_desc.setVerticalAlignment(SwingConstants.TOP);
		register_desc.setBounds(531, 550, 329, 300);
		container.add(register_desc);

		
		frame.getContentPane().add(jScrollPane);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				Dashboard h1 = new Dashboard(u);
				frame.dispose();
			}
		});
		
	}
}
