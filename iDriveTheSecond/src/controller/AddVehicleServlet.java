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

import model.InputValidationClass;
import model.VehicleSender;


@WebServlet("/addvehicle.html")
@MultipartConfig
public class AddVehicleServlet extends HttpServlet {
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
		String manufacturer = request.getParameter("carManufacturer");
		String yearManufactured = request.getParameter("yearMake");
		String carModel = request.getParameter("carModel");
		String carColor = request.getParameter("carColor");
		String carPlate = request.getParameter("carPlate");
		String carCapacity = request.getParameter("carCapacity");
		Part carImage = request.getPart("carImage");
		
		InputStream inputStream = null;
		
		if(carImage != null){
			System.out.println(carImage.getName());
			System.out.println(carImage.getSize());
			System.out.println(carImage.getContentType());
			
			inputStream = carImage.getInputStream();
		} 
		
		
		InputValidationClass validate = new InputValidationClass();
		
		if(validate.vehicleStringInputValidation(manufacturer) == true){
			if(validate.vehicleYearManufacturedInputValidation(yearManufactured) == true){
				if(validate.vehicleStringInputValidation(carModel) == true){
					if(validate.vehicleStringInputValidation(carColor) == true){
						if(validate.vehiclePlateInputValidation(carPlate) == true){
							if(validate.vehicleMaxCapacityInputValidation(carCapacity) == true){
								if(validate.vehicleImageInputValidation(carImage.getSize(), carImage.getContentType()) == true){
									VehicleSender vs = new VehicleSender();
									vs.setManufacturer(manufacturer);
									vs.setYearManufactured(yearManufactured);
									vs.setCarModel(carModel);
									vs.setCarColor(carColor);
									vs.setCarPlate(carPlate);
									vs.setCarCapacity(carCapacity);
									vs.setPhoto(inputStream);
									if(vs.duplicateVehicle(connection)){
										request.setAttribute("vehicleError", "Vehicle already exists");
										request.getRequestDispatcher("administrator").forward(request, response);
									}else{
										
										vs.executeProcess(connection);
										request.setAttribute("vehicleSuccess", "Vehicle successfully created");
										request.getRequestDispatcher("administrator").forward(request, response);
									}
								} else {
									request.setAttribute("vehicleError", "Please upload an image file");
									request.getRequestDispatcher("administrator").forward(request, response);
								}
							} else {
								request.setAttribute("vehicleError", "Capacity exceeds the limit: 25");
								request.getRequestDispatcher("administrator").forward(request, response);
							}
						} else {
							request.setAttribute("vehicleError", "Plate number must contain only 6-7 letters or numbers");
							request.getRequestDispatcher("administrator").forward(request, response);
						}
					} else {
						request.setAttribute("vehicleError", "Invalid color name");
						request.getRequestDispatcher("administrator").forward(request, response);
					}
				} else {
					request.setAttribute("vehicleError", "Enter a valid vehicle model");
					request.getRequestDispatcher("administrator").forward(request, response);
				}
			} else {
				request.setAttribute("vehicleError", "Invalid year, must contain only numbers");
				request.getRequestDispatcher("administrator").forward(request, response);
			}
		} else {
			request.setAttribute("vehicleError", "Enter a valid manufacturer name");
			request.getRequestDispatcher("administrator").forward(request, response);
		}
		
	}

}
