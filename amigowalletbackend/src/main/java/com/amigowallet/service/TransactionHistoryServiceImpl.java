package com.amigowallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.RewardPointsDAO;
import com.amigowallet.model.UserTransaction;

/**
 * This is a service class having method which contains business logic
 * related to fetching transaction history.
 * 
 * @author KARAN RAJ SINGH
 *
 */
@Service(value = "transactionHistoryService")
@Transactional
public class TransactionHistoryServiceImpl implements TransactionHistoryService{

	@Autowired
	private RewardPointsDAO rewardPointsDAO;
	
	/**
	 * this method receives the userId as argument
	 * which retrieves all the transactions details
	 * done by the user/customer.
	 * 
	 * @param userId
	 * 
	 * @return list of user transaction
	 * 
	 * @throws Exception
	 */
	@Override
	public List<UserTransaction> getAllTransactionByUserId(Integer userId) throws Exception {
		
		/*
		 * here we are passing user id as an argument in
		 * getAllTransactionByUserId method of rewardPointsDAO and gets the user
		 * transaction details from the data base table and then returns user
		 * transaction list model object If transaction is not found in the data
		 * base then it returns null
		 */
		List<UserTransaction> userTransactions = rewardPointsDAO.getAllTransactionByUserId(userId);
		
		/*
		 * if there is no transaction data in database then exception is thrown with
		 * message TransactionHistoryService.NO_TRANSACTIONS_FOUND
		 */
		if (userTransactions==null)
			throw new Exception("TransactionHistoryService.NO_TRANSACTIONS_FOUND");
		
		return userTransactions;
	}
}
