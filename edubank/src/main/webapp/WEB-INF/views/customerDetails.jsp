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
				<a href="tellerHome"> <span class="label-heading"> Create
						Account </span>
				</a>
			</div>

			<div class="col-md-4">
				<a href="customerAddMoney"> <span class="label-heading">Add Money</span>
				</a>
			</div>
			
			<div class="col-md-4">
				<div class="label-heading">My Customers</div>
				<hr class="underLine">
				<br>
			</div>

		</div>
	</div>
	<div class="row">
		<div class=" col-md-offset-2 col-md-8">
			<c:if test="${not empty listOfCustomers}">
				<table class="table table-hover">
					<tr>
						<th>Customer Id</th>
						<th>Email Id</th>
						<th>Name</th>
						<th>Date Of Birth</th>
					</tr>
					<c:forEach items="${listOfCustomers}"
						begin="${paginationBeginIndex}" end="${paginationEndIndex-1}"
						var="cust">
						<tr>
							<td>${cust.customerId}</td>
							<td>${cust.emailId}</td>
							<td>${cust.name}</td>
							<td>
								<%
									DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
											LocalDate ldt = ((Customer) pageContext.getAttribute("cust")).getDateOfBirth();
								%> <%=ldt.format(dtf)%>
							</td>
						</tr>
					</c:forEach>
				</table>

				<div class="col-xs-1">
					<c:if test="${paginationBeginIndex gt 0}">
						<form action="PreviousSetCustomers" method="post" class="form">
							<input type="hidden" name="indexFrom"
								value="${paginationBeginIndex}"> <input type="hidden"
								name="indexTo" value="${paginationEndIndex}">
							<button type="submit">Previous</button>
						</form>
					</c:if>
				</div>
				<div class="col-xs-3  col-xs-offset-3 text-center pagination-record">
					<c:if test="${listOfCustomers.size() gt 5}">
						<c:if test="${paginationEndIndex le listOfCustomers.size()}">
								${paginationBeginIndex+1} - ${paginationEndIndex} of ${listOfCustomers.size()} records 
							</c:if>
						<c:if test="${paginationEndIndex ge listOfCustomers.size()}">
								${paginationBeginIndex+1} - ${listOfCustomers.size()} of ${listOfCustomers.size()} records
							</c:if>
					</c:if>
				</div>
				<div class="col-xs-1  col-xs-offset-4">
					<c:if test="${paginationEndIndex lt listOfCustomers.size()}">
						<form action="NextSetCustomers" method="post" class="form">
							<input type="hidden" name="indexFrom"
								value="${paginationBeginIndex}"> <input type="hidden"
								name="indexTo" value="${paginationEndIndex}">
							<button type="submit">Next</button>
						</form>
					</c:if>
					<br> <br> <br> <br>
				</div>
			</c:if>
		</div>
		<br />
		<div class="row">
			<div>
				<br />
				<p class="${style } text-center">${message}</p>
			</div>
		</div>
	</div>
</body>
</html>
