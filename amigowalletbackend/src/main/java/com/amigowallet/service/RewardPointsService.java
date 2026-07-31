package com.amigowallet.service;

import com.amigowallet.dto.MoneyTransactionResponse;

/**
 * Reward-points redemption.
 *
 * @author ETA_JAVA
 */
public interface RewardPointsService {

	/**
	 * Redeems all of the authenticated user's non-redeemed (SUCCESS) reward points
	 * to wallet money, in one locked, atomic transaction (prevents concurrent
	 * double-redeem).
	 *
	 * @param userId the acting user (from the JWT)
	 * @return message + amount credited + new balance
	 */
	MoneyTransactionResponse redeemRewardPoints(Integer userId);
}
