package com.edubank.service.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.edubank.dao.CustomerDAO;
import com.edubank.dao.CustomerLoginDAO;
import com.edubank.model.Customer;
import com.edubank.model.CustomerLogin;
import com.edubank.model.CustomerLoginLockedStatus;
import com.edubank.service.CustomerLoginService;
import com.edubank.utility.Hashing;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerLoginServiceTest {

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private CustomerLoginDAO customerLoginDao;

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private CustomerDAO customerDao;

	/**
	 * This attribute is used in all the test case methods of
	 * {@link CustomerLoginServiceTest} to invoke the methods of
	 * {@link CustomerLoginService}. <br>
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private CustomerLoginService customerLoginService;

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test invalid loginName in
	 * {@link CustomerLoginService#authenticateCustomerLogin(CustomerLogin login)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateCustomerLoginInvalidLoginName() throws Exception {
		/*
		 * The following code is to create a CustomerLogin class object with
		 * sample test data. In test data login name "abcd" is invalid.
		 */
		CustomerLogin login = new CustomerLogin();
		login.setLoginName("abcd");
		login.setPassword("Steven!123");

		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(customerLoginDao.getCustomerLoginByLoginName(anyString())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.INVALID_CREDENTIALS");

		customerLoginService.authenticateCustomerLogin(login);
	}

	/**
	 * This is a negative test case method to test invalid password in
	 * {@link CustomerLoginService#authenticateCustomerLogin(CustomerLogin login)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateCustomerLoginInvalidPassword() throws Exception {
		/*
		 * The following code is to create a CustomerLogin class object with
		 * sample test data. In test data password is invalid.
		 */
		CustomerLogin login = new CustomerLogin();
		login.setLoginName("steven");
		login.setPassword("Steven#123");

		/*
		 * The below statement is to get the CustomerLogin class object used to
		 * mock the return value of call to DAO class method in service
		 */
		CustomerLogin loginFromDao = createStubCustomerLogin(login.getLoginName(), "Steven!123", 
				CustomerLoginLockedStatus.UNLOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * loginFromDao object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(customerLoginDao.getCustomerLoginByLoginName(anyString())).thenReturn(loginFromDao);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.INVALID_CREDENTIALS");

		customerLoginService.authenticateCustomerLogin(login);
	}

	/**
	 * This is a negative test case method to test locked account in
	 * {@link CustomerLoginService#authenticateCustomerLogin(CustomerLogin login)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateCustomerLoginAccountLocked() throws Exception {
		/*
		 * The following code is to create a CustomerLogin class object with
		 * sample test data.
		 */
		CustomerLogin login = new CustomerLogin();
		login.setLoginName("Jeff");
		login.setPassword("Jeff!123");

		/*
		 * The below statement is to get the CustomerLogin class object used to
		 * mock the return value of call to DAO class method in service
		 */
		CustomerLogin loginFromDao = createStubCustomerLogin(login.getLoginName(), 
				login.getPassword(), CustomerLoginLockedStatus.LOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * loginFromDao object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(customerLoginDao.getCustomerLoginByLoginName(anyString())).thenReturn(loginFromDao);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.ACCOUNT_LOCKED");

		customerLoginService.authenticateCustomerLogin(login);
	}

	/**
	 * This is a positive test case method to test in
	 * {@link CustomerLoginService#authenticateCustomerLogin(CustomerLogin login)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void authenticateCustomerLoginValidDetails() throws Exception {
		CustomerLogin login = new CustomerLogin();
		login.setLoginName("Steven");
		login.setPassword("Steven!123");

		CustomerLogin loginFromDao = createStubCustomerLogin(login.getLoginName(), login.getPassword(), 
				CustomerLoginLockedStatus.UNLOCKED);
		loginFromDao.setCustomerId(10101);
		
		when(customerLoginDao.getCustomerLoginByLoginName(anyString())).thenReturn(loginFromDao);
		
		Customer customer = new Customer();
		customer.setName("Steven Martin");
		customer.setEmailId("steven@infy.com");
		
		when(customerDao.getCustomerByCustomerId(anyInt())).thenReturn(customer);
		
		System.out.println("return value:=============:"+customerLoginService.authenticateCustomerLogin(login));
		System.out.println("customer object = "+customer);
		Assert.assertEquals(customerLoginService.authenticateCustomerLogin(login),customer);
	}

	/**
	 * This is a negative test case method to test invalid password in
	 * {@link CustomerLoginService#changeCustomerPassword(CustomerLogin login)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void changeCustomerPasswordInvalidPassword() throws Exception {
		/*
		 * The following code is to create a CustomerLogin class object with
		 * sample test data. In test data password "Steven*123" is invalid.
		 */
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setCustomerId(106038);
		customerLogin.setPassword("Steven*123");
		customerLogin.setNewPassword("Steven!123");
		customerLogin.setConfirmNewPassword("Steven!123");

		/*
		 * The below statement is to get the CustomerLogin class object used to
		 * mock the return value of call to DAO class method in service
		 */
		CustomerLogin customerLoginFromDao = createStubCustomerLogin(null, "Steven!123", null);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * loginFromDao object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(customerLoginDao.getCustomerLoginByCustomerId(106038)).thenReturn(customerLoginFromDao);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.INVALID_PASSWORD");

		customerLoginService.changeCustomerPassword(customerLogin);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerLoginService#changeCustomerPassword(CustomerLogin customerLogin)}
	 * method
	 * 
	 */
	@Test
	public void changeCustomerPasswordNewPasswordSameAsOldPassword() throws Exception {
		/*
		 * The following code is to create a CustomerLogin class object with
		 * sample test data. In test data password "Steven*123" is invalid.
		 */
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setCustomerId(106038);
		customerLogin.setPassword("Steven*123");
		customerLogin.setNewPassword("Steven*123");
		customerLogin.setConfirmNewPassword("Steven*123");

		/*
		 * The below statement is to get the CustomerLogin class object used to
		 * mock the return value of call to DAO class method in service
		 */
		CustomerLogin customerLoginFromDao = createStubCustomerLogin("Steven", "Steven*123", CustomerLoginLockedStatus.UNLOCKED);

		
		/*
		 * The below statement mocks the DAO class method call in service and
		 * loginFromDao object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(customerLoginDao.getCustomerLoginByCustomerId(106038)).thenReturn(customerLoginFromDao);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.OLD_PASSWORD_NEW_PASSWORD_SAME");

		customerLoginService.changeCustomerPassword(customerLogin);
	}
	
	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginService#changeCustomerPassword(CustomerLogin login)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void changeCustomerPasswordValidPassword() throws Exception {
		/*
		 * The following code is to create a CustomerLogin class object with
		 * sample valid test data.
		 */
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setCustomerId(106038);
		customerLogin.setPassword("Steven!123");
		customerLogin.setNewPassword("Steven*123");
		customerLogin.setConfirmNewPassword("Steven*123");
		/*-------------------------------------------------------------------------------------------*/

		/*
		 * The below statement is to get the CustomerLogin class object used to
		 * mock the return value of call to DAO class method in service
		 */
		CustomerLogin customerLoginFromDao = createStubCustomerLogin(null, "Steven!123", null);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * loginFromDao object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(customerLoginDao.getCustomerLoginByCustomerId(106038)).thenReturn(customerLoginFromDao);

		customerLoginService.changeCustomerPassword(customerLogin);
	}

	/**
	 * Creates the stub object of customerLogin and return
	 * 
	 * @param loginName
	 * @param password
	 * @param isLocked
	 * 
	 * @return customerLogin
	 * 
	 */
	private CustomerLogin createStubCustomerLogin(String loginName, String password, CustomerLoginLockedStatus lockedStatus)
			throws NoSuchAlgorithmException {
		/*
		 * The below code creates an object for CustomerLogin class with the
		 * values passed and returns the object
		 */
		CustomerLogin login = new CustomerLogin();
		login.setLoginName(loginName);
		login.setPassword(Hashing.getHashValue(password));
		login.setLockedStatus(lockedStatus);

		return login;
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginService#addCustomerLogin(Customer customer)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addCustomerLoginValidDataFirstLoginName() throws Exception {
		/*
		 * The following code is to create a Customer class object with sample
		 * valid test data.
		 */
		Customer customer = new Customer();
		customer.setName("Joe");
		customer.setEmailId("Joe@infy.com");
		customer.setDateOfBirth(LocalDate.of(1993, 10, 12));
		customer.setCustomerId(12121212);

		/*
		 * The below statement mocks the DAO class method call in service and 0
		 * is returned to the service by the mocked DAO class method
		 */
		when(customerLoginDao.checkAvailabilityOfloginName(anyString())).thenReturn(0l);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 12121 is returned to the service by the mocked DAO class method
		 */
		when(customerLoginDao.addNewCustomerLogin(any())).thenReturn(12121);

		CustomerLogin customerLogin = customerLoginService.addCustomerLogin(customer);

		/*
		 * The following line of code is to make the test case passed. If
		 * returned customerLogin object has "joe" as login name then the test
		 * case will be passed else the test case will be failed. If any
		 * exception comes from the call to the method
		 * "customerLoginService.addCustomerLogin(customer)" which is being
		 * tested then the following line of code will not be executed and the
		 * test case will be failed.
		 */
		Assert.assertTrue(
				customerLogin.getLoginName().equals("joe") && customerLogin.getPassword().equals("Joe!12345"));
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginService#addCustomerLogin(CustomerLogin login)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addCustomerLoginValidDataSecondLoginName() throws Exception {
		/*
		 * The following code is to create a Customer class object with sample
		 * valid test data.
		 */
		Customer customer = new Customer();
		customer.setName("Karan");
		customer.setEmailId("Karan@infy.com");
		customer.setDateOfBirth(LocalDate.of(1993, 10, 12));
		customer.setCustomerId(12121212);

		/*
		 * The below statement mocks the DAO class method call in service and 1
		 * is returned to the service by the mocked DAO class method
		 */
		when(customerLoginDao.checkAvailabilityOfloginName(anyString())).thenReturn(1l);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 12121 is returned to the service by the mocked DAO class method
		 */
		when(customerLoginDao.addNewCustomerLogin(any())).thenReturn(12121);

		CustomerLogin customerLogin = customerLoginService.addCustomerLogin(customer);

		/*
		 * The following line of code is to make the test case passed. If
		 * returned customerLogin object has "karan2" as login name then the
		 * test case will be passed else the test case will be failed. If any
		 * exception comes from the call to the method
		 * "customerLoginService.addCustomerLogin(customer)" which is being
		 * tested then the following line of code will not be executed and the
		 * test case will be failed.
		 */
		Assert.assertEquals("karan2", customerLogin.getLoginName());
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginService#addCustomerLogin(Customer customer)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addCustomerLoginValidDataLengthyLoginName() throws Exception

	{
		/*
		 * The following code is to create a Customer class object with sample
		 * valid test data.
		 */
		Customer customer = new Customer();
		customer.setName("Karan");
		customer.setEmailId("charlesDavidEarlFrederick@infy.com");
		customer.setDateOfBirth(LocalDate.of(1993, 10, 12));
		customer.setCustomerId(12121212);

		/*
		 * The below statement mocks the DAO class method call in service and 0
		 * is returned to the service by the mocked DAO class method
		 */
		when(customerLoginDao.checkAvailabilityOfloginName(anyString())).thenReturn(0l);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 12121 is returned to the service by the mocked DAO class method
		 */
		when(customerLoginDao.addNewCustomerLogin(any())).thenReturn(121223);

		CustomerLogin customerLogin = customerLoginService.addCustomerLogin(customer);

		/*
		 * The following line of code is to make the test case passed. If
		 * returned customerLogin object has "charlesdavidearl" as login name
		 * then the test case will be passed else the test case will be failed.
		 * If any exception comes from the call to the method
		 * "customerLoginService.addCustomerLogin(customer)" which is being
		 * tested then the following line of code will not be executed and the
		 * test case will be failed.
		 */
		Assert.assertEquals("charlesdavidearl", customerLogin.getLoginName());
	}
}
