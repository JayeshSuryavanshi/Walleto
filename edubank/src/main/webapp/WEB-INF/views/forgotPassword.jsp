<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link href="${pageContext.request.contextPath}/resources/css/bootstrap.css" rel="stylesheet">
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.css">
	<link href="${pageContext.request.contextPath}/resources/css/eduBank.css" rel="stylesheet">
	
	 <!-- It is used for applying icon for browser tab -->
	<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/images/bank_icon.ico">
	<title>EDUBank - Forgot Password</title>
	<script type = "text/javascript" >
		window.history.forward();
		
		function setAutoFocus(emailId, formHandler)
		{
			if(emailId.length==0)
				document.getElementById("emailId").focus();
			else if(formHandler=='forgotPasswordSecQA')
				document.getElementById("emailId").disabled=true;
		}
	</script>
</head>
<body onload="setAutoFocus('${emailId}', '${formHandler}')">

<jsp:include page="header.jsp" />
<br><br><br>
        <div class="col-md-4 col-md-offset-4 col-sm-8 col-sm-offset-2 col-xs-10 col-xs-offset-1 box-model">
         <c:if test="${!showResetPwdForm}">
          	<div class="col-md-12 col-sm-12 col-xs-12">
				<span class="label-heading">Forgot Password</span>
				<br>
				<hr class="underLine col-md-12 col-sm-12 col-xs-12">
			</div> <br>
            <div class="form-wrap">
	           <form  action="${formHandler }" method="POST" >
	              <div class="form-group">     
	                <input type="email" class="form-control" name="emailId" id="emailId" 
	                value="${emailId}" placeholder="Enter your registered email id" 
	                required  pattern="[^@]+@[a-zA-Z0-9]+\.[a-zA-Z0-9]+">   
	       		
				  </div> 
				      
           	   	  <span class="${style } text-center">${message}</span> 
	              <c:if test="${formHandler=='forgotPasswordSecQA'}">
	                <br><br>
		            <div class="form-group" >  
		            	 <select name="secQ" class="form-control" required >
			            	  <option value="${secQuestion }"> ${secQuestion } </option>
		            	 </select>     <br>     
		            	 <input type="hidden" value="${emailId}" name="emailId">
		           		 <input type="text"  name="secAns" id="secAnswer" class="form-control" 
		           		 title="Please fill out this filed. Security answer is case-sensitive."
		           		 placeholder="Enter your security answer" required autofocus >           		
					</div>
				 </c:if>
			
			  	 <div class="form-group text-center">
					<button type="submit">Submit</button> 
					 <a href="/EDUBank/" class="text-center">Back to Login</a>  
	             </div>
	           
			   </form>
     
            </div>
        </c:if>  
             
        <c:if test="${showResetPwdForm}">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<span class="label-heading"> Reset Password</span>
				<br>
				<hr class="underLine col-md-12 col-sm-12 col-xs-12">
			</div> <br>
		<form class="form-horizontal" action="resetPassword" method="post">
            
            <br>
             	 <input type="text" value="${emailId }" class="form-control" disabled/>
             	 <br> 
             	 <input type="hidden" value="${emailId }" name="emailId"/>
             	 
           		 <input type="password" name="newPwd" id="newPwd" placeholder="New Password*" required class="form-control" >
           		 <br>
                 <input type="password" name="confirmNewPwd" id="confirmPwd" placeholder="Confirm New Password*" required class="form-control" >
 				<br>
              <div class="form-group text-center">
				<button type="submit" > Submit</button>
       			<a href="/EDUBank/">Cancel</a>
			  </div>
            </form>
		</c:if>
	
		<span class="text-danger">${errorMessage}</span>
        </div>
  
    </body>
</html>