package com.amigowallet.api;

import java.util.ArrayList;
import java.util.List;

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

import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;
import com.amigowallet.model.User;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.service.DebitCardService;
import com.amigowallet.service.UserTransactionService;

/**
 * This class has methods to handle requests related to Debit Card.
 *
 * @author ETA_JAVA
 * 
 */
@RestController
@CrossOrigin
@RequestMapping("DebitCardAPI")
public class DebitCardAPI {
	
	/**
	 * This attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;

	@Autowired
	DebitCardService debitcardService;
	
	@Autowired
	UserTransactionService userTransactionService;
	
	static Logger logger = LogManager.getLogger(DebitCardAPI.class.getName());
	
	/**
	 * This method is used for deleting a saved card<br>
	 * This method receives the cardId in POST request<br>
	 * It calls the deleteCard method of DebitCardService
	 * 
	 * @param String
	 * 
	 * @return ResponseEntity<Card> populated with 
	 * 					successMessage,
	 * 								if successfully deleted
	 * 					errorMessage,
	 * 								if any error occurs
	 */
	@RequestMapping(value = "deleteCard", method = RequestMethod.POST)
	public ResponseEntity<Card> deleteCard(@RequestBody String cardId){
		Card cardToClient=null;
		
		logger.info("DELETING A CARD, CARD ID : "+cardId);
		
		Card card = new Card();
		
		/*
		 * The cardId is converted to an Integer from String 
		 * and set to a new card bean
		 */
		card.setCardId(Integer.parseInt(cardId));
		
		debitcardService.deleteCard(card);
		
		logger.info("CARD DELETED SUCCESSFULLY, CARD ID : "+cardId);
		
		String message = environment.getProperty("DebitCardAPI.SUCCESSFULLY_DELETED");
		
		/*
		 * The following code populates a card model with a success message
		 */
		cardToClient = new Card();
		cardToClient.setSuccessMessage(message);
		ResponseEntity<Card> responseEntity = new ResponseEntity<Card>(cardToClient, HttpStatus.OK);
		 
		return responseEntity;
	}
	
	/**
	 * This method is used for adding a new card to AmigoWallet<br>
	 * It also fetches the details of all banks<br>
	 * This method receives the card model and userId in POST request<br>
	 * It calls the addCard and fetchAllBankDetails methods of DebitCardService
	 * 
	 * @param card
	 * @param userId
	 * 
	 * @return ResponseEntity<Card> populated with 
	 * 					successMessage,
	 * 								if successfully deleted
	 * 					errorMessage,
	 * 								if any error occurs
	 *
	 */
	@RequestMapping(value = "addCard/{userId}", method = RequestMethod.POST)
	public ResponseEntity<Card> addCard(@RequestBody Card card, @PathVariable("userId") Integer userId){
		Card cardToClient=null;
		ResponseEntity<Card> responseEntity=null;
		try 
		{
			logger.info("USER TRYING TO ADD A NEW CARD, USER ID : "+userId);
			
			cardToClient = debitcardService.addCard(card,userId);
			
			String message = environment.getProperty("DebitCardAPI.SUCCESSFULLY_ADDED");
			
			logger.info("NEW CARD ADDED SUCCESSFULLY, USER ID : "+userId);
			
			/*
			 * The following code populates a card model with a success message
			 */
			cardToClient.setSuccessMessage(message);
			responseEntity=new ResponseEntity<Card>(cardToClient, HttpStatus.CREATED);
		} 
		catch (Exception e) {	
			
			throw new ResponseStatusException(HttpStatus.CONFLICT, environment.getProperty(e.getMessage()));
		}
		return responseEntity;
	}
	
	/**
	 * This method is used for fetching the 
	 * details of all the banks
	 * It calls fetchAllBankDetails method of DebitCardService
	 * 
	 * @return ResponseEntity<List<Bank>>
	 * 					In case of success,
	 * 									returns a list of bank models<br>
	 * 					In case of any error,
	 * 									returns an empty list
	 */
	@RequestMapping(value = "fetchBankDetails", method = RequestMethod.GET)
	public ResponseEntity<List<Bank>> fetchAllBankDetails(){
		ResponseEntity<List<Bank>> responseEntity=null;
		List<Bank> bankList = new ArrayList<>();	
		
		bankList=debitcardService.fetchAllBankDetails();
		
		/*
		 * List populated with bank models is returned
		 */
		responseEntity=new ResponseEntity<List<Bank>>(bankList, HttpStatus.OK);	
		
		return responseEntity;
	}
	
	/**
	 * This method is called for loading the money to the AmigoWallet 
	 * using a DebitCard
	 * Debit card is verified and then money is debited from the bank
	 * This method receives the user model and amount in POST request<br>
	 * 
	 * @param user
	 * @param amount
	 * 
	 * @return ResponseEntity<UserTransaction>
	 * It is populated with errorMessage,if any error occurs
	 */
	@RequestMapping(value="loadMoneyDebitCard/{amount:.+}", method = RequestMethod.POST)
	public ResponseEntity<UserTransaction> loadMoneyFromDebitCard(@RequestBody User user,  @PathVariable("amount")  Double amount){
	
		logger.info("USER TRYING TO LOAD MONEY FROM BANK USING DEBIT CARD, USER ID : "+user.getUserId());
		
		UserTransaction userTrasaction = userTransactionService.loadMoneyFromDebitCard(amount,user.getUserId(), null);
		
		logger.info("MONEY LOADED FROM BANK USING DEBIT CARD, USER ID : "+user.getUserId());
		
		return new ResponseEntity<UserTransaction>(userTrasaction, HttpStatus.ACCEPTED);
		
	}
	

}
