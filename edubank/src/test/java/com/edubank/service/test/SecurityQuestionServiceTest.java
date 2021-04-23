package com.edubank.service.test;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.edubank.dao.SecurityQuestionDAO;
import com.edubank.model.SecurityQuestion;
import com.edubank.service.SecurityQuestionService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityQuestionServiceTest {

	/**
	 * This attribute is initialized with a mock object as it is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private SecurityQuestionDAO securityQuestionDAO;

	/**
	 * This attribute is used in all the test case methods of
	 * {@link SecurityQuestionServiceTest} to invoke the methods of
	 * {@link SecurityQuestionService}. <br>
	 * It is annotated with @InjectMocks Mockito annotation which makes it use
	 * mocked objects.
	 */
	@Autowired
	private SecurityQuestionService securityQuestionService;

	/**
	 * This is a positive test case method to test
	 * {@link SecurityQuestionService# getAllSecurityQuestions()}
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAllSecurityQuestionsValid() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * ArrayList of SecurityQuestion is returned to the service by the
		 * mocked DAO class method
		 */
		when(securityQuestionDAO.getAllSecurityQuestions()).thenReturn(new ArrayList<SecurityQuestion>());

		List<SecurityQuestion> securityQuestionsList = securityQuestionService.getAllSecurityQuestions();

		/*
		 * The following line of code is to make the test case passed. If the
		 * returned securityQuestionList is not null then the test case will be
		 * passed. Otherwise it will be failed
		 */
		Assert.assertNotNull(securityQuestionsList);
	}
}
