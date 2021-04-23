package com.edubank.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.edubank.model.Account;
import com.edubank.model.Customer;
import com.edubank.model.CustomerLogin;
import com.edubank.model.Transaction;
import com.edubank.service.AccountService;
import com.edubank.service.CustomerLoginService;
import com.edubank.service.CustomerService;
import com.edubank.service.TransactionService;
import com.edubank.utility.ApplicationConstants;

/**
 * This controller class has methods to handle requests related customer login, view transactions,
 * change password and logout.
 * 
 * @author ETA_JAVA
 *
 */
@Controller
public class CustomerLoginController {

	@Autowired
	private Environment environment;
	
	@Autowired
	CustomerLoginService customerLoginService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	CustomerService customerService;
	
	
	static Logger logger = LogManager.getLogger(CustomerLoginController.class);
	
	/**
	 * This attribute is used to keep the number of rows displayed in pagination
	 * of Customer Transactions report. <br>
	 * Value is fixed (final) in {@link ApplicationConstants} to 10 and it is taken here to display 10 rows at a time. 
	 * As the value is fixed, 
	 * it is made as an instance variable instead of keeping it as a local variable
	 * in methods in which pagination logic is written.
	 */
	private final Integer paginationRows = ApplicationConstants.PAGINATION_ROWS;

	/**
	 * This method is used to generate the login view
	 * 
	 * @return ModelAndView
	 * 
	 */
	@RequestMapping(value = { "/" })
	public ModelAndView login() {
		ModelAndView model = new ModelAndView("customerLogin");
		return model;
	}

	/**
	 * This method is invoked from customerLogin.jsp with 
	 * customer login credentials, loginName and password
	 *  
	 * It is used to authenticate the customer login credentials, if
	 * the credentials are proper then it redirects the customer to Home Page. <br> 
	 * 
	 * It also sends the last 30 days transactions report of authenticated customer
	 * and redirects to customer's home page.
	 * 
	 * If the credentials are not proper then it shows appropriate error message on
	 * the current view (Login Page) . <br>
	 * 
	 * Note: It can accept only post request 
	 * 
	 * @param customerLogin
	 * @param httpSession
	 * 
	 * @return ModelAndView object redirecting to error page if any error
	 *         occurs,<br>
	 *         ModelAndView object redirecting to login page if authentication
	 *         fails,<br>
	 *         ModelAndView object redirecting to home page if authentication
	 *         success. <br>
	 * 
	 */
	@RequestMapping(value = { "/authenticateCustomer" }, method = RequestMethod.POST)
	public ModelAndView authenticateCustomer(@ModelAttribute CustomerLogin customerLogin, HttpSession httpSession) {
		/*
		 * First we set the view to error page, so that while code is executing if any exception/error occurs,
		 *  this object will be returned and customer will be redirected to error page. 
		 */
		ModelAndView model = new ModelAndView("error");

		logger.info("CUSTOMER LOGIN ATTEMPT, LOGIN NAME : "+customerLogin.getLoginName());
		

		/* ContextFactory.getContext() is a static method of ContextFactory class, parses the SpringConfig
		 * class and using @ComponentScan (Please see ContextFactory and SpringConfig for more details),
		 * 
		 * it scans the packages/classes for which the base package provided is
		 * 'com.edubank' then it returns the object of ApplicationContext
		 * 
		 * ApplicationContext holds the objects created by spring framework
		 * 
		 * Here we are getting the object of CustomerLoginServiceImpl from ApplicanContext 
		 * so that we can call the authenticateCustomerLogin method of that class
		 **/
//		CustomerLoginService customerLoginService = (CustomerLoginService) ContextFactory
//				.getContext().getBean("customerLoginService");

		try {
			/*
			 * Here we are calling authenticateCustomerLogin method of CustomerLoginService
			 * and passing CustomerLogin object holding data loginName and password
			 * 
			 * It returns the Customer object with details like customerId, name, email, dateOfBirth
			 * if the login credentials are valid, else it will throw exception
			 */
			Customer customer = customerLoginService
					.authenticateCustomerLogin(customerLogin);

			/*
			 * here we are setting the customerId into httpSession 
			 * so that it can be used to identify the logged in customer 
			 * through out the application 
			 */
			httpSession.setAttribute("customerId", customer.getCustomerId());

			/*
			 * This code is to change the view to Home Page, as 
			 * now we know that the customer is valid or else exception 
			 * would have been thrown while authenticating 
			 */

			logger.info("CUSTOMER LOGIN, AUTHENTICATION SUCCESS, LOGIN NAME : "+customerLogin.getLoginName());

			model = new ModelAndView("customerHome");
			
			/*
			 * here we are setting the name of customer to model object 
			 * so that it can be displayed to the home page
			 */
			model.addObject("name", customer.getName());

			/*
			 * here we are setting the customer name into httpSession 
			 * so that it can be used to display the name of logged in customer 
			 * through out the application 
			 */
			httpSession.setAttribute("name", customer.getName());
			
			/* Deleting the customer's PDF from server on the first login.
			 * PDF can be removed from server as a correct login means customer has received the PDF
			 */
//			if(PDFUtility.deletePDF(customer.getCustomerId().toString()))
//				logger.info("CUSTOMER LOGIN, PDF SUCCESSFULLY DELETED FROM SERVER FOR CUSTOMER: " + customerLogin.getLoginName());
			
			/*
			 * here we are getting the object of AccountServiceImpl so that 
			 * we can call getAccountByCustomerId method of the same
			 */
//			AccountService accountService = (AccountService) ContextFactory
//					.getContext().getBean("accountService");

			/*
			 * Here we are calling the getAccountByCustomerId method of 
			 * AccountService which return the Account details like accountNumber, 
			 * balance etc
			 */
			Account account = accountService.getAccountByCustomerId(customer
					.getCustomerId());

			
			/*
			 * converting the balance from 4 decimal places to 2 decimal places
			 */
			Double balance = account.getBalance();
			Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
			
			/*
			 * here we are setting the balance of customer to model object 
			 * so that it can be displayed to the home page
			 */
			model.addObject("balance", balanceTillTwoDecimalPlaces);
			
			/*
			 * here we are setting the balance into httpSession 
			 * so that it can be used to display the balance of logged in customer 
			 * through out the application 
			 */
			httpSession.setAttribute("balance", balanceTillTwoDecimalPlaces);
			
			/*
			 * The following code is to fetch the transaction details of logged in customer for last 30 days
			 */
			/*
			 * setting two Dates, fromDate as 30 days before now and toDate as todays date
			 */
			logger.info("CUSTOMER LOGIN, BALANCE ADDED TO VIEW, LOGIN NAME : "+customerLogin.getLoginName());
			LocalDate fromDate = LocalDate.now();
			fromDate = fromDate.minusDays(29);
			LocalDate toDate = LocalDate.now();
		
			/*
			 * adding both fromDate and toDate to the modal so that it can be displayed to the Home Page
			 */
			model.addObject("fromDate", fromDate);
			model.addObject("toDate", toDate);

			/*
			 * setting the message to be displayed on Home Page while displaying the transaction details
			 */
			model.addObject("transactionsMessage", 
					environment.getProperty("CustomerLoginController.LAST_THIRTY_DAYS_TRANSACTIONS_MESSAGE"));

			/*
			 * here we are getting the object of TransactionServiceImpl so that 
			 * we can call getLastThitryDaysTransactionsForAccount method of the same
			 */
//			TransactionService transactionService = (TransactionService) ContextFactory
//					.getContext().getBean("transactionService");

			/*
			 * here we are retrieving the list of transactions done in
			 * last 30 days for the logged in customer by calling 
			 * getLastThitryDaysTransactionsForAccount method of TransactionService
			 * 
			 * If there are no transactions in specified period, 
			 * NoTransactionsFoundFromLastThirtyDaysException is thrown
			 */
			List<Transaction> transactionsList = transactionService
					.getLastThirtyDaysTransactionsForAccount(account
							.getAccountNumber());
			/*
			 * adding the transaction list so that it can be displayed to the Home page 
			 */
			model.addObject("transactionsList", transactionsList);
			
			logger.info("CUSTOMER LOGIN, TRANSACTION ADDED TO VIEW, LOGIN NAME : "+customerLogin.getLoginName());

			/* creating and adding the index values to achieve pagination while displaying the transactions on the Home Page
			 */
			Integer paginationIndex1 = 0;
			Integer paginationIndex2 = paginationRows;
			model.addObject("paginationBeginIndex", paginationIndex1);
			model.addObject("paginationEndIndex", paginationIndex2);
		} 
		catch (Exception e) {
			/*
			 * This block will be executed if the customer is not 
			 * having any transaction in last thirty days. 
			 * 
			 * Here we are setting an error message to the model 
			 * to display that there were no transaction in last 
			 * 30 days to the Customer 
			 */ 
			if("TransactionService.NO_TRANSACTIONS_IN_LAST_THIRTY_DAYS".equals(e.getMessage())) {
				model.addObject("style", "text-danger");
			}
			
			/*
			 * This block will be executed if any other 
			 * exception occurs like invalid credentials
			 */
			else if (e.getMessage().contains("LoginService")
					|| e.getMessage().contains("LoginValidator")) {
				/*
				 * Here we are setting the view to the login page 
				 * so that if the credentials are invalid the customer 
				 * will be allowed to enter it again
				 */
				model = new ModelAndView("customerLogin");
				
				/*
				 * Here we are adding the login name to the modal 
				 * so that if credential is invalid and the login page 
				 * is reloaded, the login name will not be erased    
				 */
				model.addObject("loginName", customerLogin.getLoginName());
				
				/*
				 * Note: The password is not been set so it will be 
				 * 		 erased if invalid credentials are entered.
				 * 		
				 * 		  This is been done due to Security reasons
				 */
			}
			
			/*
			 * Here we are adding a user friendly message to the model 
			 * corresponding to the exception occurred   
			 * 
			 * The message is been read from configuration.properties file 
			 * in src/main/resources/  --> com.edubank.resources 
			 */
			model.addObject("message", environment.getProperty(e.getMessage()));
		}
		
		/*
		 * Here we are returning the model object
		 */
		return model;
	}

	/**
	 * This method is used to access the Customer Home page if the customer is
	 * already in session. If the customer's session is active then fetch the
	 * customer's details, it also sends the last 3 days transactions report of
	 * authenticated customer and show them on the Customer's Home page, else
	 * redirect to the error page
	 * 
	 * @param httpSession
	 * 
	 * @return ModelAndView
	 * 
	 */
	@RequestMapping(value = { "/customerHome" })
	public ModelAndView customerHome(HttpSession httpSession) {
		ModelAndView model = new ModelAndView("error");
		try {
			if (httpSession.getAttribute("customerId") != null) {

				/*
				 * model is the data which we are retrieving from the session
				 * and view is returned to the client side to display the values
				 */
				model = new ModelAndView("customerHome");

				Integer customerId = (Integer) httpSession
						.getAttribute("customerId");

				Customer customer = customerService
						.getCustomerByCustomerId(customerId);

				model.addObject("name", customer.getName());

				/*
				 * here we are retrieving the values from session setting it to
				 * model object which will returned to client
				 */
				Account account = accountService
						.getAccountByCustomerId(customer.getCustomerId());
				/*
				 * converting the balance from 4 decimal places to 2 decimal places
				 */
				Double balance = account.getBalance();
				Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
				
				model.addObject("balance", balanceTillTwoDecimalPlaces);
				
				/*
				 * the following code is to set the value from the session and
				 * setting it to model using httpSession.setAttribute
				 */
				httpSession.setAttribute("balance", balanceTillTwoDecimalPlaces);
				httpSession.setAttribute("name", customer.getName());

				LocalDate fromDate = LocalDate.now();
				fromDate = fromDate.minusDays(29);
				LocalDate toDate = LocalDate.now();
				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);

				model.addObject(
						"transactionsMessage",
						environment
								.getProperty("CustomerLoginController.LAST_THIRTY_DAYS_TRANSACTIONS_MESSAGE"));

				List<Transaction> transactionsList = transactionService
						.getLastThirtyDaysTransactionsForAccount(account
								.getAccountNumber());
				model.addObject("transactionsList", transactionsList);
				Integer paginationIndex1 = 0;
				Integer paginationIndex2 = paginationRows;
				model.addObject("paginationBeginIndex", paginationIndex1);
				model.addObject("paginationEndIndex", paginationIndex2);
			} else {
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}

		} catch (Exception e) {
			model.addObject("style", "text-danger");
			model.addObject("message", environment.getProperty(e.getMessage()));

		}
		return model;
	}

	/**
	 * This method is used to clear the session data and redirects to the login
	 * page with a success message.
	 * 
	 * @param httpSession
	 * 
	 * @return ModelAndView
	 *
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpSession httpSession) {

		logger.info("CUSTOMER LOGOUT ATTEMPT. CUSTOMER ID: "+httpSession.getAttribute("customerId"));
		ModelAndView model = new ModelAndView("error");
		try {
			if (httpSession.getAttribute("customerId") != null) {
				model = new ModelAndView("customerLogin");

				Enumeration<String> attributes = httpSession
						.getAttributeNames();

				while (attributes.hasMoreElements())
					httpSession.removeAttribute(attributes.nextElement());

				model.addObject("successMessage", environment
						.getProperty("LoginController.LOGOUT_SUCCESS"));
				logger.info("CUSTOMER LOGOUT SUCCESS. ALL SESSION DETAILS CREARED. CUSTOMER ID: "+httpSession.getAttribute("customerId"));
			} else {
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}
		} catch (Exception e) {
			model.addObject("style", "text-danger");
			model.addObject("message", environment.getProperty(e.getMessage()));

		}

		return model;
	}

	/**
	 * This method is used to generate the view for customerChangePassword
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = { "/customerChangePassword" })
	public ModelAndView customerChangePassword(HttpSession httpSession) {
		ModelAndView modelAndView = new ModelAndView("error");
		try {
			
			if (httpSession.getAttribute("customerId") != null) {
				modelAndView = new ModelAndView("customerChangePassword");
				modelAndView
						.addObject("name", httpSession.getAttribute("name"));
				/*
				 * This method is used to get the account details 
				 * so that we can display the updated balance to the user
				 */
				Integer customerId = (Integer) httpSession.getAttribute("customerId");
				Account account = accountService
						.getAccountByCustomerId(customerId);
				
				/*
				 * converting the balance from 4 decimal places to 2 decimal places
				 */
				Double balance = account.getBalance();
				Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
				
				modelAndView.addObject("balance", balanceTillTwoDecimalPlaces);
			} else {
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}

		} catch (Exception e) {
			modelAndView.addObject("message",
					environment.getProperty(e.getMessage()));
		}

		return modelAndView;
	}

	/**
	 * This method is used to change the password of logged in customer, it
	 * takes password, new password and confirm new password from the form and,
	 * fetches the customerId are proper then it sets the customerId from the
	 * httpSession. If the entered data is valid, it updates the password,
	 * clears the httpSession data, and redirects to login page. Note: It can
	 * accept only post request
	 * 
	 * @param customerLogin
	 * @param httpSession
	 * 
	 * @return ModelAndView object redirecting to error page if any error
	 *         occurs,<br>
	 *         ModelAndView object redirecting to login page if data entered are
	 *         valid and password is updated,<br>
	 *         ModelAndView object redirecting to the same
	 *         changeCustomerPassword page if data entered are invalid. <br>
	 * 
	 */
	@RequestMapping(value = { "/customerChangePassword" }, method = RequestMethod.POST)
	public ModelAndView customerChangePassword(
			@ModelAttribute CustomerLogin customerLogin, HttpSession httpSession) {
		logger.info("CUSTOMER PASSWORD CHANGE REQUEST. CUSTOMER ID: "+httpSession.getAttribute("customerId"));
		ModelAndView model = new ModelAndView("error");

		try {
			if (httpSession.getAttribute("customerId") != null) {
				customerLogin.setCustomerId((Integer) httpSession
						.getAttribute("customerId"));

				customerLoginService.changeCustomerPassword(customerLogin);
				logger.info("CUSTOMER PASSWORD CHANGED SUCCESSFULLY. CUSTOMER ID: "+httpSession.getAttribute("customerId"));
				model = new ModelAndView("customerLogin");
				
				Enumeration<String> attributes = httpSession
						.getAttributeNames();

				while (attributes.hasMoreElements())
					httpSession.removeAttribute(attributes.nextElement());

				model.addObject(
						"successMessage",
						environment
								.getProperty("LoginController.PASSWORD_CHANGED_SUCCESS"));
				
			} else {
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}

		} catch (Exception e) {
			if (e.getMessage().contains("LoginService")
					|| e.getMessage().contains("LoginValidator")) {
				model = new ModelAndView("customerChangePassword");
				model.addObject("errorMessage",
						environment.getProperty(e.getMessage()));
				model.addObject("password", customerLogin.getPassword());
				model.addObject("newPassword", customerLogin.getNewPassword());
				model.addObject("confirmNewPassword",
						customerLogin.getConfirmNewPassword());

			}

			model.addObject("message", environment.getProperty(e.getMessage()));
		}
		return model;
	}

	/**
	 * This method receives From Date and To Date selected by logged in customer
	 * to get the transactions report. <br>
	 * 
	 * @DateTimeFormat(pattern = "yyyy-MM-dd") is used in method signature to
	 *                         convert the date value coming in HTTP request
	 *                         directly to LocalDate. <br>
	 *                         This method populates transactions list in
	 *                         ModelAndView object and sends to client. If
	 *                         transactions are not there then it sends the
	 *                         message coming in the exception object as a
	 *                         response.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param httpSession
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/viewTransactionsDateRange", method = RequestMethod.POST)
	public ModelAndView getAccountTransactionsForDateRange(
			@RequestParam("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
			@RequestParam("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
			HttpSession httpSession) {
		ModelAndView model = new ModelAndView("error");

		try {
			if (httpSession.getAttribute("customerId") != null) {
				Integer customerId = (Integer) httpSession
						.getAttribute("customerId");

				Customer customer = customerService
						.getCustomerByCustomerId(customerId);

				model = new ModelAndView("customerHome");
				model.addObject("name", customer.getName());

				Account account = accountService
						.getAccountByCustomerId(customer.getCustomerId());
				
				/*
				 * converting the balance from 4 decimal places to 2 decimal places
				 */
				Double balance = account.getBalance();
				Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
				
				model.addObject("balance", balanceTillTwoDecimalPlaces);

				DateTimeFormatter dtf = DateTimeFormatter
						.ofPattern("dd-MMM-yyyy");

				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);
				model.addObject(
						"transactionsMessage",
						environment
								.getProperty("CustomerLoginController.TRANSACTIONS_DATE_RANGE_MESSAGE")
								+ fromDate.format(dtf)
								+ " to "
								+ toDate.format(dtf));

				List<Transaction> transactionsList = transactionService
						.getAccountTransactionsForDateRange(
								account.getAccountNumber(), fromDate, toDate);
				model.addObject("transactionsList", transactionsList);
				Integer paginationIndex1 = 0;
				Integer paginationIndex2 = paginationRows;
				model.addObject("paginationBeginIndex", paginationIndex1);
				model.addObject("paginationEndIndex", paginationIndex2);
			} else {
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}
		} catch (Exception e) {
			if (e.getMessage() != null) {
				model.addObject("style", "text-danger");
				model.addObject("message",
						environment.getProperty(e.getMessage()));
			}
		}

		return model;
	}

	/**
	 * This method receives from date, to date, pagination indexes,
	 * transactionsMessage in HTTP POST request. It calculates the pagination
	 * indexes to show the next set of rows (10 or less than 10 rows) and
	 * returns the updated pagination indexes as response to the client.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param paginationIndex1
	 * @param paginationIndex2
	 * @param transactionsMessage
	 * @param httpSession
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/Next", method = RequestMethod.POST)
	public ModelAndView getNextSet(
			@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
			@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
			@RequestParam("indexFrom") Integer paginationIndex1,
			@RequestParam("indexTo") Integer paginationIndex2,
			@RequestParam("transactionsMessage") String transactionsMessage,
			HttpSession httpSession) {
		ModelAndView model = new ModelAndView("error");
		try {
			if (httpSession.getAttribute("customerId") != null) {
				model = new ModelAndView("customerHome");

				Integer customerId = (Integer) httpSession
						.getAttribute("customerId");

				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);

				Customer customer = customerService
						.getCustomerByCustomerId(customerId);

				model.addObject("name", customer.getName());

				Account account = accountService
						.getAccountByCustomerId(customer.getCustomerId());
				
				/*
				 * converting the balance from 4 decimal places to 2 decimal places
				 */
				Double balance = account.getBalance();
				Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
				
				model.addObject("balance", balanceTillTwoDecimalPlaces);

				paginationIndex1 = paginationIndex1 + paginationRows;
				paginationIndex2 = paginationIndex2 + paginationRows;
				model.addObject("paginationBeginIndex", paginationIndex1);
				model.addObject("paginationEndIndex", paginationIndex2);

				model.addObject("transactionsMessage", transactionsMessage);

				List<Transaction> transactionsList = transactionService
						.getAccountTransactionsForDateRange(
								account.getAccountNumber(), fromDate, toDate);
				model.addObject("transactionsList", transactionsList);
			} else {
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}

		} catch (Exception e) {
			model.addObject("style", "text-danger");
			model.addObject("message", environment.getProperty(e.getMessage()));
		}
		return model;

	}

	/**
	 * This method receives from date, to date, pagination indexes,
	 * transactionsMessage in HTTP POST request. It calculates the pagination
	 * indexes to show the previous set of rows (previous 10 rows) and returns
	 * the updated pagination indexes as response to the client.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @param paginationIndex1
	 * @param paginationIndex2
	 * @param transactionsMessage
	 * @param httpSession
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/Previous", method = RequestMethod.POST)
	public ModelAndView getPreviousSet(
			@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
			@RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
			@RequestParam("indexFrom") Integer paginationIndex1,
			@RequestParam("indexTo") Integer paginationIndex2,
			@RequestParam("transactionsMessage") String transactionsMessage,
			HttpSession httpSession) {

		ModelAndView model = new ModelAndView("error");
		try {
			if (httpSession.getAttribute("customerId") != null) {
				model = new ModelAndView("customerHome");

				Integer customerId = (Integer) httpSession
						.getAttribute("customerId");

				Customer customer = customerService
						.getCustomerByCustomerId(customerId);

				model.addObject("name", customer.getName());

				Account account = accountService
						.getAccountByCustomerId(customer.getCustomerId());
				
				/*
				 * converting the balance from 4 decimal places to 2 decimal places
				 */
				Double balance = account.getBalance();
				Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
				
				model.addObject("balance", balanceTillTwoDecimalPlaces);

				paginationIndex1 = paginationIndex1 - paginationRows;
				paginationIndex2 = paginationIndex2 - paginationRows;
				model.addObject("paginationBeginIndex", paginationIndex1);
				model.addObject("paginationEndIndex", paginationIndex2);

				model.addObject("transactionsMessage", transactionsMessage);

				model.addObject("fromDate", fromDate);
				model.addObject("toDate", toDate);

				List<Transaction> transactionsList = transactionService
						.getAccountTransactionsForDateRange(
								account.getAccountNumber(), fromDate, toDate);
				model.addObject("transactionsList", transactionsList);
			} else {
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}

		} catch (Exception e) {
			model.addObject("style", "text-danger");
			model.addObject("message", environment.getProperty(e.getMessage()));
		}
		return model;
	}
}
