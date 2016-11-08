package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Validate {
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int accountChecker(Connection connection){
		try{
			String query = "SELECT * FROM employee WHERE employeeID = ? AND pass =  ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString( 1, getUsername());
			pstmt.setString( 2, getPassword()); 
			ResultSet rs =pstmt.executeQuery();
			if(rs.next()){
				return rs.getInt("accountTypeID");
			}else{
				return 0;
			}
		}catch(SQLException sqle){
			return 0;
		}
	}
	
	public boolean userValidate(){

		for (char userName:getUsername().toCharArray())
	    {
	        if (!Character.isDigit(userName)){
	        	return false;
	        }
	    }
	    return true;
	}
}