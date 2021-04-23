package com.edubank.controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.edubank.model.Account;
import com.edubank.model.DebitCard;
import com.edubank.service.AccountService;
import com.edubank.service.DebitCardService;

/**
 * The {@code DebitCardController} class has methods to handle change debit card pin requests
 * coming in the HTTP request.
 * 
 * @author ETA_JAVA
 *
 */
@Controller
public class DebitCardController {
	/**
	 * The attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;

	@Autowired
	AccountService accountService;
	
	@Autowired
	DebitCardService debitCardService;
	
	static Logger logger = LogManager.getLogger(DebitCardController.class);

	/**
	 * This method is used to generate the changeDebitCardPin view
	 * 
	 * @return ModelAndView
	 * 
	 */
	@RequestMapping(value = { "/changeDebitCardPin" })
	public ModelAndView getChangeDebitCardView(HttpSession httpSession) {

		/*
		 * model is the data which we are retrieving from the session and view
		 * is returned to the client side to display the values
		 */
		ModelAndView model = new ModelAndView("error");
		try {
			/*
			 * httpSession.getAttribute Returns the object bound with the
			 * specified name in this session, or null if no object is bound
			 * under the name ,here if we are retrieving the value from session
			 * then we are adding a model view else we are throwing proper error
			 * message
			 */
			if (httpSession.getAttribute("customerId") != null) {
				Integer customerId = (Integer) httpSession.getAttribute("customerId");
				model = new ModelAndView("changeDebitCardPin");
				
				Account account = accountService
						.getAccountByCustomerId(customerId);
				
				/*
				 * converting the balance from 4 decimal places to 2 decimal places
				 */
				Double balance = account.getBalance();
				Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
				
				model.addObject("balance", balanceTillTwoDecimalPlaces);
				
			} else {
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}
		} catch (Exception e) {
			model.addObject("message", environment.getProperty(e.getMessage()));
		}

		return model;
	}

	/**
	 * This method is used to change the debit card pin of the logged in
	 * customer, it receives current pin, newPin and confirmNewPin in the debit
	 * card. The customerId is fetched from the httpSession.
	 * 
	 * @param debitCard
	 * @param httpSession
	 * 
	 * @return ModelAndView
	 * 
	 */
	@RequestMapping(value = { "/changeDebitCardPin" }, method = RequestMethod.POST)
	public ModelAndView changeDebitCardPin(DebitCard debitCard, HttpSession httpSession) {

		ModelAndView model = new ModelAndView("error");
		
		try
		{
			Integer customerId = (Integer) httpSession.getAttribute("customerId");
			
			logger.info("DEBIT CARD PIN CHANGE REQUEST. CUSTOMER ID "+customerId);
			
			if (customerId != null) {
				model = new ModelAndView("changeDebitCardPin");

				/*
				 * This method is used to change the debit card pin, it receives
				 * a DebitCard object reference, validates the details by
				 * calling corresponding validator class method, Then it
				 * verifies the current pin. If the data is valid, it updates
				 * the debit card pin for the current customer
				 */
				debitCardService.changeDebitCardPin(customerId, debitCard);
				
				logger.info("DEBIT CARD PIN CHANGED SUCCESSFULLY. CUSTOMER ID "+customerId);
				
				/*
				 * This method is used to get the account details 
				 * so that we can display the updated balance to the user
				 */
				Account account = accountService.getAccountByCustomerId(customerId);
				
				/*
				 * converting the balance from 4 decimal places to 2 decimal places
				 */
				Double balance = account.getBalance();
				Double balanceTillTwoDecimalPlaces = Math.round(balance*100)/100.0;
				
				model.addObject("balance", balanceTillTwoDecimalPlaces);
		
				model.addObject("successMessage",
						environment.getProperty("DebitCardController.DEBIT_CARD_PIN_CHANGE_SUCCESS"));
			}
			else 
			{
				throw new Exception(
						"Controller.UNAUTHORSED_USER");
			}

		}
		catch (Exception e) {
			model.addObject("pin", debitCard.getPin());
			model.addObject("newPin", debitCard.getNewPin());
			model.addObject("confirmNewPin", debitCard.getConfirmNewPin());

			model.addObject("errorMessage",
					environment.getProperty(e.getMessage()));
		}

		return model;
	}
}
