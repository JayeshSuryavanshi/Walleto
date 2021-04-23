package com.edubank.dao;

import java.security.NoSuchAlgorithmException;

import com.edubank.model.CustomerLogin;

/**
 * This Interface contains the method responsible for interacting the database
 * with respect to Login module like getLoginOfCustomer, changeCustomerPassword.
 * 
 * @author ETA_JAVA
 *
 */
public interface CustomerLoginDAO {
	/**
	 * This method is used to get a CustomerLogin model corresponding to its
	 * loginName<br>
	 * 
	 * @param loginName
	 * 
	 * @return customerLogin
	 * 
	 * @throws Exception 
	 */
	public CustomerLogin getCustomerLoginByLoginName(String loginName) throws Exception;

	/**
	 * This method is used to get a CustomerLogin model corresponding to its
	 * customerId<br>
	 * 
	 * @param customerId
	 * 
	 * @return customerLogin
	 * 
	 * @throws Exception 
	 */
	public CustomerLogin getCustomerLoginByCustomerId(Integer customerId) throws Exception;

	/**
	 * This method is used to change the password of an existing Customer. It
	 * takes CustomerLogin as a parameter, which includes, customerId and
	 * newPassword, it fetches an entity on the basis of customerId, and updates
	 * the password to newPassword received.<br>
	 * 
	 * @param customerLogin
	 * 
	 * @throws Exception 
	 */
	public void changeCustomerPassword(CustomerLogin customerLogin) throws Exception;
	
	/**
	 * This method is check weather the loginName is already present in the database 
	 * if so return the number of records with similar loginName<br>
	 *
	 * @param loginName
	 * 
	 * @return numberOfRecordWithSameLogin
	 * 
	 * @throws Exception
	 */
	public Long checkAvailabilityOfloginName(String loginName) throws Exception;

	/**
	 * This method is used to persist a new login details for the newly added 
	 * Customer  with the data from the model object<br>
	 *
	 * it finds the loginName by using entered eame
	 * 
	 * populate all the values to the entity from bean and persist to the database
	 * 
	 * @param customer
	 * 
	 * @return customerLoginId
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	public Integer addNewCustomerLogin(CustomerLogin customerLogin) throws NoSuchAlgorithmException, Exception;
	
}
