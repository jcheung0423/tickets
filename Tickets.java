// Jacky Cheung
// 5/11/2019
//Tickets.java
//Final Project
/*This is the Tickets java file. The purpose of this file is code all of the functionalities of the program. The functions Update Ticket, Delete Ticket, Close Ticket
 * Open Ticket and View Ticket(s) are all part of this file.
 * 
 * 
 * */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Tickets extends JFrame implements ActionListener {
	
	// class level member objects
		Dao dao = null; // for CRUD operations
		Boolean chkIfAdmin = null;

		// Main menu object items
		private JMenu mnuFile = new JMenu("File");
		private JMenu mnuAdmin = new JMenu("Admin");
		private JMenu mnuTickets = new JMenu("Tickets");

		// Sub menu item objects for all Main menu item objects
		JMenuItem mnuItemExit;
		JMenuItem mnuItemUpdate;
		JMenuItem mnuItemDelete;
		JMenuItem mnuItemOpenTicket;
		JMenuItem mnuItemViewTicket;
		JMenuItem mnuItemCloseTicket;

		public Tickets(Boolean isAdmin) {

			chkIfAdmin = isAdmin;
			createMenu();
			prepareGUI();

		}

		private void createMenu() {

			/* Initialize sub menu items **************************************/

			// initialize sub menu item for File main menu
			mnuItemExit = new JMenuItem("Exit");
			// add to File main menu item
			mnuFile.add(mnuItemExit);

			// initialize first sub menu items for Admin main menu
			mnuItemUpdate = new JMenuItem("Update Ticket");
			// add to Admin main menu item
			mnuAdmin.add(mnuItemUpdate);

			// initialize second sub menu items for Admin main menu
			mnuItemDelete = new JMenuItem("Delete Ticket");
			// add to Admin main menu item
			mnuAdmin.add(mnuItemDelete);
			
			// initialize second sub menu items for Admin main menu
			mnuItemCloseTicket = new JMenuItem("Close Ticket");
			//add to Admin main menu item
			mnuAdmin.add(mnuItemCloseTicket);

			// initialize first sub menu item for Tickets main menu
			mnuItemOpenTicket = new JMenuItem("Open Ticket");
			// add to Ticket Main menu item
			mnuTickets.add(mnuItemOpenTicket);

			// initialize second sub menu item for Tickets main menu
			mnuItemViewTicket = new JMenuItem("View Ticket");
			// add to Ticket Main menu item
			mnuTickets.add(mnuItemViewTicket);

			// initialize any more desired sub menu items below

			/* Add action listeners for each desired menu item *************/
			mnuItemExit.addActionListener(this);
			mnuItemUpdate.addActionListener(this);
			mnuItemDelete.addActionListener(this);
			mnuItemOpenTicket.addActionListener(this);
			mnuItemCloseTicket.addActionListener(this);
			mnuItemViewTicket.addActionListener(this);

			// add any more listeners for any additional sub menu items if desired

		}

		private void prepareGUI() {

			// create jmenu bar
			JMenuBar bar = new JMenuBar();
			
			//check if the user is an admin, if user is an admin, then all features will be accessible
			if(chkIfAdmin) {
				bar.add(mnuFile); // add main menu items in order, to JMenuBar
				bar.add(mnuAdmin);
				bar.add(mnuTickets);
			}
			else { //if user is not admin only open ticket, update ticket and view ticket featues will be accessible
				bar.add(mnuFile);
				bar.add(mnuTickets);
			}
			
			// add menu bar components to frame
			setJMenuBar(bar);

			addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
			    System.exit(0);
			}
			});
			// set frame options
			setSize(500, 500);
			getContentPane().setBackground(Color.LIGHT_GRAY);
			setLocationRelativeTo(null);
			setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			try {
				dao = new Dao();
			}
			catch(SQLException se) {
				se.printStackTrace();
			}
			
			//If the exit button is selected, the program will close
			if(e.getSource() == mnuItemExit) {
				System.exit(0);
			}
			else if(e.getSource() == mnuItemUpdate) { 
				//if the update ticket button is selected the user will be prompted for the id of the ticket they want to update
				//then the user will be prompted to input what they want to update the ticket with
				
				//prompts the user for the ticket id of the ticket they want to update
				String s = JOptionPane.showInputDialog("Enter the id of the ticket you want to update:");
				
				//prompts the user for the updated text
				String s1 = JOptionPane.showInputDialog("Enter the information you want to update with:");
				
				dao.updateTicket(Integer.parseInt(s), s1);
				
				//display a message informing user that the ticket was successfully updated
				JOptionPane.showMessageDialog(mnuItemUpdate, "Ticket " + s + " has been updated", "Updated", NORMAL);
			}
			else if(e.getSource() == mnuItemDelete) {
				//if the delete ticket button is selected the user will be prompted for the id of the ticket they want to delete
				//the user will then be asked if they are sure they want to delete the ticket
				
				//prompts the user for the ticket id of the ticket they want to delete
				String s = JOptionPane.showInputDialog("Enter the id of the ticket you want to delete:");
				
				//Prints a message asking if the user is sure they want to delete the ticket
				String s1 = JOptionPane.showInputDialog("Are you sure you want to delete ticket " + s + "? \n Enter 0 if no else 1 if yes:");
				
				if(Integer.parseInt(s1) == 0) {
					JOptionPane.showMessageDialog(mnuItemDelete, "Ticket " + s + " will not be deleted.", "Deleted", NORMAL);
				}
				else
				{
					dao.deleteTicket(Integer.parseInt(s));
					JOptionPane.showMessageDialog(mnuItemDelete, "Ticket " + s + " is now deleted.", "Deleted", NORMAL);
				}
			}
			else if(e.getSource() == mnuItemCloseTicket) {
				//if the close ticket button is selected the user will be prompted for the id of the ticket they want to close
				//then the user will be asked if they are sure they want to close the ticket
				
				//prompts the user for the id
				String s = JOptionPane.showInputDialog("Enter the id of the ticket you want to close:");
				
				//message asking user if they want to close the ticket
				String s1 = JOptionPane.showInputDialog("Are you sure you want to close ticket " + s +"? \n Enter 0 if no else 1 if yes:");
				
				if(Integer.parseInt(s1) == 0) {
					JOptionPane.showMessageDialog(mnuItemCloseTicket, "Ticket " + s + " will not be closed.", "Closed", NORMAL);
				}
				else {
					dao.closeTicket(Integer.parseInt(s));
					JOptionPane.showMessageDialog(mnuItemCloseTicket, "Ticket " + s + " is now closed.", "Closed", NORMAL);
				}
				
			}
			else if(e.getSource() == mnuItemOpenTicket) {
				//if the open ticket button is selected the user will gain access to some text fields where they can fill out the information
				
				//creating a text field for the user to input information of a ticket
				setSize(600, 600);
				setLayout(new GridLayout(6,2));
				setLocationRelativeTo(null);
				
				JLabel lblTicketIssuer = new JLabel("Ticket Issuer", JLabel.LEFT);
				JLabel lblTicketName = new JLabel("Ticket Name", JLabel.LEFT);
				JLabel lblTicketDescrip = new JLabel("Ticket Description", JLabel.LEFT);
				JLabel lblStatus = new JLabel(" ", JLabel.CENTER);
				
				JTextField txtTicketIssuer = new JTextField(20);
				JTextField txtTicketName = new JTextField(50);
				JTextField txtTicketDescrip = new JTextField(1000);
				
				JButton but = new JButton("Submit");
				JButton butExit = new JButton("Exit");
				
				add(lblTicketIssuer);
				add(txtTicketIssuer);
				add(lblTicketName);
				add(txtTicketName);
				add(lblTicketDescrip);
				add(txtTicketDescrip);
				add(but);
				add(butExit);
				add(lblStatus);
				
				but.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
						//creating the ticket 
						dao.insertTickets(txtTicketIssuer.getText(), txtTicketName.getText(), txtTicketDescrip.getText());
						
						//Tells the user that their ticket was created
						JOptionPane.showMessageDialog(mnuItemOpenTicket, "Your ticket has been opened", "Opened", JOptionPane.PLAIN_MESSAGE);
						
						//remove everything after ticket is created
						remove(lblTicketIssuer);
						remove(txtTicketIssuer);
						remove(lblTicketName);
						remove(txtTicketName);
						remove(lblTicketDescrip);
						remove(txtTicketDescrip);
						remove(but);
						remove(butExit);
						remove(lblStatus);
						revalidate();
						repaint();
					}
					
				});
				butExit.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						//remove everything if exit is pressed
						remove(lblTicketIssuer);
						remove(txtTicketIssuer);
						remove(lblTicketName);
						remove(txtTicketName);
						remove(lblTicketDescrip);
						remove(txtTicketDescrip);
						remove(but);
						remove(butExit);
						remove(lblStatus);
						revalidate();
						repaint();
					}
				});
				setVisible(true); //Show the frame
			}
			else if(e.getSource() == mnuItemViewTicket) {
				//if the view ticket button is selected the user will be prompted to enter the id or all to view one or all tickets respectively
				
				JFrame frame;
				JTable table; //creating a table to show all the tickets
				String[] columns = {"Ticket ID", "Ticket Issuer", "Ticket Name", "Ticket Description"," Ticket Status", "Start Date", "End Date"}; //creating the columns of the table
				
				frame = new JFrame("Ticket List");
				frame.setLayout(new BorderLayout());
				DefaultTableModel m = new DefaultTableModel();
				m.setColumnIdentifiers(columns);
				
				table = new JTable();
				table.setModel(m);
				
				JScrollPane scr = new JScrollPane(table);
				scr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				scr.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				int tid;
				String ticketIssuer;
				String ticketName;
				String ticketDescrip;
				String ticketStatus;
				Timestamp startDate;
				Timestamp endDate;
				
				ResultSet rs = null;
				
				try {
					//prompts the user for id
					String s = JOptionPane.showInputDialog("Enter the id of the ticket or enter all to view all tickets:");
					if(s.equals("all") || s.equals("All")) {
						rs = dao.retrieveRecords();
						
						while(rs.next()) {
							tid = rs.getInt("tid");
							ticketIssuer = rs.getString("ticket_issuer");
							ticketName = rs.getString("ticket_name");
							ticketDescrip = rs.getString("ticket_desc");
							ticketStatus = rs.getString("ticket_status");
							startDate = rs.getTimestamp("startDate");
							endDate = rs.getTimestamp("endDate");
							m.addRow(new Object[] {tid, ticketIssuer, ticketName, ticketDescrip, ticketStatus, startDate, endDate});
						}
					}
					else {
						rs = dao.viewTicket(Integer.parseInt(s));
						while(rs.next()) {
		
								tid = Integer.parseInt(s);
								ticketIssuer = rs.getString("ticket_issuer");
								ticketName = rs.getString("ticket_name");
								ticketDescrip = rs.getString("ticket_desc");
								ticketStatus = rs.getString("ticket_status");
								startDate = rs.getTimestamp("startDate");
								endDate = rs.getTimestamp("endDate");
								m.addRow(new Object[] {tid, ticketIssuer, ticketName, ticketDescrip, ticketStatus, startDate, endDate});
							
						}
					}
				}
				catch(SQLException se) {
					se.printStackTrace();
				}
				
				frame.add(scr);
				frame.setVisible(true);
				frame.setSize(600,600);
				
			}
		}
}
