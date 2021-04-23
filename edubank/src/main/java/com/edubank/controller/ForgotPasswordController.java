package com.edubank.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.edubank.model.Customer;
import com.edubank.model.SecurityQuestion;
import com.edubank.service.CustomerService;
import com.edubank.service.ForgotPasswordService;
import com.edubank.service.SecurityQuestionService;

/**
 * This class has methods to handle forgot password and reset password requests.
 *
 * @author ETA_JAVA
 *
 */
@Controller
public class ForgotPasswordController 
{
	/**
	 * The attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;

	@Autowired
	ForgotPasswordService forgotPasswordService;
	
	@Autowired
	SecurityQuestionService securityQuestionService;
	
	@Autowired
	CustomerService customerService;
	
	private static Logger logger = LogManager.getLogger(ForgotPasswordController.class);
	
	/**
	 * This method is used to generate the forgot password view
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
	public ModelAndView getForgotPasswordView()
	{
		ModelAndView model = new ModelAndView("forgotPassword");
		
		model.addObject("formHandler", "forgotPassword");
		
		return model;
	}

	/**
	 * This method receives the emailId in POST request and calls a method of 
	 * ForgotPasswordService class to verify the email id and populates customer Security Question and its id  
	 * if email id verification is success.
	 * 
	 * @param emailId
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
	public ModelAndView forgotPassword(@RequestParam String emailId) {

		/*
		 * model is the data which we are retrieving from the session and view
		 * is returned to the client side to display the values
		 */
		ModelAndView model = new ModelAndView("forgotPassword");
	

		try 
		{
			logger.info("FORGOT PASSWORD REQUEST. EMAIL ID : "+emailId);
			
			
			forgotPasswordService.authenticateEmailId(emailId);

			// Getting customer data for the email id submitted by customer
			Customer customer=customerService.getCustomerByEmailId(emailId);
			
			/*
			 * Getting all the Security Questions and their IDs by calling service method  
			 */
			List<SecurityQuestion> securityQuestions = securityQuestionService.getAllSecurityQuestions();
	
			String securityQuestion=null;
			
			/*
			 * Getting customer's security question from the list of security questions for adding to model
			 */
			for (SecurityQuestion secQuestion : securityQuestions)
			{
				if(secQuestion.getQuestionId().equals(customer.getSecurityQuestionId()))
				{
					securityQuestion=secQuestion.getQuestion();
				}
			}
			
			/* here we are setting the values to model object which will be sent
			 * to client
			 */
			model.addObject("style", "text-info");
			model.addObject("secQuestion", securityQuestion);
			model.addObject("message", environment.getProperty("ForgotPasswordController.FORGOT_PASSWORD_SECQ_ANS"));
			model.addObject("formHandler", "forgotPasswordSecQA");
		} 
		catch (Exception e)
		{
			model.addObject("style", "text-danger");
			model.addObject("errorMessage", environment.getProperty(e.getMessage()));
			model.addObject("formHandler", "forgotPassword");
		}
		
		model.addObject("emailId", emailId.toLowerCase());
		return model;
	}

	/**
	 * This method is used to generate reset password view to client after successful verification. It receives email id, 
	 * security question  and security answer in the POST request and verifies security answer.
	 * 
	 * @param emailId, secQ, secAns
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/forgotPasswordSecQA", method = RequestMethod.POST)
	public ModelAndView generateResetPasswordView(@RequestParam("emailId") String emailId, @RequestParam("secQ") String secQ,
			@RequestParam("secAns") String secAns) {
		
		/*
		 * model is the data which we are retrieving from the session and view
		 * is returned to the client side to display the values
		 */
		ModelAndView model = new ModelAndView("forgotPassword");

		Customer customer=null;
		
		try
		{
			logger.info("FORGOT PASSWORD REQUEST with Security question and answer for EMAIL ID : "+emailId);

			// Getting customer data for the email id submitted by customer
			customer=customerService.getCustomerByEmailId(emailId);
			
			//Calling service method for Verifying Security answer submitted by customer
			forgotPasswordService.checkSecQuestionAnswer(secAns, customer.getSecurityAnswer());
			
			//Setting showResetPwdForm to true displays Reset Password form to customer 
			model.addObject("showResetPwdForm", true);

		} 
		catch (Exception e) 
		{
			model.addObject("style", "text-danger");
			model.addObject("message", environment.getProperty("ForgotPasswordController.FORGOT_PASSWORD_SECQ_ANS"));
			model.addObject("errorMessage", environment.getProperty(e.getMessage()));
			model.addObject("formHandler", "forgotPasswordSecQA");
			
			/*
			 *  The below statement is to generate customer's security question 
			 */
			model.addObject("secQuestion", secQ);
		}
		
		model.addObject("emailId", emailId.toLowerCase());
		
		return model;
	}

	/**
	 * This method is used to reset the password. It receives email id, 
	 * new password and confirm  new password in POST request. If the password
	 * reset is success it sends redirect request to client in HTTP response
	 * header else it sends Reset password view as response with appropriate error message.
	 * 
	 * @param emailId
	 * @param newPassword
	 * @param confirmNewPassword
	 * @param response
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
	public ModelAndView resetPassword(@RequestParam("emailId") String emailId, @RequestParam("newPwd") String newPassword,
			@RequestParam("confirmNewPwd") String confirmNewPassword, HttpServletResponse response) 
	{
		ModelAndView model = null;
		
		try 
		{
			logger.info("RESETTING PASSWORD For Email Id : " + emailId);
			
			// Resetting the password for the given email id by calling service method 
			forgotPasswordService.resetPassword(emailId, newPassword, confirmNewPassword);

			logger.info("PASSWORD RESET SUCCESS For Emaild Id : "+emailId);
			
			/*
			 * Sends a temporary redirect response to the client using the
			 * specified redirect location URL and clears the buffer. The buffer
			 * will be replaced with the data set by this method. Calling this
			 * method sets the status code to SC_FOUND 302 (Found). This method
			 * can accept relative URLs;the servlet container must convert the
			 * relative URL to an absolute URL before sending the response to
			 * the client. If the location is relative without a leading '/' the
			 * container interprets it as relative to the current request URI.
			 * If the location is relative with a leading '/' the container
			 * interprets it as relative to the servlet container root.
			 * 
			 * If the response has already been committed, this method throws an
			 * IllegalStateException. After using this method, the response
			 * should be considered to be committed and should not be written
			 * to.
			 */
			response.sendRedirect("resetPasswordSuccess");
			
		} 
		catch (Exception e) 
		{
			model = new ModelAndView("forgotPassword");
			model.addObject("showResetPwdForm", true);
			model.addObject("emailId", emailId.toLowerCase());
			model.addObject("style", "text-danger");
			model.addObject("errorMessage", environment.getProperty(e.getMessage()));
		}
		
		return model;
	}

	/**
	 * This method is used to send the Customer Login view as response with
	 * reset password success message.
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/resetPasswordSuccess")
	public ModelAndView resetPasswordSuccess() 
	{
		/*
		 * model is the data which we are retrieving from the session and view
		 * is returned to the client side to display the values
		 */
		ModelAndView model = new ModelAndView("customerLogin");
		
		/*
		 * here we are setting the values to model object which will 
		 * be sent to client.
		 * getProperty Return the property value associated with the
		 * given key, or null if the key cannot be resolved.
		 * 
		 */
		model.addObject("successMessage", environment.getProperty("ForgotPasswordController.RESET_PASSWORD_SUCCESS"));
		
		return model;
	}
}
