package com.edubank.dao.test;

import java.time.LocalDate;
import java.util.List;

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

import com.edubank.dao.CustomerDAO;
import com.edubank.model.Customer;

/**
 * This is a JUnit test class to test the methods of {@link CustomerDAO}. <br>
 * It covers both positive and negative test cases.
 *
 * @author ETA_JAVA
 *
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class CustomerDAOTest {
	
	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link CustomerDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private CustomerDAO customerDAO;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	/**
	 * This is a negative test case method to test
	 * {@link CustomerDAO#getCustomerByCustomerIdInvalidCustomerId(Integer customerId)}
	 * method. <br>
	 * In test data customer id 1324 is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getCustomerByCustomerIdInvalidCustomerId() throws Exception {
			
		/*
		 * This customer id is a sample invalid test data to test the method
		 */
		Integer customerId = 1324;

		Customer customer = customerDAO.getCustomerByCustomerId(customerId);
			
		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "customerDAO.getCustomerByCustomerId(customerId)" which is
		 * being tested or return value is not null then the test case
		 * will not be passed.
		 */
		Assert.assertNull(customer);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerDAO#getCustomerByCustomerIdInvalidCustomerId(Integer customerId)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getCustomerByCustomerIdValidCustomerId() throws Exception 
	{
		/*
		 * This customer id is a sample invalid test data to test the method
		 */
		Integer customerId = 106038;

		Customer customer = customerDAO.getCustomerByCustomerId(customerId);

		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "customerDAO.getCustomerByCustomerId(customerId)" which is
		 * being tested or return value is null then the test case
		 * will not be passed.
		 */
		Assert.assertNotNull(customer);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerDAO#addNewCustomer(Customer customer, Integer tellerId)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void addNewCustomerTest() throws Exception {

		/*
		 * The following code is to create a Customer class object with
		 * sample valid test data.
		 */
		Customer customer = new Customer();
		customer.setDateOfBirth(LocalDate.of(1993, 12, 13));
		customer.setEmailId("Karan@infy.com");
		customer.setName("Karan");
		customer.setCreatedBy(103);

		Integer customerId = customerDAO.addNewCustomer(customer);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "customerDAO.addNewCustomer(customer)" which is being tested or return value is null 
		 * then the test case will not be passed.
		 */
		Assert.assertNotNull(customerId);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerDAO#getNoOfCustomers()}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getNoOfCustomersValid() throws Exception 
	{
		Long noOfCustomers = customerDAO.getNoOfCustomers();

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "customerDAO.getNoOfCustomers()" which is being tested or return value is null 
		 * then the test case will not be passed.
		 */
		Assert.assertNotNull(noOfCustomers);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerDAO#checkEmailAvailability(String emailId)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void checkEmailAvailabilityExistingEmail() throws Exception {
		
		String emailId = "martin@infy.com";
		
		Boolean result = customerDAO.checkEmailAvailability(emailId);

		Assert.assertTrue(result);	
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerDAO#checkEmailAvailability(String emailId)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void checkEmailAvailabilityNewEmail() throws Exception {
		
		
		String emailId = "joseph@infy.com";

		Boolean result = customerDAO.checkEmailAvailability(emailId);
		
		Assert.assertFalse(result);
	}
	
	/**
	 * This is a negative test case method to test
	 * {@link CustomerDAO#getCustomerByEmailId(String emailId)}
	 * method. <br>
	 * In test data email id "steven@infy.org" is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getCustomerByEmailIdInvalidEmailId() throws Exception 
	{
		String emailId = "steven@infy.org";

		Customer customer = customerDAO.getCustomerByEmailId(emailId);
		Assert.assertNull(customer);
		
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerDAO#getCustomerByEmailId(String emailId)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getCustomerByEmailIdValidEmailId() throws Exception 
	{
		String emailId = "steven@infy.com";

		Customer customer = customerDAO.getCustomerByEmailId(emailId);
		
		Assert.assertNotNull(customer);
	}
	
	@Test
	public void getAllCustomerOfTellerValidTellerId() throws Exception {
		
		Integer tellerId = 103; 
		
		List<Customer> customers = customerDAO.getAllCustomersOfTeller(tellerId);
		
		Assert.assertTrue(customers.size()>0);
		
	}
	
	@Test
	public void getAllCustomerOfTellerInvalidTellerId() throws Exception {
		
		Integer tellerId = 309; 
		
		List<Customer> customers = customerDAO.getAllCustomersOfTeller(tellerId);
		
		Assert.assertTrue(customers.size()==0);
		
	}
}
