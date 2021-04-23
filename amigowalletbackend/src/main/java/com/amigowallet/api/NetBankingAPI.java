package com.amigowallet.api;

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

import com.amigowallet.model.User;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.service.UserTransactionService;

/**
 * This class has methods to handle net banking requests
 *
 *  @author ETA_JAVA
 *  
 */
@CrossOrigin
@RestController
@RequestMapping("NetBankingAPI")
public class NetBankingAPI {
	
	/**
	 * This attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;
	
	@Autowired
	UserTransactionService userTransactionService;
	
	static Logger logger = LogManager.getLogger(NetBankingAPI.class.getName());
	
	/**
	 * This method is called for loading the money to AmigoWallet using net banking 
	 * Net Banking credentials are verified and
	 * then money is debited from bank
	 * 
	 * This method receives the user model in POST request
	 * 
	 * @param user
	 * 
	 */
	@RequestMapping(value = "loadMoneyNetBanking", method = RequestMethod.POST)
	public ResponseEntity<UserTransaction> loadMoneyThroughNetBanking(@RequestBody User user)
	{
			logger.info("USER TRYING TO LOAD MONEY FROM BANK USING NET BANKING, USER ID : "+user.getUserId());
			
			UserTransaction userTrasaction = userTransactionService.loadMoneyFromNetBanking
											(user.getBalance(), user.getUserId(), "");
			
			logger.info("MONEY LOADED FROM BANK USING NET BANKING, USER ID : "+user.getUserId());
			
			return new ResponseEntity<UserTransaction>(userTrasaction, HttpStatus.OK);	
	}
	
}
