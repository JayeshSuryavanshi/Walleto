package com.amigowallet.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.RegistrationDAO;
import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;
import com.amigowallet.utility.HashingUtility;
import com.amigowallet.validator.RegistrationValidator;

/**
 * This is a service class having methods which contain
 * business logic related to user registration.
 * 
 * @author ETA_JAVA
 * 
 */
@Service(value = "registrationService")
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private RegistrationDAO registrationDao;

	/*
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
	@Override
	public void validateUser(User user) throws Exception {

		/*
		 * here we are validating the user details by passing the user object to
		 * validateUserDetails method of RegistrationValidator
		 */
		RegistrationValidator.validateUserDetails(user);

		/*
		 * here we are checking the availability of email we passing email id
		 * checkEmailAvailability method of registrationDao as argument which
		 * Check whether email is already their in database
		 */
		Boolean emailAvailable = registrationDao.checkEmailAvailability(user
				.getEmailId());

		/*
		 * if email id is already their in database then exception is thrown with
		 * message RegistrationService.EMAIL_ALREADY_PRESENT
		 */
		if (emailAvailable) {
			throw new Exception(
					"RegistrationService.EMAIL_ALREADY_PRESENT");
		}

		/*
		 * here we are checking the availability of mobile number we passing mobile no
		 * checkMobileNumberAvailability method of registrationDao as argument which
		 * Check whether mobile no is already their in database
		 */
		Boolean mobileNumberAvailable = registrationDao
				.checkMobileNumberAvailability(user.getMobileNumber());

		/*
		 * if mobile no is already their in database then exception is thrown with
		 * message RegistrationService.MOBILE_NUMBER_ALREADY_PRESENT
		 */
		if (mobileNumberAvailable) {
			throw new Exception(
					"RegistrationService.MOBILE_NUMBER_ALREADY_PRESENT");
		}
	}

	/**
	 * @see com.amigowallet.service.RegistrationService#registerUser(com.amigowallet.model.User)
	 */
	@Override
	public Integer registerUser(User user) throws NoSuchAlgorithmException {

		/*
		 * here we are generating the hash value of the password entered by the
		 * user then adding that hash value to user bean object
		 */
		user.setPassword(HashingUtility.getHashValue(user.getPassword()));
		
		user.setEmailId(user.getEmailId().toLowerCase());
		
		/*
		 * here we are adding the user to database by passing user object as
		 * argument to addNewUser method of registrationDao which will return
		 * user id after successful registration
		 */
		Integer userId = registrationDao.addNewUser(user);
		
		return userId;
	}


	@Override
	public ArrayList<SecurityQuestion> getAllSecurityQuestions(){

		ArrayList<SecurityQuestion> securityQuestions = registrationDao.getAllSecurityQuestions();
		
		return securityQuestions;
	}

}
