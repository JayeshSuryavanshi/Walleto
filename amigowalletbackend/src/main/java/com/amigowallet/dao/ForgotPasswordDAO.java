package com.amigowallet.dao;

import com.amigowallet.model.User;

/**
 * This is a DAO interface having methods to perform CRUD operations on user
 * reset password and user tables.
 *
 * @author ETA_JAVA
 */
public interface ForgotPasswordDAO {
	
	/**
	 * This method receives email id as argument and verifies it. <br>
	 * If verification is success it returns "Found" else it returns "Not Found".
	 * 
	 * @param emailId
	 * 
	 * @return User
	 */
	public User authenticateEmailId(String emailId) throws Exception;

	/**
	 * This method receives email id and hashed password as arguments and
	 * updates the password of the customer to whom the passed email id belongs
	 * 
	 * @param integer
	 * @param hashedPassword
	 */
	public void resetPassword(Integer integer, String hashedPassword);

	public User validateSecurityAnswer(User user);
}
