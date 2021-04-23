package com.amigowallet.dao;

import java.util.List;

import com.amigowallet.model.UserTransaction;

/**
 * This Interface contains the methods responsible for interacting the database
 * with respect to reward points for the user module .
 * 
 * @author ETA_JAVA
 *
 */
public interface RewardPointsDAO {

	/**
	 * This method receives user id as argument and gets the transaction
	 * details for that user
	 * It returns user transactions list
	 * If no transaction is not found in the data base then it returns
	 * null
	 * 
	 * @param userId
	 * 
	 * @return list of user transactions
	 */
	public List<UserTransaction> getAllTransactionByUserId(Integer userId);

	/**
	 * This method is used for redeeming all the reward points
	 * It updates the isRedeemed field of the userTransactionEntity
	 * 
	 * @param userId
	 */
	public void redeemAllRewardPoints(Integer userId);

	/**
	 * This method adds the redeemed amount to the wallet
	 * and the userTransaction list is updated
	 * 
	 * @param userId
	 * @param userTranscation
	 */
	public void addRedeemedMoneyToWallet(Integer userId,
			UserTransaction userTransaction);
}
