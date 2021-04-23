package com.amigowallet.dao;

import com.amigowallet.model.UserTransaction;

/**
 * This is a DAO interface having method to perform CRUD operations on user and  transactions
 * on performing different transactions like loadMoney, debitMoney etc.
 * 
 * @author ETA_JAVA
 *
 */
public interface UserTransactionDAO {

	/**
	 * This method is used to add the transaction for given userId for loading the money to amigo wallet.
	 * 
	 * @param userTransaction
	 * @param userId
	 * 
	 * @return userTransaction
	 */
	UserTransaction loadMoney(UserTransaction userTransaction, Integer userId);
	
	/**
	 * This method is used to add the transaction for given userId for sending the money to a bank account.
	 * 
	 * @param userTransaction
	 * @param userId
	 * 
	 * @return userTransaction
	 */
	UserTransaction sendMoneyToBankAccount(UserTransaction userTransaction, Integer userId);

}
