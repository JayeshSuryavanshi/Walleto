package com.amigowallet.api;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amigowallet.model.User;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.service.TransactionHistoryService;

/**
 * This class has methods to handle requests related to transaction history.
 * 
 *  @author KARAN RAJ SINGH
 * 
 */
@RestController
@CrossOrigin
@RequestMapping("TransactionHistoryAPI")
public class TransactionHistoryAPI {

	/**
	 * This attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;
	
	@Autowired
	TransactionHistoryService transactionHistoryService;
	
	static Logger logger = LogManager.getLogger(TransactionHistoryAPI.class.getName());
	
	/**
	 * This method receives the user model in POST request and calls
	 * TransactionHistoryService method to fetch transaction history. <br>
	 * 
	 * @param user
	 * 
	 * @return ResponseEntity<List<UserTransaction>>
	 */
	@RequestMapping(value = "getAllTransactions", method = RequestMethod.POST)
	public ResponseEntity<List<UserTransaction>> getAllTransactions(@RequestBody User user){
		Integer userId=user.getUserId();
		ResponseEntity<List<UserTransaction>> responseEntity = null;
		try {
			logger.info("USER TRYING TO FETCH TRANSACTION HISTORY, USER ID : "+user.getUserId());
			
			List<UserTransaction> userTransactions = transactionHistoryService.getAllTransactionByUserId(userId);
			
			logger.info("FETCHING OF TRANSACTION HISTORY SUCCESSFULLY, USER ID : "+user.getUserId());
			
			responseEntity = new ResponseEntity<List<UserTransaction>>(userTransactions, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, environment.getProperty(e.getMessage()));
		}
		return responseEntity;
	}
	
}
