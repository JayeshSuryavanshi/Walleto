package com.edubank.dao.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.SecurityQuestionDAO;
import com.edubank.model.SecurityQuestion;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class SecurityQuestionDAOTest {
	
	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link SecurityQuestionDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private SecurityQuestionDAO securityQuestionDAO;

	/**
	 * This is a positive test case method to test
	 * {@link SecurityQuestionDAO#getAllSecurityQuestions() }
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getAllSecurityQuestionsTest() throws Exception {
			
			List<SecurityQuestion> securityQuestionsList = securityQuestionDAO.getAllSecurityQuestions();

			/*
			 * The following line of code is to make the test case passed. If
			 * 'securityQuestionsList' is not empty then the test case
			 * will be passed. If 'transactionList' is empty
			 * then the test case will be failed. If any exception comes from
			 * the call to the method which is being tested then the
			 * following line of code will not be executed and the test case
			 * will not be passed.
			 */
			Assert.assertFalse(securityQuestionsList.isEmpty());
	}
}
