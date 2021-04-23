package com.amigowallet.dao;

import java.util.ArrayList;

import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;

/**
 *  
 * This is DAO interface contains the methods responsible for interacting with
 * the database with respect to user registration details. 
 * 
 * @author ETA_JAVA
 *
 */

public interface RegistrationDAO {

	/**
	 * This method is used for checking whether the email id is used by any
	 * already registered user
	 * 
	 * @param emailId
	 *            
	 * @return boolean
	 */
	public Boolean checkEmailAvailability(String emailId);

	/**
	 * This method is used for checking whether the mobile number is used by any
	 * already registered user
	 * 
	 * @param mobileNumber
	 *            
	 * @return boolean
	 */
	public Boolean checkMobileNumberAvailability(String mobileNumber);

	/**
	 * This method is used to add a new User to the database<br>
	 * It uses session.save() method to save the entity to the database
	 * 
	 * @param user
	 * 
	 * @return userId
	 */
	public Integer addNewUser(User user);

	public ArrayList<SecurityQuestion> getAllSecurityQuestions();

}
