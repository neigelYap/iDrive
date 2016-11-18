package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Validate;
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
public class AccountCheckerServlet extends HttpServlet {
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
		
		String remoteAddr = request.getRemoteAddr();
	    ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
	    reCaptcha.setPrivateKey("6LfQNwwUAAAAAERiI2YtwOyJeWt_iyaTJzAiEb9F");
	
	    String challenge = request.getParameter("recaptcha_challenge_field");
	    String uresponse = request.getParameter("recaptcha_response_field");
	    ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

		String user = request.getParameter("username");
		String password =  request.getParameter("password");
		
		if(user.isEmpty() || password.isEmpty()){
			request.setAttribute("invalid", "Please enter a username or password");
			request.getRequestDispatcher("login").include(request,response);
		}else{
		
			Validate val = new Validate();
			val.setUsername(user);
			val.setPassword(password);
			
			boolean isValid = val.userValidate();
			
			if(isValid){

				int username =  Integer.parseInt(user);
				
				val.setUsername(user);
				int accountType = val.accountChecker(connection);
				
				if(accountType != 0){
					if(reCaptchaResponse.isValid()){
						request.setAttribute("account", username);
						request.setAttribute("type", accountType);
						request.setAttribute("pass", password);
						request.setAttribute("fName", val.getfName());
						request.setAttribute("lName", val.getlName());
						request.setAttribute("deptName", val.getDeptName());
						request.setAttribute("email", val.getEmail());
						
						request.getRequestDispatcher("accountseparator.html").forward(request,response);
					}else{
						request.setAttribute("invalid", "CAPTCHA code was incorrect");
						request.getRequestDispatcher("login").include(request,response);
					}
				}else{
					request.setAttribute("invalid", "Invalid username or password, try again");
					request.getRequestDispatcher("login").forward(request,response);
				}
			}else{
				request.setAttribute("invalid", "Invalid username or password, try again");
				request.getRequestDispatcher("login").forward(request,response);
			}
		}
	}

}
