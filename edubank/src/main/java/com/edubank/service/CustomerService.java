package com.edubank.service;

import java.util.List;

import com.edubank.model.Customer;

/**
 * This interface contains the methods for business logics related to Customer,
 * like getCustomer, registerCustomer
 * 
 * @author ETA_JAVA
 *
 */
public interface CustomerService
{
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
	public Customer getCustomerByCustomerId(Integer customerId) throws Exception;

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
	public Integer addCustomer(Customer customer) throws Exception;
	
	/**
	 * This method is used to get count of customers
	 * 
	 * @return number of customers
	 */
	public Long getNoOfCustomers()throws Exception;

	
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
	public Customer getCustomerByEmailId(String emailId) throws Exception;

	public List<Customer> getAllCustomersOfTeller(Integer tellerId) throws Exception;
	
}
