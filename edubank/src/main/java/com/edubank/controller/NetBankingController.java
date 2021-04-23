package com.edubank.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.edubank.service.NetBankingService;

/**
 * This controller class has methods to handle net banking related requests. 
 * 
 * @author ETA_JAVA
 *
 */
@Controller
public class NetBankingController {

	@Autowired
	private Environment environment;

	@Autowired
	NetBankingService netBankingService;

	@Autowired
	CustomerLoginService customerLoginService;
	
	@Autowired
	AccountService accountService;
	
	private Account account;
	private Double amount;

	private String host;
	private String port;
	private String requestPage;
	private String protocol;
	
	static Logger logger = LogManager.getLogger(NetBankingController.class);
	
	
	/**
	 * This method is used to generate the net banking view
	 * 
	 * @return ModelAndView
	 * 
	 */
	@CrossOrigin
	@RequestMapping(value = { "/netBankingLoginView/{amt:.+}" })
	public ModelAndView netBankingLoginView(@PathVariable("amt") Double amount,
			@RequestParam("protocol") final String protocol,
			@RequestParam("host") final String host,
			@RequestParam("port") final String port,
			@RequestParam("requestPage") final String requestPage,
			HttpServletRequest request) {
		this.amount = amount;
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.requestPage = requestPage;
		logger.info("INTERNET BANKING LOGIN REQUEST FROM HOST : "+host+", PORT : "+port+", PROTOCOL : "+protocol+",AND AMOUNT : "+amount);
		ModelAndView model = new ModelAndView("netBankingLogin");
		return model;
	}

	/**
	 * This method is used to authenticate the login for net banking
	 * 
	 * @param customerLogin
	 * @param httpSession
	 * 
	 * @return modelAndView redirecting to netBankingHome page if the
	 *         credentials are valid, redirecting to the same page if
	 *         credentials are invalid or account is locked, redirecting to the
	 *         error page is any error occurs while execution
	 */
	@RequestMapping(value = { "/netBankingLogin", "/netBankingLoginView/netBankingLogin" }, method = RequestMethod.POST)
	public ModelAndView netBankingLogin(CustomerLogin customerLogin,
			HttpSession httpSession) {
		ModelAndView model = new ModelAndView("error");

		logger.info("INTERNET BANKING CUSTOMER LOGIN ATTEMPT FOR LOGIN NAME : "+customerLogin.getLoginName());

		try {
			/*
			 * This method is used to authenticate the login credentials entered
			 * by customer. It checks the loginName and password with input
			 * field validation, if valid it verifies the credential with the
			 * database. If verified, it checks whether the account is locked.
			 */
			Customer customer = customerLoginService
					.authenticateCustomerLogin(customerLogin);
			
			logger.info("INTERNET BANKING CUSTOMER LOGIN, AUTHENTICATION SUCCESS, LOGIN NAME : "+customerLogin.getLoginName());
			
			/*
			 * model is the data which we are retrieving from the session and
			 * view is returned to the client side to display the values
			 */
			model = new ModelAndView("netBankingHome");

			/*
			 * Binds an object to this session, using the name specified. If an
			 * object of the same name is already bound to the session, the
			 * object is replaced.
			 * 
			 * After this method executes, and if the new object implements
			 * HttpSessionBindingListener, the container calls
			 * HttpSessionBindingListener.valueBound. The container then
			 * notifies any HttpSessionAttributeListeners in the web
			 * application.
			 * 
			 * If an object was already bound to this session of this name that
			 * implements HttpSessionBindingListener, its
			 * HttpSessionBindingListener.valueUnbound method is called.
			 * 
			 * If the value passed in is null, this has the same effect as
			 * calling removeAttribute().
			 */
			httpSession.setAttribute("customerId", customer.getCustomerId());
			account = accountService.getAccountByCustomerId(customer
					.getCustomerId());

			/*
			 * here we are setting the values to model object which will be sent
			 * to client
			 */
			model.addObject("name", customer.getName());
			
			/*
			 * converting the balance from 4 decimal places to 2 decimal places
			 */
			Double balance = account.getBalance();
			Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
			
			model.addObject("balance", balanceTillTwoDecimalPlaces);
			model.addObject("amount", amount);

			logger.info("INTERNET BANKING CUSTOMER LOGIN, BALANCE ADDED TO VIEW, LOGIN NAME : "+customerLogin.getLoginName());

			/* here we are checking for the sufficient balance in the account if
			 * amount is insufficient the proper error message is thrown
			 */
			if (account.getBalance() < amount) {
				model.addObject("errorMessage",
						environment.getProperty("NetBankingController.INSUFFICIENT_BALANCE"));
			}
		} catch (Exception e) {
			model = new ModelAndView("netBankingLogin");
			if (e.getMessage().contains("LoginService")
					|| e.getMessage().contains("LoginValidator")) {
				
				model.addObject("loginName", customerLogin.getLoginName());
			}

			model.addObject("message", environment.getProperty(e.getMessage()));
		}
		return model;
	}

	/**
	 * This method is used to make payment by net banking
	 * 
	 * @param remarks
	 * @param httpSession
	 * 
	 * @return String
	 * 
	 */
	@RequestMapping(value = { "/payByNetBanking" }, method = RequestMethod.POST)
	public String payByNetBanking(String remarks, HttpSession httpSession) {
		
		String redirectTo=null;
		try
		{
			redirectTo = "redirect:"+this.protocol+"//" + this.host + ":" + this.port + "/" +"AW/"+ "#/"
					+ this.requestPage;
			logger.info("PAYMENT REQUEST THROUGH INTERNET BANKING FROM CUSTOMER ID : "+httpSession.getAttribute("customerId")+", FOR AMMOUNT : "+amount);
			String accountNumber = account.getAccountNumber();
			
			Transaction transaction =new Transaction();
			transaction.setAmount(amount);
			transaction.setAccountNumber(accountNumber);
			transaction.setRemarks(remarks);

			/*
			 * This method is used to make payment by net banking, It sets the
			 * required details to the transaction object and sends to the updated
			 * object to the corresponding dao
			 */
			Long transactionId = netBankingService.payByNetBanking(transaction);

			String amountstr = amount.toString().replaceAll("\\.", "_");
			
			redirectTo = redirectTo + "/" + amountstr + "/" + transactionId;
			
			logger.info("PAYMENT DONE THROUGH INTERNET BANKING FROM CUSTOMER ID : "+httpSession.getAttribute("customerId")+", FOR AMMOUNT : "+amount);
			logger.info("Redirecting to : "+redirectTo);
		}
		catch(Exception e)
		{
			redirectTo = redirectTo + "/" + amount.toString().replaceAll("\\.", "_") + "/" + e.getMessage();
		}
		
		return redirectTo;
		
	}

	@RequestMapping(value = { "/netBankingPaymentCancled" })
	public String netBankingPaymentCancled() {
		logger.info("INTERNET BANKING REQUEST CANCLED");
		String redirectTo = "redirect:"+ this.protocol +"//" + this.host + ":" + this.port + "/" + "#/"
				+ this.requestPage;
		
		String amountstr = amount.toString().replaceAll("\\.", "_");
		
		String errorMessage = "TRANSACTION_CANCLED";
		redirectTo = redirectTo + "/" + amountstr + "/" + errorMessage;
		return redirectTo;
	}
}
