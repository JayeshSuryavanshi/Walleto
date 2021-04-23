package com.edubank.validator.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.edubank.model.Teller;
import com.edubank.validator.TellerValidator;

/**
 * This is a JUnit test class to test the methods of {@link TellerValidator}
 *
 * @author ETA_JAVA
 */
public class TellerValidatorTest {

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidFormat1() throws Exception {
		Boolean result = TellerValidator.validatePassword("abcd");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidFormat6() throws Exception {
		Boolean result = TellerValidator.validatePassword("abcdef1234567890abcdef");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidFormat5() throws Exception {
		Boolean result = TellerValidator.validatePassword("abcdefgh");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidFormat2() throws Exception {
		Boolean result = TellerValidator.validatePassword("ABCDABCDEF");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidFormat3() throws Exception {
		Boolean result = TellerValidator.validatePassword("ABCDabcdef");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidFormat4() throws Exception {
		Boolean result = TellerValidator.validatePassword("ABCDabcd1234");
		Assert.assertFalse(result);
	}

	/**
	 * This is a positive test case method to test
	 * {@link TellerValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordValidFormat() throws Exception {
		Boolean result = TellerValidator.validatePassword("ABCD!abcd1234");
		Assert.assertTrue(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validateLoginName(String loginName)} method
	 * 
	 */
	@Test
	public void validateLoginNameInvalidFormat() throws Exception {
		Boolean result = TellerValidator.validateLoginName("Abe3452");
		Assert.assertFalse(result);
	}

	/**
	 * This is a positive test case method to test
	 * {@link TellerValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validateLoginNameValidFormat() throws Exception {
		Boolean result = TellerValidator.validateLoginName("T5214");
		Assert.assertTrue(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validate(Teller teller)} method with invalid
	 * teller loginName
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateTellerLoginInvalidCredentials1() throws Exception {
		Teller teller = new Teller();
		teller.setLoginName("Sam");
		teller.setPassword("Abcd!1234");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TellerValidator.INVALID_CREDENTIALS");

		TellerValidator.validate(teller);
	}

	/**
	 * This is a negative test case method to test
	 * {@link TellerValidator#validate(Teller teller)} method with invalid
	 * teller password
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateTellerLoginInvalidCredentials2() throws Exception {
		Teller teller = new Teller();
		teller.setLoginName("T2151");
		teller.setPassword("Abcd1234");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TellerValidator.INVALID_CREDENTIALS");

		TellerValidator.validate(teller);
	}

	/**
	 * This is a positive test case method to test
	 * {@link TellerValidator#validate(Teller teller)} method with valid teller
	 * loginName and password format
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateTellerLoginValidCredentials() throws Exception {
		Teller teller = new Teller();
		teller.setLoginName("T2151");
		teller.setPassword("Abcd!1234");
		TellerValidator.validate(teller);
	}
}
