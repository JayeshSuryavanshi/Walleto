package com.amigowallet.service;

import com.amigowallet.model.UserTransaction;

/**
 * This is a service interface which includes methods for performing different transactions 
 * like loadMoneyFromDebitCard, loadMoneyFromNetBanking
 * 
 * @author ETA_JAVA
 *
 */
public interface UserTransactionService {

	/**
	 * This method id used to do load money i.e. credit money to the amigo wallet which is called after 
	 * debiting the money from bank account through debit card banking
	 *
	 * @param amount
	 * @param userId
	 * @param remarks
	 * 
	 * @return userTransaction
	 */
	UserTransaction loadMoneyFromDebitCard(Double amount, Integer userId, String remarks);

	/**
	 * This method id used to do load money i.e. credit money to the amigo wallet which is called after 
	 * debiting the money from bank account through net banking
	 * 
	 * @param amount
	 * @param userId
	 * @param remarks
	 * 
	 * @return userTransaction
	 */
	UserTransaction loadMoneyFromNetBanking(Double amount, Integer userId, String remarks);
	
	/**
	 * This method id used to send money i.e. debit money from the amigo wallet which is called before
	 * crediting the money to bank account.
	 * 
	 * @param amount
	 * @param userId
	 * @param remarks
	 * 
	 * @return userTransaction
	 */
	UserTransaction sendMoneyToBankAccount(Double amount, Integer userId, String remarks);
}
