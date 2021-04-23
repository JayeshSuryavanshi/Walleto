package com.edubank.validator.test;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.edubank.validator.AccountValidator;

/**
 * This is a JUnit test class to test the methods of {@link AccountValidator}
 *
 * @author ETA_JAVA
 */
public class AccountValidatorTest {

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test
	 * {@link AccountValidator#validateAccountNumber(String accountNumber)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void validateAccountNumberInvalidAccountNumber() throws Exception {

		String accountNumber = "12345";

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountValidator.INVALID_ACCOUNT_NUMBER");

		AccountValidator.validateAccountNumber(accountNumber);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountValidator#validateAmount(Double amount)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void validateAmountInvalidAmount() throws Exception {

		Double amount = -500d;

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountValidator.INVALID_AMOUNT");

		AccountValidator.validateAmount(amount);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountValidator#validateAccountNumber(String accountNumber)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void validateAccountNumberValid() throws Exception {

		String accountNumber = "678454099024629";

		AccountValidator.validateAccountNumber(accountNumber);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountValidator#validateAmount(Double amount)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void validateAmountValid() throws Exception {

		Double amount = 500d;

		AccountValidator.validateAmount(amount);
	}
}