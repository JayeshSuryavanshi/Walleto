package com.amigowallet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.ForgotPasswordDAO;
import com.amigowallet.model.User;
import com.amigowallet.utility.HashingUtility;
import com.amigowallet.validator.UserLoginValidator;

/**
 * This is service class implementing the service interface
 * {@link ForgotPasswordService} having methods which contain business logic related to Forgot password and 
 * Reset password.
 * 
 * @see ForgotPasswordService
 * 
 * @author ETA_JAVA
 * 
 */
@Service(value = "forgotPasswordService")
@Transactional
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
	
	@Autowired
	private ForgotPasswordDAO forgotPasswordDAO;

	/**
	 * @see com.amigowalletmigowallet.service.ForgotPasswordService#authenticateEmailId(java.lang.String)
	 */
	@Override
	public User authenticateEmailId(String emailId)
			throws Exception {

		/*
		 * here we passes email id as argument and verifies it. 
		 * we pass the email id to method of 
		 * forgotPasswordDAO as an argument which after
		 * successful verification returns the user
		 * else it returns null
		 */
		User user = forgotPasswordDAO.authenticateEmailId(emailId);

		/*
		 * if the status of email is null then exception is thrown as
		 * ForgotPasswordService.INVALID_EMAIL
		 */
		if (user == null) {
			throw new Exception(
					"ForgotPasswordService.INVALID_EMAIL");
		}
		return user;
		
	}

	@Override
	public void validateSecurityAnswer(User user) throws Exception {
		
		User userFromDAO = forgotPasswordDAO.validateSecurityAnswer(user);

		
		if(!userFromDAO.getSecurityAnswer().equalsIgnoreCase(user.getSecurityAnswer())) {
			throw new Exception("ForgotPasswordService.INVALID_SECURITY_ANSWER");
		}
	}

	@Override
	public void resetPassword(User user) throws Exception {
		/*
		 * here we are validating newPassword and confirmNewPassword whether
		 * they are in proper format or not. Valid format:- should be of 8 to 20
		 * characters, should have at least one upper case, one lower case, one
		 * number and one special character among {!, #, $, %, ^, &, *, (, )}
		 */
		UserLoginValidator.validateResetPasswordDetails(user.getPassword());

		/*
		 * resetPassword method of forgotPasswordDAO receives email id
		 * and hashed password as arguments and updates the password
		 * of the customer to whom the passed email id belongs
		 */
		forgotPasswordDAO.resetPassword(user.getUserId(),
				HashingUtility.getHashValue(user.getPassword()));

		
	}
}
