package com.edubank.dao.test;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.NetBankingDAO;
import com.edubank.model.Transaction;
import com.edubank.model.TransactionType;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class NetBankingDAOTest {

	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link NetBankingDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private NetBankingDAO netBankingDAO;

	/**
	 * This is a positive test case method to test
	 * {@link NetBankingDAO#payByNetBanking(Transaction transaction)} method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void payByNetBankingValid() throws Exception {

		/*
		 * The following code is to create a Transaction class object with
		 * sample valid test data.
		 */
		Transaction transaction = new Transaction();
		transaction.setAccountNumber("632941935815667");
		transaction.setAmount(10.0);
		transaction.setInfo("From:- Account To:- AmigoWallet, Reason:- Payment");
		transaction.setRemarks("RemarksDATA");
		transaction.setTransactionDateTime(LocalDateTime.now());
		transaction.setTransactionMode("Internet-Banking");
		transaction.setType(TransactionType.DEBIT);

		Long transactionId = netBankingDAO.payByNetBanking(transaction);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "netBankingDAO.payByNetBanking(transaction)" which is being tested or return value is null 
		 * then the test case will not be passed.
		 */
		Assert.assertNotNull(transactionId);
	}
}
