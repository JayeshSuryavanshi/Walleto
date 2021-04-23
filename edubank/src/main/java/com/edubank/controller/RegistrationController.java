package com.edubank.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.edubank.model.Account;
import com.edubank.model.Customer;
import com.edubank.model.CustomerLogin;
import com.edubank.model.DebitCard;
import com.edubank.service.AccountService;
import com.edubank.service.CustomerLoginService;
import com.edubank.service.CustomerService;
import com.edubank.service.DebitCardService;
import com.edubank.utility.PDFUtility;

/**
 * This controller class has methods to handle requests related to customer registration by teller. 
 * 
 * @author ETA_JAVA
 *
 */
@Controller
public class RegistrationController {

	@Autowired
	private Environment environment;

	@Autowired
	CustomerLoginService customerLoginService;

	@Autowired
	AccountService accountService;
	
	@Autowired
	CustomerService customerService;

	@Autowired
	DebitCardService debitCardService;
	static Logger logger = LogManager.getLogger(RegistrationController.class);
	
	@RequestMapping(value = { "/authenticateTeller" })
	public ModelAndView getTellerHome() {
		ModelAndView model = new ModelAndView("tellerHome");
		return model;
	}

	/**
	 * This method is used for registering a new customer and redirect to
	 * teller's home page with a success message if registration is success
	 * proper error message if not success
	 * 
	 * @param name
	 * @param email
	 * @param dob
	 * @param securityQuestionId
	 * @param securityAnswer
	 * @param httpSession
	 * @param request
	 * @param response
	 * 
	 * @return ModelAndView object redirecting to error page if any error
	 *         occurs,<br>
	 *         ModelAndView object redirecting to home page if registration
	 *         success<br>
	 */
	@RequestMapping(value = "registerCustomer", method = RequestMethod.POST)
	public ModelAndView registerCustomer(@RequestParam("custName") String name,
			@RequestParam String email, @RequestParam String dob,
			@RequestParam("secQId") Integer securityQuestionId, @RequestParam("secAns") String securityAnswer,
			HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {
		

		logger.info("REGISTERING NEW CUSTOMER.... ");
		logger.info("CUSTOMER NAME : "+name+",  EMAIL : "+email+",  DATE OF BIRTH : "+dob +
				",  SECURITY QUESTION : " + securityQuestionId + ",  SECURITY ANSWER : " + securityAnswer);
		logger.info("LOGGED IN TELLER ID: "+httpSession.getAttribute("tellerId"));
		
		ModelAndView model = new ModelAndView("tellerError");
		
		/*
		 * Returns the object bound with the specified name in this session, or
		 * null if no object is bound under the name.
		 */
		Integer tellerId = (Integer) httpSession.getAttribute("tellerId");

		Customer customer = new Customer();

		customer.setName(name);
		customer.setEmailId(email);
		customer.setDateOfBirth(LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		customer.setSecurityQuestionId(securityQuestionId);
		customer.setSecurityAnswer(securityAnswer);
		customer.setCreatedBy(tellerId);

		Integer customerId = null;
		if (tellerId != null) {

			try {
				/*
				 * This method is used to register a new customer by adding each
				 * record to Customer, CustomerLogin, Account,
				 * AccountCustomerMapping and DebitCard database tables after
				 * validating input parameters. After successful registration a
				 * mail will be sent to the customer registered mail id with the
				 * details generated
				 */
				customerId = customerService.addCustomer(customer);

				logger.info("NEW CUSTOMER REGESTERED. CUSTOMER ID : "+customerId);
				customer.setCustomerId(customerId);

				CustomerLogin customerLogin = customerLoginService.addCustomerLogin(customer);
				logger.info("NEW CUSTOMER LOGIN DETAILS CREATED. LOGIN NAME : "+customerLogin.getLoginName());

				/*
				 * This method is used to add a new account details for the
				 * newly added Customer with the data from the model object
				 * populate all the values to the bean and pass it to the
				 * database
				 */
				Account account = accountService.createNewAccount();
				logger.info("NEW ACCOUNT DETAILS CREATEED. ACCOUNT NUMBER : "+account.getAccountNumber());
				Integer mappingId = accountService.mapCustomerWithAccount(
						customerId, account.getAccountNumber());
				logger.info("NEW ACCOUNT MAPPED TO NEW CUSTOMER CREATED. CUSTOMER ID: "+customerId+", ACCOUNT NUMBER : "+account.getAccountNumber());

				/*
				 * This method is used to add a debit card record for the newly
				 * added customer create the debitcardnumber using the Luhn
				 * algorithm populate the bean and pass it on to the database
				 */
				DebitCard card = debitCardService.createNewDebitCard(mappingId, tellerId);
				logger.info("NEW DEBIT CARD DETAILS CREATED. CARD NUMBER : "+card.getDebitCardNumber());
				
				/*
				 * The following code generates the PDF file containing the customer details
				 */
				logger.info("CREATING PDF WITH CUSTOMER DETAILS");
				
				createPDF(customer, customerLogin, account, card);
				logger.info("PDF CREATED SUCCESSFULLY");
				
				Long noOfCustomers = customerService.getNoOfCustomers();

				/*
				 * model is the data which we are retrieving from the session and
				 * view is returned to the client side to display the values
				 */
				model = new ModelAndView("tellerHome");
				
				/*
				 * here we are setting the values to model object which will be sent
				 * to client
				 */
				model.addObject("accountNumber", account.getAccountNumber());
				model.addObject("noOfCustomers", noOfCustomers);
				model.addObject(
						"successMessage",
						environment
								.getProperty("RegistrationController.REGISTRATION_SUCCESS1")+customerId+".\n"+environment
								.getProperty("RegistrationController.REGISTRATION_SUCCESS2"));
				model.addObject("downloadMessage", environment.getProperty("RegistrationController.REGISTRATION_SUCCESS3"));
				model.addObject("fileId", customerId);
				
			}
			catch (Exception e) {
				e.printStackTrace();
				Long noOfCustomers=null;
				
				try {
					noOfCustomers = customerService.getNoOfCustomers();
				} 
				catch (Exception e1) {
					
					noOfCustomers=0l;
				}
				model = new ModelAndView("tellerHome");

				model.addObject("errorMessage",
						environment.getProperty(e.getMessage()));

				model.addObject("noOfCustomers", noOfCustomers);
			}
		}
		return model;
	}
	
	/**
	 * This method is used to serve the customer details PDF file
	 * for download on completion of the registration process
	 * 
	 * @param id
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value = "download")
	public void serveFile(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fileName = "EduBank-Details-" + id + ".pdf";
		FileInputStream in = null;
		OutputStream out = null;
		try {
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition","attachment; filename=" + fileName);
			File file = new File(PDFUtility.getServerFilePath(), fileName);

			out = response.getOutputStream();
			in = new FileInputStream(file);
			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0){
				out.write(buffer, 0, length);
			}
			out.flush();
			
		}
		catch(IOException e) {
			logger.error(e.getMessage(), e);
		}
		finally{
			in.close();
			out.close();
		}
		logger.info("File downloaded for customerId: " + id);
	}
	
	/**
	 * This method is used to generate the PDF file containing
	 * customer details on completion of the registration process
	 * 
	 * @param customer
	 * @param customerLogin
	 * @param account
	 * @param card
	 * @throws IOException 
	 */
	private File createPDF(Customer customer, CustomerLogin customerLogin, Account account, DebitCard card) throws IOException {
		String pdfContent = "Dear Customer, \n Welcome to EDUBank. You are registered to the EDUBank with the following details: \n  ";
		pdfContent += "\n Customer Name: " + customer.getName();
		pdfContent += "\n Customer Date of Birth: "
				+ customer.getDateOfBirth();
		pdfContent += "\n Customer Email Id: " + customer.getEmailId();
		pdfContent += "\n Customer Login Name: "
				+ customerLogin.getLoginName();
		pdfContent += "\n Customer Password: " + customerLogin.getPassword();

		pdfContent += "\n\n Account Details ";
		pdfContent += "\n Account Number: " + account.getAccountNumber();
		pdfContent += "\n Balance: " + account.getBalance();
		pdfContent+="\n Account Branch Name: "+account.getBranchName();
		pdfContent+="\n Account Branch IFSC: "+account.getIfsc();

		pdfContent += "\n\n Debit card details ";
		pdfContent += "\n Card number: " + card.getDebitCardNumber();
		pdfContent += "\n CVV: " + card.getCvv();
		pdfContent += "\n PIN: " + card.getPin();
		pdfContent += "\n Valid till: " + card.getValidThru();

		pdfContent += "\n\nThanks and regards,";
		pdfContent += "\nEDUBank";
		
		return PDFUtility.createPDF(pdfContent, customer.getCustomerId().toString());
	}
}
