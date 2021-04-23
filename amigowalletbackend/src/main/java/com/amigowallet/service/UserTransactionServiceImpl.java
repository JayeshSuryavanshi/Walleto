package com.amigowallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.UserTransactionDAO;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;

/**
 * This is a service class which includes methods for performing different transactions 
 * like loadMoneyFromDebitCard, loadMoneyFromNetBanking
 * 
 * @author ETA_JAVA
 *
 */
@Service("userTransactionService")
@Transactional
public class UserTransactionServiceImpl implements UserTransactionService {

	@Autowired
	private UserTransactionDAO userTransactionDAO;
	
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
	@Override
	public UserTransaction loadMoneyFromDebitCard(Double amount, Integer userId, String remarks)
	{
		
		UserTransaction userTransaction = new UserTransaction();
		
		userTransaction.setAmount(amount);
		userTransaction.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_FROM_BANK_TO_EWALLET_USING_DEBIT_CARD);
		userTransaction.setPointsEarned(0);
		userTransaction.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
		userTransaction.setRemarks(remarks);
		
		/*
		 * DAO method for loading money to the wallet is called here
		 */
		userTransaction = userTransactionDAO.loadMoney(userTransaction,userId);
		
		/*
		 * The userTransaction bean is returned here
		 */
		return userTransaction;
	}
	

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
	@Override
	public UserTransaction loadMoneyFromNetBanking(Double amount, Integer userId, String remarks) {
		
		UserTransaction userTransaction = new UserTransaction();
		
		/*
		 * A new userTransaction is created here and all the properties are populated
		 */
		userTransaction.setAmount(amount);
		userTransaction.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_FROM_BANK_TO_EWALLET_USING_NET_BANKING);
		userTransaction.setPointsEarned(0);
		userTransaction.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
		userTransaction.setRemarks(remarks);
		
		/*
		 * DAO method for loading money to the wallet is called here
		 */
		userTransaction = userTransactionDAO.loadMoney(userTransaction,userId);
		
		/*
		 * The userTransaction bean is returned here
		 */
		return userTransaction;		
	}

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
	@Override
	public UserTransaction sendMoneyToBankAccount(Double amount,
			Integer userId, String remarks) {
		UserTransaction userTransaction = new UserTransaction();
		
		/*
		 * A new userTransaction is created here and all the properties are populated
		 */
		userTransaction.setAmount(amount);
		userTransaction.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_SENT_TO_BANK_ACCOUNT_FROM_EWALLET);
		userTransaction.setPointsEarned(0);
		userTransaction.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
		userTransaction.setRemarks(remarks);
		
		/*
		 * DAO method for sending money to the wallet is called here
		 */
		userTransaction = userTransactionDAO.sendMoneyToBankAccount(userTransaction, userId);
		
		/*
		 * The userTransaction bean is returned here
		 */
		return userTransaction;	
	}

}
