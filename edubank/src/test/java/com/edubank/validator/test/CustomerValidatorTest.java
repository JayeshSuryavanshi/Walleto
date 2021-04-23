package com.edubank.validator.test;

import java.time.LocalDate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.edubank.model.Customer;
import com.edubank.validator.CustomerValidator;

/**
 * This is a JUnit test class to test the methods of {@link CustomerValidator}
 *
 * @author ETA_JAVA
 */
public class CustomerValidatorTest {

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test
	 * {@link CustomerValidator#validateCustomerDetails(Customer customer)}
	 * method
	 * 
	 */
	@Test
	public void validateCustomerDetailsInvalidName() throws Exception {

		Customer customer = new Customer();
		customer.setName("Hari5");
		customer.setDateOfBirth(LocalDate.of(1988, 3, 13));
		customer.setEmailId("hari@gmail.com");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerValidator.INVALID_CUSTOMER_NAME");

		CustomerValidator.validateCustomerDetails(customer);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerValidator#validateCustomerDetails(Customer customer)}
	 * method
	 * 
	 */
	@Test
	public void validateCustomerDetailsInvalidEmail() throws Exception {

		Customer customer = new Customer();
		customer.setName("Hari Sharma");
		customer.setDateOfBirth(LocalDate.of(1988, 3, 13));
		customer.setEmailId("h@ari@gmailcom");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerValidator.INVALID_EMAIL");

		CustomerValidator.validateCustomerDetails(customer);
	}

	/**
	 * This is a negative test case method to test
	 * {@link CustomerValidator#validateCustomerDetails(Customer customer)}
	 * method
	 * 
	 */
	@Test
	public void validateCustomerDetailsInvalidAge() throws Exception {

		Customer customer = new Customer();
		customer.setName("Hari Sharma");
		customer.setDateOfBirth(LocalDate.now());
		customer.setEmailId("hari@gmail.com");

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("CustomerValidator.INVALID_AGE_OF_CUSTOMER");

		CustomerValidator.validateCustomerDetails(customer);
	}

	/**
	 * This is a positive test case method to test
	 * {@link CustomerValidator#validateCustomerDetails(Customer customer)}
	 * method
	 * 
	 */
	@Test
	public void validateCustomerDetailsValidDetails() throws Exception {

		Customer customer = new Customer();
		customer.setName("Hari Sharma");
		customer.setDateOfBirth(LocalDate.of(1988, 3, 13));
		customer.setEmailId("hari@gmail.com");

		CustomerValidator.validateCustomerDetails(customer);
	}
}
