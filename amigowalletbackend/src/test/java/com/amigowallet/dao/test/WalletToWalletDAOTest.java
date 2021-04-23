package com.amigowallet.dao.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.WalletToWalletDAO;
import com.amigowallet.model.PaymentType;
import com.amigowallet.model.UserTransaction;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class WalletToWalletDAOTest {
	/**
	 * This attribute is used in all the test case methods to invoke the methods of {@link WalletTransferDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */

	@Rule
	public ExpectedException ee=ExpectedException.none();
	
	@Autowired
	private WalletToWalletDAO walletTransferDAO;
	/**
	 * This is a positive test case method to test
	 * {@link WalletToWalletDAO#transferToWallet(Integer userId, Double amountToTransfer, String emailIdToTransfer)}
	 * 
	 */
	
	@Test
	public void transferToWalletTestValid() throws Exception {
		
		Integer result=walletTransferDAO.transferToWallet(12121, 200d,"donette@infy.com");
		Integer expected=1;
		Assert.assertEquals(expected, result);
		
	}
	/**
	 * This is a negative test case method to test
	 * {@link WalletToWalletDAO#transferToWallet(Integer userId, Double amountToTransfer, String emailIdToTransfer)}
	 * 
	 */
	@Test
	public void transferToWalletTestInValid() throws Exception {
		ee.expect(Exception.class);
		ee.expectMessage("WalletToWalletService.INVALID_EMAIL");
		
		walletTransferDAO.transferToWallet(12121, 200d,"abc@infy.com");
		
		
	}
	/**
	 * This is a negative test case method to test
	 * {@link WalletToWalletDAO#walletDebit(UserTransaction userTransaction, Integer userId)}
	 * 
	 */
	@Test
	public void walletDebitInValid() throws Exception {
		UserTransaction userTransaction=new UserTransaction();
		Integer result=walletTransferDAO.walletDebit(userTransaction,12100);
		Integer expected=0;
		Assert.assertEquals(expected, result);
	}
	/**
	 * This is a negative test case method to test
	 * {@link WalletToWalletDAO#walletCredit(UserTransaction userTransaction, Integer userId)}
	 * 
	 */
	@Test
	public void walletCreditInValid() throws Exception {
		UserTransaction userTransaction=new UserTransaction();
		Integer result=walletTransferDAO.walletCredit(userTransaction,12100);
		Integer expected=0;
		Assert.assertEquals(expected, result);
	}
	/**
	 * This is a positive test case method to test
	 * {@link WalletToWalletDAO#walletDebit(UserTransaction userTransaction, Integer userId)}
	 * 
	 */
	@Test
	public void walletDebitValid() throws Exception {
		UserTransaction userTransaction=new UserTransaction();
		userTransaction.setAmount(1234.0);
		userTransaction.setInfo("String");
		userTransaction.setIsRedeemed('Y');
		userTransaction.setPointsEarned(0);
		PaymentType pt=new PaymentType();
		userTransaction.setPaymentType(pt);
		userTransaction.setRemarks("");
	
	Integer result=walletTransferDAO.walletDebit(userTransaction,12121);
	Integer expected=1;
		Assert.assertEquals(expected, result);
	}
	/**
	 * This is a positive test case method to test
	 * {@link WalletToWalletDAO#walletCredit(UserTransaction userTransaction, Integer userId)}
	 * 
	 */
	@Test
	public void walletCreditValid() throws Exception {
		UserTransaction userTransaction=new UserTransaction();
		userTransaction.setAmount(1234.0);
		userTransaction.setInfo("String");
		userTransaction.setIsRedeemed('Y');
		userTransaction.setPointsEarned(0);
		PaymentType pt=new PaymentType();
	userTransaction.setPaymentType(pt);
		userTransaction.setRemarks("");
	
	Integer result=walletTransferDAO.walletCredit(userTransaction,12121);
	Integer expected=1;
		Assert.assertEquals(expected, result);
	}


}
