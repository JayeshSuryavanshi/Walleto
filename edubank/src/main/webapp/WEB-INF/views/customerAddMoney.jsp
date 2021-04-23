<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDate"%>
<%@page import="com.edubank.model.Customer"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.css"
	rel="stylesheet">

<link
	href="${pageContext.request.contextPath}/resources/css/eduBank.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/css/font-awesome.css"
	rel="stylesheet">

<!-- Is used for applying icon for browser tab -->
<link rel="icon" type="image/x-icon"
	href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
<title>Teller Home</title>
</head>
<body>
	<%-- include header --%>
	<jsp:include page="tellerheader.jsp" />


	<div class="row">
		<div class=" col-md-offset-1 col-md-11">

			<div class="col-md-4">
				<a href="tellerHome"> 
					<span class="label-heading"> Create	Account </span>
				</a>
			</div>

			<div class="col-md-4">
				<span class="label-heading">Add Money</span>
				<hr class="underLine">
				<br>
				<div class="col-md-12">

					<br>
					<form action="findAccount" method="post">
						<input type="text" id="accountNumber" name="accountNumber"
							placeholder="Enter Account Number*" value="${ accountNumber}"
							required="required" />&nbsp;&nbsp; 
						<input type="hidden"
							name="indexFrom" value="${paginationBeginIndex}"> 
						<input
							type="hidden" name="indexTo" value="${paginationEndIndex}">
						<button>Search</button>
					</form>
				</div>

				<div class="col-md-12">
					<c:if test="${accountHolderName ne null}">
						<span class="label-heading-small"> Account holder name: </span>
						<br>
						<span class="label-heading-medium"> ${accountHolderName} </span>
					</c:if>
				</div>
				<div class="col-md-12">
					<c:if test="${accountHolderName ne null}">
						<form action="addMoney" method="post">
							<br> <input type="text" name="accountNumber"
								value="${accountNumber}" hidden="true"> <br> <br>
							<input type="number" step="0.01" name="amount"
								placeholder="Enter Amount*">
							<input type="hidden" name="indexFrom"
								value="${paginationBeginIndex}"> 
							<input type="hidden"
								name="indexTo" value="${paginationEndIndex}">
							<button type="submit" formaction="addMoney" formmethod="post">Add
								Money</button>
						</form>
					</c:if>
				</div>
				<div class="col-md-12 text-center">
					<div class="text-center text-danger">${addMoneyErrorMessage}</div>
				</div>
				<div class="col-md-12 text-center">
					<div class="text-center text-success">${addMoneyMessage}</div>
				</div>

			</div>
			<div class="col-md-4">
				<a href="customerDetails"> 
					<span class="label-heading">My Customers</span>
				</a>
			</div>
		</div>
	</div>
</body>
</html>
