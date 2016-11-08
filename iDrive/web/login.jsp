<%
	if (session.getAttribute("account")!=null || session.getAttribute("pass")!=null) {
		
		int type = (Integer) session.getAttribute("type");
		if(type == 1){
			response.sendRedirect("employe");
		}else if(type == 2){
			response.sendRedirect("manager");
		}else if(type == 3){
			response.sendRedirect("administrator");
		}
	}
%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="base.css" type="text/css">
<title>iDrive</title>
</head>
<body>
<div class="navbar navbar-default navbar-static-top">
	<div class="container">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand">iDrive <span class="glyphicon glyphicon-road"></span></a>
			</div>
		</div>
	</div>
</div>
<div class="container">
<div class="jumbotron"><h1>iDrive</h1></div>
<div class="row">
  <div class="col-sm-4"></div>
  <div class="col-sm-4"></div>
  <div class="col-sm-4"></div>
</div>
<div class="row">
  <div class="col-md-4"></div>
  <div class="col-md-4">
<div class="well">
	<%
		String error = (String)request.getAttribute("invalid");
		if (error != null) {

			out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 

		}
	%>
	<form action="accountchecker.html" method="post">
	<div class="form-group">
    	<label for="username">Username:</label>
	    <input type="text" class="form-control" name="username" id="username" placeholder="Employee I.D." required="required" >
  	</div>
  	
  	<div class="form-group">
    	<label for="password">Password:</label>
	    <input type="password" class="form-control" name="password" id="password" placeholder="Password"required="required">
  	</div>

  	<div class="form-group"> 
	    <center><button type="submit" class="btn btn-primary btn-block">Login</button></center>
  	</div>
	</form>
</div>
</div>
  <div class="col-md-4"></div>
</div>
</div>
</body>
</html>