<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link
	href="${pageContext.request.contextPath}/resources/css/bootstrap.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/eduBank.css" rel="stylesheet" >

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.css">

 <!-- Is used for applying icon for browser tab -->
  <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
<title>EDUBank - Change Debitcard PIN</title>
</head>
<body>

<div class="container-fluid">

		<%-- include header --%>
		<jsp:include page="header.jsp" />
		
		<!-- displaying balance and user option to change pin, password and logout-->
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
		<!-- displaying balance ----   end -->
		<br><br>
		<div class="row" >
		<div class="col-md-offset-1 col-md-11 col-sm-offset-1 col-sm-11 col-xs-12">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<span class="label-heading">
						Change Pin
				</span>	
					<br>
				<hr class="underLine">
				
			</div>
			<div class="col-md-12">
				<form class="form-horizontal" action="changeDebitCardPin" method="post">
         
                    <input type="password"  name="pin" id="pin" placeholder="Current Pin" required value="${pin}">
        		<br><br>
   
                    <input type="password"  name="newPin" id="newPin" placeholder="New Pin" required value="${newPin}">
   				<br><br>
            

                    <input type="password"  name="confirmNewPin" id="confirmNewPin" placeholder="Confirm New Pin" required value="${confirmNewPin}">
				<br><br>
   
				<button type="submit" ><i class="fa fa-sign-in" aria-hidden="true"></i> Submit </button>
 
                        
               <a href="customerHome">Cancel</a>
                <br>
     
            </form>
			</div>
			
              
            
            <!-- Displaying success and error message -->
            <div class="col-md-12">
				<c:if test="${successMessage ne null}">
					<div class="alert alert-success col-md-offset-3 col-md-6 col-sm-offset-1 col-sm-10 col-xs-12" align="center">${successMessage}</div>
				</c:if>
				<c:if test="${errorMessage ne null}">
					<div class="alert alert-danger col-md-offset-3 col-md-6 col-sm-offset-1 col-sm-10 col-xs-12" align="center">${errorMessage}</div>
				</c:if>
			</div>
			
		</div>
		</div>
		</div>
</body>
</html>
