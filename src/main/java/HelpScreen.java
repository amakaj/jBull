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
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.SwingConstants;

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
		
		JLabel jBullIcon = new JLabel("");
		jBullIcon.setBounds(43, 6, 98, 106);
		jBullIcon.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png"))
				.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(jBullIcon);
	
		
		JLabel Titled = new JLabel("<html>\n<h1> Developed By</h1>\n<hr/>\nSoftware Engineering <br/>\nSpring 2022\n</html>");
		Titled.setVerticalAlignment(SwingConstants.TOP);
		Titled.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		Titled.setBounds(153, 6, 225, 106);
		frame.getContentPane().add(Titled);
		
		JLabel help_login = new JLabel("");
		help_login.setBounds(43, 124, 500, 400);
		help_login.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/help_login.jpeg"))
				.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT)));
		frame.getContentPane().add(help_login);
		
		JLabel lblNewLabel = new JLabel("<html>\n <h2>Login Screen</h2> \n<h3> <b>Desc </b>: The login screen is where the "
				+ "user will login to the application  </h3>\n<figure>\n<figcaption> Feautures </figcaption>\n <ul> \n    "
				+ "<li>Username/Email: The user will type their username/email here to login</li> \n    "
				+ "<li>Password: The user will type their password here to login </li> \n   <li>Login Button: "
				+ "This is what the user will press after entering their username and password</li>\n   "
				+ "<li>Create Button: If the user does not have an account, the user will press this button to create one </li> "
				+ "\n </ul>\n</figure>\n</html> \n\n");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setBounds(565, 124, 329, 300);
		frame.getContentPane().add(lblNewLabel);
	
		
		
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
