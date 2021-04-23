package com.edubank.service.test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.edubank.dao.TellerDAO;
import com.edubank.model.Teller;
import com.edubank.service.TellerService;
import com.edubank.utility.Hashing;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TellerServiceTest {

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private TellerDAO tellerDao;

	/**
	 * This attribute is used in all the test case methods of
	 * {@link TellerServiceTest} to invoke the methods of {@link TellerService}.
	 * <br>
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private TellerService tellerService;

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test invalid LoginName in
	 * {@link TellerService#authenticateTellerLogin(Teller teller)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateTellerLoginIncorrectLoginName() throws Exception {
		/*
		 * The following code is to create a Teller class object with sample
		 * test data. In test data login name "T1111" is invalid.
		 */
		Teller teller = new Teller();
		teller.setLoginName("T1111");
		teller.setPassword("Steven!123");

		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(tellerDao.getLoginForTeller(anyString())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TellerService.TELLER_CREDENTIALS_INCORRECT");

		tellerService.authenticateTellerLogin(teller);
	}

	/**
	 * This is a negative test case method to test invalid Password in
	 * {@link TellerService#authenticateTellerLogin(Teller teller)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateTellerLoginCorrectLoginNameIncorrectPassword() throws Exception {
		/*
		 * The following code is to create a Teller class object with sample
		 * test data. In test data password "T137c#123" is invalid.
		 */
		Teller login = new Teller();
		login.setLoginName("T1378");
		login.setPassword("T137c#123");

		/*
		 * The below statement is to get the Teller class object used to mock
		 * the return value of call to DAO class method in service
		 */
		Teller loginFromDao = createStubTellerLogin(login.getLoginName(), "T1378*abc");

		/*
		 * The below statement mocks the DAO class method call in service and
		 * loginFromDao object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(tellerDao.getLoginForTeller(anyString())).thenReturn(loginFromDao);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TellerService.TELLER_CREDENTIALS_INCORRECT");

		tellerService.authenticateTellerLogin(login);
	}

	/**
	 * This is a positive test case method to test
	 * {@link TellerService#authenticateTellerLogin(Teller teller)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateTellerLoginCorrectDetails() throws Exception {
		/*
		 * The following code is to create a Teller class object with sample
		 * valid test data.
		 */
		Teller login = new Teller();
		login.setLoginName("T1378");
		login.setPassword("T1378*abc");

		/*
		 * The below statement is to get the Teller class object used to mock
		 * the return value of call to DAO class method in service
		 */
		Teller loginFromDao = createStubTellerLogin(login.getLoginName(), login.getPassword());

		/*
		 * The below statement mocks the DAO class method call in service and
		 * loginFromDao object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(tellerDao.getLoginForTeller(anyString())).thenReturn(loginFromDao);

		Assert.assertEquals(tellerService.authenticateTellerLogin(login),loginFromDao);
	}

	/**
	 * method to create the stub teller object
	 * 
	 * @param loginName
	 * @param password
	 * 
	 * @return teller
	 * 
	 */
	private Teller createStubTellerLogin(String loginName, String password) throws NoSuchAlgorithmException {

		/*
		 * The below code creates an object for Teller class with the values
		 * passed and returns the object
		 */
		Teller login = new Teller();
		login.setLoginName(loginName);
		login.setPassword(Hashing.getHashValue(password));

		return login;
	}
}
