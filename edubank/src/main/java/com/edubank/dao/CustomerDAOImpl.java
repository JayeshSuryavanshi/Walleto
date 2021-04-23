package com.edubank.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.edubank.entity.CustomerEntity;
import com.edubank.model.Customer;

/**
 * This Class contains the methods responsible for interacting with the database
 * with respect to Customer module like getCustomer, registerCustomer.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "customerDao")
public class CustomerDAOImpl implements CustomerDAO {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * This method is used to get a Customer model corresponding to its
	 * customerId<br>
	 * It uses session.get() method to interact with database for fetching the data
	 * 
	 * @param customerId
	 * 
	 * @return Customer
	 */
	@Override
	public Customer getCustomerByCustomerId(Integer customerId) throws Exception {

		Customer customer = null;

		/*
		 * here we are fetching the entity from database on the given condition
		 **/
		CustomerEntity customerEntity = entityManager.find(CustomerEntity.class, customerId);

		/*
		 * if we receive the entity then we are creating the bean object then setting
		 * the values from entity to bean object
		 **/
		if (customerEntity != null) {
			customer = new Customer();

			customer.setCustomerId(customerEntity.getCustomerId());
			customer.setEmailId(customerEntity.getEmailId());
			customer.setName(customerEntity.getName());
			customer.setDateOfBirth(customerEntity.getDateOfBirth());
			customer.setSecurityQuestionId(customerEntity.getSecurityQuestionId());
			customer.setSecurityAnswer(customerEntity.getSecurityAnswer());
		}

		return customer;

	}

	/**
	 * This method is used to add a new Customer with the data from the model
	 * object<br>
	 * It uses session.save() method to save the entity to the database we use
	 * tellerId from http session to set created by field
	 * 
	 * 
	 * @param customer
	 *            , tellerId
	 * 
	 * @return customerId
	 */
	@Override
	public Integer addNewCustomer(Customer customer) throws Exception {

		Integer customerId = null;

		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setName(customer.getName());
		customerEntity.setDateOfBirth(customer.getDateOfBirth());
		customerEntity.setEmailId(customer.getEmailId());
		customerEntity.setCreatedBy(customer.getCreatedBy());
		customerEntity.setSecurityQuestionId(customer.getSecurityQuestionId());
		customerEntity.setSecurityAnswer(customer.getSecurityAnswer());

		/*
		 * here we are adding the Entity to table using save method save method will
		 * return primary key of that row
		 */
		entityManager.persist(customerEntity);
		System.out.println(customerEntity.getCustomerId());

		customerId = customerEntity.getCustomerId();

		/*
		 * If we want the changes done to an entity which is in manage state(Application
		 * context) to be reflected in the database, then the transaction must be
		 * committed.
		 */

		return customerId;

	}

	/**
	 * This method is used to get the number customers present with same emailId It
	 * uses criteria to fetch the data from the database
	 * 
	 * @param emailId
	 * 
	 * @return boolean value true if the customer with same emailId present false if
	 *         the customer with same emailId is not present
	 */
	@Override
	public Boolean checkEmailAvailability(String emailId) throws Exception {

		Query query = entityManager.createQuery("select count(customerId) from CustomerEntity where emailId=:emailId");
		query.setParameter("emailId", emailId);

		Long value = (Long) query.getSingleResult();

		/*
		 * here according to the fetched result we are checking it in condition and
		 * returning the corresponding value
		 **/
		if (value == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * This method is used to get count of customers
	 * 
	 * @return number of customers
	 */
	@Override
	public Long getNoOfCustomers() throws Exception {

		Long noOfCustomers = null;

		Query query = entityManager.createQuery("select count(customerId) from CustomerEntity");

		noOfCustomers = (Long) query.getSingleResult();

		return noOfCustomers;
	}

	/**
	 * This method is used to get a Customer model corresponding to the given
	 * customer emailId. <br>
	 * It uses Hibernate criteria to interact with database for fetching the data
	 * 
	 * @param emailId
	 * 
	 * @return Customer
	 */
	@Override
	public Customer getCustomerByEmailId(String emailId) throws Exception {

		Customer customer = null;

		Query query = entityManager.createQuery("from CustomerEntity where emailId=:emailId");
		query.setParameter("emailId", emailId.toLowerCase());

		List<CustomerEntity> customerEntities = query.getResultList();

		/*
		 * if we receive the entity then we are creating the model object then setting
		 * the values from entity to model object
		 **/
		if (!customerEntities.isEmpty()) {
			CustomerEntity customerEntity = customerEntities.get(0);
			
			customer = new Customer();

			customer.setCustomerId(customerEntity.getCustomerId());
			customer.setEmailId(customerEntity.getEmailId());
			customer.setName(customerEntity.getName());
			customer.setDateOfBirth(customerEntity.getDateOfBirth());
			customer.setSecurityQuestionId(customerEntity.getSecurityQuestionId());
			customer.setSecurityAnswer(customerEntity.getSecurityAnswer());

		}

		return customer;
	}

	@Override
	public List<Customer> getAllCustomersOfTeller(Integer tellerId) throws Exception {

		Query query = entityManager.createQuery("from CustomerEntity where createdBy=:createdBy");
		query.setParameter("createdBy", tellerId);

		List<CustomerEntity> customerEntities = query.getResultList();

		/*
		 * if we receive the entity then we are creating the model object then setting
		 * the values from entity to model object
		 **/
		List<Customer> customers = new ArrayList<Customer>();
		for (CustomerEntity customerEntity : customerEntities) {
			Customer customer = new Customer();

			customer.setCustomerId(customerEntity.getCustomerId());
			customer.setEmailId(customerEntity.getEmailId());
			customer.setName(customerEntity.getName());
			customer.setDateOfBirth(customerEntity.getDateOfBirth());
			customer.setSecurityQuestionId(customerEntity.getSecurityQuestionId());
			customer.setSecurityAnswer(customerEntity.getSecurityAnswer());
			customers.add(customer);
		}

		return customers;
	}
}
