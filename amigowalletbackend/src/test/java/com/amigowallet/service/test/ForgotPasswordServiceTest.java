package com.amigowallet.service.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amigowallet.dao.ForgotPasswordDAO;
import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;
import com.amigowallet.service.ForgotPasswordService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForgotPasswordServiceTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This attribute is initialized with a mock object as It is annotated with @Mock
	 * Mockito annotation.
	 */
	@MockBean
	private ForgotPasswordDAO forgotPasswordDAO;
	
	/**
	 * This attribute is used in all the test case methods of
	 * {@link ForgotPasswordServiceTest} to invoke the methods of {@link ForgotPasswordService}. <br>
	 * 
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	
	@Test
	public void authenticateEmailIdInvalidEmail() throws Exception
	{
		when(forgotPasswordDAO.authenticateEmailId(anyString())).thenReturn(null);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("ForgotPasswordService.INVALID_EMAIL");
		forgotPasswordService.authenticateEmailId("Johns@gmail.com");
		
	}
	
	@Test
	public void authenticateEmailIdValidEmail() throws Exception
	{
		User user = new User();
		user.setUserId(12345);
		when(forgotPasswordDAO.authenticateEmailId(anyString())).thenReturn(user);
		User userFromService = forgotPasswordService.authenticateEmailId("Johns@gmail.com");
		Assert.assertEquals(user, userFromService);
	}
	
	@Test
	public void validateSecurityAnswerInvalidAnswer() throws Exception {
		User user = new User();
		user.setUserId(12345);
		user.setSecurityAnswer("Raju");
		when(forgotPasswordDAO.validateSecurityAnswer(any())).thenReturn(user);
		User userInput = new User();
		userInput.setUserId(12345);
		SecurityQuestion securityQuestion = new SecurityQuestion();
		securityQuestion.setQuestion("What is your nickname?");
		securityQuestion.setQuestionId(12345);
		userInput.setSecurityQuestion(securityQuestion);
		userInput.setSecurityAnswer("Raj");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("ForgotPasswordService.INVALID_SECURITY_ANSWER");
		forgotPasswordService.validateSecurityAnswer(userInput);
		
	}
	
	@Test
	public void validateSecurityAnswerValidAnswer() throws Exception {
		User user = new User();
		user.setUserId(12345);
		user.setSecurityAnswer("Raju");
		when(forgotPasswordDAO.validateSecurityAnswer(any())).thenReturn(user);
		User userInput = new User();
		userInput.setUserId(12345);
		SecurityQuestion securityQuestion = new SecurityQuestion();
		securityQuestion.setQuestion("What is your nickname?");
		securityQuestion.setQuestionId(12345);
		userInput.setSecurityQuestion(securityQuestion);
		userInput.setSecurityAnswer("Raju");
		forgotPasswordService.validateSecurityAnswer(userInput);
		Assert.assertTrue(true);
	}
	
	@Test
	public void resetPasswordInvalidNewPassword() throws Exception {
		User user = new User();
		user.setUserId(12345);
		user.setPassword("James");
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginValidator.INVALID_NEW_PASSWORD_FORMAT");
		forgotPasswordService.resetPassword(user);	
	}
	
	@Test
	public void resetPasswordValidNewPassword() throws Exception {
		User user = new User();
		user.setUserId(12345);
		user.setPassword("James#123");
		forgotPasswordService.resetPassword(user);	
		Assert.assertTrue(true);
	}
	
}
