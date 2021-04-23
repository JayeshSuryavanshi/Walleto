package com.amigowallet.api;

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

import com.amigowallet.model.User;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.service.UserTransactionService;

@RestController
@CrossOrigin
@RequestMapping("BankTrasnferAPI")
public class BankTransferAPI {

	/**
	 * This attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;

	@Autowired
	UserTransactionService userTransactionService;

	static Logger logger = LogManager.getLogger(DebitCardAPI.class.getName());

	/**
	 * This method is called for sending money from the AmigoWallet to the bank
	 * using a BankAccount Bank account is verified and then money is credited
	 * to the bank This method receives the user model and amount in POST
	 * request<br>
	 * 
	 * @param user
	 * @param amount
	 * 
	 * @return ResponseEntity<UserTransaction> It is populated with
	 *         errorMessage,if any error occurs
	 */
	@RequestMapping(value = "sendMoneyBankAccount/{amount:.+}", method = RequestMethod.POST)
	public ResponseEntity<UserTransaction> sendMoneyToBankAccount(
			@RequestBody User user, @PathVariable("amount") Double amount) {

		logger.info("USER TRYING TO SEND MONEY TO BANK ACCOUNT, USER ID : "
				+ user.getUserId());

//		UserTransaction userTrasaction = null;
		UserTransaction userTrasaction = userTransactionService.sendMoneyToBankAccount(amount, user.getUserId(), null);

		logger.info("MONEY SENT TO BANK ACCOUNT, USER ID : " + user.getUserId());

		return new ResponseEntity<UserTransaction>(userTrasaction,
				HttpStatus.ACCEPTED);

	}

}
