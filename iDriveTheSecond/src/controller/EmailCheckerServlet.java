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
import model.ForgotGenerator;
import util.EmailSender;
//import net.tanesha.recaptcha.ReCaptchaImpl;
//import net.tanesha.recaptcha.ReCaptchaResponse;

public class EmailCheckerServlet extends HttpServlet {
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
		
		String email = request.getParameter("email");
		System.out.println(email);
		if(email.isEmpty()){
			System.out.println("pumasok");
			request.setAttribute("invalid", "Please enter your e-mail!");
			request.getRequestDispatcher("forgot").include(request,response);
		}else{
			
			Validate val = new Validate();
			val.setEmail(email);
			EmailSender es = new EmailSender();
				int accountType = val.emailChecker(connection);
				
				if(accountType != 0){

						ForgotGenerator fGen = new ForgotGenerator();
						fGen.generateCleaner(connection);
						fGen.generateForgot(connection, email);
						es.forgotSent(connection, email);
						request.setAttribute("successMsg", "E-mail Sent");
						request.getRequestDispatcher("forgot").forward(request,response);
					
				}else{
					request.setAttribute("invalid", "Invalid email!");
					request.getRequestDispatcher("forgot").forward(request,response);
				}
			}
		}
	}

