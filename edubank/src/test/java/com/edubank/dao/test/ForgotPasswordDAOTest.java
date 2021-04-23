package com.edubank.dao.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.ForgotPasswordDAO;
import com.edubank.utility.Hashing;

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

	/**
	 * This is a positive test case method to test
	 * {@link ForgotPasswordDAO#authenticateEmailId(String emailId)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void authenticateEmailIdValid() throws Exception {
			
		/*
		 * "martin@infy.com" is a sample valid test data in the below
		 * statement to test the method
		 */
		String result = forgotPasswordDAO.authenticateEmailId("martin@infy.com");

		/*
		 * The following line of code is to make the test case passed. If
		 * result variable's value is "Found" then the test case will be
		 * passed. If result variable's value is not "Found" then the test
		 * case will be failed. If any exception comes from the call to the
		 * method "forgotPasswordDAO.authenticateEmailId("martin@infy.com")"
		 * which is being tested then the following line of code will not be
		 * executed and the test case will not be passed.
		 */
		Assert.assertEquals("Found", result);
	}

	/**
	 * This is a negative test case method to test
	 * {@link ForgotPasswordDAO#authenticateEmailId(String emailId)} In test
	 * data email id "martin@infy.org" is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void authenticateEmailIdInValidEmailId() throws Exception {

		/*
		 * "martin@infy.org" is a sample invalid test data in the below
		 * statement to test the method
		 */
		String result = forgotPasswordDAO
				.authenticateEmailId("martin@infy.org");

		/*
		 * The following line of code is to make the test case passed. If
		 * result variable's value is "Not Found" then the test case will be
		 * passed. If result variable's value is not "Not Found" then the
		 * test case will be failed. If any exception comes from the call to
		 * the method "forgotPasswordDAO.authenticateEmailId("martin@infy.org")" which
		 * is being tested then the following line of code will not be
		 * executed and the test case will not be passed.
		 */
		Assert.assertEquals("Not Found", result);
		
	}

	/**
	 * This is a positive test case method to test
	 * {@link ForgotPasswordDAO#resetPassword(String emailId, String hashedPassword)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void resetPasswordValid() throws Exception {

		/*
		 * "martin@infy.com" and "Martin$12345" is a sample valid test data
		 * in the below statement to test the method
		 */
		forgotPasswordDAO.resetPassword("martin@infy.com",
				Hashing.getHashValue("Martin$12345"));

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "forgotPasswordDAO.resetPassword ("martin@infy.com",
		 * Hashing.getHashValue("Martin$12345"))" which is being tested then
		 * the following line of code will not be executed and the test case
		 * will not be passed.
		 */
		Assert.assertTrue(true);
	}
}
