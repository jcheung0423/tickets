// Jacky Cheung
// 5/11/2019
//Login.java
//Final Project
/*This is the Login java file. The purpose of this file is to create the Login GUI for users to log in and gain access to ticket functions. This file will call the Ticket
 * Object if login is successful and give users access to all of the functionalities of the entire program. This file also contains the main method to run everything.
 * 
 * 
 * */
import java.awt.GridLayout; //useful for layouts
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//controls-label text fields, button
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {
	
	Dao conn = null;
	
	public Login() throws SQLException{
		super("USER LOGIN");
		conn = new Dao();
		
		setSize(400, 210);
		setLayout(new GridLayout(4, 2));
		setLocationRelativeTo(null); // centers window
		
		// SET UP CONTROLS
		JLabel lblUsername = new JLabel("Username", JLabel.LEFT);
		JLabel lblPassword = new JLabel("Password", JLabel.LEFT);
		JLabel lblStatus = new JLabel(" ", JLabel.CENTER);
		// JLabel lblSpacer = new JLabel(" ", JLabel.CENTER);
		
		JTextField txtUname = new JTextField(10);

		JPasswordField txtPassword = new JPasswordField();
		JButton btn = new JButton("Login");
		JButton btnExit = new JButton("Exit");

		// constraints

		lblStatus.setToolTipText("Contact help desk to unlock password");
		lblUsername.setHorizontalAlignment(JLabel.CENTER);
		lblPassword.setHorizontalAlignment(JLabel.CENTER);
 
		// ADD OBJECTS TO FRAME
		add(lblUsername);// 1st row filler
		add(txtUname);
		add(lblPassword); // 2nd row
		add(txtPassword);
		add(btn); // 3rd row
		add(btnExit);
		add(lblStatus); // 4th row
		
		btn.addActionListener(new ActionListener() {
			int count = 0; // count agent

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean admin = false;
				count = count + 1;
				// verify credentials of user 
				
				//takes user input and puts it into a pepared statement
				String query = "SELECT * FROM jcheung2_users WHERE userName = ? and userPass = ?;";
				try (PreparedStatement stmt = conn.connect().prepareStatement(query)) {
					stmt.setString(1, txtUname.getText());
					stmt.setString(2, txtPassword.getText());
					ResultSet rs = stmt.executeQuery();
					
					//checks if the user is an admin, if the username or password is wrong it will display an error message
					//after 3 failed attempts, the program will exit
					if (rs.next()) {
						admin = rs.getBoolean("admin"); // get table column value
						new Tickets(admin);
						setVisible(false); // HIDE THE FRAME
						dispose(); // CLOSE OUT THE WINDOW
					} else 
						lblStatus.setText("Try again! " + (3 - count) + " / 3 attempts left");
						if(count == 3) dispose();
						rs.close();
						
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
 			 
			}
		});
		btnExit.addActionListener(e -> System.exit(0));
		setVisible(true); // SHOW THE FRAME
		
	}
	public static void main(String[] args) {
		try {
			new Login();
		}
		catch(SQLException se) {
			se.printStackTrace();
		}
	}
}
