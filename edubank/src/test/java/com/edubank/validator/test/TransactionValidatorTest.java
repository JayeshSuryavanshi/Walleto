package com.edubank.validator.test;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.edubank.validator.TransactionValidator;

/**
 * This is a JUnit test class to test the methods of
 * {@link TransactionValidator}
 *
 * @author ETA_JAVA
 */
public class TransactionValidatorTest {
	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a positive test case method to test
	 * {@link TransactionValidator# validateTransactionsDateRange(LocalDate fromDate, LocalDate toDate)}
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateTransactionsDateRangeValid() throws Exception {
		LocalDate fromDate = LocalDate.of(2016, 01, 01);
		LocalDate toDate = LocalDate.of(2016, 12, 31);

		TransactionValidator.validateTransactionsDateRange(fromDate, toDate);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionValidator# validateTransactionsDateRange(LocalDate fromDate, LocalDate toDate)}
	 * <br>
	 * In test data from date is greater than today and it is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateTransactionsDateRangeFromDateGreaterThanToday() throws Exception {
		LocalDate fromDate = LocalDate.now().plusDays(10);
		LocalDate toDate = LocalDate.of(2016, 12, 31);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionValidator.FROM_DATE_GREATER_THAN_TODAY");

		TransactionValidator.validateTransactionsDateRange(fromDate, toDate);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionValidator# validateTransactionsDateRange(LocalDate fromDate, LocalDate toDate)}
	 * <br>
	 * In test data to date is greater than today and it is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateTransactionsDateRangeToDateGreaterThanToday() throws Exception {
		LocalDate fromDate = LocalDate.of(2016, 01, 01);
		LocalDate toDate = LocalDate.now().plusDays(10);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionValidator.TO_DATE_GREATER_THAN_TODAY");

		TransactionValidator.validateTransactionsDateRange(fromDate, toDate);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionValidator# validateTransactionsDateRange(LocalDate fromDate, LocalDate toDate)}
	 * <br>
	 * In test data both from and to dates are greater than today and both are
	 * invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateTransactionsDateRangeFromToDatesGreaterThanToday() throws Exception {
		LocalDate fromDate = LocalDate.now().plusDays(10);
		LocalDate toDate = LocalDate.now().plusDays(10);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionValidator.TO_AND_FROM__DATE_GREATER_THAN_TODAY");

		TransactionValidator.validateTransactionsDateRange(fromDate, toDate);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TransactionValidator# validateTransactionsDateRange(LocalDate fromDate, LocalDate toDate)}
	 * <br>
	 * In test data from date is greater than to date and it is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateTransactionsDateRangeFromDateGreaterThanToDate() throws Exception {
		LocalDate fromDate = LocalDate.of(2016, 12, 01);
		LocalDate toDate = LocalDate.of(2016, 01, 31);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionValidator.FROM_DATE_GREATER_THAN_TO_DATE");

		TransactionValidator.validateTransactionsDateRange(fromDate, toDate);
	}
}
