package com.edubank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.ForgotPasswordDAO;
import com.edubank.utility.Hashing;
import com.edubank.validator.CustomerLoginValidator;

/**
 * This is service class implementing the service interface
 * {@link ForgotPasswordService}. It has methods which contain business logic related to Forgot password and 
 * Reset password.
 * 
 * @see ForgotPasswordService
 *
 */
@Service(value = "forgotPasswordService")
@Transactional
public class ForgotPasswordServiceImpl implements ForgotPasswordService 
{
	@Autowired
	private ForgotPasswordDAO forgotPasswordDAO;

	/**
	 * @see com.edubank.service.ForgotPasswordService#authenticateEmailId(java.lang.String)
	 */
	@Override
	public void authenticateEmailId(String emailId) throws 
			Exception 
	{
		
		/*
		 * This code receives email id as argument and verifies it. If
		 * verification is success it returns "Found" else it returns
		 * "Not Found"
		 */
		String emailStatus = forgotPasswordDAO.authenticateEmailId(emailId);

		/*
		 * here we are checking if email status has "Found" in it if not
		 * Corresponding error message is thrown
		 */
		if (!"Found".equals(emailStatus)) 
		{
			throw new Exception("ForgotPasswordService.INVALID_EMAIL");
		}

	}


	/**
	 * @see com.edubank.service.ForgotPasswordService#resetPassword
	 *      (java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public void resetPassword(String emailId, String newPassword, String confirmNewPassword) throws Exception
	{
		//Validating newPassword, confirmNewPassword by calling validator method
		CustomerLoginValidator.validateResetPasswordDetails(newPassword,
				confirmNewPassword);

		//Resetting password for the customer by calling DAO method with customer emailId and hashed new password
		forgotPasswordDAO.resetPassword(emailId, Hashing.getHashValue(newPassword));
	}

	/**
	 * @see com.edubank.service.ForgotPasswordService#checkSecQuestionAnswer
	 *      (java.lang.String, java.lang.String)
	 */
	@Override
	public void checkSecQuestionAnswer(String secAns, String customersecAns) 
			throws Exception 
	{
		//Comparing "secAns" submitted by customer with customer's actual security answer "customersecAns" 
		if (!secAns.equalsIgnoreCase(customersecAns)) 
		{
			throw new Exception("ForgotPasswordService.SECURITYQUESTION_ANSWER_MISMATCH");
		}
	}
}
