<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.css">

<link href="${pageContext.request.contextPath}/resources/css/eduBank.css" rel="stylesheet">
 
 <!-- Is used for applying icon for browser tab -->
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
<title>EDUBank - Login</title>
</head>
<body>
<jsp:include page="header.jsp" />
<br><br><br>
<div class="container" >

<div class="row">	
	<div class="col-md-offset-1 col-md-4 col-sm-offset-1 col-sm-5 col-xs-offset-1 col-xs-7">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<span class="label-heading">CUSTOMER LOGIN</span>
			<br>
			<hr class="underLine col-md-10 col-sm-12 col-xs-12">
		</div>
		
		<br>
		
		<div class="col-md-12 col-sm-12 col-xs-12">	
			<!-- This is the form in which loginName and password will be entered.
			once the form is submitted using submit button it will search for a method in 
			controller package with mapping name authenticateCustomer and will call the same 
			
			You can check for the method authenticateCustomer in CustomerLoginController class in 
			com.edubank.controller package -->
			<form  class="form-horizontal" action="authenticateCustomer" method="post">
				<!-- Input tag is used for displaying a textbox on the browser, here it is been used for entering the loginName -->
				<input type="text" name="loginName" id="userId" placeholder="Login Name" required value="${loginName}">
		        <br><br>
		        <!-- This input tag is been used for entering password -->
		        <input type="password" name="password" id="password" placeholder="Password" required value="${password}">        
				<br>
				<!-- This is a link, when clicked, the page will redirect to forgot password page -->
				<h6>
					<a href="forgotPassword" >Forgot password ?</a>
				</h6>
				<br><br>
				<!-- This is submit button used to submit the data entered in a form -->
				<button type="submit"><i class="fa fa-sign-in" aria-hidden="true"></i> Login</button>
				
			</form>
			<div class="text-center text-danger">${message}</div>
			
		</div>
		
	</div>

</div>	
         <div class="text-center">
			<c:if test="${successMessage ne null}">
				<div class="alert alert-success col-md-6 col-md-offset-3">${successMessage}</div>
			</c:if>
		
		</div>
</div>
    </body>
</html>