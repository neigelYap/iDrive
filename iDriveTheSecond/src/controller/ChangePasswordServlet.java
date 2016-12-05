package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.InputValidationClass;
import model.ChangePasswordClass;
//import net.tanesha.recaptcha.ReCaptchaImpl;
//import net.tanesha.recaptcha.ReCaptchaResponse;

public class ChangePasswordServlet extends HttpServlet {
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
		InputValidationClass ivc = new InputValidationClass();
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String token = request.getParameter("token");
		System.out.println("ffs" + token);
		System.out.println("ffs" + password);
		System.out.println("ffs" + password2);
		if(ivc.passwordInputValidation(password, password2) == true)
		{
			if(ivc.tokenInputValidation(connection, token) == true)
			{
				ChangePasswordClass cpc = new ChangePasswordClass();
				
				cpc.changePassword(connection, password, token);
				request.setAttribute("change", "Successful password change");
				request.getRequestDispatcher("login").forward(request, response);
				}
			else
			{
				request.setAttribute("invalid", "Your request has expired.");
				request.setAttribute("token", token);
				request.getRequestDispatcher("login").forward(request, response);
				}
			}
		else
		{
			request.setAttribute("error", "Invalid password combination.");
			request.setAttribute("token", token);
			request.getRequestDispatcher("forgot").forward(request, response);
			
			//request.getRequestDispatcher("forgot" + "?token=" + token).forward(request,response);
			}
		}
	}

