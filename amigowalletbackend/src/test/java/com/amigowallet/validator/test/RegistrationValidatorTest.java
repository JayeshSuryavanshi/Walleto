package com.amigowallet.validator.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.amigowallet.model.User;
import com.amigowallet.validator.RegistrationValidator;

/**
 * This is a JUnit test class to test the methods of {@link RegistrationValidator}. <br>
 * It covers both positive and negative test cases.
 *
 * @author ETA_JAVA
 *
 */
public class RegistrationValidatorTest
{

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void validateUserDetailsValid() throws Exception
	{
		 User user=new User();
		 user.setEmailId("Joseph@infy.com");
		 user.setName("Joseph");
		 user.setPassword("Joseph#123");
		 user.setMobileNumber("9697074340");
		 
		 RegistrationValidator.validateUserDetails(user);
		 
		 Assert.assertTrue(true);
	}

	@Test
	public void validateUserDetailsInvalidEmailId() throws Exception
	{
		User user=new User();
		user.setEmailId("Joseph@infy");
		user.setName("Joseph");
		user.setPassword("Joseph#123");
		user.setMobileNumber("9697074340");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationValidator.INVALID_EMAIL");
		RegistrationValidator.validateUserDetails(user);
	}

	@Test
	public void validateUserDetailsInvalidName() throws Exception
	{
		User user=new User();
		user.setEmailId("Joseph@infy.com");
		user.setName("Joseph7");
		user.setPassword("Joseph#123");
		user.setMobileNumber("9697074340");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationValidator.INVALID_USER_NAME");
		RegistrationValidator.validateUserDetails(user);
	}
	
	@Test
	public void validateUserDetailsInvalidMobileNumber() throws Exception
	{
		User user=new User();
		user.setEmailId("Joseph@infy.com");
		user.setName("Joseph");
		user.setPassword("Joseph#123");
		user.setMobileNumber("23365");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationValidator.INVALID_CONTACT");
		RegistrationValidator.validateUserDetails(user);
	}
	
	@Test
	public void validateUserDetailsInvalidPassword() throws Exception
	{
		User user=new User();
		user.setEmailId("Joseph@infy.com");
		user.setName("Joseph");
		user.setPassword("Joseph123");
		user.setMobileNumber("9697074340");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationValidator.INVALID_PASSWORD");
		RegistrationValidator.validateUserDetails(user);
	}

}
