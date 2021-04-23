package com.amigowallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.RewardPointsDAO;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;

/**
 * This is a service class having method which contains business logic
 * related to redeeming reward points.
 * 
 * @author ETA_JAVA
 *
 */
@Service(value = "rewardPointsService")
@Transactional
public class RewardPointsServiceImpl implements RewardPointsService {

	@Autowired
	private RewardPointsDAO rewardPointsDAO;

	/**
	 * This method receives the userId as argument
	 * which redeem the unredeemed reward points so that
	 * we can convert the points to wallet money.
	 * 
	 * @param userId
	 * 
	 * @throws Exception
	 */
	@Override
	public void redeemRewardPoints(Integer userId)
			throws Exception {

		/*
		 * here we are passing user id as an argument in
		 * getAllTransactionByUserId method of rewardPointsDAO and gets the user
		 * transaction details from the data base table and then returns user
		 * transaction list model object If transaction is not found in the data
		 * base then it returns null
		 */
		List<UserTransaction> userTransactions = rewardPointsDAO
				.getAllTransactionByUserId(userId);

		Integer rewardPoints = 0;

		/* here we are calculating the total reward point for the user */
		if (userTransactions != null) {
			for (UserTransaction userTransaction : userTransactions) {
				if (AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO
						.equals(userTransaction.getIsRedeemed().toString())) {
					rewardPoints += userTransaction.getPointsEarned();
				}
			}
		}

		/*
		 * here if reward points is less than 100 then points can not be
		 * redeemed and exception is thrown with message
		 * RewardPointsService.REWARD_POINTS_NOT_ENOUGH_TO_REDEEM
		 */
		if (rewardPoints < 10) {
			throw new Exception(
					"RewardPointsService.REWARD_POINTS_NOT_ENOUGH_TO_REDEEM");
		}

		/* here we are calculating the amount according to the reward points */
		Double amountToBeCredited = rewardPoints / 10.0;

		/*
		 * here we are redeeming all the reward points by passing userId as
		 * argument to redeemAllRewardPoints method of rewardPointsDAO
		 */
		rewardPointsDAO.redeemAllRewardPoints(userId);

		
		UserTransaction userTransaction = new UserTransaction();
		userTransaction.setAmount(amountToBeCredited);
		userTransaction
				.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_BY_REDEEMING_REWARD_POINTS);
		userTransaction.setPointsEarned(0);
		userTransaction
				.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES
						.charAt(0));

		/*
		 * here we are adding money to wallet according to the reward points by
		 * passing userId and userTransaction to addRedeemedMoneyToWallet method
		 * of rewardPointsDAO
		 */
		rewardPointsDAO.addRedeemedMoneyToWallet(userId, userTransaction);

	}
}
