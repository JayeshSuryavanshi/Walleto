package com.amigowallet.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.RegistrationDAO;
import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;
import com.amigowallet.validator.RegistrationValidator;

/**
 * Business logic for user registration. Passwords are hashed with the injected
 * {@link PasswordEncoder} (BCrypt) rather than the legacy SHA-256 utility.
 *
 * @author ETA_JAVA
 */
@Service(value = "registrationService")
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

	private final RegistrationDAO registrationDao;
	private final PasswordEncoder passwordEncoder;

	public RegistrationServiceImpl(RegistrationDAO registrationDao, PasswordEncoder passwordEncoder) {
		this.registrationDao = registrationDao;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void validateUser(User user) throws Exception {

		RegistrationValidator.validateUserDetails(user);

		Boolean emailAvailable = registrationDao.checkEmailAvailability(user.getEmailId());
		if (emailAvailable) {
			throw new Exception("RegistrationService.EMAIL_ALREADY_PRESENT");
		}

		Boolean mobileNumberAvailable = registrationDao.checkMobileNumberAvailability(user.getMobileNumber());
		if (mobileNumberAvailable) {
			throw new Exception("RegistrationService.MOBILE_NUMBER_ALREADY_PRESENT");
		}
	}

	@Override
	public Integer registerUser(User user) throws NoSuchAlgorithmException {

		/* Store the password as a BCrypt hash. */
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		user.setEmailId(user.getEmailId().toLowerCase());

		return registrationDao.addNewUser(user);
	}

	@Override
	public ArrayList<SecurityQuestion> getAllSecurityQuestions() {
		return registrationDao.getAllSecurityQuestions();
	}
}
