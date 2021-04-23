package com.edubank.controller;

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
import com.edubank.model.Transaction;
import com.edubank.model.TransactionType;
import com.edubank.service.AccountService;
import com.edubank.service.CustomerService;
import com.edubank.service.TransactionService;

/**
 * This controller class has methods to handle requests related to adding money to
 * customer account by teller.
 * 
 * @author ETA_JAVA
 *
 */
@Controller
public class AddMoneyController {

	@Autowired
	private Environment environment;

	@Autowired
	AccountService accountService;
	
	@Autowired
	TransactionService transactionService;
	
	@Autowired
	CustomerService customerService;
	
	
	private Long numberOfCustomers;

	static Logger logger = LogManager.getLogger(AddMoneyController.class);
	
	@RequestMapping(value = { "/customerAddMoney" })
	public ModelAndView getAddMoneyView() {
		ModelAndView model = new ModelAndView("customerAddMoney");
		return model;
	}

	/**
	 * This method is used to add money to the customer account and redirects to
	 * teller's home page with a success message else show proper error message
	 * on the current page Note: It can accept only post request
	 * 
	 * @param accountNumber
	 * @param amount
	 * @param httpSession
	 * 
	 * @return ModelAndView object redirecting to home page<br>
	 */
	@RequestMapping(value = "addMoney", method = RequestMethod.POST)
	public ModelAndView addMoney(@RequestParam("accountNumber") String accNo,
			@RequestParam("amount") Double amt, HttpSession httpSession) {
		ModelAndView model = new ModelAndView("customerAddMoney");
		Integer tellerId = null;
		try
		{
			logger.info("ADD MONEY REQUEST... ACCOUNT NUMBER : "+accNo+", AMMOUNT : "+amt+", WHO IS ADDING - TELLER ID : "+httpSession.getAttribute("tellerId"));

			/* httpSession.getAttribute Returns the object bound with the
			 * specified name in this session, or null if no object is bound
			 * under the name.
			 */
			if (httpSession.getAttribute("tellerId") != null) {
				tellerId = (Integer) httpSession.getAttribute("tellerId");
			}
			
			/* This method is used to add money to a customer account */
			accountService.addMoney(accNo, amt);

			Transaction transaction = new Transaction();

			transaction.setAccountNumber(accNo);
			transaction.setAmount(amt);
			transaction.setType(TransactionType.CREDIT);
			transaction.setInfo("From: EDUBank To: Account Reason: LoadMoney");
			transaction.setTransactionMode("Teller");

			/*
			 * This method is used to add the transaction to the database by
			 * calling the corresponding DAO method
			 */
			Transaction transaction2 = transactionService.addTransaction(transaction, "" + tellerId);
			
			numberOfCustomers = customerService.getNoOfCustomers();
			
			
			/*
			 * here we are setting the values to model object which eill be sent
			 * to client
			 */
			model.addObject("addMoneyMessage", environment
					.getProperty("AddMoneyController.ADD_MONEY_SUCCESS")+transaction2.getTransactionId());

			model.addObject("noOfCustomers", numberOfCustomers);
			
			logger.info("MONEY ADDED SUCCFULLY TO ACC NO : "+accNo+", AMOUNT : "+amt);

		} catch (Exception exception) {
			model.addObject("addMoneyErrorMessage",
					environment.getProperty(exception.getMessage()));
		}

		return model;

	}

	@RequestMapping(value = "findAccount", method = RequestMethod.POST)
	public ModelAndView findAccountByAccountNumber(@RequestParam String accountNumber)
	{
		ModelAndView modelAndView = new ModelAndView("customerAddMoney");
		try
		{
			Account account = accountService.getAccountByAccountNumber(accountNumber);
			modelAndView.addObject("accountNumber", accountNumber);
			modelAndView.addObject("accountHolderName", account.getAccountHolderName());

			
		}
		catch(Exception e)
		{
			modelAndView.addObject("accountNumber", accountNumber);
			modelAndView.addObject("addMoneyErrorMessage", environment.getProperty(e.getMessage()));
		}
		
		return modelAndView;
		
	}
}
