<%@page import="java.sql.Date"%>
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
<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head style="height:100%;">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/ionicons.min.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/normalize.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/component.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/content.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/ekko-lightbox.min.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/dark.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/owl.carousel.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/animate.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/fractionslider.css" type="text/css">
<link rel="stylesheet" href="http://iacademy.edu.ph/assets/themes/version2/css/agency.css?update=2016-11-15%2009:04:26" type="text/css">

<title>iDrive</title>
</head>
<body style="background: rgb(237, 238, 238);">
<div class="navbar navbar-default navbar-static-top"style="background:#014FB3;">
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
			<li><a href="logoutservlet.html">Logout</a></li>
		</ul>
		</div>
	</div>
	</div>
</div>
<div class="container inner-page">
<!-- <img src="logo.png" alt="iDrive" style="width:250px;margin-bottom: 15px;margin-top: -30px;"> -->
<div class="row">
  <div class="col-sm-6">
  <%
  	Date date =  Date.parse(request.getParameter("tripDate"));
  	int resID = (int) request.getAttribute("reservationID");
  	String departure = (String) request.getAttribute("departure");
  	String destination = (String) request.getAttribute("destination");
  	String purpose = (String) request.getAttribute("purpose");
  	String passengers = (String) request.getAttribute("passengers");
  	int passengerNum = (int) request.getAttribute("passengerNum");
  %>
	<legend><b>TRIP TICKET FORM</b></legend>
	<div class="form-group">
		<label for="tripDate">Trip Date:</label>
		<p><%out.print(departure);%></p>
	</div>
	</div>
	<div class="col-sm-6">
	<div class="well">
		<%
			String first = (String)session.getAttribute("fName");
			String last = (String)session.getAttribute("lName");
			String fullName = first+" "+last;
			out.println("<h3>"+fullName+"</br>"+"<small>Division Head</small>"+"</h3>");
		%>
	</div>
	</div>
</div>
</div>
<footer>
        <div class="container">
            <div class="row">
                
            </div>
            <div style="width:150px;margin:0 auto;position: relative;margin-bottom: -13rem;" class="hidden-xs">
                <a href="http://iacademy.edu.ph/"><img class="img-responsive" src="http://iacademy.edu.ph/assets/themes/version2/images/seal-small.png"></a>
            </div>
            <hr>
            <div class="row">
                <div class="col-md-4" style="text-align:left;">
                    <span class="copyright">Copyright © iACADEMY 2014</span>
                </div>
                
            </div>
        </div>
    </footer>
    <div class="address-bottom" style="font-family: inherit;">
        <b>iACADEMY Plaza</b><br> 324 Senator Gil J. Puyat Ave. <br>Bel-air,
        Makati City Philippines 1209
    </div>
</body>
</html>
<%
	}
%>