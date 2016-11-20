package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import model.InputValidationClass;
import model.ReservationSender;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ReservationSender;
import util.EmailSender;
/**
 * Servlet implementation class SendReservationServlet
 */
@WebServlet("/sendreservation.html")
public class SendReservationServlet extends HttpServlet {
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
		String date = request.getParameter("tripDate");
		String timeHours = request.getParameter("hours");
		String timeMinutes = request.getParameter("minutes");
		String timeOfDay = request.getParameter("time");
		String destination = request.getParameter("destination");
		String purpose = request.getParameter("travelPurpose");
		String numPassengers = request.getParameter("numPassengers");
		String passengers = request.getParameter("passengers");
		String location = "";
		
		HttpSession session = request.getSession();
		int accType = (Integer)session.getAttribute("type");
		int empId = (Integer)session.getAttribute("account");
		int department = (Integer)session.getAttribute("deptName");
		switch(accType){
			case 1: 
				location = "employee";
				break;
			case 2:
				location = "manager";
				break;
			case 3:
				location = "administrator";
				break;
			default:
				location = "login";
				break;
		}
		
		InputValidationClass validate = new InputValidationClass();
		
		if(validate.dateInputValidation(date) == true){
			if(validate.timeInputValidation(timeHours, timeMinutes, timeOfDay) == true){
				if(validate.destinationInputValidation(destination) == true){
					if(validate.purposeInputValidation(purpose) == true){
						if(validate.numberInputValidation(numPassengers) == true){
							if(validate.passengerInputValidation(passengers) == true){
								ReservationSender sender = new ReservationSender();
								sender.setDate(date);
								sender.setTimeHours(timeHours);
								sender.setTimeMinutes(timeMinutes);
								sender.setTimeOfDay(timeOfDay);
								sender.setDestination(destination);
								sender.setPurpose(purpose);
								sender.setNumPassengers(numPassengers);
								sender.setPassengers(passengers);
								sender.setEmpId(empId);
								sender.sendReservation(connection, accType, department);
								EmailSender emSend = new EmailSender();
								emSend.reservationSent(connection,accType,department);
								request.setAttribute("successMsg", "Reservation has been successfully sent for approval");
								request.getRequestDispatcher(location).forward(request,response);
							} else {
								//error for passengers input message
								System.out.println(validate.passengerInputValidation(passengers));
								request.setAttribute("errorMsg", "Please enter correct details in the Passengers field");
								request.getRequestDispatcher(location).forward(request,response);
							}
						} else {
							//error for numPassengers input message
							System.out.println(validate.numberInputValidation(numPassengers));
							request.setAttribute("errorMsg", "Invalid number of passengers");
							request.getRequestDispatcher(location).forward(request,response);
						}
					} else {
						//error for purpose input message
						System.out.println(validate.purposeInputValidation(purpose));
						request.setAttribute("errorMsg", "Please enter a valid purpose of travel");
						request.getRequestDispatcher(location).forward(request,response);
					}
				} else {
					//error for destination input message
					System.out.println(validate.destinationInputValidation(destination));
					request.setAttribute("errorMsg", "Invalid destination details");
					request.getRequestDispatcher(location).forward(request,response);
				}
			} else {
				//error for time input message
				System.out.println(validate.timeInputValidation(timeHours, timeMinutes, timeOfDay));
				request.setAttribute("errorMsg", "Please enter a correct time");
				request.getRequestDispatcher(location).forward(request,response);
			}
		} else {
			//error for trip date message
			System.out.println(validate.dateInputValidation(date));
			request.setAttribute("errorMsg", "Please enter a date 3 days before departure or within the next three months");
			request.getRequestDispatcher(location).forward(request,response);
		}
		
		
		
	}

}
