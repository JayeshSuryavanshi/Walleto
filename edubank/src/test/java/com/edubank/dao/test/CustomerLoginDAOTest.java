package com.edubank.dao.test;

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

import com.edubank.dao.CustomerLoginDAO;
import com.edubank.model.CustomerLogin;
import com.edubank.model.CustomerLoginLockedStatus;
import com.edubank.utility.Hashing;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class CustomerLoginDAOTest {

	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link CustomerLoginDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private CustomerLoginDAO customerLoginDAO;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test invalid loginName
	 * {@link CustomerLoginDAO#getCustomerLoginByLoginName(String loginName)}
	 * method. <br>
	 * In test data customer login name "abcd" is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getCustomerLoginByLoginNameInvalidLoginName() throws Exception 
	{
		/*
		 * "abcd" is a sample invalid test data in the below statement to
		 * test the method
		 */
		
		CustomerLogin customerLogin = customerLoginDAO.getCustomerLoginByLoginName("abcd");
		Assert.assertNull(customerLogin);

	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginDAO#getCustomerLoginByLoginName(String loginName)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getCustomerLoginByLoginNameValidLoginName() throws Exception 
	{
		/*
		 * "Steven" is a sample valid test data in the below statement to
		 * test the method
		 */
		CustomerLogin customerLogin = customerLoginDAO
				.getCustomerLoginByLoginName("Steven");

		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "customerLoginDAO.getCustomerLoginByLoginName(loginName)"
		 * which is being tested or return value is null then the
		 * test case will not be passed.
		 */
		Assert.assertNotNull(customerLogin);
	}

	/**
	 * This is a negative test case method to test invalid customerId
	 * {@link CustomerLoginDAO#getCustomerLoginByCustomerId(Integer customerId)}
	 * method. <br>
	 * In test data customer id 102 is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getCustomerLoginByCustomerIdInvalidCustomerId() throws Exception
	{
		CustomerLogin customerLogin = customerLoginDAO.getCustomerLoginByCustomerId(102);
		Assert.assertNull(customerLogin);

	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginDAO#getCustomerLoginByCustomerId(Integer customerId)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getCustomerLoginByCustomerIdValidCustomerId() throws Exception
	{
		CustomerLogin customerLogin = customerLoginDAO
				.getCustomerLoginByCustomerId(552092);

		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "customerLoginDAO.getCustomerLoginByCustomerId(102)"
		 * which is being tested or return value is null then the
		 * test case will not be passed.
		 */
		Assert.assertNotNull(customerLogin);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginDAO#changeCustomerPassword(CustomerLogin customerLogin)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void changeCustomerPasswordValidDetails() throws Exception
	{
		/*
		 * The following code is to create a CustomerLogin class object with
		 * sample valid test data.
		 */
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setCustomerId(106038);
		customerLogin.setNewPassword(Hashing.getHashValue("Steven^123"));

		customerLoginDAO.changeCustomerPassword(customerLogin);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "customerLoginDAO.changeCustomerPassword(customerLogin)" which is
		 * being tested then the following line of code will not be executed
		 * and the test case will not be passed.
		 */
		Assert.assertTrue(true);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginDAO#addNewCustomerLogin(CustomerLogin customerLogin)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void addNewCustomerLoginValidDetails() throws Exception {

		/*
		 * The following code is to create a CustomerLogin class object with
		 * sample valid test data.
		 */
		CustomerLogin customerLogin = new CustomerLogin();
		customerLogin.setLoginName("Karan2");
		customerLogin.setLockedStatus(CustomerLoginLockedStatus.UNLOCKED);
		customerLogin.setPassword("Karan!123");
		customerLogin.setCustomerId(719027);

		Integer customerLoginId = customerLoginDAO
				.addNewCustomerLogin(customerLogin);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "customerLoginDAO.addNewCustomerLogin(customerLogin)" which is
		 * being tested or return value is null then 
		 * the test case will not be passed.
		 */
		Assert.assertNotNull(customerLoginId);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginDAO#checkAvailabilityOfloginName(String loginName) }
	 * 
	 * @throws Exception 
	 */
	@Test
	public void checkAvailabilityOfloginNameExistingLoginName() throws Exception
	{
		// "martin" is a sample test data to test the method. It is an already existing login name.	
		String loginName = "martin";
			
		Long noOfCustomerWithLoginName = customerLoginDAO.checkAvailabilityOfloginName(loginName);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "customerLoginDAO.checkAvailabilityOfloginName(loginName)" which is
		 * being tested or return value is null or zero then 
		 * the test case will not be passed.
		 */
		Assert.assertTrue(noOfCustomerWithLoginName!=null && noOfCustomerWithLoginName>0);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerLoginDAO#checkAvailabilityOfloginName(String loginName) }
	 * 
	 * @throws Exception 
	 */
	@Test
	public void checkAvailabilityOfloginNameNewLoginName() throws Exception {
		
		// "joseph" is a sample test data to test the method. It is a new login name.	
		String loginName = "joseph";
		
		Long noOfCustomerWithLoginName = customerLoginDAO.checkAvailabilityOfloginName(loginName);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "customerLoginDAO.checkAvailabilityOfloginName(loginName)" which is
		 * being tested or return value is not null or not zero then 
		 * the test case will not be passed.
		 */
		Assert.assertTrue(noOfCustomerWithLoginName==null || noOfCustomerWithLoginName==0);
	}
}
