package com.edubank.service.test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import com.edubank.dao.CustomerDAO;
import com.edubank.model.Customer;
import com.edubank.service.CustomerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceTest {

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private CustomerDAO customerDao;

	/**
	 * This attribute is used in all the test case methods of
	 * {@link CustomerServiceTest} to invoke the methods of
	 * {@link CustomerService}. <br>
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private CustomerService customerService;

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test invalid customerId in
	 * {@link CustomerService#getCustomerByCustomerId(Integer customerId)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void getCustomerByCustomerIdInvalidCustomerId() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(customerDao.getCustomerByCustomerId(anyInt())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerService.CUSTOMER_NOT_FOUND");

		/*
		 * The below statement makes a call to
		 * "customerService.getCustomerByCustomerId(1234)" method which is being
		 * tested with customer id 1234 as sample invalid test data
		 */
		customerService.getCustomerByCustomerId(1234);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerService#getCustomerByCustomerId(Integer customerId)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void getCustomerByCustomerIdValidCustomerId() throws Exception {
		Customer customer = new Customer();
		customer.setCustomerId(106038);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * customer object created in above is returned to the service by the
		 * mocked DAO class method
		 */
		when(customerDao.getCustomerByCustomerId(anyInt())).thenReturn(customer);

		/*
		 * The below statement makes a call to
		 * "customerService.getCustomerByCustomerId(106038)" method which is
		 * being tested with customer id 106038 as sample valid test data
		 */
		Assert.assertEquals(customerService.getCustomerByCustomerId(106038),customer);
	}

	/**
	 * This is a negative test case method to test invalid Name in
	 * {@link CustomerService#addCustomer(Customer customer, Integer tellerId)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addCustomerInvalidName() throws Exception {
		/*
		 * The following code is to create a Customer class object with sample
		 * test data. In test data customer name "Karan 98" is invalid.
		 */
		Customer customer = new Customer();
		customer.setName("Karan 98");
		customer.setEmailId("Karan@infy.com");
		customer.setDateOfBirth(LocalDate.of(1993, 10, 12));
		customer.setCreatedBy(123);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * false is returned to the service by the mocked DAO class method
		 */
		when(customerDao.checkEmailAvailability("Karan@infy.com")).thenReturn(false);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 123123 is returned to the service by the mocked DAO class method
		 */
		when(customerDao.addNewCustomer(customer)).thenReturn(123123);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerValidator.INVALID_CUSTOMER_NAME");

		customerService.addCustomer(customer);
	}

	/**
	 * This is a negative test case method to test invalid emailId in
	 * {@link CustomerService#addCustomer(Customer customer, Integer tellerId)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addCustomerInvalidEmailId() throws Exception {
		/*
		 * The following code is to create a Customer class object with sample
		 * test data. In test data email id "Ka@ran@infy.com" is invalid.
		 */
		Customer customer = new Customer();
		customer.setName("Karan");
		customer.setEmailId("Ka@ran@infy.com");
		customer.setDateOfBirth(LocalDate.of(1993, 10, 12));
		customer.setCreatedBy(123);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * false is returned to the service by the mocked DAO class method
		 */
		when(customerDao.checkEmailAvailability("Karan@infy.com")).thenReturn(false);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 123123 is returned to the service by the mocked DAO class method
		 */
		when(customerDao.addNewCustomer(customer)).thenReturn(123123);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerValidator.INVALID_EMAIL");

		customerService.addCustomer(customer);
	}

	/**
	 * This is a negative test case method to test invalid emailId in
	 * {@link CustomerService#addCustomer(Customer customer, Integer tellerId)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addCustomerExistingEmailId() throws Exception {
		/*
		 * The following code is to create a Customer class object with sample
		 * test data. In test data email id "Ka@ran@infy.com" is invalid.
		 */
		Customer customer = new Customer();
		customer.setName("Martin Jack");
		customer.setEmailId("martin@infy.com");
		customer.setDateOfBirth(LocalDate.of(1993, 10, 12));
		customer.setCreatedBy(123);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * false is returned to the service by the mocked DAO class method
		 */
		when(customerDao.checkEmailAvailability("martin@infy.com")).thenReturn(true);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 123123 is returned to the service by the mocked DAO class method
		 */
		when(customerDao.addNewCustomer(customer)).thenReturn(123123);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerService.EMAIL_ALREADY_PRESENT");

		customerService.addCustomer(customer);
	}

	/**
	 * This is a negative test case method to test invalid dateOfBirth in
	 * {@link CustomerService#addCustomer(Customer customer, Integer tellerId)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addCustomerInvalidDateOfBirth() throws Exception {
		/*
		 * The following code is to create a Customer class object with sample
		 * test data. In test data DateOfBirth 2014-10-12 is invalid.
		 */
		Customer customer = new Customer();
		customer.setName("Karan");
		customer.setEmailId("Karan@infy.com");
		customer.setDateOfBirth(LocalDate.of(2014, 10, 12));
		customer.setCreatedBy(123);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * false is returned to the service by the mocked DAO class method
		 */
		when(customerDao.checkEmailAvailability("Karan@infy.com")).thenReturn(false);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 123123 is returned to the service by the mocked DAO class method
		 */
		when(customerDao.addNewCustomer(customer)).thenReturn(123123);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerValidator.INVALID_AGE_OF_CUSTOMER");

		customerService.addCustomer(customer);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerService#addCustomer(Customer customer, Integer tellerId)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addCustomerValidData() throws Exception {
		/*
		 * The following code is to create a Customer class object with sample
		 * valid test data.
		 */
		Customer customer = new Customer();
		customer.setName("Karan");
		customer.setEmailId("Karan@infy.com");
		customer.setDateOfBirth(LocalDate.of(1993, 10, 12));
		customer.setCreatedBy(123);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * false is returned to the service by the mocked DAO class method
		 */
		when(customerDao.checkEmailAvailability("Karan@infy.com")).thenReturn(false);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 123123 is returned to the service by the mocked DAO class method
		 */
		when(customerDao.addNewCustomer(customer)).thenReturn(123123);

		Assert.assertEquals(new Integer(123123), customerService.addCustomer(customer));
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerService#getNoOfCustomers()} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void getNoOfCustomersValid() throws Exception {
		when(customerDao.getNoOfCustomers()).thenReturn(1l);

		Long noOfCustomers = customerService.getNoOfCustomers();

		Assert.assertEquals(noOfCustomers.longValue(),1l);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerService#getCustomerByEmailId(String emailId)} method with
	 * invalid email id.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void getCustomerByEmailIdInvalidEmailId() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(customerDao.getCustomerByEmailId(anyString())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerService.CUSTOMER_NOT_FOUND");

		/*
		 * The below statement makes a call to
		 * "customerService.getCustomerByEmailId("steven@infy.org")" method
		 * which is being tested with customer email id "steven@infy.org" as
		 * sample invalid test data
		 */
		customerService.getCustomerByEmailId("steven@infy.org");
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerService#getCustomerByEmailId(String emailId)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void getCustomerByEmailIdValidEmailId() throws Exception {
		Customer customer = new Customer();
		customer.setCustomerId(106038);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * customer object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(customerDao.getCustomerByEmailId(anyString())).thenReturn(customer);

		/*
		 * The below statement makes a call to
		 * "customerService.getCustomerByEmailId("steven@infy.com")" method
		 * which is being tested with customer email id "steven@infy.com" as
		 * sample valid test data
		 */
		Assert.assertEquals(customerService.getCustomerByEmailId("steven@infy.com"),customer);
	}
	
	@Test
	public void getAllCustomersOfTellerIdWithNoCustomerForTeller() throws Exception {
		
		when(customerDao.getAllCustomersOfTeller(anyInt())).thenReturn(new ArrayList<>());
		
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TellerService.NO_CUSTOMERS_FOR_TELLER");
		customerService.getAllCustomersOfTeller(101);
	}
	
	@Test
	public void getAllCustomersOfTellerIdForTeller() throws Exception {
		
		ArrayList<Customer> customers = new ArrayList<>();
		
		customers.add(new Customer());
		
		when(customerDao.getAllCustomersOfTeller(anyInt())).thenReturn(customers);
		
		Assert.assertEquals(customerService.getAllCustomersOfTeller(101),customers);
	}
	
}
