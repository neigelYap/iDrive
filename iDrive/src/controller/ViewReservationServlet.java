package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.ReservationUpdater;
import model.ReservationViewer;

public class ViewReservationServlet extends HttpServlet {
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
		
		HttpSession session = request.getSession();
		int deptID = (int) session.getAttribute("deptName");
		int accType = (int) session.getAttribute("type");
		int acc = (int) session.getAttribute("account");
		ReservationUpdater update = new ReservationUpdater();
		String location="";
		if(request.getParameter("approved") != null){
			
			int approved = Integer.parseInt(request.getParameter("approved"));
			if(accType == 2){
				update.approvedReservationManager(connection, approved);
			}else if(accType == 3){
				update.approvedReservationAdmin(connection, approved);
			}else{
				
			}

			
		}else if(request.getParameter("denied")!= null){
			
			int denied = Integer.parseInt(request.getParameter("denied"));
			String reason="";
			if(request.getParameter("denialReason")!=null){
				reason = request.getParameter("denialReason");
				
			}else{
				reason = "None";
			}
			update.deniedReservation(connection, denied, reason);
			
		}else{
			response.sendRedirect("login");
		}
		
		try{
			ReservationViewer rv = new ReservationViewer();
			if(accType==1){
				ResultSet rs1 = rv.employeeReservations(connection, acc);
				List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();

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
					details.setTrackId(rs1.getInt("trackingID"));
					details.executeProcess(connection);
					reservationDetails.add(details);

				}
				location ="employee";
				session.removeAttribute("approved");
				session.setAttribute("approved", reservationDetails);
			}else if(accType == 2){
					
				ResultSet rs1 = rv.managerReservations(connection, deptID);
				ResultSet rs2 = rv.employeeReservations(connection, acc);
				List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();
				List<ReservationViewer> reservationDetails2 = new ArrayList<ReservationViewer>();
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
				while(rs2.next()){
					ReservationViewer details2 = new ReservationViewer();
					details2.setReservationIDEmp(rs2.getInt("reservationID"));
					details2.setTripDateEmp(rs2.getDate("tripDate"));
					details2.setDepartureEmp(rs2.getString("departure"));
					details2.setDestinationEmp(rs2.getString("destination"));
					details2.setPurposeEmp(rs2.getString("travelPurpose"));
					details2.setPassengersEmp(rs2.getString("passengers"));
					details2.setPassengerNumEmp(rs2.getInt("passengerNum"));
					details2.setTrackId(rs2.getInt("trackingID"));
					details2.executeProcess(connection);
					reservationDetails2.add(details2);
				}
				location = "manager";
				session.removeAttribute("pending");
				session.setAttribute("pending", reservationDetails);
				session.removeAttribute("approved");
				session.setAttribute("approved", reservationDetails2);
			}else if(accType == 3){
				ResultSet rs2 = rv.adminReservations(connection);
				List<ReservationViewer> reservationDetails = new ArrayList<ReservationViewer>();
				while(rs2.next()){
					ReservationViewer details = new ReservationViewer();
					details.setReservationID(rs2.getInt("reservationID"));
					details.setTripDate(rs2.getDate("tripDate"));
					details.setDeparture(rs2.getString("departure"));
					details.setDestination(rs2.getString("destination"));
					details.setPurpose(rs2.getString("travelPurpose"));
					details.setPassengers(rs2.getString("passengers"));
					details.setPassengerNum(rs2.getInt("passengerNum"));
					details.setDepartmentID(rs2.getInt("departmentID"));
					details.setEmployeeID(rs2.getInt("employeeID"));
					details.executeProcess(connection);
					reservationDetails.add(details);
				}
				
				location = "administrator";
				session.removeAttribute("pending");
				session.setAttribute("pending", reservationDetails);
			}else{
				
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			sqle.printStackTrace();
		}
		response.sendRedirect(location);
		
	}

}
