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
<link
	href="${pageContext.request.contextPath}/resources/css/font-awesome.css"
	rel="stylesheet">	
 <!-- Is used for applying icon for browser tab -->
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
<title>EDUBank - Net Banking Home</title>
</head>
<body>
<jsp:include page="header.jsp" />
<div class="container-fluid">

		<%-- include header --%>
		
	
	<div class="row">
	<form action="../payByNetBanking" method="post">
	<div class="col-md-offset-1 col-md-5">
			<div class="col-md-12">
				<span class="label-heading">
					Balance
				</span>
				<br>
				<hr class="underLine">
			</div>
			
			<div class="col-md-12">
					<span class="balance-content"><i class="fa fa-usd"></i> ${balance}</span>
			</div>
		<br><br><br><br><br><br><br>
			<div class="col-md-12">
				<span class="label-heading">
					Amount to Pay
				</span>
				<br>
				<hr class="underLine">
				
			</div>
			
			<div class="col-md-12">
					<span class="balance-content"><i class="fa fa-usd"></i> ${amount}</span>
			</div>
		
		<br><br><br><br><br><br><br>
		<div class="col-md-12">
		<span class="label-heading">
				Remarks
			</span>
			<br>
			<hr class="underLine">
		</div>
		<div class="col-md-8">
			<input type="text" class="form-control col-md-8" name="remarks" id="remarks" placeholder="optional" autofocus>
		</div>
		
		<div class="col-md-offset-2 col-md-12">
			<c:if test="${balance < amount}">
			<br>
					<button disabled>Pay</button>
				</c:if>
				<c:if test="${balance >= amount}">
				<br>
					<button>Pay</button>
				</c:if>
				<button formaction="../netBankingPaymentCancled">Cancel</button>
		</div>
	</div>
		</form>
		
	
	</div>
	
 </div>
 </body>
</html>
