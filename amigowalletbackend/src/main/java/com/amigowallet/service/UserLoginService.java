package com.amigowallet.service;

import java.security.NoSuchAlgorithmException;

import com.amigowallet.model.User;

/**
 * This interface contains the methods for business logics related to Login
 * module, like authenticate, change user password, get user by userId.
 * 
 * @author ETA_JAVA
 *
 */
public interface UserLoginService {
	
	/**
	 * This method is used to authenticate the login credentials entered by
	 * user. It checks the loginName and password with input field validation,
	 * if valid it verifies the credential with the database. If verified, it
	 * checks whether the account is locked.
	 * 
	 * @param user
	 *           
	 * @return user object if the credentials are valid and the account is not
	 *         locked
	 *         
	 * @throws Exception
	 *             if the loginName is not matching any record in the database
	 *             or the password entered is incorrect with respect to the
	 *             loginName.
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 */
	public User authenticate(User user) throws Exception;

	/**
	 * This method is used to change the password of logged in user. It checks
	 * the password, newPassword and confirmNewPassword with input field
	 * validation, if they are in proper format, it checks whether the
	 * newPassword is matching the confirmNewPassword, then it checks if the old
	 * password and the new password are same.
	 * 
	 * @param user
	 * 
	 * @throws Exception
	 *             if the password is not in proper format
	 * 				or
	 *             if the newPassword is not in proper format
	 * 				or
	 *             if the confirmNewPassword is not in proper format
	 * 				or
	 *             if newPassword is same as old password
	 * 				or
	 *             if confirmNewPassword is not same as newPassword
	 * 				or
	 *             if the current password is not matching the record of logged
	 *             in user
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 */
	public void changeUserPassword(User user)
			throws Exception;

	/**
	 * This method returns the bean object by passing user id as argument this
	 * checks the user id in database and returns that user.
	 * 
	 * @param userId
	 * 
	 * @return user
	 * 
	 * @throws Exception
	 */
	public User getUserbyUserId(Integer userId)
			throws Exception;

}
