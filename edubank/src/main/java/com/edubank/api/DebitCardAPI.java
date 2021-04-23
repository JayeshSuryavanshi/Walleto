package com.edubank.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.edubank.model.DebitCard;
import com.edubank.model.Transaction;
import com.edubank.model.TransactionType;
import com.edubank.service.AccountService;
import com.edubank.service.DebitCardService;
import com.edubank.service.TransactionService;


/**
 * The {@code DebitCardAPI} class has methods to handle the requests related to
 * DebitCards of Customers
 * 
 * @author ETA_JAVA
 */
@CrossOrigin
@RestController
@RequestMapping("DebitCardAPI")
public class DebitCardAPI 
{
	/**
	 * The attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */ 
	@Autowired
	private Environment environment;

	@Autowired
	DebitCardService debitCardService;
	
	@Autowired
	AccountService accountService;

	@Autowired
	TransactionService transactionService;
	
	static Logger logger = LogManager.getLogger(DebitCardAPI.class);
	
	/**
	 * This method is a web service api method used to verify the debit card
	 * details coming in the HTTP request. This method is exposed as a web
	 * service to make it available for other applications to consume
	 * 
	 * @param debitCard
	 * 
	 * @return the status of the debit card
	 */
	@RequestMapping(value = { "cardVerification" }, method = RequestMethod.POST)
	public ResponseEntity<String> verifyCardDetails(@RequestBody DebitCard debitCard) {
		String status = null;
		try {

			logger.info("CARD VERIFICATION REQUEST FOR DEBIT CARD NO : "+debitCard.getDebitCardNumber()+", CARD HOLDER NAME : "+debitCard.getCardHolderName());

			/*
			 * The following code verify debitCard values corresponding
			 * to the debitCard sent
			 */
			debitCardService.verifyCardDetails(debitCard);
			
			logger.info("VALID CARD: CARD VERIFICATION SUCCESS FOR DEBIT CARD NO : "+debitCard.getDebitCardNumber()+", CARD HOLDER NAME : "+debitCard.getCardHolderName());
			
			/*
			 * the following code fetches the message from
			 * configuration.properties file using environment object
			 */
			status = environment.getProperty("DebitCardController.DEBITCARD_VERIFICATION_SUCCESS");

			/*
			 * ResponseEntity is created and returned using the message fetched
			 * in previous step with HttpStatus as OK
			 */
			return new ResponseEntity<String>(status, HttpStatus.ACCEPTED);

		} 
		catch (Exception e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, environment.getProperty(e.getMessage()));
		}
	}
	
	/**
	 * This method is a web service API method used to make debit card payment.
	 * This method is exposed as a web service to make it available for other applications to consume. <br>
	 * 
	 * In the annotation given for this method header, "cardPayment/{amount:.+}" the notation "amount:.+"
	 * indicates that decimal point numbers can also be received in the request.
	 * 
	 * @param debitCard
	 * @param amount
	 * 
	 * @return the status of the debit card payment
	 */
	@RequestMapping(value = { "cardPayment/{amount:.+}" }, method = RequestMethod.POST)
	public ResponseEntity<String> cardPayment(@RequestBody DebitCard debitCard,  @PathVariable("amount")  Double amount)	
	{
		logger.info("PAY THROUGH CARD REQUEST FOR DEBIT CARD NO : "+debitCard.getDebitCardNumber()+", CARD HOLDER NAME : "+debitCard.getCardHolderName()+", AMOUNT : "+amount);
		String status=null;
		try 
		{
			Integer mappingId = debitCardService.verifyPinAndGetAccountCustomerMappingId
								(debitCard.getDebitCardNumber(), debitCard.getPin());
			
		
			String accountNumber = accountService.getAccountNumberFromMappingId(mappingId);
			
			accountService.debitAmount(accountNumber,amount);
			
			Transaction transaction = new Transaction();
			

			transaction.setAccountNumber(accountNumber);
			transaction.setAmount(amount);
			transaction.setType(TransactionType.DEBIT);
			transaction.setInfo("From:- Account To:- AmigoWallet, Reason:- Payment");
			transaction.setTransactionMode("Debit-Card");
			
			transactionService.addTransaction(transaction, "AmigoWallet");
			
			/*
			 * the following code fetches the message from
			 * configuration.properties file using environment object
			 */
			status=environment.getProperty("DebitCardAPI.DEBITCARD_PAYMENT_SUCCESS");

			logger.info("PAYMENT DONE THROUGH CARD FOR DEBIT CARD NO : "+debitCard.getDebitCardNumber()+", CARD HOLDER NAME : "+debitCard.getCardHolderName()+", AMOUNT : "+amount);

			/* ResponseEntity is created and returned using the message fetched
			 * in previous step with HttpStatus as OK
			 */
			return new ResponseEntity<String>(status, HttpStatus.OK);
		} 
		catch (Exception e) 
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, environment.getProperty(e.getMessage()));
		}		
	}
}
