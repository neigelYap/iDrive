package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.PasswordAuthentication;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class EmailSender {
	private String reason;
	private Session session;
	private String sender = "idrive.bot@gmail.com";
	private String password = "harambelivesonforever52816";
	private String token;
	

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private void googleMailSession(){
		 Properties properties = System.getProperties();

		    properties.setProperty("mail.smtp.host", "smtp.gmail.com");
		    properties.put("mail.smtp.port", "465");
		    properties.put("mail.smtp.ssl.enable", "true");
		    properties.setProperty("mail.smtp.auth", "true");

		    session = Session.getDefaultInstance(properties,new javax.mail.Authenticator() {
		    	
		  			protected PasswordAuthentication getPasswordAuthentication() {
		  				
		  					return new PasswordAuthentication(sender, password);
		  					
		  				}
		  			
		  	});
	}
	//When reservation form is first sent to the managers or the administrator (if the managers are the ones
	//who sent it).
	public void reservationSent(Connection connection, int accType, int department, String employeeName, String tripDate, String hours, String minutes, String timeOfDay, String destination,
								String numPassengers, String purpose){
		googleMailSession();	
		 
		String receiver="";
		String receiverName="";
			int receiverAccType;
			if(accType == 1){
				receiverAccType = 2;
			}else{
				receiverAccType = 3;
			}
			
		/*
		 * StringBuilder contentBuilder = new StringBuilder();
			try {
			    BufferedReader in = new BufferedReader(new FileReader("mypage.html"));
			    String str;
			    while ((str = in.readLine()) != null) {
			        contentBuilder.append(str);
			    }
			    in.close();
			} catch (IOException e) {
			}
			String content = contentBuilder.toString();	
		 */
		MimeMessage message = new MimeMessage(session);
		
		
        String query = "SELECT * FROM employee WHERE departmentName = ? AND accountTypeID =  ?";
        StringBuilder contentBuilder = new StringBuilder();
		try{
			if(receiverAccType==2){
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt( 1, department);
				pstmt.setInt( 2, receiverAccType); 
				ResultSet rs =pstmt.executeQuery();
				if(rs.next()){
					receiver = rs.getString("email");
					receiverName = rs.getString("firstName") + " " + rs.getString("lastName");
				}else{
					
				}
			}else if(receiverAccType==3){
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, receiverAccType);
				pstmt.setInt(2, 1);
				ResultSet rs =pstmt.executeQuery();
				if(rs.next()){
					receiver = rs.getString("email");
					receiverName = rs.getString("firstName") + " " + rs.getString("lastName");
				}else{
					
				}
			}
			
			
			
			message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			message.setSubject("iDrive - New Reservation Request");
			BufferedReader in = new BufferedReader(new FileReader("C:/iDriveEmail/email.html"));
			  String str;
			  while ((str = in.readLine()) != null) {
			        contentBuilder.append(str);
			    }
			    in.close();
			String content = contentBuilder.toString();	
			message.setText(content, "utf-8", "html");
		/*	message.setText("Good Day, " + receiverName.toUpperCase() + " ! \n\n"
		 			+ employeeName + " has requested for a trip reservation with the following details: \n\n"
	         		+ "Trip Date: " + tripDate + "\n"
	         		+ "Departure Time: " + hours + ":" + minutes + " " + timeOfDay + "\n"
	         		+ "Destination: " + destination + "\n"
	         		+ "Number of Passengers: " + numPassengers + "\n\n"
	         		+ "The purpose of this trip is to as stated by " + employeeName + ": " + purpose + "\n" 
	         		+"\n\n<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
		*/
	         Transport.send(message);	
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }catch(SQLException sqle){
	    	 System.out.println(sqle);
	     }
		  catch(IOException ioe)
		{
			System.out.println(ioe);  
			}
		
	}
	//When the pending reservation is approved by the manager.
	public void reservationApprovedManager(String receiver, String date){
		googleMailSession();
		try{

	         MimeMessage message = new MimeMessage(session);

	         message.setFrom(new InternetAddress(sender));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
	         message.setSubject("Manager Approval");
	         message.setText("Good Day! Your reservation on " + date + " has been approved by your Department Manager."
	         		+ "It is now currently pending for approval by the Administrator."+"\n\n<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
	         Transport.send(message);
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }
	}
	//When the pending reservation is approved by the administrator. This will include a QR code.
	public void reservationApprovedAdministrator(String receiver, String date){
		googleMailSession();
		try{

	         MimeMessage message = new MimeMessage(session);

	         message.setFrom(new InternetAddress(sender));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
	         message.setSubject("Administrator Approval");
	         message.setText("Good Day! Your reservation on " + date + " has been approved by the Administrator.");
	         message.setText("<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
	         Transport.send(message);
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }
	}
	//When the pending reservation is denied by the manager or the administrator.
	public void reservationDenied(String receiver, String date){
		googleMailSession();
		try{

	         MimeMessage message = new MimeMessage(session);

	         message.setFrom(new InternetAddress(sender));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
	         message.setSubject("Reservation Denied");
	         message.setText("Good Day! Your reservation on " + date + " has been denied due to:"
	         		+ reason);
	         message.setText("<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
	         Transport.send(message);
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }
	}
	
	public void forgotSent(Connection connection, String email){
			googleMailSession();	
			
			String receiver=email;
			
			
			/*
			* StringBuilder contentBuilder = new StringBuilder();
			try {
			BufferedReader in = new BufferedReader(new FileReader("mypage.html"));
			String str;
			while ((str = in.readLine()) != null) {
			contentBuilder.append(str);
			}
			in.close();
			} catch (IOException e) {
			}
			String content = contentBuilder.toString();	
			*/
			MimeMessage message = new MimeMessage(session);

			String query = "SELECT * FROM forgot WHERE email = ?";
			StringBuilder contentBuilder = new StringBuilder();
			try{
			
			PreparedStatement pstmt = connection.prepareStatement(query);
			pstmt.setString(1, receiver);
			ResultSet rs =pstmt.executeQuery();
			if(rs.next()){
			setToken(rs.getString("token"));
			}else{
			
			}	
			message.setFrom(new InternetAddress(sender));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
			message.setSubject("iDrive - Forgot Password");
			BufferedReader in = new BufferedReader(new FileReader("C:/iDriveEmail/email.html/"));
			String str;
			while ((str = in.readLine()) != null) {
			contentBuilder.append(str);
			}
			in.close();
			String content = contentBuilder.toString();	
			message.setText(content, "utf-8", "html");
			message.setText("localhost:8080/iDriveTheSecond/forgot.jsp?token=" + getToken());
			/*	message.setText("Good Day, " + receiverName.toUpperCase() + " ! \n\n"
				+ employeeName + " has requested for a trip reservation with the following details: \n\n"
				+ "Trip Date: " + tripDate + "\n"
				+ "Departure Time: " + hours + ":" + minutes + " " + timeOfDay + "\n"
				+ "Destination: " + destination + "\n"
				+ "Number of Passengers: " + numPassengers + "\n\n"
				+ "The purpose of this trip is to as stated by " + employeeName + ": " + purpose + "\n" 
				+"\n\n<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
			*/
			Transport.send(message);	
			
			}catch(AddressException ae)
			{
				ae.printStackTrace();
			}catch (MessagingException mex) {
			mex.printStackTrace();
			}catch(SQLException sqle){
			System.out.println(sqle);
			}
			catch(IOException ioe)
			{
			System.out.println(ioe);  
			}
			
			}
}