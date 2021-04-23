package com.amigowallet.dao.test;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.ForgotPasswordDAO;
import com.amigowallet.model.User;
import com.amigowallet.utility.HashingUtility;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class ForgotPasswordDAOTest {
	
	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link ForgotPasswordDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private ForgotPasswordDAO forgotPasswordDAO;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a positive test case method to test
	 * {@link ForgotPasswordDAO#resetPassword(String emailId, String hashedPassword)}
	 * 
	 */
	@Test
	public void resetPasswordValid() throws NoSuchAlgorithmException{
		forgotPasswordDAO.resetPassword(12121,
				HashingUtility.getHashValue("James$123"));
		Assert.assertTrue(true);
	}

	@Test
	public void authenticateEmailIdValidEmail() throws Exception {
		User user = forgotPasswordDAO.authenticateEmailId("James@infy.com");
		
		Assert.assertEquals(12121, user.getUserId().intValue());
	}
	
	@Test
	public void authenticateEmailIdInvalidEmail() throws Exception {
		
		User user = forgotPasswordDAO.authenticateEmailId("James@infosys.com");
		Assert.assertNull(user);
	}

	@Test
	public void validateSecurityAnswerValidUser() {
		User user = new User();
		user.setUserId(12121);
		User userFromDAO = forgotPasswordDAO.validateSecurityAnswer(user);
		Assert.assertEquals(user.getUserId(), userFromDAO.getUserId());
	}
	
	@Test
	public void validateSecurityAnswerInvalidUser() {
		User user = new User();
		user.setUserId(200001);
		User userFromDAO = forgotPasswordDAO.validateSecurityAnswer(user);
		Assert.assertEquals(null, userFromDAO);
	}
	
	@Test
	public void resetPasswordCheck() {
		forgotPasswordDAO.resetPassword(12121, "James!123");
		Assert.assertTrue(true);
	}
	
	
}
