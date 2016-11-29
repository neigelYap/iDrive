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
<%@ page import="model.ReservationViewer" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head style="height:100%;">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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
<link rel="stylesheet" href="base.css" type="text/css">
<!-- <link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/ionicons.min.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/normalize.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/component.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/content.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/ekko-lightbox.min.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/dark.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/owl.carousel.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/animate.css" type="text/css">
<link rel="stylesheet" href="https://www.iacademy.edu.ph/assets/themes/version2/css/fractionslider.css" type="text/css">
<link rel="stylesheet" href="http://iacademy.edu.ph/assets/themes/version2/css/agency.css?update=2016-11-15%2009:04:26" type="text/css">-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script type="text/javascript">
function maxLimit(){
	var d = new Date();
	document.getElementById("yearMake").max = d.getFullYear();
}

</script>
<title>iDrive</title>
</head>
<body onload="maxLimit()">
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
<ul class="nav nav-tabs">
	<li class="active"><a data-toggle="tab" href="#profile">Profile</a></li>
    <li><a data-toggle="tab" href="#pending">Pending Reservations <span class="badge"><%=pending.size()%></span></a></li>
    <li><a data-toggle="tab" href="#departments">Departments</a></li>
    <li><a data-toggle="tab" href="#vehicles">Vehicles</a></li>
    <li><a data-toggle="tab" href="#csv">Accounts</a></li>
</ul>
<br>

<div class="tab-content">

	<div id="pending" class="tab-pane fade">
		<h5><b>PENDING</b></h5>
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
              	<textarea id="denialReason" class="form-control" name="denialReason" rows="3" placeholder="Write a short reason for denying the request" ></textarea>
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
	
	<div id="profile" class="tab-pane fade in active">
		<h5><b>PROFILE</b></h5>
		<hr style="border-color: #014FB3;">
		<div class="well">
			<%
				String first = (String)session.getAttribute("fName");
				String last = (String)session.getAttribute("lName");
				String fullName = first+" "+last;
				out.println("<h3>"+fullName+"</br>"+"<small>Administrator</small>"+"</h3>");
			%>
		</div>
	</div>
	
	<div id="departments" class="tab-pane fade">
		<h5><b>ADD NEW DEPARTMENT</b></h5>
		<hr style="border-color: #014FB3;">
		<%
			String error = (String)request.getAttribute("errorMsg");
			String success = (String)request.getAttribute("successMsg");
			String department = "0";
			String employeeId= "0";
			if (error != null) {
				department = request.getParameter("department");
				employeeId = request.getParameter("employeeId");
				request.setAttribute("department", department);
				request.setAttribute("employeeId", employeeId);
				out.println("<div class='alert alert-danger fade in'>"+error+"</div>"); 
	
			}else if(success!=null){
				out.println("<div class='alert alert-success fade in'>"+success+"</div>");
			}
		%>
		<form class="form-vertical" action="generatedepartment.html" method="post" enctype="multipart/form-data">
		
			<div class="form-group">
			<label for="department">Department Name</label>
					<input type="text" id="department" class="form-control" name="department" 
					<%if(department.equals("0")){ %>placeholder="Department Name" <%}
					else{ %>value="${department}" <% }%>/>
			</div>
			
			<div class="form-group">
			<label for="employeeId">Assign Division Head</label>
					<input type="text" id="employeeId" min="1" max="25" class="form-control" name="employeeId" 
					<%if(employeeId.equals("0")){ %>placeholder="Employee I.D." <%} 
					else{ %>value="${employeeId}" <% } %>/>
			</div>
			
			<div class="form-group">
			<label for="photo">Portrait Photo</label>
			<label class="btn btn-primary btn-file btn-block">
					UPLOAD AN IMAGE<input type="file" id="photo" name="photo" style="display: none;"/></label>
			</div>
			
			<div class="form-group">
				<button type="submit" class="btn btn-success">Submit</button>
			</div>	
		</form>
	</div>
	
	<div id="vehicles" class="tab-pane fade">
		<h5><b>ADD NEW VEHICLE</b></h5>
		<hr style="border-color: #014FB3;">
		<%
			String vehicleError = (String)request.getAttribute("vehicleError");
			String vehicleSuccess = (String)request.getAttribute("vehicleSuccess");
			String carManufacturer = "0";
			String yearMake = "0";
			String carModel = "0";
			String carColor = "0";
			String carPlate = "0";
			String carCapacity = "0";
			if (vehicleError != null) {
				
				carManufacturer = request.getParameter("carManufacturer");
				yearMake = request.getParameter("yearMake");
				carModel = request.getParameter("carModel");
				carColor = request.getParameter("carColor");
				carPlate = request.getParameter("carPlate");
				carCapacity = request.getParameter("carCapacity");
				request.setAttribute("carManufacturer", carManufacturer);
				request.setAttribute("yearMake", yearMake);
				request.setAttribute("carModel", carModel);
				request.setAttribute("carColor", carColor);
				request.setAttribute("carPlate", carPlate);
				request.setAttribute("carCapacity", carCapacity);
				out.println("<div class='alert alert-danger fade in'>"+vehicleError+"</div>"); 
	
			}else if(vehicleSuccess!=null){
				out.println("<div class='alert alert-success fade in'>"+vehicleSuccess+"</div>");
			}
		%>
		<form action="addvehicle.html" method="post" enctype="multipart/form-data">
		<div class="row">
			<div class="col-sm-6">
				<div class="form-group">
				<label for="carManufacturer">Manufacturer:</label> 
					<input type="text" id="carManufacturer" name="carManufacturer" required="required" class="form-control" maxlength="50" <%if(carManufacturer.equals("0")){ %>placeholder="Manufacturer Name"<%}else{ %>value="${carManufacturer}" <% } %>/>
				</div>
				<div class="form-group">
				<label for="yearMake">Year Manufactured:</label>
					<input type="number" id="yearMake" name="yearMake" min="2000"class="form-control" required="required" <%if(yearMake.equals("0")){ %>placeholder="Year Manufactured"<%}else{ %>value="${yearMake}" <% } %>/>
				</div>
				<div class="form-group">
				<label for="carModel">Vehicle Model:</label>
					<input type="text" id="carModel" name="carModel" required="required" class="form-control"maxlength="50"<%if(carModel.equals("0")){ %>placeholder="Model"<%}else{ %>value="${carModel}" <% } %>/>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
				<label for="carColor">Vehicle Color:</label>
					<input type="text" id="carColor" name="carColor"class="form-control" required="required"<%if(carColor.equals("0")){ %>placeholder="Color"<%}else{ %>value="${carColor}" <% } %>/>
				</div>
				<div class="form-group">
				<label for="carPlate">Vehicle Plate Number:</label>
					<input type="text" id="carPlate" name="carPlate" required="required" class="form-control"maxlength="7"<%if(carPlate.equals("0")){ %>placeholder="Plate No. (ABC1234)"<%}else{ %>value="${carPlate}" <% } %>/>
				</div>
				<div class="form-group">
				<label for="carCapacity">Vehicle Max Capacity:</label>
					<input type="number" id="carCapacity" name="carCapacity" min="1" max="25"class="form-control"required="required"<%if(carCapacity.equals("0")){ %>placeholder="Maximum capacity is 25"<%}else{ %>value="${carCapacity}" <% } %>/>
				</div>
			</div>
		</div>
				<div class="form-group">
				<label for="carImage">Vehicle Image:</label>
					<label class="btn btn-primary btn-file btn-block">
					UPLOAD AN IMAGE<input type="file" id="carImage" name="carImage" class="form-control"accepts="image/*"style="display: none;"/></label>
				</div>
		<div class="form-group">				
			<button type="submit" class="btn btn-success">Submit</button>
		</div>
		</form>
	</div>
	
	<div id="csv" class="tab-pane fade">
		<h5><b>IMPORT CSV ACCOUNTS</b></h5>
		<hr style="border-color: #014FB3;">
		<form action = "import.html" method="post" enctype="multipart/form-data">
			<div class="form-group">
				<label class="btn btn-primary btn-file btn-block">
				UPLOAD A CSV FILE<input type ="file" name="csv" accept=".csv" required="required"style="display: none;"></label>
			</div>
			<div class="form-group">
				<button type ="submit" class="btn btn-success">Import</button>
			</div>
		</form>
	</div>
</div>

</div>
</body>
</html>
<%
	}
%>