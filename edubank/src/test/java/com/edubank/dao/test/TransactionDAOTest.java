package com.edubank.dao.test;

import java.time.LocalDate;
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

import com.edubank.dao.TransactionDAO;
import com.edubank.model.Transaction;
import com.edubank.model.TransactionType;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class TransactionDAOTest {

	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link TransactionDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private TransactionDAO transactionDAO;

	/**
	 * This is a positive test case method to test
	 * {@link TransactionDAO#getAccountTransactionsForDateRange (String accountNo, LocalDate fromDate, LocalDate toDate)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getAccountTransactionsForDateRangeValid() throws Exception {
		
		/*
		 * The following code is to make test data which is used to test the
		 * method
		 */
		String accountNumber = "443328602688019";
		LocalDate fromDate = LocalDate.of(2015, 01, 01);
		LocalDate toDate = LocalDate.of(2015, 12, 31);

		List<Transaction> transactionList = transactionDAO
				.getAccountTransactionsForDateRange(accountNumber, fromDate, toDate);

		/*
		 * The following line of code is to make the test case passed. If
		 * 'transactionList' variable's value is not null and not empty then the test case
		 * will be passed. If 'transactionList' variable's value is null or empty 
		 * then the test case will be failed. If any exception comes from
		 * the call to the method "transactionDAO.getAccountTransactionsForDateRange
		 * (accountNumber, fromDate, toDate)" which is being tested then the
		 * following line of code will not be executed and the test case will not be passed.
		 */
		Assert.assertTrue(transactionList!=null && !transactionList.isEmpty());
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionDAO#getAccountTransactionsForDateRange (String accountNo, LocalDate fromDate, LocalDate toDate)}
	 * <br>
	 * In test data account number "44332860268801" is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getAccountTransactionsForDateRangeInvalidAccountNumber() throws Exception {
			
		/*
		 * The following code is to make test data which is used to test the
		 * method
		 */
		String accountNumber = "44332860268801";
		LocalDate fromDate = LocalDate.of(2015, 01, 01);
		LocalDate toDate = LocalDate.of(2015, 12, 31);

		List<Transaction> transactionList = transactionDAO
				.getAccountTransactionsForDateRange(accountNumber, fromDate, toDate);

		/*
		 * The following line of code is to make the test case passed. If
		 * 'transactionList' variable's value is null then the test case
		 * will be passed. If 'transactionList' variable's value is not null
		 * then the test case will be failed. If any exception comes from
		 * the call to the method "transactionDAO.getAccountTransactionsForDateRange
		 * (accountNumber, fromDate, toDate)" which is being tested then the
		 * following line of code will not be executed and the test case will not be passed.
		 */
		Assert.assertNull(transactionList);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionDAO#getAccountTransactionsForDateRange (String accountNo, LocalDate fromDate, LocalDate toDate)}
	 * <br>
	 * In test data transactions date range from "2016-01-01" to "2016-12-31" is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getAccountTransactionsForDateRangeInvalidDateRange() throws Exception {
			
		/*
		 * The following code is to make test data which is used to test the
		 * method
		 */
		String accountNumber = "443328602688019";
		LocalDate fromDate = LocalDate.of(2010, 01, 01);
		LocalDate toDate = LocalDate.of(2010, 12, 31);

		List<Transaction> transactionList = transactionDAO
				.getAccountTransactionsForDateRange(accountNumber, fromDate, toDate);

		/*
		 * The following line of code is to make the test case passed. If
		 * 'transactionList' variable's value is null then the test case
		 * will be passed. If 'transactionList' variable's value is not null
		 * then the test case will be failed. If any exception comes from
		 * the call to the method "transactionDAO.getAccountTransactionsForDateRange
		 * (accountNumber, fromDate, toDate)" which is being tested then the
		 * following line of code will not be executed and the test case will not be passed.
		 */
		Assert.assertNull(transactionList);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionDAO#addTransaction(Transaction transaction, String createdBy) }
	 * <br>
	 * @throws Exception 
	 */
	@Test
	public void addTransactionValid() throws Exception {
		
		//populating sample test data for transaction
		Transaction transaction = new Transaction();
		transaction.setAccountNumber("443328602688019");
		transaction.setAmount(500.0);
		transaction.setType(TransactionType.CREDIT);
		transaction.setInfo("From: EDUBank To: Account Reason: LoadMoney");
		transaction.setTransactionMode("Teller");
		transaction.setTransactionDateTime(LocalDateTime.now());

		Long transactionId = transactionDAO.addTransaction(transaction,	"T7302");

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "transactionDAO.addTransaction(transaction,	"T7302")" which is being tested or return value is null 
		 * then the test case will not be passed.
		 */
		Assert.assertNotNull(transactionId);
	}
}
