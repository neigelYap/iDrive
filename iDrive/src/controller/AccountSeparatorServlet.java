package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccountSeparatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Connection connection = null;
	public void init() throws ServletException {
		try {
			Class.forName(getServletContext().getInitParameter("dbDriver"));
			
			StringBuffer dbConfig = new StringBuffer("jdbc:")
				.append(getServletContext().getInitParameter("dbType"))
				.append("://")
				.append(getServletContext().getInitParameter("dbServerName"))
				.append(":")
				.append(getServletContext().getInitParameter("dbPort"))
				.append("/")
				.append(getServletContext().getInitParameter("dbDatabase"));
			
			connection = DriverManager
				.getConnection(dbConfig.toString(), getServletContext().getInitParameter("dbUsername"), getServletContext().getInitParameter("dbPasswword"));
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		} catch (SQLException sqle) {
			System.err.println("SQLE - " + sqle.getMessage());
			sqle.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int accType = (int) request.getAttribute("type");
		int acc = (int) request.getAttribute("account");
		String pass = (String) request.getAttribute("pass");
		String fName = (String) request.getAttribute("fName");
		String lName = (String) request.getAttribute("lName");
		String email = (String) request.getAttribute("email");
		int deptName = (int) request.getAttribute("deptName");
		
		String location = "";
		if(acc != 0 && accType != 0 && pass != null){
			if(accType == 1){
				location = "employee";
			}else if(accType == 2){
				location = "manager";
			}else if(accType == 3){
				location = "administrator";
			}else{
				request.setAttribute("invalid", "Account does not exist");
				request.getRequestDispatcher("login").forward(request,response);
			}
			HttpSession session = request.getSession();
			session.setAttribute("account", acc);
			session.setAttribute("type", accType);
			session.setAttribute("pass", pass);
			session.setAttribute("fName", fName);
			session.setAttribute("lName", lName);
			session.setAttribute("deptName", deptName);
			session.setAttribute("email", email);
			// ADD RESULTSET
			response.sendRedirect(location);
		}else{
			request.setAttribute("invalid", "Account does not exist");
			request.getRequestDispatcher("login").forward(request,response);
		}
	}

}
