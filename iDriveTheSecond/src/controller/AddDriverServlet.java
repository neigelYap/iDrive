package controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.DriverSender;
import model.InputValidationClass;

/**
 * Servlet implementation class AddDriverServlet
 */
@WebServlet("/adddriver.html")
@MultipartConfig
public class AddDriverServlet extends HttpServlet {
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
		try{
			String employeeID = request.getParameter("employeeID");
			String licenseID = request.getParameter("licenseID");
			String restrictionID[] = request.getParameterValues("licenseRestriction");
			Part driverImage = request.getPart("driverImage");
			
			InputStream inputStream = null;
			
			if(driverImage != null){
				System.out.println(driverImage.getName());
				System.out.println(driverImage.getSize());
				System.out.println(driverImage.getContentType());
				
				inputStream = driverImage.getInputStream();
			}
			
			InputValidationClass validate = new InputValidationClass();
			
			if(validate.employeeIdInputValidation(employeeID) == true){
				if(validate.licenseInputValidation(licenseID) == true){
					if(validate.restrictionInputValidation(restrictionID) == true){
						if(validate.driverImageInputValidation(driverImage.getSize(), driverImage.getContentType()) == true){
							System.out.println("pumasok");
							DriverSender ds = new DriverSender();
							ds.setEmployeeID(employeeID);
							ds.setLicenseID(licenseID);
							ds.setRestrictionID(restrictionID);
							ds.setDriverImage(inputStream);
							if(ds.duplicateDriver(connection)==true){
								request.setAttribute("errorMsgDriver", "Driver already exists");
								request.getRequestDispatcher("administrator").forward(request,response);
							}else if(ds.duplicateLicense(connection)==true){
								request.setAttribute("errorMsgDriver", "License already assigned to a driver");
								request.getRequestDispatcher("administrator").forward(request,response);
							}else{
								ds.execute(connection);
								System.out.println("all good");
								request.setAttribute("successMsgDriver", "Driver successfully created");
								request.getRequestDispatcher("administrator").forward(request,response);
							}
						} else {
							System.out.println("error 4");
							request.setAttribute("errorMsgDriver", "Please upload an image file");
							request.getRequestDispatcher("administrator").forward(request,response);
						}
					} else {
						System.out.println("error 3");
						request.setAttribute("errorMsgDriver", "Choose the correct restriction");
						request.getRequestDispatcher("administrator").forward(request,response);
					}
				} else {
					System.out.println("error 2");
					request.setAttribute("errorMsgDriver", "Invalid license I.D.");
					request.getRequestDispatcher("administrator").forward(request,response);
				}
			} else {
				System.out.println("error 1");
				request.setAttribute("ererrorMsgDriverrorMsg", "Employee does not exist");
				request.getRequestDispatcher("administrator").forward(request,response);
			}
			
		} catch (Exception e){
			e.printStackTrace();
			response.sendRedirect("administrator");
		}
		
	}

}
