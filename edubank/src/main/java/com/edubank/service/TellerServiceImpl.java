package com.edubank.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.TellerDAO;
import com.edubank.model.Teller;
import com.edubank.utility.Hashing;
import com.edubank.validator.TellerValidator;

/**
 * This class contains methods for business logics related to Login module
 * of teller, like authenticate teller login.
 * 
 * @author ETA_JAVA
 *
 */
@Service("tellerService")
@Transactional
public class TellerServiceImpl implements TellerService {

	@Autowired
	private TellerDAO tellerDao;

	/**
	 * This method is used to authenticate the login credentials entered by
	 * teller. It checks the loginName and password with input field validation,
	 * if valid it verifies the credential with the database. <br>
	 * 
	 * @param teller
	 * 
	 * @return teller object if the credentials are valid

	 * @throws Exception
	 *             if the login name and/or password is not in proper format
	 *             
	 * 			   or 
	 *             if the loginName is not matching any record in the database
	 *             or the password entered is incorrect with respect to the
	 *             loginName.
	 *             
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 *             
	 * @throws Exception 
	 */
	@Override
	public Teller authenticateTellerLogin(Teller teller) throws Exception			
	{
		
		/*
		 * the below statement calls Validator method to validate the teller login credentials
		 */
		TellerValidator.validate(teller);

		Teller tellerFromDao = null;
		
		/*
		 * This code is used to get a Teller model corresponding to its loginName
		 */
		tellerFromDao = tellerDao.getLoginForTeller(teller.getLoginName());

		/*
		 * here we are checking weather we get any teller for corresponding name
		 * if not exception is thrown
		 */
		if (tellerFromDao == null) {
			throw new Exception("TellerService.TELLER_CREDENTIALS_INCORRECT");
		}
		
		/* here we are comparing the hash values password */
		if (!tellerFromDao.getPassword().equals(
				Hashing.getHashValue(teller.getPassword()))) {

			throw new Exception("TellerService.TELLER_CREDENTIALS_INCORRECT");
		}
		
		return tellerFromDao;
	}

}
