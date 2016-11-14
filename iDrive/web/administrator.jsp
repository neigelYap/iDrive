<%
	if (session.getAttribute("account")==null || session.getAttribute("pass")==null || session.getAttribute("type")==null) {
		
		request.setAttribute("invalid", "Please login your account");
		request.getRequestDispatcher("login").forward(request,response);

	}else{
		Integer account = (Integer) session.getAttribute("type");
		if(account!=3){
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
		<form action = "import.html" method="post" enctype="multipart/form-data">
		<div class="form-group"> 
			<input type ="file" name="csv" accept=".csv" required="required">
			<button type ="submit" class="btn btn-primary">Import</button>
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