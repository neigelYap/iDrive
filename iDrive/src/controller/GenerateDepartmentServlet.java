package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.InputValidationClass;
import model.ReservationSender;

import javax.servlet.http.Part;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import model.DepartmentGenerator;
import util.EmailSender;
/**
 * Servlet implementation class SendReservationServlet
 */

@WebServlet("/generatedepartment.html")
@MultipartConfig
public class GenerateDepartmentServlet extends HttpServlet {
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
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String newDept = request.getParameter("department");
		String employeeId = request.getParameter("employeeId");
		Part filePart = request.getPart("photo");
		InputStream inputStream = null;
		
		if (filePart != null) {
            // prints out some information for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());
             
            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }
		
		InputValidationClass validate = new InputValidationClass();
		
		if(validate.departmentInputValidation(newDept) == true)
		{
			if(validate.imageInputValidation(filePart.getContentType()) == true)
			{	
			if(validate.employeeIdInputValidation(employeeId) == true)
			{
				DepartmentGenerator sender = new DepartmentGenerator();
				sender.setDepartment(newDept);
				sender.setEmployeeId(employeeId);
				sender.setPhoto(inputStream);
				if(sender.duplicateDepartment(connection)){
					request.setAttribute("errorMsg", "Department already exists");
					request.getRequestDispatcher("administrator").forward(request,response);
				}else{
					if(sender.existingEmployee(connection)){
						sender.sendDepartment(connection);
						sender.executeProcess(connection);
						request.setAttribute("successMsg", "Department successfully created");
						request.getRequestDispatcher("administrator").forward(request,response);
					}else{
						request.setAttribute("errorMsg", "Employee does not exist");
						request.getRequestDispatcher("administrator").forward(request,response);
					}
				}
			}
			else
			{
				System.out.println(validate.employeeIdInputValidation(employeeId));
				request.setAttribute("errorMsg", "Please enter correct details in the Employee I.D. field");
				request.getRequestDispatcher("administrator").forward(request,response);
				}
			}
			else
			{
				System.err.println(validate.imageInputValidation(filePart.getContentType()));
				request.setAttribute("errorMsg", "Please upload an image file");
				request.getRequestDispatcher("administrator").forward(request, response);
				}
			}
		else
		{
			System.out.println(validate.departmentInputValidation(newDept));
			request.setAttribute("errorMsg", "Please enter correct details in the Department field");
			request.getRequestDispatcher("administrator").forward(request,response);
			}
			
		
		
		
	}

}
