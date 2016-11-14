package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class ReservationSender {
	private String date;
	private String timeHours;
	private String timeMinutes;
	private String timeOfDay;
	private String destination;
	private String purpose;
	private String numPassengers;
	private String passengers;
	private int empId;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTimeHours() {
		return timeHours;
	}
	public void setTimeHours(String timeHours) {
		this.timeHours = timeHours;
	}
	public String getTimeMinutes() {
		return timeMinutes;
	}
	public void setTimeMinutes(String timeMinutes) {
		this.timeMinutes = timeMinutes;
	}
	public String getTimeOfDay() {
		return timeOfDay;
	}
	public void setTimeOfDay(String timeOfDay) {
		this.timeOfDay = timeOfDay;
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
	public String getNumPassengers() {
		return numPassengers;
	}
	public void setNumPassengers(String numPassengers) {
		this.numPassengers = numPassengers;
	}
	public String getPassengers() {
		return passengers;
	}
	public void setPassengers(String passengers) {
		this.passengers = passengers;
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public void sendReservation(Connection connection){
		try{
			String query ="INSERT INTO reservations (tripDate, departure, destination, travelPurpose, passengers, passengerNum, employeeID) values (?,?,?,?,?,?,?)";
			PreparedStatement pstmt = connection.prepareStatement(query);
			
			SimpleDateFormat forDate = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = forDate.parse(getDate());
            java.sql.Date sqlDate = new Date(date.getTime());
            
            String departureFull = getTimeHours()+":"+getTimeMinutes()+" "+getTimeOfDay();
            
			pstmt.setDate(1, sqlDate);
			pstmt.setString(2, departureFull);
			pstmt.setString(3, getDestination());
			pstmt.setString(4, getPurpose());
			pstmt.setString(5,getPassengers());
			pstmt.setInt(6, Integer.parseInt(getNumPassengers()));
			pstmt.setInt(7, getEmpId());
			pstmt.executeUpdate();
		}catch(SQLException sqle){
			System.out.println(sqle);
		} catch (ParseException pe) {
			System.out.println(pe);
		}
	}
}
