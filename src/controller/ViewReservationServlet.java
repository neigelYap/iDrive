package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		
		int reservationID = Integer.parseInt(request.getParameter("id"));
		
		ReservationViewer rv = new ReservationViewer();
		HttpSession session = request.getSession();
		int deptID = (int) session.getAttribute("deptName");
		try{
			ResultSet rs = rv.viewReservation(connection, deptID, reservationID);
			
			while(rs.next()){
				request.setAttribute("reservationID", rs.getInt("reservationID"));
				request.setAttribute("tripDate", rs.getDate("tripDate"));
				request.setAttribute("departure", rs.getString("departure"));
				request.setAttribute("destination", rs.getString("destination"));
				request.setAttribute("purpose", rs.getString("travelPurpose"));
				request.setAttribute("passengers", rs.getString("passengers"));
				request.setAttribute("passengerNum", rs.getInt("passengerNum"));
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		
		request.getRequestDispatcher("reservation.jsp").forward(request, response);
	}

}
