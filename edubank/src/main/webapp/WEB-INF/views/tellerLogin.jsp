<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/eduBank.css">

 <!-- Is used for applying icon for browser tab -->
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
<title>EDUBank - Login</title>
</head>
<body>
      

<jsp:include page="header.jsp" />
<br><br>
<div class="row">	
	<div class="col-md-3 col-md-offset-1">
		<div class="col-md-12">
				<span class="label-heading">Teller Login </span>
				<br>
				<hr class="underLine">
		</div>
			
		<br>
		
		
		<div class="col-md-12">
				<form class="form-horizontal" action="authenticateTeller" method="post">
				<input type="text"  name="loginName" id="tellerId" placeholder="Teller Login Name" required value="${loginName}">
        		<br><br>
        		<input type="password" name="password" id="password" placeholder="Password" required value="${password}">
        

		
				<br><br>
				<button type="submit"><i class="fa fa-sign-in"></i> Login</button>
		
				</form>
		</div>
		
		<div class="text-center text-danger">${message}</div>
		
	</div>


</div>

<div class="text-center">
<c:if test="${successMessage ne null}">
				<div class="alert alert-success col-md-6 col-md-offset-3">${successMessage}</div>
		</c:if>
		
</div>
    </body>
</html>