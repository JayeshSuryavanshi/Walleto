package com.edubank.dao;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.edubank.entity.CustomerLoginEntity;
import com.edubank.model.CustomerLogin;
import com.edubank.utility.Hashing;

/**
 * This class contains the methods responsible for interacting the database with
 * respect to Login module like getLoginOfCustomer, changeCustomerPassword.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "customerloginDao")
public class CustomerLoginDAOImpl implements CustomerLoginDAO {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * This method is used to get a CustomerLogin model corresponding to its
	 * loginName<br>
	 * 
	 * @param loginName
	 * 
	 * @return customerLogin
	 */
	@Override
	public CustomerLogin getCustomerLoginByLoginName(String loginName) throws Exception {

		loginName = loginName.toLowerCase();
		CustomerLogin customerLogin = null;

		Query query = entityManager.createQuery("from CustomerLoginEntity where loginName=:loginName");
		query.setParameter("loginName", loginName);

		List<CustomerLoginEntity> customerLoginEntities = query.getResultList();
		
		
		
		/* here we are getting values from entity class and setting it to bean class */
		if (!customerLoginEntities.isEmpty()) {
			CustomerLoginEntity customerLoginEntity = customerLoginEntities.get(0);
			customerLogin = new CustomerLogin();
			customerLogin.setCustomerLoginId(customerLoginEntity.getCustomerLoginId());
			customerLogin.setCustomerId(customerLoginEntity.getCustomerId());
			customerLogin.setLoginName(customerLoginEntity.getLoginName());
			customerLogin.setLockedStatus(customerLoginEntity.getLockedStatus());
			customerLogin.setPassword(customerLoginEntity.getPassword());
		}

		return customerLogin;
	}

	/**
	 * This method is used to get a CustomerLogin model corresponding to its
	 * customerId<br>
	 * 
	 * @param customerId
	 * 
	 * @return customerLogin
	 */
	@Override
	public CustomerLogin getCustomerLoginByCustomerId(Integer customerId) throws Exception {
		CustomerLogin customerLogin = null;

		Query query = entityManager.createQuery("from CustomerLoginEntity where customerId=:customerId");
		query.setParameter("customerId", customerId);

		List<CustomerLoginEntity> customerLoginEntities = query.getResultList();

		/* here we are getting values from entity class and setting it to bean class */
		if (!customerLoginEntities.isEmpty()) {
			CustomerLoginEntity customerLoginEntity = customerLoginEntities.get(0);
			customerLogin = new CustomerLogin();
			customerLogin.setCustomerLoginId(customerLoginEntity.getCustomerLoginId());
			customerLogin.setCustomerId(customerLoginEntity.getCustomerId());
			customerLogin.setLoginName(customerLoginEntity.getLoginName());
			customerLogin.setLockedStatus(customerLoginEntity.getLockedStatus());
			customerLogin.setPassword(customerLoginEntity.getPassword());
		}

		return customerLogin;
	}

	/**
	 * This method is used to change the password of an existing Customer. It takes
	 * CustomerLogin as a parameter, which includes, customerId and newPassword, it
	 * fetches an entity on the basis of customerId, and updates the password to
	 * newPassword received.<br>
	 * 
	 * @param customerLogin
	 * 
	 */
	@Override
	public void changeCustomerPassword(CustomerLogin customerLogin) throws Exception {

		Query query = entityManager.createQuery("from CustomerLoginEntity where customerId=:customerId");
		query.setParameter("customerId", customerLogin.getCustomerId());

		CustomerLoginEntity customerLoginEntity = (CustomerLoginEntity) query.getResultList().get(0);

		/*
		 * Where we are setting the new value in entity which is to be updated
		 */
		customerLoginEntity.setPassword(customerLogin.getNewPassword());

		/*
		 * If we want the changes done to an entity which is in manage state(Application
		 * context) to be reflected in the database, then the transaction must be
		 * committed.
		 */

	}

	/**
	 * This method is check weather the loginName is already present in the database
	 * if so return the number of records with similar loginName<br>
	 *
	 * @param loginName
	 * 
	 * @return numberOfRecordWithSameLogin
	 */
	@Override
	public Long checkAvailabilityOfloginName(String loginName) throws Exception {

		Long noOfCustomerWithsameLoginName = null;

		Query query = entityManager
				.createQuery("select count(customerLoginId) from CustomerLoginEntity where loginName like :loginName");
		query.setParameter("loginName", loginName + "%");

		noOfCustomerWithsameLoginName = (Long) query.getSingleResult();

		return noOfCustomerWithsameLoginName;

	}

	/**
	 * This method is used to persist a new login details for the newly added
	 * Customer with the data from the model object<br>
	 *
	 * it finds the loginName by using entered name
	 * 
	 * populate all the values to the entity from bean and persist to the database
	 * 
	 * @param customer
	 * 
	 * @return customerLoginId
	 */
	@Override
	public Integer addNewCustomerLogin(CustomerLogin customerLogin) throws NoSuchAlgorithmException, Exception {

		Integer customerLoginId = null;

		CustomerLoginEntity customerLoginEntity = new CustomerLoginEntity();

		customerLoginEntity.setCustomerId(customerLogin.getCustomerId());
		customerLoginEntity.setLockedStatus(customerLogin.getLockedStatus());
		customerLoginEntity.setLoginName(customerLogin.getLoginName());
		customerLoginEntity.setPassword(Hashing.getHashValue(customerLogin.getPassword()));

		/*
		 * here we are adding the customerLoginEntity to CUSTOMER_LOGIN table using save
		 * method save method will return primary key of that row
		 */
		entityManager.persist(customerLoginEntity);

		customerLoginId = customerLoginEntity.getCustomerLoginId();

		/*
		 * If we want the changes done to an entity which is in manage state(Application
		 * context) to be reflected in the database, then the transaction must be
		 * committed.
		 */

		return customerLoginId;

	}
}
