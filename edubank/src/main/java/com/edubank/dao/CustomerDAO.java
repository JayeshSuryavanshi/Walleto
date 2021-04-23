package com.edubank.dao;

import java.util.List;

import com.edubank.model.Customer;

/**
 * This Interface contains the method responsible for interacting the database with
 * respect to Customer module like getCustomer, registerCustomer.
 * 
 * @author ETA_JAVA
 *
 */
public interface CustomerDAO {

	/**
	 * This method is used to get a Customer model corresponding to its
	 * customerId<br>
	 * It uses session.get() method to interact with database for fetching the
	 * data
	 * 
	 * @param customerId
	 * 
	 * @return Customer
	 */
	public Customer getCustomerByCustomerId(Integer customerId) throws Exception;
	
	/**
	 * This method is used to add a new Customer with the data from the model object<br>
	 * It uses session.save() method to save the entity to the database
	 * we use tellerId from http session to set created by field
	 * 
	 * 
	 * @param customer, tellerId
	 * 
	 * @return customerId
	 */
	public Integer addNewCustomer(Customer customer) throws Exception;

	/**
	 * This method is used to get the number customers present with same emailId
	 * It uses criteria to fetch the data from the database
	 * 
	 * @param emailId
	 * 
	 * @return boolean value
	 * 		true if the customer with same emailId present
	 * 		false if the customer with same emailId is not present
	 */
	public Boolean checkEmailAvailability(String emailId) throws Exception;
	
	/**
	 * This method is used to get count of customers
	 * 
	 * @return number of customers
	 */
	public Long getNoOfCustomers() throws Exception;

	/**
	 * This method is used to get a Customer model corresponding to the given customer 
	 * emailId. <br>
	 * It uses Hibernate criteria to interact with database for fetching the
	 * data
	 * 
	 * @param emailId
	 * 
	 * @return Customer
	 */
	public Customer getCustomerByEmailId(String emailId) throws Exception;

	List<Customer> getAllCustomersOfTeller(Integer tellerId) throws Exception;
	
}
