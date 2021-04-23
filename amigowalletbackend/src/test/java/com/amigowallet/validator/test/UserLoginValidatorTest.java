package com.amigowallet.validator.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.amigowallet.model.User;
import com.amigowallet.validator.UserLoginValidator;

/**
 * This is a JUnit test class to test the methods of {@link UserLoginValidator}
 * It covers both positive and negative test cases.
 * 
 * @author ETA_JAVA
 * 
 */
public class UserLoginValidatorTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This method tests the password entered by user for password length less
	 * than 8
	 */
	@Test
	public void validatePasswordInvalidPassword1() {
		String password = "abcd";
		boolean result = UserLoginValidator.validatePassword(password);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the password entered by user for password length more
	 * than 20
	 */
	@Test
	public void validatePasswordInvalidPassword2() {
		String password = "abcde1234567890abcde1234";
		boolean result = UserLoginValidator.validatePassword(password);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the password entered by user for password without any
	 * upper case letter
	 */
	@Test
	public void validatePasswordInvalidPassword3() {
		String password = "abcde1234";
		boolean result = UserLoginValidator.validatePassword(password);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the password entered by user for password without any
	 * lower case letter
	 */
	@Test
	public void validatePasswordInvalidPassword4() {
		String password = "ABCDE13456";
		boolean result = UserLoginValidator.validatePassword(password);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the password entered by user for password without any a
	 * number
	 */
	@Test
	public void validatePasswordInvalidPassword5() {
		String password = "abcdABCDE";
		boolean result = UserLoginValidator.validatePassword(password);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the password entered by user for password without any
	 * specified special characters(!,#,$,%,^,&,*,(,))
	 */
	@Test
	public void validatePasswordInvalidPassword6() {
		String password = "abcd@FGH1234";
		boolean result = UserLoginValidator.validatePassword(password);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the password entered by user for password with a valid
	 * password format
	 */
	@Test
	public void validatePasswordValidPassword() {
		String password = "abcd#ABC1234";
		boolean result = UserLoginValidator.validatePassword(password);

		Assert.assertTrue(result);
	}

	/**
	 * This method tests the EmailId entered by user for EmailId without a @ and
	 * dot(.)
	 * 
	 */
	@Test
	public void validateUserEmailInvalidEmail1() {
		String userEmail = "abcd";
		boolean result = UserLoginValidator.validateUserEmail(userEmail);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the EmailId entered by user for EmailId with #
	 * 
	 */
	@Test
	public void validateUserEmailInvalidEmail2() {
		String userEmail = "abcd#efgh@xyz.com";
		boolean result = UserLoginValidator.validateUserEmail(userEmail);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the EmailId entered by user for EmailId with multiple @
	 * symbols
	 * 
	 */
	@Test
	public void validateUserEmailInvalidEmail3() {
		String userEmail = "abcd@efgh@xyz.com";
		boolean result = UserLoginValidator.validateUserEmail(userEmail);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the EmailId entered by user for EmailId with numbers
	 * after @
	 * 
	 */
	@Test
	public void validateUserEmailInvalidEmail4() {
		String userEmail = "abcd_efgh@xy123z.com";
		boolean result = UserLoginValidator.validateUserEmail(userEmail);

		Assert.assertFalse(result);
	}

	/**
	 * This method tests the EmailId entered by user for EmailId with valid
	 * email format
	 * 
	 */
	@Test
	public void validateUserEmailValidEmail() {
		String userEmail = "abcd_efgh@xyz.com";
		boolean result = UserLoginValidator.validateUserEmail(userEmail);

		Assert.assertTrue(result);
	}

	@Test
	public void validateUserLoginValid() throws Exception{
		User user = new User();
		user.setEmailId("Joseph@infy.com");
		user.setPassword("Joseph#123");

		UserLoginValidator.validateUserLogin(user);

		Assert.assertTrue(true);
	}
	
	/**
	 * This method tests the login credentials with invalid emailId
	 */
	@Test
	public void validateUserLoginTestInvalidEmailId() throws Exception{
		User user = new User();
		user.setEmailId("abcd");
		user.setPassword("Abcd#123");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_CREDENTIALS");
		UserLoginValidator.validateUserLogin(user);
	}
	
	/**
	 * This method tests the login credentials with invalid password
	 */
	@Test
	public void validateUserLoginTestInvalidPassword() throws Exception{
		User user = new User();
		user.setEmailId("abcd@xyz.com");
		user.setPassword("abcd123");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_CREDENTIALS");
		UserLoginValidator.validateUserLogin(user);
	}
	
	/**
	 * This is positive test case method to test
	 * {@link UserLoginValidator#validateResetPasswordDetails(String, String)}
	 * <br>
	 * 
	 */
	@Test
	public void validateResetPasswordDetailsValid() throws Exception{
		UserLoginValidator.validateResetPasswordDetails("Markus^123");
		Assert.assertTrue(true);
	}

	/**
	 * This is a negative test case method to test
	 * {@link UserLoginValidator#validateResetPasswordDetails(String, String)}
	 * <br>
	 * In test data new password is invalid
	 * 
	 */
	@Test
	public void validateResetPasswordDetailsInvalidNewPasswordFormat() throws Exception{
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_NEW_PASSWORD_FORMAT");
		UserLoginValidator.validateResetPasswordDetails("Markus123");
	}

	/**
	 * This is a negative test case method to test
	 * {@link UserLoginValidator#validateChangePasswordDetails(User user)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsValid() throws Exception {
		User user = new User();
		user.setPassword("Markus^123");
		user.setNewPassword("Markus$123");
		user.setConfirmNewPassword("Markus$123");

		UserLoginValidator.validateChangePasswordDetails(user);

		Assert.assertTrue(true);
	}

	/**
	 * This is a negative test case method to test
	 * {@link UserLoginValidator#validateChangePasswordDetails(User user)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsInvalidPasswordFormat() throws Exception {
		User user = new User();
		user.setPassword("Mar 23");
		user.setNewPassword("Markus^123");
		user.setConfirmNewPassword("Markus^123");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_PASSWORD_FORMAT");
		UserLoginValidator.validateChangePasswordDetails(user);
	}

	/**
	 * This is a negative test case method to test
	 * {@link UserLoginValidator#validateChangePasswordDetails(User user)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsInvalidNewPasswordFormat() throws Exception {
		User user = new User();
		user.setPassword("Markus^123");
		user.setNewPassword("abcd");
		user.setConfirmNewPassword("Mark^123");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_NEW_PASSWORD_FORMAT");
		UserLoginValidator.validateChangePasswordDetails(user);
	}

	/**
	 * This is a negative test case method to test
	 * {@link UserLoginValidator#validateChangePasswordDetails(User user)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsInvalidConfirmNewPasswordFormat() throws Exception {
		User user = new User();
		user.setPassword("Mark*123");
		user.setNewPassword("Markus^123");
		user.setConfirmNewPassword("123456789");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT");
		UserLoginValidator.validateChangePasswordDetails(user);
	}

	/**
	 * This is a negative test case method to test
	 * {@link UserLoginValidator#validateChangePasswordDetails(User user)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsNewPasswordConfirmNewPasswordMismatch() throws Exception {
		User user = new User();
		user.setPassword("Markus*123");
		user.setNewPassword("Markus^123");
		user.setConfirmNewPassword("Markus$123");

		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.CONFIRM_NEW_PASSWORD_MISSMATCH");
		UserLoginValidator.validateChangePasswordDetails(user);
	}

	/**
	 * This is a negative test case method to test
	 * {@link UserLoginValidator#validateChangePasswordDetails(User user)}
	 * method
	 * 
	 */
	@Test
	public void validateChangePasswordDetailsNewPasswordSameAsOldPassword() throws Exception {
		User user = new User();
		user.setPassword("Markus^123");
		user.setNewPassword("Markus^123");
		user.setConfirmNewPassword("Markus^123");

		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.OLD_PASSWORD_NEW_PASSWORD_SAME");
		UserLoginValidator.validateChangePasswordDetails(user);
	}

}
