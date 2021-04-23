package com.edubank.service;

import java.security.NoSuchAlgorithmException;

import com.edubank.model.Customer;
import com.edubank.model.CustomerLogin;

/**
 * This interface contains the methods for business logics related to Login
 * modules, like authenticate customer login, change password, etc.
 * 
 * @author ETA_JAVA
 *
 */
public interface CustomerLoginService
{
	
	/**
	 * This method is used to authenticate the login credentials entered by
	 * customer. It checks the loginName and password with input field
	 * validation, if valid it verifies the credential with the database. If
	 * verified, it checks whether the account is locked. <br>
	 * 
	 * @param customerLogin
	 * 
	 * @return customer object if the credentials are valid and the account is
	 *         not locked
	 * 
	 * @throws Exception
	 *             if the loginName is not in proper format
	 * 				or
	 *             if the password is not in proper format
	 *				or
	 *             if the loginName is not matching any record in the database
	 *             or the password entered is incorrect with respect to the
	 *             loginName.
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 * @throws Exception
	 *             if the credentials are proper but the account is locked
	 * @throws Exception
	 */
	public Customer authenticateCustomerLogin(CustomerLogin customerLogin)
			throws Exception;
	
	/**
	 * This method is used to change the password of logged in user. It checks
	 * the password, newPassword and confirmNewPassword with input field
	 * validation, if they are in proper format, it checks whether the
	 * newPassword is matching the confirmNewPassword, then it checks if the old
	 * password and the new password are same.
	 * 
	 * @param customerLogin
	 * 
	 * @throws Exception
	 *             if the password is not in proper format or 

	 *             if the newPassword is not in proper format or

	 *             if the confirmNewPassword is not in proper format or 

	 *             if newPassword is same as old password or

	 *             if confirmNewPassword is not same as newPassword or

	 *             if the current password is not matching the record of logged
	 *             in user
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 * @throws Exception
	 */
	public void changeCustomerPassword(CustomerLogin customerLogin) throws Exception;


	/**
	 * This method is used to add a new login details for the newly added 
	 * Customer  with the data from the model object<br>
	 *
	 * it finds the loginName by using entered emailId, i.e. it takes first part of it (before @)
	 * then it is checked in the database for any usage of the same login name
	 * if same loginName present then get number of loginNames with same name
	 * add one to it and append it to the loginName
	 * 
	 * populate all the values to the bean and pass it to the database
	 * 
	 * @param customer
	 * 
	 * @return customerLogin
	 */
	public CustomerLogin addCustomerLogin(Customer customer) throws Exception;
}
