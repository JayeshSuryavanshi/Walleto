package com.edubank.service;

import java.security.NoSuchAlgorithmException;

import com.edubank.model.Teller;

/**
 * This interface contains methods for business logics related to Login module of teller,
 * like authenticate teller login.
 * 
 * @author ETA_JAVA
 *
 */
public interface TellerService {

	/**
	 * This method is used to authenticate the login credentials entered by
	 * teller. It checks the loginName and password with input field
	 * validation, if valid it verifies the credential with the database.
	 * <br>
	 * 
	 * @param teller
	 * 
	 * @return teller object if the credentials are valid
	 * 
	 * @throws Exception
	 *             if the login name and/or password is not in proper format
	 *             
	 * 			   or 
	 *             if the loginName is not matching any record in the database
	 *             or the password entered is incorrect with respect to the
	 *             loginName.
	 *             
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 *             
	 * @throws Exception 
	 */
	public Teller authenticateTellerLogin(Teller teller) throws Exception;
	

}
