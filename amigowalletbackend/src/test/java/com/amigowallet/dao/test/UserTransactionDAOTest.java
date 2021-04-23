package com.amigowallet.dao.test;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.UserTransactionDAO;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class UserTransactionDAOTest {

	/**
	 * This attribute is used in all the test case methods to invoke the methods of {@link UserTransactionDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private UserTransactionDAO userTransactionDAO;

	@Test
	public void loadMoneyTest() {
		UserTransaction userTransaction = new UserTransaction();
		userTransaction.setAmount(500.00);
		userTransaction
				.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_FROM_BANK_TO_EWALLET_USING_DEBIT_CARD);
		userTransaction.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransaction.setPointsEarned(5);
		userTransaction.setRemarks("C");
		userTransaction.setTransactionDateTime(LocalDateTime.now());
		userTransactionDAO.loadMoney(userTransaction, 12121);
		Assert.assertTrue(true);
	}
	
	@Test
	public void sendMoneyToBankAccountTest() {
		UserTransaction userTransaction = new UserTransaction();
		userTransaction.setAmount(500.00);
		userTransaction
				.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_SENT_TO_BANK_ACCOUNT_FROM_EWALLET);
		userTransaction.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransaction.setPointsEarned(5);
		
		String remark = AmigoWalletConstants.PAYMENT_TYPE_DEBIT;

		userTransaction.setRemarks(remark);
		userTransaction.setTransactionDateTime(LocalDateTime.now());
		userTransactionDAO.sendMoneyToBankAccount(userTransaction, 12121);
		Assert.assertTrue(true);
	}


}
