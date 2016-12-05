package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class ChangePasswordClass {
	private String password;
	private String token;
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	
	public void changePassword(Connection connection, String password, String token)
	{
		try{
			System.out.println("this is my shingaling " + token);
			String query ="SELECT * FROM forgot WHERE token = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, token);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				 setEmail(rs.getString("email"));
			}
			System.out.println("this my though " + email);
			String query2 ="UPDATE employee SET pass=? WHERE email=?";
			PreparedStatement pstmt2 = connection.prepareStatement(query2);
			pstmt2.setString(1, password);
			pstmt2.setString(2, getEmail());
			pstmt2.executeUpdate();
			
			String query3 = "DELETE From forgot WHERE token = ?";
		      PreparedStatement pstmt3 = connection.prepareStatement(query3);
		      pstmt3.setString(1, token);
		      pstmt3.executeUpdate();
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		}
	
}
