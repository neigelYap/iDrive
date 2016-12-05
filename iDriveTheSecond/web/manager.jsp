<%
	
	if (session.getAttribute("account")==null || session.getAttribute("pass")==null || session.getAttribute("type")==null) {
		
		request.setAttribute("invalid", "Please login your account");
		request.getRequestDispatcher("login").forward(request,response);

	}else{
		Integer account = (Integer) session.getAttribute("type");
		if(account!=2){
			request.setAttribute("invalid", "Unauthorized access");
			request.getRequestDispatcher("login").forward(request,response);
		}
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setDateHeader("Expires", 0);
%>
<%@ page import="model.ReservationViewer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head style="height:100%;">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="base.css" rel="stylesheet" type="text/css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> 
<style>
  .modal-header, h4, .close {
      background-color: #014FB3;
      color:white !important;
      text-align: center;
      font-size: 30px;
  }
  .modal-footer {
      background-color: #f9f9f9;
  }
</style>
<title>iDrive</title>
</head>
<body>
<div class="navbar navbar-default navbar-static-top">
<div class="container">
	<div class="container-fluid">
		<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span> 
      </button>
			<a class="navbar-brand"><img style="width: 36px;margin-top: -10px;margin-left: -15px" src="crest.png"><img src="logo-white.png" alt="iDrive" style="width:130px;margin-bottom: 15px;margin-top: -40px;margin-left: 30px;"></a>
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
		
		<ul class="nav navbar-nav navbar-right">
			<li><a href="logoutservlet.html">LOGOUT</a></li>
		</ul>
		</div>
	</div>
	</div>
</div>
<%
	List<ReservationViewer> pending = (List<ReservationViewer>) session.getAttribute("pending");
%>
<div class="container">
<!-- <img src="logo.png" alt="iDrive" style="width:250px;margin-bottom: 15px;margin-top: -30px;"> -->
<ul class="nav nav-tabs">
	<li class="active"><a data-toggle="tab" href="#profile">Profile</a></li>
    <li ><a data-toggle="tab" href="#pending">Pending Reservations <span class="badge"><%=pending.size()%></span></a></li>
    <li><a data-toggle="tab" href="#trip">Reserve a Trip</a></li>
    <li><a data-toggle="tab" href="#reservations">Reservations</a></li>
</ul>
<br>

<div class="tab-content">
	<div id="trip" class="tab-pane fade">
		<h5><b>TRIP TICKET FORM</b></h5>
		<hr style="border-color: #014FB3;">
		<%
			String error = (String)request.getAttribute("errorMsg");
			String success = (String)request.getAttribute("successMsg");
			String tripDate = "0";
			String hours = "0";
			String minutes = "0";
			String time = "0";
			String destination = "0";
			String travelPurpose = "0";
			String passengers = "0";
			String numPassengers= "0";
			if (error != null) {
				tripDate = request.getParameter("tripDate");
				hours = request.getParameter("hours");
				minutes = request.getParameter("minutes");
				time = request.getParameter("time");
				destination = request.getParameter("destination");
				travelPurpose = request.getParameter("travelPurpose");
				passengers = request.getParameter("passengers");
				numPassengers = request.getParameter("numPassengers");
				request.setAttribute("tripDate", tripDate);
				request.setAttribute("destination", destination);
				request.setAttribute("travelPurpose", travelPurpose);
				request.setAttribute("passengers", passengers);
				request.setAttribute("numPassengers", numPassengers);
				out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 
		
			}else if(success!=null){
				out.println("<div class='alert alert-success fade in'>"+success+"</div>");
			}
		%>
		<form class="form-vertical" action="sendreservation.html" method="post">
			<div class="form-group">
				<label for="tripDate">Trip Date:</label>
					<input type="date" id="tripDate" class="form-control" name="tripDate" required="required"<% if(!tripDate.equals("0")){ %> value="${tripDate}" <% } %>/>
			</div>
			<div class="form-group">
			<label for="hours">Departure:</label></div>
			<div class="form-group">
			<div class="input-group">
				<select name="hours" id="hours" class="form-control"required="required">
					<option value="01" <% if(hours.equals("01")){%> selected="selected" <% } %>>01</option>
					<option value="02" <% if(hours.equals("02")){%> selected="selected" <% } %>>02</option>
					<option value="03" <% if(hours.equals("03")){%> selected="selected" <% } %>>03</option>
					<option value="04" <% if(hours.equals("04")){%> selected="selected" <% } %>>04</option>
					<option value="05" <% if(hours.equals("05")){%> selected="selected" <% } %>>05</option>
					<option value="06" <% if(hours.equals("06")){%> selected="selected" <% } %>>06</option>
					<option value="07" <% if(hours.equals("07")){%> selected="selected" <% } %>>07</option>
					<option value="08" <% if(hours.equals("08")){%> selected="selected" <% } %>>08</option>
					<option value="09" <% if(hours.equals("09")){%> selected="selected" <% } %>>09</option>
					<option value="10" <% if(hours.equals("10")){%> selected="selected" <% } %>>10</option>
					<option value="11" <% if(hours.equals("11")){%> selected="selected" <% } %>>11</option>
					<option value="12" <% if(hours.equals("12")){%> selected="selected" <% } %>>12</option>
				</select>
				<span class="input-group-addon">:</span>
				
				<select name="minutes" id="minutes" class="form-control" required="required">
					<option value="00" <% if(minutes.equals("00")){%> selected="selected" <% } %>>00</option>
					<option value="05" <% if(minutes.equals("05")){%> selected="selected" <% } %>>05</option>
					<option value="10" <% if(minutes.equals("10")){%> selected="selected" <% } %>>10</option>
					<option value="15" <% if(minutes.equals("15")){%> selected="selected" <% } %>>15</option>
					<option value="20" <% if(minutes.equals("20")){%> selected="selected" <% } %>>20</option>
					<option value="25" <% if(minutes.equals("25")){%> selected="selected" <% } %>>25</option>
					<option value="30" <% if(minutes.equals("30")){%> selected="selected" <% } %>>30</option>
					<option value="35" <% if(minutes.equals("35")){%> selected="selected" <% } %>>35</option>
					<option value="40" <% if(minutes.equals("40")){%> selected="selected" <% } %>>40</option>
					<option value="45" <% if(minutes.equals("45")){%> selected="selected" <% } %>>45</option>
					<option value="50" <% if(minutes.equals("50")){%> selected="selected" <% } %>>50</option>
					<option value="55" <% if(minutes.equals("55")){%> selected="selected" <% } %>>55</option>
				</select>
				<span class="input-group-addon"> </span>
				<select name="time" id="time" class="form-control" required="required">
					<option value="AM" <% if(time.equals("AM")){%> selected="selected" <% } %>>AM</option>
					<option value="PM" <% if(time.equals("PM")){%> selected="selected" <% } %>>PM</option>
				</select></div></div>
			<div class="form-group">
			<label for="destination">Destination:</label>
					<input type="text" id="destination" maxlength="50" class="form-control" name="destination" required="required"
					<%if(destination.equals("0")){ %>placeholder="Destination of travel" <%} 
					else{ %> value="${destination}" <%} %>/>
			</div>
			<div class="form-group">
			<label for="travelPurpose">Purpose of Travel:</label>
					<textarea id="travelPurpose" class="form-control" name="travelPurpose" required = "required" rows="3" <%if(travelPurpose.equals("0")){ %>placeholder="Write a short reason for your reservation"<%}%>><%if(!travelPurpose.equals("0")){%>${travelPurpose}<%} %></textarea>
			</div>
			<div class="form-group">
			<label for="passengers">Passengers (Optional):</label>
					<input type="text" id="passengers" class="form-control" name="passengers" 
					<%if(passengers.equals("0")){ %>placeholder="(e.g., John Doe, Jane Doe, etc.)" <%}
					else{ %>value="${passengers}" <% }%>/>
			</div>
			<div class="form-group">
			<label for="numPassengers">Number of Passengers:</label>
					<input type="number" id="numPassengers" min="1" max="25" class="form-control" name="numPassengers" 
					<%if(numPassengers.equals("0")){ %>placeholder="Total number of included passengers" <%} 
					else{ %>value="${numPassengers}" <% } %>/>
			</div>
			<div class="form-group">
				<button type="submit" class="btn btn-success" >
					Submit
				</button>
			</div>		
		</form>
    </div>
    <div id="profile" class="tab-pane fade in active">
		<h5><b>PROFILE</b></h5>
		<hr style="border-color: #014FB3;">
		<div class="well">
			<%
				String first = (String)session.getAttribute("fName");
				String last = (String)session.getAttribute("lName");
				String fullName = first+" "+last;
				out.println("<h3>"+fullName+"</br>"+"<small>Division Head</small>"+"</h3>");
			%>
		</div>
    </div>
    <div id="pending" class="tab-pane fade">
		<h5><b>PENDING RESERVATIONS</b></h5>
		<hr style="border-color: #014FB3;">
		<div class="table-responsive">
	<table class="table table-hover">
		<thead style="background: #014FB3;">
		<tr>
			<th><center><font color="white">#</font></center></th>
			<th><center><font color="white">REQUESTER</font></center></th>
			<th><center><font color="white">DATE</font></center></th>
			<th><center><font color="white">DEPARTURE</font></center></th>
			<th><center><font color="white">DESTINATION</font></center></th>
			<th></th>
		</tr>
		</thead>
	<tbody>
    <% 
		
		for(int ctr = 0; ctr < pending.size(); ctr++){
	%>
	
      <tr>
        <td align="center"><%=pending.get(ctr).getReservationID() %></td>
        <td align="center"><%=pending.get(ctr).getName() %></td>
        <td align="center"><%=pending.get(ctr).getTripDate() %></td>
        <td align="center"><%=pending.get(ctr).getDeparture() %></td>
        <td align="center"><%=pending.get(ctr).getDestination() %></td>
        <td align="center">
			<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#viewModal<%=pending.get(ctr).getReservationID() %>">View</button>
			</td>
      </tr>
      
  <div class="modal fade" id="viewModal<%=pending.get(ctr).getReservationID() %>" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header" style="padding:15px 50px;">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4>Reservation # <%=pending.get(ctr).getReservationID() %></h4>
        </div>
        <div class="modal-body" style="padding:40px 50px;">
          <form role="form-vertical" action="viewreservationservlet.html" method="post">
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
	              		<label><b>REQUESTER:</b></label>
	              		<p><%=pending.get(ctr).getName()%></p>
	            	</div>
					<div class="form-group">
						<label><b>DEPARTURE TIME:</b></label>
						<p><%=pending.get(ctr).getDeparture()%></p>
					</div>
            	</div>
				<div class="col-sm-6">
					<div class="form-group">
						<label><b>TRIP DATE:</b></label>
						<p><%=pending.get(ctr).getTripDate()%></p>
	           		</div>
					<div class="form-group">
						<label><b>DESTINATION:</b></label>
						<p><%=pending.get(ctr).getDestination()%></p>
	           		</div>
           		</div>
           		
			</div>

            <div class="form-group">
              <label><b>PURPOSE OF TRAVEL:</b></label>
              <p><%=pending.get(ctr).getPurpose()%></p>
            </div>
            
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
	              		<label><b>PASSENGERS INCLUDED:</b></label>
	              		<p><%=pending.get(ctr).getPassengers()%></p>
	            	</div>
            	</div>
				<div class="col-sm-6">
					<div class="form-group">
						<label><b>TOTAL PASSENGERS:</b></label>
						<p><%=pending.get(ctr).getPassengerNum()%></p>
	           		</div>
           		</div>
           	<div class="form-group">
              <label><b>REASON FOR DENIAL:</b></label>
              	<textarea id="denialReason" class="form-control" name="denialReason" rows="3" placeholder="Write a short reason for denying the request"></textarea>
            </div>
			</div>
              <button type="submit" name ="approved" id="approved" value="<%=pending.get(ctr).getReservationID() %>"class="btn btn-success btn-block" style="border-color: #52a55f;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #52a55f;border-radius: 0;">Approve</button>
              <button type="submit" name ="denied" id="denied" value="<%=pending.get(ctr).getReservationID() %>"class="btn btn-danger btn-block" style="border-color: #c44a4a;text-transform: uppercase;font-weight: 700;color: #fff !important;background-color: #c44a4a;border-radius: 0;">Deny</button>
          </form>
        </div>
      </div>
      
    </div>
  </div>
    <%
      }
    %>
    </tbody>
  </table>
  </div>
    </div>
	<div id="reservations" class="tab-pane fade">
		<h5><b>PENDING RESERVATIONS</b></h5>
		<hr style="border-color: #014FB3;">
	    <% 
	    List<ReservationViewer> approved = (List<ReservationViewer>) session.getAttribute("approved");
	    if(approved.isEmpty()){
	    %>
			<center><h5>No existing reservations</h5></center>
	    <%	
	    }else{
			for(int ctr = 0; ctr < approved.size(); ctr++){
	    %>
	    <table class="table table-hover">
			<thead style="background: #014FB3;">
		      	<tr>
		        <th><center><font color="white">#</font></center></th>
		        <th><center><font color="white">Date</font></center></th>
		        <th><center><font color="white">Departure</font></center></th>
		        <th><center><font color="white">Destination</font></center></th>
		        <th><center><font color="white">Tracker</font></center></th>
		        <th></th>
		      	</tr>
			</thead>
	    <tbody>
		    <tr>
		        <td align="center"><%=approved.get(ctr).getReservationIDEmp() %></td>
		        <td align="center"><%=approved.get(ctr).getTripDateEmp() %></td>
		        <td align="center"><%=approved.get(ctr).getDepartureEmp() %></td>
		        <td align="center"><%=approved.get(ctr).getDestinationEmp() %></td>
		        <td align="center"><%=approved.get(ctr).getTrackWord() %></td>
		        <td align="center">
					<!-- <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#viewModal<%=approved.get(ctr).getReservationIDEmp() %>">View</button> -->
					</td>
			</tr>
	    <%
			}
	    }
	    %>
		</table>
    </div>
  </div>

<footer>

            <div style="width:150px;margin:0 auto;position: relative;margin-bottom: -13rem;" class="hidden-xs">
                <a href="http://iacademy.edu.ph/"><img class="img-responsive" src="http://iacademy.edu.ph/assets/themes/version2/images/seal-small.png"></a>
            </div>
            <hr style="border-color: #00000;">
            <div class="row">
                <div class="col-md-4" style="text-align:left;">
                    <span class="copyright">Copyright © iACADEMY 2014</span>
                </div>
                
            </div>
    </footer>
</div>
</body>
</html>
<%
	}
%>