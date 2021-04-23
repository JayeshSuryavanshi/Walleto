package com.amigowallet.service;

import com.amigowallet.model.User;

/**
 * This is a service interface having methods which contain business logic related to Forgot password and 
 * Reset password.
 * 
 * @author ETA_JAVA
 *
 */
public interface ForgotPasswordService
{
	/**
	 * This method receives email id as argument and verifies it by making a call to DAO class method. <br>
	 * If verification fails then it throws exception {@link Exception}
	 *  
	 * @param emailId
	 * @return 
	 * 
	 * @throws Exception
	 */
	public User authenticateEmailId(String emailId) throws Exception;

	public void validateSecurityAnswer(User user) throws Exception;

	public void resetPassword(User user) throws Exception;
}
