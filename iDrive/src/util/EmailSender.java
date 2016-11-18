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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.PasswordAuthentication;

public class EmailSender {
	private String reason;
	private Session session;
	private String sender = "idrive.bot@gmail.com";
	private String password = "harambelivesonforever52816";
	
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
	public void reservationSent(Connection connection, int accType, int department){
		String receiver="";
		int receiverAccType;
		if(accType == 1){
			receiverAccType = 2;
		}else{
			receiverAccType = 3;
		}
		
		googleMailSession();
		try{
			if(receiverAccType==2){
				String query = "SELECT * FROM employee WHERE departmentName = ? AND accountTypeID =  ?";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt( 1, department);
				pstmt.setInt( 2, receiverAccType); 
				ResultSet rs =pstmt.executeQuery();
				if(rs.next()){
					receiver = rs.getString("email");
				}else{
					
				}
			}else if(receiverAccType==3){
				String query = "SELECT * FROM employee WHERE accountTypeID = ? AND departmentName = ?";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt( 1, receiverAccType);
				pstmt.setInt( 2, 1);
				ResultSet rs =pstmt.executeQuery();
				if(rs.next()){
					receiver = rs.getString("email");
				}else{
					
				}
			}
	         MimeMessage message = new MimeMessage(session);

	         message.setFrom(new InternetAddress(sender));
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
	         message.setSubject("New Reservation Request");
	         message.setText("Good Day! There is a new request for a Trip Reservation. Please login to your account "
	         		+ "to validate this pending request.\n\n\n"
	         		+"<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
	         Transport.send(message);
	         
	     }catch (MessagingException mex) {
	    	 mex.printStackTrace();
	     }catch(SQLException sqle){
	    	 System.out.println(sqle);
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
	         		+ "It is now currently pending for approval by the Administrator.");
	         message.setText("<PLEASE DO NOT REPLY, THIS IS AN AUTO-GENERATED EMAIL>");
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
}
