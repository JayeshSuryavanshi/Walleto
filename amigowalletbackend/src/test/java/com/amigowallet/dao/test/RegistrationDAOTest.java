package com.amigowallet.dao.test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.RegistrationDAO;
import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;
import com.amigowallet.utility.HashingUtility;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class RegistrationDAOTest {

	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link RegistrationDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private RegistrationDAO registrationDAO;
	
	@Test
	public void addNewUser() throws NoSuchAlgorithmException
	{
		User user = new User();
		user.setName("Rohny");
		user.setEmailId("rohny@infy.com");
		user.setMobileNumber("8525252223");
		user.setPassword(HashingUtility.getHashValue("Rohny!445"));
		
		SecurityQuestion securityQuestion = new SecurityQuestion();
		securityQuestion.setQuestionId(210001);
		
		user.setSecurityQuestion(securityQuestion);
		user.setSecurityAnswer("ronny");
		Integer userId = registrationDAO.addNewUser(user);
		Assert.assertNotNull(userId);
	}
	
	@Test
	public void checkEmailAvailabilityValidEmail()
	{
		Boolean result = registrationDAO.checkEmailAvailability("james@infy.com");
		Assert.assertTrue(result);
	}
	
	@Test
	public void checkEmailAvailabilityInvalidEmail()
	{
		Boolean result = registrationDAO.checkEmailAvailability("abcdef");
		Assert.assertFalse(result);
	}
	
	@Test
	public void checkMobileNumberAvailabilityValidMobileNumber()
	{
		Boolean result = registrationDAO.checkMobileNumberAvailability("9863266023");
		Assert.assertTrue(result);
	}
	
	@Test
	public void checkMobileNumberAvailabilityInvalidMobileNumber()
	{
		Boolean result = registrationDAO.checkMobileNumberAvailability("200000002");
		Assert.assertFalse(result);
	}
	
	@Test
	public void checkgetAllSecurityQuestions() {
		ArrayList<SecurityQuestion> securityQuestions = registrationDAO.getAllSecurityQuestions();
		if(securityQuestions.isEmpty())
			Assert.assertTrue(false);
		else
			Assert.assertTrue(true);
	}
	
}
