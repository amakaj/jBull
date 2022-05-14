// jBull | Displays Help Screen
package main.java;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class HelpScreen {

	public HelpScreen(User u) {
		//Frame creation
		JFrame frame = new JFrame("Help");
		frame.setSize(900, 600);
		frame.setResizable(false);
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel container = new JPanel();
		
		JScrollPane jScrollPane = new JScrollPane(container);
		jScrollPane.setBounds(0,0,900,600);
		container.setPreferredSize(new Dimension(1000,2500));
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		container.setLayout(null);
		container.setBackground(Color.WHITE);

		JLabel jBullIconLabel = new JLabel("");
		ImageIcon jBullIcon = new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/bull.png"))
				.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT));
		jBullIconLabel.setBounds(43, 6, 98, 106);
		jBullIconLabel.setIcon(jBullIcon);
		container.add(jBullIconLabel);
		frame.setIconImage(jBullIcon.getImage());
	
		/*----------ALL DATA FOR HELP SCREEN----------*/
		JLabel Titled = new JLabel("<html>\n<h1> Developed By Anthony, Lamia, Ibrahim and Imu</h1>\n<hr/>\nSoftware Engineering <br/>\nSpring 2022\n</html>");
		Titled.setVerticalAlignment(SwingConstants.TOP);
		Titled.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		Titled.setBounds(153, 6, 588, 119);
		container.add(Titled);
		
		JLabel help_login = new JLabel("");
		help_login.setBounds(19, 139, 500, 400);
		help_login.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/help_login.jpeg"))
				.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT)));
		
		JLabel help_register = new JLabel("");
		help_register.setBounds(19,550,500,400);
		help_register.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/help_register_account.jpeg"))
				.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT)));
		
		container.add(help_login);
		container.add(help_register);
		
		JLabel help_desc = new JLabel("<html>\n <h2>Login Screen</h2> \n<h3>  The login screen is where the "
				+ "user will login to the application  </h3>\n<figure>\n<figcaption> Features </figcaption>\n <ul> \n    "
				+ "<li>Username/Email: The user will type their username/email here to login</li> \n    "
				+ "<li>Password: The user will type their password here to login </li> \n   <li>Login Button: "
				+ "This is what the user will press after entering their username and password</li>\n   "
				+ "<li>Create Button: If the user does not have an account, the user will press this button to create one </li> "
				+ "\n </ul>\n</figure>\n</html> \n\n");
		help_desc.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		help_desc.setVerticalAlignment(SwingConstants.TOP);
		help_desc.setBounds(531, 124, 329, 300);
		container.add(help_desc);
		
		JLabel register_desc = new JLabel("<html>\n <h2>Register Screen</h2> \n<h3> The register screen is where the user will create their account if they dont have one already</h3>\n<figure>\n<figcaption> Feautures </figcaption>\n <ul> \n    <li>First Name: The user will type their first name</li> \n    <li>Last Name: The user will type their last name </li> \n   <li>Username:  The user will create their username</li>\n   <li>Password: The user will create their password </li> \n   <li>Email: The user will create their email</li>\n   <li>Back to Login: The user will press this button to return to login screen</li>\n   <li>Create: The user will press this button to register their account</li> \n </ul>\n</figure>\n</html> \n\n");
		register_desc.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
		register_desc.setVerticalAlignment(SwingConstants.TOP);
		register_desc.setBounds(531, 550, 329, 300);
		container.add(register_desc);
		
		JLabel dashboard = new JLabel("");
		dashboard.setBounds(19,1000,500,500);
		dashboard.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/dashboard.jpeg"))
				.getImage().getScaledInstance(500, 400, Image.SCALE_DEFAULT)));
		container.add(dashboard);
		
		JLabel dashboard_desc = new JLabel();
		dashboard_desc.setText("<html>\n <h2>Dashboard Screen</h2> \n<h3> The dashboard screen is the home page where user can view stock information </h3>\n<figure>\n<figcaption> Feautures </figcaption>\n <ul> \n    <li>Stock Owned:  This lists all the stocks the user currently owns with the name of the stock, the number of shares and the price per share</li> \n    <li>Top 20 Stocks: Lists the top 20 stocks in the market updated in real time </li> \n   <li>Stock Chart: A visual representation of how much each stock takes up from the balance</li>\n   <li>Portfolio Balance: The total amount of the stocks the user currently owns subject to market changes</li> \n   <li>Cash Balance: Balance user has remaining to purchase more stocks</li>\n   <li>Total Balance: The sum of the Cash Balance and Portfolio Balance; How much cash the user has in total</li>\n\n </ul>\n</figure>\n</html> \n\n");
		dashboard_desc.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		dashboard_desc.setVerticalAlignment(SwingConstants.TOP);
		dashboard_desc.setBounds(531, 1000, 329, 300);
		container.add(dashboard_desc);
		
		JLabel buy_sell = new JLabel();
		buy_sell.setBounds(19,1700,400,400);
		buy_sell.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/buy_sell.jpeg"))
				.getImage().getScaledInstance(400, 400, Image.SCALE_DEFAULT)));
		container.add(buy_sell);
		
		JLabel buy_sell_desc = new JLabel("<html>\n <h2>Buy/Sell Screen</h2> \n<h3> the buy sell screen is where the user can buy and sell shares of stocks</h3>\n<figure>\n<figcaption> Feautures </figcaption>\n <ul> \n    <li>Stock Search:  This is where the user can search for stocks to add to their list</li> \n    <li>Price: This displays how much the stocks the user is buying: The price of the stock times number of shares </li> \n   <li>Buy: The user presses this to buy the stocks</li>\n   <li>Sell: The user presses this to sell the stocks</li> \n   <li>Stocks Owned: List of the stocks the user owns including number of shares and price per share</li>\n   <li>Porfolio Balance: Total value of the stocks the user owns</li>\n<li>Cash Balance: Total buying power user has left after buying stocks</li>\n<li>Total Balance: The total amount of assets; portfolio balance plus cash balance</li>\n\n\n </ul>\n</figure>\n</html> \n\n");
		buy_sell_desc.setBounds(531, 1700, 329, 300);
		container.add(buy_sell_desc);
				
		JLabel profile = new JLabel();
		profile.setBounds(19,2100,400,400);
		profile.setIcon(new ImageIcon(new ImageIcon(LoginScreen.class.getResource("/main/resources/profile.jpeg"))
				.getImage().getScaledInstance(400, 400, Image.SCALE_DEFAULT)));
		container.add(profile);
		
		JLabel profile_desc = new JLabel("<html>\n <h2>Profile Screen</h2> \n<h3> User Information from Register Screen</h3>\n<figure>\n<figcaption> Feautures </figcaption>\n <ul> \n    <li>Username</li> \n    <li>First Name</li> \n   <li>Lastname</li>\n   <li>Email </li> \n </ul>\n</figure>\n</html> \n\n");
		profile_desc.setBounds(531, 2100, 329, 300);
		container.add(profile_desc);

		frame.getContentPane().add(jScrollPane);
		frame.setVisible(true);
		
		//Frame window listener to bring user back to dashboard
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				Dashboard h1 = new Dashboard(u);
				frame.dispose();
			}
		});
		
	}
}
