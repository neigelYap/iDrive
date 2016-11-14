<%
	if (session.getAttribute("account")==null || session.getAttribute("pass")==null || session.getAttribute("type")==null) {
		
		request.setAttribute("invalid", "Please login your account");
		request.getRequestDispatcher("login").forward(request,response);

	}else{
		Integer account = (Integer) session.getAttribute("type");
		if(account!=1){
			request.setAttribute("invalid", "Unauthorized access");
			request.getRequestDispatcher("login").forward(request,response);
		}
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	    response.setDateHeader("Expires", 0);
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="base2.css" type="text/css">
<title>iDrive</title>
</head>
<body>
<div class="navbar navbar-default navbar-static-top">
<div class="container">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">
			iDrive
			</a>
		</div>
	</div>
	</div>
</div>
<div class="container">
<div class="row">
  <div class="col-sm-6">
	<div class="well">
	<legend>Trip Ticket Form</legend>
	<%
		String error = (String)request.getAttribute("errorMsg");
		String success = (String)request.getAttribute("successMsg");
		if (error != null) {

			out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 

		}else if(success!=null){
			out.println("<div class='alert alert-success fade in'>"+success+"</div>");
		}
	%>
	<form class="form-vertical" action="sendreservation.html" method="post">
		<div class="form-group">
			<label for="tripDate">Trip Date:</label>
				<input type="date" id="tripDate" class="form-control" name="tripDate" required="required"/>
		</div>
		<div class="form-group">
		<label for="hours">Departure:</label></div>
		<div class="form-group">
		<div class="input-group">
			<select name="hours" id="hours" class="form-control"required="required">
				<option value="01">01</option>
				<option value="02">02</option>
				<option value="03">03</option>
				<option value="04">04</option>
				<option value="05">05</option>
				<option value="06">06</option>
				<option value="07">07</option>
				<option value="08">08</option>
				<option value="09">09</option>
				<option value="10">10</option>
				<option value="11">11</option>
				<option value="12">12</option>
			</select>
			<span class="input-group-addon">:</span>
			
			<select name="minutes" id="minutes" class="form-control" required="required">
				<option value="00">00</option>
				<option value="05">05</option>
				<option value="10">10</option>
				<option value="15">15</option>
				<option value="20">20</option>
				<option value="25">25</option>
				<option value="30">30</option>
				<option value="35">35</option>
				<option value="40">40</option>
				<option value="45">45</option>
				<option value="50">50</option>
				<option value="55">55</option>
			</select>
			<span class="input-group-addon"> </span>
			<select name="time" id="time" class="form-control" required="required">
				<option value="AM">AM</option>
				<option value="PM">PM</option>
			</select></div></div>
		<div class="form-group">
		<label for="destination">Destination:</label>
				<input type="text" id="destination" maxlength="50" class="form-control" name="destination" required="required" placeholder="Destination of travel"/>
		</div>
		<div class="form-group">
		<label for="travelPurpose">Purpose of Travel:</label>
				<textarea id="travelPurpose" class="form-control" name="travelPurpose" required = "required" rows="3" placeholder="Write a short reason for your reservation"></textarea>
		</div>
		<div class="form-group">
		<label for="passengers">Passengers (Optional):</label>
				<input type="text" id="passengers" class="form-control" name="passengers" placeholder="(e.g., John Doe, Jane Doe, etc.)"/>
		</div>
		<div class="form-group">
		<label for="numPassengers">Number of Passengers:</label>
				<input type="number" id="numPassengers" min="1" max="25" class="form-control" name="numPassengers" required="required"placeholder="Total number of included passengers"/>
		</div>
		<div class="form-group">
			<button type="submit" class="btn btn-success">Submit</button>
			<button type="reset" class="btn btn-primary">Reset</button>	
		</div>	
	</form>

	</div>
	</div>
	<div class="col-sm-6">
	<div class="well">
		<%
			String first = (String)session.getAttribute("fName");
			String last = (String)session.getAttribute("lName");
			String fullName = first+" "+last;
			out.println("<h3>"+fullName+"</h3>");
		%>
				<form action="logoutservlet.html" method="post">
			<div class="form-group"> 
		    	<button type="submit" class="btn btn-primary">Logout</button>
	  		</div>
		</form>
	</div>
	</div>
</div>
</div>
</body>
</html>
<%
	}
%>