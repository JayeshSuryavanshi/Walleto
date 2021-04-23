package com.edubank.service.test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.edubank.dao.ForgotPasswordDAO;
import com.edubank.service.ForgotPasswordService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForgotPasswordServiceTest {

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private ForgotPasswordDAO forgotPasswordDAO;

	/**
	 * This attribute is used in all the test case methods of
	 * {@link ForgotPasswordServiceTest} to invoke the methods of
	 * {@link ForgotPasswordService}. <br>
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private ForgotPasswordService forgotPasswordService;

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a positive test case method to test
	 * {@link ForgotPasswordService#authenticateEmailId(String emailId)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateEmailIdValid() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * "Found" is returned to the service by the mocked DAO class method
		 */
		when(forgotPasswordDAO.authenticateEmailId(anyString())).thenReturn("Found");

		/*
		 * "martin@infy.com" is a sample valid test data in the below statement
		 * to test the method
		 */
		forgotPasswordService.authenticateEmailId("martin@infy.com");
	}

	/**
	 * This is a negative test case method to test
	 * {@link ForgotPasswordService#authenticateEmailId(String emailId)} In test
	 * data email id "martin@infy.org" is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateEmailIdInValidEmailId() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * "Not Found" is returned to the service by the mocked DAO class method
		 */
		when(forgotPasswordDAO.authenticateEmailId(anyString())).thenReturn("Not Found");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("ForgotPasswordService.INVALID_EMAIL");

		/*
		 * "martin@infy.org" is a sample invalid test data in the below
		 * statement to test the method
		 */
		forgotPasswordService.authenticateEmailId("martin@infy.org");
	}

	/**
	 * This is positive test case method to test
	 * {@link ForgotPasswordService#resetPassword(Integer, String, String)} <br>
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void resetPasswordValid() throws Exception {
		/*
		 * "steven@infy.com", "Martin$123", "Martin$123" are sample valid test
		 * data in the below statement to test the method
		 */
		forgotPasswordService.resetPassword("steven@infy.com", "Martin$123", "Martin$123");
	}

	/**
	 * This is positive test case method to test
	 * {@link ForgotPasswordService#checkSecQuestionAnswer(String, String)} <br>
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void checkSecQuestionAnswerValid() throws Exception {
		/*
		 * "Mysore", "Mysore" is a sample valid test data in the below statement
		 * to test the method
		 */
		forgotPasswordService.checkSecQuestionAnswer("Mysore", "Mysore");
	}

	/**
	 * This is negative test case method to test
	 * {@link ForgotPasswordService#checkSecQuestionAnswer(String, String)} <br>
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void checkSecQuestionAnswerInvalidSecurityAnswer() throws Exception {
		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("ForgotPasswordService.SECURITYQUESTION_ANSWER_MISMATCH");

		/*
		 * "Mysore", "MYSORE" is a sample invalid test data in the below
		 * statement to test the method
		 */
		forgotPasswordService.checkSecQuestionAnswer("Mysore", "MYSORE123");
	}
}
