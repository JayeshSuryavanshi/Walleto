package com.edubank.validator.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.edubank.model.CustomerLogin;
import com.edubank.validator.CustomerLoginValidator;

/**
 * This is a JUnit test class to test the methods of
 * {@link CustomerLoginValidator}
 *
 * @author ETA_JAVA
 */
public class CustomerLoginValidatorTest {

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateLoginDetails(CustomerLogin customerLogin)}
	 * method
	 * 
	 */
	@Test
	public void validateLoginDetailsInvalidLoginNameFormat() throws Exception {
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setLoginName("ab cd");
		customerLogin.setPassword("Steven!123");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_CREDENTIALS");

		CustomerLoginValidator.validateLoginDetails(customerLogin);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateLoginDetails(CustomerLogin customerLogin)}
	 * method
	 * 
	 */
	@Test
	public void validateLoginDetailsInvalidPasswordFormat() throws Exception {
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setLoginName("abcd");
		customerLogin.setPassword("Steven123");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_CREDENTIALS");

		CustomerLoginValidator.validateLoginDetails(customerLogin);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidPasswordFormat1() {

		Boolean result = CustomerLoginValidator.validatePassword("Stev");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidPasswordFormat2() {

		Boolean result = CustomerLoginValidator.validatePassword("Steven1234567890abcdef");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidPasswordFormat3() {

		Boolean result = CustomerLoginValidator.validatePassword("stevenqwrty");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidPasswordFormat4() {

		Boolean result = CustomerLoginValidator.validatePassword("STEVENQWERTY");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidPasswordFormat5() {

		Boolean result = CustomerLoginValidator.validatePassword("123456789");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidPasswordFormat6() {

		Boolean result = CustomerLoginValidator.validatePassword("Steven1234");
		Assert.assertFalse(result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordInvalidPasswordFormat7() {

		Boolean result = CustomerLoginValidator.validatePassword("Stevenasd");
		Assert.assertFalse(result);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginValidator#validatePassword(String password)} method
	 * 
	 */
	@Test
	public void validatePasswordValidPasswordFormat() {

		Boolean result = CustomerLoginValidator.validatePassword("Steven!1234");
		Assert.assertTrue(result);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginValidator#validateLoginDetails(CustomerLogin customerLogin)}
	 * method
	 * 
	 */
	@Test
	public void validateLoginDetailsValidCredentials() throws Exception {
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setLoginName("abcd");
		customerLogin.setPassword("Steven!123");
		CustomerLoginValidator.validateLoginDetails(customerLogin);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateChangePasswordDetails(CustomerLogin customerLogin)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsInvalidPasswordFormat() throws Exception {
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setPassword("Mar 23");
		customerLogin.setNewPassword("Markus^123");
		customerLogin.setConfirmNewPassword("Markus^123");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_PASSWORD_FORMAT");

		CustomerLoginValidator.validateChangePasswordDetails(customerLogin);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateChangePasswordDetails(CustomerLogin customerLogin)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsInvalidNewPasswordFormat() throws Exception {
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setPassword("Markus^123");
		customerLogin.setNewPassword("abcd");
		customerLogin.setConfirmNewPassword("Mark^123");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_NEW_PASSWORD_FORMAT");

		CustomerLoginValidator.validateChangePasswordDetails(customerLogin);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateChangePasswordDetails(CustomerLogin customerLogin)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsInvalidConfirmNewPasswordFormat() throws Exception {
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setPassword("Mark*123");
		customerLogin.setNewPassword("Markus^123");
		customerLogin.setConfirmNewPassword("123456789");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT");

		CustomerLoginValidator.validateChangePasswordDetails(customerLogin);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateChangePasswordDetails(CustomerLogin customerLogin)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsNewPasswordConfirmNewPasswordMismatch() throws Exception {
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setPassword("Markus*123");
		customerLogin.setNewPassword("Markus^123");
		customerLogin.setConfirmNewPassword("Markus$123");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.CONFIRM_NEW_PASSWORD_MISSMATCH");

		CustomerLoginValidator.validateChangePasswordDetails(customerLogin);
	}

	/**
	 * This is positive test case method to test
	 * {@link CustomerLoginValidator#validateResetPasswordDetails(String, String)}
	 * <br>
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateResetPasswordDetailsValid() throws Exception {
		CustomerLoginValidator.validateResetPasswordDetails("Markus^123", "Markus^123");
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateResetPasswordDetails(String, String)}
	 * <br>
	 * In test data new password is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateResetPasswordDetailsInvalidNewPasswordFormat() throws Exception {

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_NEW_PASSWORD_FORMAT");

		CustomerLoginValidator.validateResetPasswordDetails("Markus123", "Markus^123");
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateResetPasswordDetails(String, String)}
	 * <br>
	 * In test data confirm new password is invalid
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateResetPasswordDetailsInvalidConfirmNewPasswordFormat() throws Exception {

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT");

		CustomerLoginValidator.validateResetPasswordDetails("Markus^123", "Markus@123");
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginValidator#validateResetPasswordDetails(String, String)}
	 * <br>
	 * In test data new password and confirm new passwords mismatch
	 * 
	 * @throws Exception
	 */
	@Test
	public void validateResetPasswordDetailsNewPasswordConfirmNewPasswordMismatch() throws Exception {

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.CONFIRM_NEW_PASSWORD_MISSMATCH");

		CustomerLoginValidator.validateResetPasswordDetails("Markus^123", "Markus!123");
	}
}
