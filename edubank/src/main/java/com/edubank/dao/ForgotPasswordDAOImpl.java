package com.edubank.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.edubank.entity.CustomerEntity;
import com.edubank.entity.CustomerLoginEntity;

/**
 * This is a DAO class implementing ForgotPasswordDAO having methods to perform
 * CRUD operations on CUSTOMER and CUSTOMER_LOGIN tables for customer
 * forgot/reset password requests
 *
 * @author ETA_JAVA
 * 
 */
@Repository(value = "forgotPasswordDAO")
public class ForgotPasswordDAOImpl implements ForgotPasswordDAO {
	/** This is a spring auto-wired attribute used to create data base sessions */
	@PersistenceContext
	EntityManager entityManager;

	/**
	 * @see com.edubank.dao.ForgotPasswordDAO#authenticateEmailId(java.lang.String)
	 */
	@Override
	public String authenticateEmailId(String emailId) throws Exception {
		Query query = entityManager.createQuery("from CustomerEntity where emailId=:emailId");
		query.setParameter("emailId", emailId.toLowerCase());
		List<CustomerEntity> listOfUserEntities = query.getResultList();

		if (listOfUserEntities != null && !listOfUserEntities.isEmpty())
			return "Found";
		else
			return "Not Found";
	}

	/**
	 * @see com.edubank.dao.ForgotPasswordDAO#resetPassword(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void resetPassword(String emailId, String hashedPassword) throws Exception {

		Query query = entityManager.createQuery("from CustomerEntity where emailId=:emailId");
		query.setParameter("emailId", emailId.toLowerCase());
		CustomerEntity customerEntity = (CustomerEntity) query.getSingleResult();

		Query query2 = entityManager.createQuery("from CustomerLoginEntity where customerId=:customerId");
		query2.setParameter("customerId", customerEntity.getCustomerId());
		CustomerLoginEntity customerLoginEntity = (CustomerLoginEntity) query2.getSingleResult();
		customerLoginEntity.setPassword(hashedPassword);

	}

}
