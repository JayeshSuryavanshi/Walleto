package com.amigowallet.service;

/**
 * This is a service interface having method which contains business logic
 * related to redeeming reward points.
 * 
 * @author ETA_JAVA
 *
 */
public interface RewardPointsService {

	/**
	 * this method receives the userId as argument
	 * which redeem the unredeemed reward points so that
	 * we can convert the points to wallet money.
	 * 
	 * @param userId
	 * 
	 * @throws Exception
	 */
	public void redeemRewardPoints(Integer userId)
			throws Exception;
}
