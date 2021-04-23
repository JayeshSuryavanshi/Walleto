package com.amigowallet.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;

/**
 * This is a service interface having methods which contain business logic
 * related to user registration.
 * 
 * @author ETA_JAVA
 *
 */
public interface RegistrationService {
	
	/**
	 * This method receives a user as argument, 
	 * this method holds the logic to validate the user.
	 * If user is invalid exception is thrown,
	 * checks the availability of email id and mobile no
	 * if mobile or email is not available exception is thrown.
	 * 
	 * @param user
	 *
	 * @throws Exception 
	 */
	public void validateUser(User user) throws Exception;
	
	/**
	 * This method receives a user as argument
	 * and register the user with generating the new password
	 * into the database by calling the method from dao class
	 * 
	 * @param user
	 * 
	 * @return integer
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public Integer registerUser(User user) throws NoSuchAlgorithmException;

	public ArrayList<SecurityQuestion> getAllSecurityQuestions();
	
}
