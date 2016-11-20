package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReservationViewer implements Serializable{
	
	private int reservationID;
	private Date tripDate;
	private String departure;
	private String destination;
	private String purpose;
	private String passengers;
	private int passengerNum;
	private String department;
	private String name;
	
	private int departmentID;
	private int employeeID;
	
	public ReservationViewer(){
		
	}
	
	
	public int getReservationID() {
		return reservationID;
	}
	public void setReservationID(int reservationID) {
		this.reservationID = reservationID;
	}
	public Date getTripDate() {
		return tripDate;
	}
	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getPassengers() {
		return passengers;
	}
	public void setPassengers(String passengers) {
		this.passengers = passengers;
	}
	public int getPassengerNum() {
		return passengerNum;
	}
	public void setPassengerNum(int passengerNum) {
		this.passengerNum = passengerNum;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDepartmentID() {
		return departmentID;
	}
	public void setDepartmentID(int departmentID) {
		this.departmentID = departmentID;
	}
	public int getEmployeeID() {
		return employeeID;
	}
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}

	public ResultSet managerReservations(Connection connection, int deptID){
		try{
			String query ="SELECT * FROM reservations WHERE trackingID = 1 AND departmentID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, deptID);
			ResultSet pendingReservations = pstmt.executeQuery();
			
			return pendingReservations;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	public ResultSet viewReservation(Connection connection, int deptID, int reserveID){
		try{
			String query ="SELECT * FROM reservations WHERE trackingID = 1 AND departmentID = ? AND reservationID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, deptID);
			pstmt.setInt(2, reserveID);
			ResultSet pendingReservations = pstmt.executeQuery();
			
			return pendingReservations;
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
		return null;
	}
	
	private void departmentName(Connection connection, int deptID){
		deptID=departmentID;
		try{
			String query ="SELECT * FROM departments WHERE departmentID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, deptID);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				setDepartment(rs.getString("departmentName"));
			}else{

			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("department exception omg");
		}
	}
	
	private void fullName(Connection connection, int empID){
		empID=employeeID;
		try{
			String query ="SELECT * FROM employee WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setInt(1, empID);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				String fName = rs.getString("firstName");
				String lName = rs.getString("lastName");
				setName(fName+" "+lName);
			}else{

			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			System.out.println("full name exception ohno");
		}
	}
	
	public void executeProcess(Connection connection){
		departmentName(connection, getDepartmentID());
		fullName(connection, getEmployeeID());
	}
}
