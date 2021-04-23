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
				<span class="label-heading"> Create Account </span>
				<hr class="underLine">


				<div class="col-md-12">
					<form action="registerCustomer" method="post">
						<input type="text" id="custName" name="custName"
							placeholder="Enter Name*" required="required" /><br> <br>
						<input type="email" id="email" name="email"
							placeholder="Email Id*" required="required" /> <br> <br>

						<label for="dob" class="label-header-small"><span
							class="label-heading-small">Date of Birth:</span></label> <br> <input
							type="date" id="dob" name="dob" placeholder="Date of birth"
							required="required" /> <br> <br> <label for="secQId"
							class="label-header-small"><span
							class="label-heading-small">Security Question:</span></label> <br> <select
							class="form-control" id="secQId" name="secQId"
							required="required">
							<option>-- Select --</option>
							<c:forEach items="${sessionScope.securityQuestions}" var="secQ">
								<option value="${secQ.questionId}">${secQ.question}</option>
							</c:forEach>
						</select> <br> <input type="text" id="secAns" name="secAns"
							placeholder="Security Answer" required="required" /> <br> <br>
						<br>
						<input type="hidden"
							name="indexFrom" value="${paginationBeginIndex}"> 
						<input
							type="hidden" name="indexTo" value="${paginationEndIndex}">
	
						<button type="submit">Add customer</button>
						<button type="reset">Cancel</button>
					</form>
				</div>

				<div class="col-md-12 text-center">
					<div class="text-center text-success">${successMessage}</div>
				</div>

				<div class="col-md-12 text-center">
					<div class="text-center text-danger">${errorMessage}</div>
				</div>
				<br> <br>
				<c:if test="${downloadMessage ne null}">
					<div class="col-md-12 text-center" align="center">
						<a href="download?id=${fileId}">${downloadMessage}</a>
					</div>
				</c:if>

			</div>

			<div class="col-md-4">
				<a href="customerAddMoney">
					<span class="label-heading">Add	Money</span>
				</a>
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
