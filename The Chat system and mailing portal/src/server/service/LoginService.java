package server.service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import server.client.User;

/**
 * 
 * @author Anushka
 *
 */
public class LoginService {
	
	private Connection connection;
		
	public LoginService() {
		try {
			this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/userregistration", "root", "mysql");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void registerUser(String firstName, String lastName, String userName, String password, String emailId, String gender) {
		
		try {
			
			Statement statement = connection.createStatement();
			
			String string = "insert into user values (" + "'" +firstName + "'" + "," +  "'" + lastName + "'" + "," + "'" + userName + "'" + "," + "'" + password + "'" + "," +  "'" + emailId + "'" + "," + "'" + gender + "'" + ")";
			int result = statement.executeUpdate(string);
		}
		catch(Exception exp) {
			exp.printStackTrace();
		}
	}
	
	public boolean isValidUser(String username, String password) {
		
		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet2 = statement.executeQuery("select count(*) from user where username =" + "'" + username + "'" + " AND password = " +  "'" + password + "'" );
			resultSet2.next();
			int count = resultSet2.getInt(1);
			
			if(count == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean doesUserNameExists(String userName) {
		
		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet2 = statement.executeQuery("select count(*) from user where username =" + "'" + userName + "'" );
			resultSet2.next();
			int count = resultSet2.getInt(1);
			if(count == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
	
	public boolean doesEmailExists(String emailId) {

		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet2 = statement.executeQuery("select count(*) from user where email_id =" + "'" + emailId + "'" );
			resultSet2.next();
			int count = resultSet2.getInt(1);			
			if(count == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		  }

		return false;
	}
}