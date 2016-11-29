package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ReservationViewer;

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
			session.setMaxInactiveInterval(-1);
			switch(accType){
			case 1:
				try{
					
					ReservationViewer manager = new ReservationViewer();
					ResultSet rs2 = manager.employeeReservations(connection, acc);
					List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();
					
					while(rs2.next()){
						System.out.println(rs2.getInt("reservationID"));
						ReservationViewer details = new ReservationViewer();
						details.setReservationIDEmp(rs2.getInt("reservationID"));
						details.setTripDateEmp(rs2.getDate("tripDate"));
						details.setDepartureEmp(rs2.getString("departure"));
						details.setDestinationEmp(rs2.getString("destination"));
						details.setPurposeEmp(rs2.getString("travelPurpose"));
						details.setPassengersEmp(rs2.getString("passengers"));
						details.setPassengerNumEmp(rs2.getInt("passengerNum"));
						details.setDepartmentID(rs2.getInt("departmentID"));
						details.setEmployeeID(rs2.getInt("employeeID"));
						details.setTrackId(rs2.getInt("trackingID"));
						details.executeProcess(connection);
						reservationDetails.add(details);

					}
					session.setAttribute("approved", reservationDetails);
				}catch(SQLException sqle){
					System.out.println(sqle);
					sqle.printStackTrace();
				}
				break;
			case 2:
				try{
					ReservationViewer manager = new ReservationViewer();
					ResultSet rs1 = manager.managerReservations(connection, deptName);
					ResultSet rs2 = manager.employeeReservations(connection, acc);
					List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();
					List<ReservationViewer> reservationDetails2 = new ArrayList<ReservationViewer>();

					while(rs1.next()){
						System.out.println(rs1.getInt("reservationID"));
						ReservationViewer details = new ReservationViewer();
						details.setReservationID(rs1.getInt("reservationID"));
						details.setTripDate(rs1.getDate("tripDate"));
						details.setDeparture(rs1.getString("departure"));
						details.setDestination(rs1.getString("destination"));
						details.setPurpose(rs1.getString("travelPurpose"));
						details.setPassengers(rs1.getString("passengers"));
						details.setPassengerNum(rs1.getInt("passengerNum"));
						details.setDepartmentID(rs1.getInt("departmentID"));
						details.setEmployeeID(rs1.getInt("employeeID"));
						details.executeProcess(connection);
						reservationDetails.add(details);

					}
					
					while(rs2.next()){
						System.out.println(rs2.getInt("reservationID"));
						ReservationViewer details2 = new ReservationViewer();
						details2.setReservationIDEmp(rs2.getInt("reservationID"));
						details2.setTripDateEmp(rs2.getDate("tripDate"));
						details2.setDepartureEmp(rs2.getString("departure"));
						details2.setDestinationEmp(rs2.getString("destination"));
						details2.setPurposeEmp(rs2.getString("travelPurpose"));
						details2.setPassengersEmp(rs2.getString("passengers"));
						details2.setPassengerNumEmp(rs2.getInt("passengerNum"));
						details2.setDepartmentID(rs2.getInt("departmentID"));
						details2.setEmployeeID(rs2.getInt("employeeID"));
						details2.setTrackId(rs2.getInt("trackingID"));
						details2.executeProcess(connection);
						reservationDetails2.add(details2);

					}
					session.setAttribute("approved", reservationDetails2);
					session.setAttribute("pending", reservationDetails);
				}catch(SQLException sqle){
					System.out.println(sqle);
					sqle.printStackTrace();
				}
				break;
			case 3:
				try{
					ReservationViewer admin = new ReservationViewer();
					ResultSet rs1 = admin.adminReservations(connection);
					List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();

					while(rs1.next()){
						ReservationViewer details = new ReservationViewer();
						details.setReservationID(rs1.getInt("reservationID"));
						details.setTripDate(rs1.getDate("tripDate"));
						details.setDeparture(rs1.getString("departure"));
						details.setDestination(rs1.getString("destination"));
						details.setPurpose(rs1.getString("travelPurpose"));
						details.setPassengers(rs1.getString("passengers"));
						details.setPassengerNum(rs1.getInt("passengerNum"));
						details.setDepartmentID(rs1.getInt("departmentID"));
						details.setEmployeeID(rs1.getInt("employeeID"));
						details.executeProcess(connection);
						reservationDetails.add(details);
					}
					session.setAttribute("pending", reservationDetails);
				}catch(SQLException sqle){
					System.out.println(sqle);
					sqle.printStackTrace();
				}
				break;
			default:
				break;
		}
		
			response.sendRedirect(location);
		}else{
			request.setAttribute("invalid", "Account does not exist");
			request.getRequestDispatcher("login").forward(request,response);
		}
	}

}
