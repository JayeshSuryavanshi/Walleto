package com.amigowallet.service;

import java.util.List;

import com.amigowallet.model.UserTransaction;

/**
 * This is a service interface having method which contains business logic
 * related to fetching transaction history.
 * 
 * @author KARAN RAJ SINGH
 *
 */
public interface TransactionHistoryService {
	
	/**
	 * this method receives the userId as argument
	 * which retrieves all the transactions details
	 * done by the user/customer.
	 * 
	 * @param userId
	 * 
	 * @return userTransactions
	 * 
	 * @throws Exception
	 */
	public List<UserTransaction> getAllTransactionByUserId(Integer userId) throws Exception;
}
