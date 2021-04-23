<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="com.edubank.model.Transaction"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.css"
	rel="stylesheet">

<link href="${pageContext.request.contextPath}/resources/css/eduBank.css" rel="stylesheet">	
	
 <!-- Is used for applying icon for browser tab -->
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
<title>Error Page</title>
</head>
<body>
<jsp:include page="header.jsp" />

<div class="text-center">
<span class="label-heading-medium">
	Oops....!!!
</span>
<br>
<span class="label-heading-small">
	${message}
</span>
<br>
<span class="label-heading-small">	
		 Please <a href="/EDUBank/">login</a>
</span>	

</div>
</body>
</html>