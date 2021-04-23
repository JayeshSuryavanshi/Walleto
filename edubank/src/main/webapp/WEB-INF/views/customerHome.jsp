<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="com.edubank.model.Transaction"%>
<%@page import="com.edubank.model.TransactionType"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/css/font-awesome.css"
	rel="stylesheet">

<link href="${pageContext.request.contextPath}/resources/css/eduBank.css" rel="stylesheet">	
	
 <!-- Is used for applying icon for browser tab -->
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
<title>Customer Home</title>
</head>
<body>
<%-- include header --%>
	<jsp:include page="header.jsp" />
<div class="container-fluid" >
	
	<!-- displaying balance and user option to change pin, password and logout-->
		<div class="row">
		
		<div class="col-md-offset-1 col-md-5 col-sm-offset-1 col-sm-5 col-xs-6">
				<div class="col-md-12 col-sm-12 col-xs-12">
					<span class="label-heading">
						Balance
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
		<!-- displaying balance ----   end -->
	<br><br>
	<div class="row" >
		<div class="col-md-offset-1 col-md-10 col-sm-offset-1 col-sm-11 col-xs-12" >
		<div class="col-md-12 col-sm-12 col-xs-12">
			<span class="label-heading">
				View Transactions
			</span>
			<br>
			<hr class="underLine">
		</div>
		<div class="col-md-12 col-sm-12 col-xs-12">
			<form action="viewTransactionsDateRange" method="post" class="form-horizontal">
			<div class="col-md-3 col-sm-4 col-xs-12">
				From &nbsp; &nbsp; <input type="date" name="fromDate" value="${fromDate}" required="required" />
			</div> 
			<div class="col-md-3 col-sm-4 col-xs-12">
				To &nbsp; &nbsp; <input type="date" name="toDate" value="${toDate}" required="required"/>
			</div>
			<div class="col-md-6 col-sm-4 col-xs-12"> 
				<button >VIEW TRANSACTIONS</button>
			</div>  
			</form>
		</div>		
		<br>
		
		<div class="col-md-12 col-sm-12 col-xs-12">
		<c:if test="${not empty transactionsList}">
					<div class="row">
						<div class="text-center">
							
							<h3>${transactionsMessage}</h3>
						</div>
					</div><br>
						<table class="table table-hover"> 
							<tr>
								<th>Transaction Date &amp; Time</th>
								<th>Transaction Id</th>
								<th class="text-center">Transaction Info</th>
								<th>CR/DR</th>
								<th>Amount</th>
							</tr>
							<c:forEach items="${transactionsList}" 
								begin="${paginationBeginIndex}" end="${paginationEndIndex-1}" var="tran">
								<tr>
									<td>
									  <% 
									    DateTimeFormatter dtf=DateTimeFormatter.ofPattern("dd-MMM-yyyy  hh:mm:ss  a");
									    LocalDateTime ldt=((Transaction)pageContext.getAttribute("tran")).getTransactionDateTime();
										
									   %>
									   <%= ldt.format(dtf)%>
									</td>
									<td>${tran.transactionId}</td>	
									<td>${tran.info}</td>
									<c:set var="credit" value="<%=TransactionType.CREDIT%>" />
									<c:set var="debit" value="<%=TransactionType.DEBIT%>" />
									<c:if test="${tran.type==credit }">
										<td>Cr.</td>
									</c:if>
									<c:if test="${tran.type==debit }">
										<td>Dr.</td>
									</c:if>																	
									<td><i class="fa fa-usd"></i> ${tran.amount}</td>						
								</tr>
							</c:forEach>
						</table>
						
						<div class="col-xs-1">	
							<c:if test="${paginationBeginIndex gt 0}">
								<form action="Previous" method="post" class="form">
									<input type="hidden" name="from" value="${fromDate}">
									<input type="hidden" name="to" value="${toDate}">		
									<input type="hidden" name="indexFrom" value="${paginationBeginIndex}">
									<input type="hidden" name="indexTo" value="${paginationEndIndex}">
									<input type="hidden" name="transactionsMessage" value="${transactionsMessage}">
									<button type="submit">Previous</button>
								</form>
							</c:if>
						</div>
						<div class="col-xs-3  col-xs-offset-3 text-center pagination-record">
						<c:if test="${transactionsList.size() gt 10}">
							<c:if test="${paginationEndIndex le transactionsList.size()}">
								${paginationBeginIndex+1} - ${paginationEndIndex} of ${transactionsList.size()} records 
							</c:if>
							<c:if test="${paginationEndIndex ge transactionsList.size()}">
								${paginationBeginIndex+1} - ${transactionsList.size()} of ${transactionsList.size()} records
							</c:if>
						</c:if>
						</div>
						<div class="col-xs-1  col-xs-offset-4">
							<c:if test="${paginationEndIndex lt transactionsList.size()}">
								<form action="Next" method="post" class="form">
									<input type="hidden" name="from" value="${fromDate}">
									<input type="hidden" name="to" value="${toDate}">		
									<input type="hidden" name="indexFrom" value="${paginationBeginIndex}">
									<input type="hidden" name="indexTo" value="${paginationEndIndex}">
									<input type="hidden" name="transactionsMessage" value="${transactionsMessage}">
									<button type="submit">Next</button>
								</form>
							</c:if>
							<br> <br> <br> <br>
						</div>
					</c:if>
				</div>
				<br/>
				<div class="row">
					<div>
						<br/>
						<p class="${style } text-center">${message}</p>
					</div>
				</div>
		
		</div>
	</div>
</div>
 
 </body>
</html>
