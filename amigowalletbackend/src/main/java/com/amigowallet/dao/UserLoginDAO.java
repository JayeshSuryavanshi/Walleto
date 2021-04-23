package com.amigowallet.dao;

import com.amigowallet.model.User;

/**
 *  This Interface contains the methods responsible for interacting the database
 *  with respect to Login module like getUserByEmailId, change user password, getUserByUserId.
 * 
 *  @author ETA_JAVA
 */

public interface UserLoginDAO {
	
	/**
	 * This method is used to get a UserLogin model corresponding to its
	 * emailId<br>
	 * @param emailId
	 * 
	 * @return User
	 * @throws Exception 
	 */

	public User getUserByEmailId(String emailId) throws Exception;
	
	/**
	 * This method is used to change the password of an existing Customer. It
	 * takes CustomerLogin as a parameter, which includes, customerId and
	 * newPassword, it fetches an entity on the basis of customerId, and updates
	 * the password to newPassword received.<br>
	 * @param user
	 */
	public void changeUserPassword(User user);

	/**
	 * this method takes the userId as a argument and get the user from the database
	 * 
	 * @param userId
	 * 
	 * @return user
	 */
	public User getUserByUserId(Integer userId);

}
