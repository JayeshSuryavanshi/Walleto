package com.edubank.service.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.edubank.dao.TransactionDAO;
import com.edubank.model.Transaction;
import com.edubank.model.TransactionType;
import com.edubank.service.TransactionService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private TransactionDAO transactionDAO;

	/**
	 * This attribute is used in all the test case methods of
	 * {@link TransactionServiceTest} to invoke the methods of
	 * {@link TransactionService}. <br>
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private TransactionService transactionService;

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a positive test case method to test
	 * {@link TransactionService#getLastThirtyDaysTransactionsForAccount(String accountNumber)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void getLastThreeDaysTransactionsForAccountValid() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * ArrayList of Transactions is returned to the service by the mocked
		 * DAO class method
		 */
		when(transactionDAO.getAccountTransactionsForDateRange(anyString(), any(), any()))
				.thenReturn(new ArrayList<Transaction>());

		/*
		 * 443328602688019 is a sample valid test data in the below statement
		 */
		List<Transaction> transactionsList = transactionService
				.getLastThirtyDaysTransactionsForAccount("443328602688019");

		/*
		 * The following line of code is to make the test case passed. If
		 * 'transactionsList' variable's value is not null then the test case
		 * will be passed. If 'transactionsList' variable's value is null then
		 * the test case will be failed. If any exception comes from the call to
		 * the method
		 * "transactionService.getLastThreeDaysTransactionsForAccount("
		 * 443328602688019")" which is being tested then the following line of
		 * code will not be executed and the test case will be failed.
		 */
		Assert.assertNotNull(transactionsList);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionService#getLastThirtyDaysTransactionsForAccount(String accountNumber)}
	 * <br>
	 * In test data account number 44332860268801 is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void getLastThirtyDaysTransactionsForAccountInvalidAccountNumber() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(transactionDAO.getAccountTransactionsForDateRange(anyString(), any(), any())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionService.NO_TRANSACTIONS_IN_LAST_THIRTY_DAYS");

		/*
		 * 44332860268801 is a sample invalid test data in the below statement
		 */
		transactionService.getLastThirtyDaysTransactionsForAccount("44332860268801");
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionService#getLastThirtyDaysTransactionsForAccount(String accountNumber)}
	 * <br>
	 * In test data account number is valid and no transactions for the account
	 * number from last 3 days
	 * 
	 * @throws Exception
	 */
	@Test
	public void getLastThirtyDaysTransactionsForAccountValidAccountNumberNoTransactions() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(transactionDAO.getAccountTransactionsForDateRange(anyString(), any(), any())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionService.NO_TRANSACTIONS_IN_LAST_THIRTY_DAYS");

		/* 44332860268801 is a sample test data in the below statement */
		transactionService.getLastThirtyDaysTransactionsForAccount("632941935815667");
	}

	/**
	 * This is a positive test case method to test
	 * {@link TransactionService# getAccountTransactionsForDateRange(String accountNumber, LocalDate fromDate, LocalDate toDate)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAccountTransactionsForDateRangeValid() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * ArrayList of Transactions is returned to the service by the mocked
		 * DAO class method
		 */
		when(transactionDAO.getAccountTransactionsForDateRange(anyString(), any(), any()))
				.thenReturn(new ArrayList<Transaction>());

		/*
		 * The below code sets the sample valid test data to test the method
		 */
		String accountNumber = "443328602688019";
		LocalDate fromDate = LocalDate.of(2015, 01, 01);
		LocalDate toDate = LocalDate.of(2015, 12, 31);
		// ------------------------------------------------------------------

		List<Transaction> transactionsList = transactionService.getAccountTransactionsForDateRange(accountNumber,
				fromDate, toDate);

		/*
		 * The following line of code is to make the test case passed. If
		 * 'transactionsList' variable's value is not null then the test case
		 * will be passed. If 'transactionsList' variable's value is null then
		 * the test case will be failed. If any exception comes from the call to
		 * the method
		 * "transactionService.getAccountTransactionsForDateRange(accountNumber, fromDate, toDate)"
		 * which is being tested then the following line of code will not be
		 * executed and the test case will be failed.
		 */
		Assert.assertNotNull(transactionsList);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionService# getAccountTransactionsForDateRange(String accountNumber, LocalDate fromDate, LocalDate toDate)}
	 * <br>
	 * In test data account number 44332860268801 is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAccountTransactionsForDateRangeInvalidAccountNumber() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(transactionDAO.getAccountTransactionsForDateRange(anyString(), any(), any())).thenReturn(null);

		/* The below code sets the sample test data to test the method. */
		String accountNumber = "44332860268801";
		LocalDate fromDate = LocalDate.of(2015, 01, 01);
		LocalDate toDate = LocalDate.of(2015, 12, 31);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionService.NO_TRANSACTIONS_IN_DATE_RANGE");

		transactionService.getAccountTransactionsForDateRange(accountNumber, fromDate, toDate);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionService# getAccountTransactionsForDateRange(String accountNumber, LocalDate fromDate, LocalDate toDate)}
	 * <br>
	 * In test data account number "443328602688019" is valid and no
	 * transactions for the account number in the date range
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAccountTransactionsForDateRangeNoTransactions() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(transactionDAO.getAccountTransactionsForDateRange(anyString(), any(), any())).thenReturn(null);

		/* The below code sets the sample test data to test the method. */
		String accountNumber = "443328602688019";
		LocalDate fromDate = LocalDate.of(2016, 01, 01);
		LocalDate toDate = LocalDate.of(2016, 12, 31);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionService.NO_TRANSACTIONS_IN_DATE_RANGE");

		transactionService.getAccountTransactionsForDateRange(accountNumber, fromDate, toDate);
	}

	/**
	 * This is a positive test case method to test
	 * {@link TransactionService#addTransaction(Transaction, String)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addTransactionValid() throws Exception {
		Transaction transaction = new Transaction();
		transaction.setAccountNumber("443328602688019");
		transaction.setAmount(500.0);
		transaction.setType(TransactionType.CREDIT);
		transaction.setInfo("From: EDUBank To: Account Reason: LoadMoney");
		transaction.setTransactionMode("Teller");

		when(transactionDAO.addTransaction(transaction, "T7302")).thenReturn(12345l);

		Transaction returnTransaction = transactionService.addTransaction(transaction, "T7302");
		Assert.assertEquals(returnTransaction,transaction);
	}
}
