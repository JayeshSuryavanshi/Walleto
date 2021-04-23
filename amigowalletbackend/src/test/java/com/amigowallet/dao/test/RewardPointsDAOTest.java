package com.amigowallet.dao.test;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.RewardPointsDAO;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class RewardPointsDAOTest {

	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link RewardPointsDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private RewardPointsDAO rewardPointsDAO;

	@Test
	public void getAllTransactionByUserIdValidUserId() {
		List<UserTransaction> list = rewardPointsDAO
				.getAllTransactionByUserId(12121);
		Assert.assertNotNull(list);
	}
	
	@Test
	public void redeemAllRewardPointsValidUserId()
	{
		rewardPointsDAO.redeemAllRewardPoints(12121);
		Assert.assertTrue(true);
	}
	
	@Test
	public void addRedeemedMoneyToWalletValid()
	{
		UserTransaction userTransaction = new UserTransaction();
		userTransaction.setAmount(123.6);
		userTransaction
				.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_BY_REDEEMING_REWARD_POINTS);
		userTransaction.setPointsEarned(0);
		userTransaction.setTransactionDateTime(LocalDateTime.now());
		userTransaction.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));			
		rewardPointsDAO.addRedeemedMoneyToWallet(12121, userTransaction);
	}
}
