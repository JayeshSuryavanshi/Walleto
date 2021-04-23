package com.edubank.service;

import java.security.NoSuchAlgorithmException;

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
	 * This method receives email id as argument and verifies it. 
	 * If verification fails it throws InvalidEmailException.
	 *  
	 * @param emailId
	 * 
	 * @throws Exception
	 */
	public void authenticateEmailId(String emailId) throws Exception;

	/**
	 * This method receives customer emailId, new password and confirm new password as arguments.
	 * It validates new password and confirm new password. If validation fails then exception comes from Validator is thrown
	 * else it resets the password for the customer by calling DAO method.
	 * 
	 * @param emailId
	 * @param newPassword
	 * @param confirmNewPassword
	 * 
	 * @throws Exception 
	 * @throws NoSuchAlgorithmException 
	 * @throws Exception 
	 */
	public void resetPassword(String emailId, String newPassword, String confirmNewPassword) 
			throws Exception;

	/**
	 * This method checks security answer entered by the customer against the actual security answer
	 * of the customer. If mismatch then it throws SecurityQuestionException.
	 * 
	 * @param secAns
	 * @param customersecAns
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	public void checkSecQuestionAnswer(String secAns, String customersecAns)
			throws Exception;

}
