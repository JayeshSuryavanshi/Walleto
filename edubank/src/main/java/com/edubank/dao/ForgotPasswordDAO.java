package com.edubank.dao;

/**
 * This is a DAO interface having methods to perform CRUD operations on CUSTOMER and CUSTOMER_LOGIN tables 
 * for customer forgot/reset password requests
 *
 * @author ETA_JAVA
 * 
 */
public interface ForgotPasswordDAO
{
	/**
	 * This method receives customer email id as argument and retrieves customer for the given email id. <br>
	 * If customer found for the given email id then it returns "Found" else it returns "Not Found"
	 * 
	 * @param emailId
	 * 
	 * @return String
	 */
	public String authenticateEmailId(String emailId) throws Exception;

	/**
	 * This method receives email id and hashed password as arguments and updates the password of the customer 
	 * to whom the passed email id belongs
	 * 
	 * @param emailId
	 * @param hashedPassword
	 */
	public void resetPassword(String emailId, String hashedPassword) throws Exception;

}
