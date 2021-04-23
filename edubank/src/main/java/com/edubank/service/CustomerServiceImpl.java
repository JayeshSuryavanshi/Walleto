package com.edubank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.CustomerDAO;
import com.edubank.model.Customer;
import com.edubank.validator.CustomerValidator;

/**
 * This Class contains the methods for business logics related to Customer, like
 * getCustomer, registerCustomer
 * 
 * @author ETA_JAVA
 *
 */
@Service(value = "customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDAO customerDao;

	/**
	 * This method is used to get the Customer object corresponding to the given
	 * customerId
	 * 
	 * @param customerId
	 * 
	 * @return customer
	 * 
	 * @throws Exception
	 *             if the there is no Customer record corresponding to the given
	 *             customerId
	 */
	@Override
	public Customer getCustomerByCustomerId(Integer customerId)
			throws Exception {

		/*
		 * This method is used to get a Customer model corresponding to its
		 * customerId It uses session.get() method to interact with database for
		 * fetching the data
		 */
		Customer customer = customerDao.getCustomerByCustomerId(customerId);
		
		if (customer == null) {
			throw new Exception(
					"CustomerService.CUSTOMER_NOT_FOUND");
		}

		return customer;
	}

	/**
	 * This method is used to register a new customer by adding each record to
	 * Customer, CustomerLogin, Account, AccountCustomerMapping and DebitCard
	 * database tables after validating input parameters
	 * 
	 * After successful registration a mail will be sent to the customer
	 * registered mail id with the details generated
	 * 
	 * 
	 * @param customer, tellerId
	 * 
	 * @return Integer
	 * 
	 * @throws Exception 
	 *             if the name specified by the customer is not valid
	 *             or if the email specified by the customer
	 *             is not valid or if the age of the customer
	 *             is less than 18 years
	 *             
	 * @throws Exception 
	 * 
	 * @throws Exception 
	 * 				if email given by the customer is already registered with the EDUBank
	 *             
	 */
	@Override
	public Integer addCustomer(Customer customer) throws Exception
	{
		/*
		 * The below statement calls Validator method to validate registration details
		 */
		CustomerValidator.validateCustomerDetails(customer);

		customer.setEmailId(customer.getEmailId().toLowerCase());
		
		/*
		 * This method is used to get the number customers present with same
		 * emailId It uses criteria to fetch the data from the database
		 */
		Boolean notAvailable = customerDao.checkEmailAvailability(customer.getEmailId());

		if (notAvailable) {
			throw new Exception("CustomerService.EMAIL_ALREADY_PRESENT");
		}

		/*
		 * This method is used to add a new Customer with the data from the
		 * model object It uses session.save() method to save the entity to the
		 * database we use tellerId from HTTP session to set created by field
		 */
		Integer customerId = customerDao.addNewCustomer(customer);

		return customerId;
	}

	/**
	 * This method is used to get count of customers
	 * 
	 * @return number of customers
	 */
	@Override
	public Long getNoOfCustomers()throws Exception {
		
		return customerDao.getNoOfCustomers();

	}

	/**
	 * This method is used to get the Customer object corresponding to the given
	 * emailId by making a corresponding DAO call
	 * 
	 * @param emailId
	 * 
	 * @return Customer
	 * 
	 * @throws Exception
	 *             if  there is no Customer record corresponding to the given emailId
	 */
	@Override
	public Customer getCustomerByEmailId(String emailId) throws Exception
	{

		Customer customer = null;

		/*
		 * The below method call is used to get a Customer model corresponding to its
		 * emailId. It uses session.get() method to interact with database for
		 * fetching the data
		 */
		customer = customerDao.getCustomerByEmailId(emailId);
		
		if (customer == null) {
			throw new Exception(
					"CustomerService.CUSTOMER_NOT_FOUND");
		}

		return customer;
	}

	@Override
	public List<Customer> getAllCustomersOfTeller(Integer tellerId) throws Exception {
		List<Customer> customers = customerDao.getAllCustomersOfTeller(tellerId);
		if(customers.isEmpty()) {
			throw new Exception("TellerService.NO_CUSTOMERS_FOR_TELLER");
		}
		return customers;
	}

}
