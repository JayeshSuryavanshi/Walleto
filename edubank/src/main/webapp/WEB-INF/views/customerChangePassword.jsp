<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">

<link href="${pageContext.request.contextPath}/resources/css/eduBank.css" rel="stylesheet" >
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.css">

 <!-- Is used for applying icon for browser tab -->
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
<title>EDUBank - Change Password</title>
</head>
<body>
<div class="container-fluid">

		<%-- include header --%>
		<jsp:include page="header.jsp" />
		
		
		<div class="row">
		
			<div class="col-md-offset-1 col-md-5 col-sm-offset-1 col-sm-5 col-xs-6">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<span class="label-heading">
						BALANCE
					</span>
					<br>
					<hr class="underLine"></hr>
				</div>
				
				<div class="col-md-12 col-sm-12 col-xs-12">
				
					<span class="balance-content"><i class="fa fa-usd"></i> ${balance}</span>
					
				</div>
			<br>
			<br>
			</div>
			<div class="col-md-12 col-sm-12 col-xs-12 text-right customer-profile">
				<div class="dropdown">
				  <span class="label-heading">${name} &#9662</span>
				  <div class="dropdown-content">
				    <p><a href="customerChangePassword">Change Password</a></p>
				    <p><a href="changeDebitCardPin">Change Pin</a></p>
				    <p><a href='logout'>Logout</a></p>
				  </div>
				</div>
			</div>
		
		</div>
		<br><br>
		<div class="row" >

		<div class="col-md-offset-1 col-md-11 col-sm-offset-1 col-sm-11 col-xs-12">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<span class="label-heading">
						Change Password
				</span>	
					<br>
				<hr class="underLine">
				
			</div>
			<div class="col-md-12 col-sm-12 col-xs-12">
			<form class="form-horizontal" action="customerChangePassword" method="post">
  
                    <input type="password" name="password" id="password" placeholder="Current Password" required value="${password}">
      				<br>
      				<br>
			
                    <input type="password"  name="newPassword" id="newPassword" placeholder="New Password" required value="${newPassword}">
          			<br>
     				<br>
                    <input type="password" name="confirmNewPassword" id="confirmNewPassword" placeholder="Confirm New Password" required value="${confirmNewPassword}">
					<br>
					<br>
				<button type="submit"><i class="fa fa-sign-in" aria-hidden="true"></i> Submit</button>
  
                      
               <a href="customerHome">Cancel</a> 
                <br>
       
            </form>
            </div>
        </div>
		</div>
		<c:if test="${successMessage ne null}">
				<div class="alert alert-success col-md-offset-3 col-md-6 col-sm-offset-1 col-sm-10 col-xs-12" align="center">${successMessage}</div>
		</c:if>
		<c:if test="${errorMessage ne null}">
				<div class="alert alert-danger col-md-offset-3 col-md-6 col-sm-offset-1 col-sm-10 col-xs-12" align="center">${errorMessage}</div>
		</c:if>
			
			
			
			
	
</div>
		
    </body>
</html>