// Jacky Cheung
// 5/11/2019
//Dao.java
//Final Project
/*This is the Data Access Object file. The purpose of this file is to create all of the necessary CRUD(Create Read Update Delete) like operations.
 * 
 * 
 * */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class Dao {
	
	Connection conn = null;
	Statement stmt = null;
	
	public Dao() throws SQLException {
		conn = connect();
		//createTicketTable();
		//createUserTable();
	}
	
	// Code database URL
		static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false";
		// Database credentials	
		static final String USER = "fp411", PASS = "411";

		public Connection connect() throws SQLException {
			return DriverManager.getConnection(DB_URL, USER, PASS);
		}
	
	//Method to create the ticket table
	public void createTicketTable() {
		try {
			//Open a connection
			//System.out.println("Connecting to a selected database to create Table...");
			//System.out.println("Connected database successfully...");
			
			//Execute create query
			System.out.println("Creating table in given database...");
			
			stmt = conn.createStatement();
			
			String sql = "CREATE TABLE jCheung2_ftickets " + 
		             	 "(tid INTEGER not NULL AUTO_INCREMENT, " +
		             	 " ticket_issuer VARCHAR (100), " +
		             	 " ticket_name VARCHAR(100), " +
		             	 " ticket_desc VARCHAR(200), " + 
		             	 " ticket_status VARCHAR(10), " +
		             	 " startDate DATETIME, " +
		             	 " endDate DATETIME, " +
		             	 " PRIMARY KEY ( tid ))";
			
			stmt.executeUpdate(sql);
			System.out.println("Created table in given database...");
			conn.close(); //close db connection
		}
		catch(SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		}
	}
	
	//method to create the user table
	public void createUserTable() {
		try {
			//Open a connection
			//System.out.println("Connecting to a selected database to create Table...");
			//System.out.println("Connected database successfully...");
			
			//Execute create query
			System.out.println("Creating table in given database...");
			
			stmt = conn.createStatement();
			
			String sql = "CREATE TABLE jCheung2_users " + 
		             	 "(pid INTEGER not NULL AUTO_INCREMENT, " +
		             	 " userName VARCHAR(50), " +
		             	 " userPass VARCHAR(50), " + 
		             	 " admin TINYINT(10), " +
		             	 " PRIMARY KEY ( pid ))";
			
			stmt.executeUpdate(sql);
			System.out.println("Created table in given database...");
			conn.close(); //close db connection
		}
		catch(SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
		}
	}
	
	//method to insert into the user table
	public void insertUsers() {
		try {
			//Execute a query
			//System.out.println("Connecting to a selected database for Inserts...");
			//System.out.println("Connected database successfully...");
			//System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();
			String sql = null;
			
			sql = "INSERT INTO jCheung2_users(userName,userPass,admin) " + "VALUES ('user1', 'user411', '0')";
			stmt.executeUpdate(sql);
			
			//System.out.println("Inserted records into the table...");
			conn.close(); //close db connection
		}
		catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	//method to insert into the ticket table
	public void insertTickets(String issuer, String name, String text) {
		try {
			//Execute a query
			//System.out.println("Connecting to a selected database for Inserts...");
			//System.out.println("Connected database successfully...");
			//System.out.println("Inserting records into the table...");
			stmt = conn.createStatement();
			String sql = null;
			java.sql.Timestamp dateTime = new java.sql.Timestamp(new java.util.Date().getTime());
			
			sql = "INSERT INTO jCheung2_ftickets(ticket_issuer, ticket_name, ticket_desc, ticket_status, startDate) " + "VALUES ('"+issuer+"','"+name+"','"+text+"','open','"+dateTime+"')";
			PreparedStatement pstate = conn.prepareStatement(sql);
			pstate.executeUpdate();
			
			//System.out.println("Inserted records into the table...");
		}
		catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	//method to delete a ticket in the ticket table by the ticket id
	public void deleteTicket(int id) {
		try {
			
			PreparedStatement pstate = null;
			String sql = null;
			
			sql = "DELETE FROM jcheung2_ftickets " + "WHERE tid = ?";
			pstate = conn.prepareStatement(sql);
			pstate.setInt(1, id);
			pstate.executeUpdate();
		}
		catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	//method to update the description of a ticket by the ticket id
	public void updateTicket(int id, String update) {
		try {
			
			stmt = conn.createStatement();
			String sql = null;
			String newText = "UPDATED: " + update;
			
			sql = "UPDATE jcheung2_ftickets " + "SET ticket_desc = ? WHERE tid = ?";
			PreparedStatement pstate = conn.prepareStatement(sql);
			pstate.setString(1, newText);
			pstate.setInt(2, id);
			pstate.executeUpdate();
		}
		catch(SQLException se) {
			se.printStackTrace();
		}
	}
	
	//method to close a ticket by ticket id, adds an end date to signify when the ticket was closed
	public void closeTicket(int id) {
		java.sql.Timestamp endDate = new java.sql.Timestamp(new java.util.Date().getTime());
		try {
			PreparedStatement pstate = null;
			String sql = null;
			
			sql = "UPDATE jcheung2_ftickets " + "Set ticket_status = ?, endDate = ? WHERE tid = ?";
			pstate = conn.prepareStatement(sql);
			pstate.setString(1, "closed");
			pstate.setTimestamp(2, endDate);
			pstate.setInt(3, id);
			pstate.executeUpdate();
		}
		catch (SQLException se) {
			se.printStackTrace();
		}
	}
	
	//method to retrieve all of the tickets from the ticket table and put it into a result set object
	public ResultSet retrieveRecords() {
		ResultSet rs = null;
		try {
			
			//System.out.println("Connecting to a selected database for Record retrievals...");
			//System.out.println("Connected database successfully...");
			//System.out.println("Creating Select statement...");
			stmt = conn.createStatement();
			
			String sql = "SELECT tid, ticket_issuer, ticket_name, ticket_desc, ticket_status, startDate, endDate FROM jcheung2_ftickets";

			rs = stmt.executeQuery(sql);

		}
		catch(SQLException se) {
			se.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet viewTicket(int id) {
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			
			//String sql = "SELECT 'tid', 'ticket_issuer', ticket_name', ticket_desc', 'ticket_status', 'startDate', 'endDate' FROM 'jcheung2_ftickets' WHERE tid = 2";
			String sql = "SELECT ticket_issuer, ticket_name, ticket_desc, ticket_status, startDate, endDate FROM jcheung2_ftickets WHERE tid = '"+id+"'";
			rs = stmt.executeQuery(sql);
			
		}
		catch(SQLException se) {
			se.printStackTrace();
		}
		return rs;
	}
}

