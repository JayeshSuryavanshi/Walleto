package com.amigowallet.service.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amigowallet.dao.RegistrationDAO;
import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;
import com.amigowallet.service.RegistrationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationServiceTest
{

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This attribute is initialized with a mock object as It is annotated with @Mock
	 * Mockito annotation.
	 */
	@MockBean
	private RegistrationDAO registrationDAO;
	
	/**
	 * This attribute is used in all the test case methods of
	 * {@link RegistrationServiceTest} to invoke the methods of {@link RegistrationService}. <br>
	 * 
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private RegistrationService registrationService;
	
	@Test
	public void validateUserValid() throws Exception
	{
		User user=new User();
		user.setEmailId("Joseph@infy.com");
		user.setMobileNumber("9701706215");
		user.setPassword("Joseph#123");
		user.setName("Joseph");
		when(registrationDAO.checkEmailAvailability(anyString())).thenReturn(false);
		when(registrationDAO.checkMobileNumberAvailability(anyString())).thenReturn(false);
		registrationService.validateUser(user);
		Assert.assertTrue(true);
	}
	
	@Test
	public void validateUserExistingEmailId() throws Exception
	{
		User user=new User();
		user.setEmailId("Joseph@infy.com");
		user.setMobileNumber("9701706215");
		user.setPassword("Joseph#123");
		user.setName("Joseph");
		when(registrationDAO.checkEmailAvailability(anyString())).thenReturn(true);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationService.EMAIL_ALREADY_PRESENT");
		registrationService.validateUser(user);
	}
	
	@Test
	public void validateUserNewEamilIdExistingMobileNumber() throws Exception
	{
		User user=new User();
		user.setEmailId("James@infy.com");
		user.setMobileNumber("9701706215");
		user.setPassword("Joseph#123");
		user.setName("Joseph");
		when(registrationDAO.checkEmailAvailability(anyString())).thenReturn(false);
		when(registrationDAO.checkMobileNumberAvailability(anyString())).thenReturn(true);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RegistrationService.MOBILE_NUMBER_ALREADY_PRESENT");
		registrationService.validateUser(user);
	}

	@Test
	public void registerUserValid() throws Exception
	{
		User user=new User();
		user.setEmailId("Joseph@infy.com");
		user.setMobileNumber("9701706215");
		user.setPassword("Joseph#123");
		when(registrationDAO.addNewUser(any(User.class))).thenReturn(12890);
		Integer userId=registrationService.registerUser(user);
		Assert.assertEquals(userId.intValue(), 12890);
	}
	
	@Test
	public void checkGetAllSecurityQuestions() throws Exception {
		
		ArrayList<SecurityQuestion> securityQuestions = new ArrayList<>();
		SecurityQuestion securityQuestion = new SecurityQuestion();
		securityQuestion.setQuestion("What is your nick name");
		securityQuestion.setQuestionId(200001);
		securityQuestions.add(securityQuestion);
		when(registrationDAO.getAllSecurityQuestions()).thenReturn(securityQuestions);
		ArrayList<SecurityQuestion> securityQuestionsFromService = registrationService.getAllSecurityQuestions();
		Assert.assertEquals(securityQuestion, securityQuestionsFromService.get(0));		
	}
}
