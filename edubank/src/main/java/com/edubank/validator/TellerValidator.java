package com.edubank.validator;

import com.edubank.model.Teller;

/**
 * The TellerValidator class is a utility class having static methods, used to
 * validate the data entered from the login form of teller. <br>
 * <i>Example:-</i> Login Name, Password
 * 
 * @author ETA_JAVA
 *
 */
public class TellerValidator {

	/**
	 * This method validates loginName and password whether they are in proper
	 * format or not. Valid format:- <h4>loginName</h4> should start with 'T' 
	 * followed by 4 digits.
	 * 
	 * <h4>password</h4> should be of 8 to 20 characters, should have at least
	 * one upper case, one lower case, one number and one special character
	 * among {!, #, $, %, ^, &, *, (, )}
	 *
	 * @param teller
	 * 
	 * @throws Exception
	 *             with message <i>TellerValidator.INVALID_CREDENTIALS</i>
	 *             if the loginName is not matching the above format
	 * 				or
	 *             with message <i>TellerValidator.INVALID_CREDENTIALS</i> if
	 *             the password is not matching the above format
	 */
	
	public static void validate(Teller teller) throws Exception
	{
		/*
		 * here teller login name is validated
		 */
		if (!validateLoginName(teller.getLoginName())) {
			throw new Exception("TellerValidator.INVALID_CREDENTIALS");
		}
	
		/*
		 * here teller password is validated
		 */
		if (!validatePassword(teller.getPassword())) {
			throw new Exception("TellerValidator.INVALID_CREDENTIALS");
		}

	}

	/**
	 * This method is used to validate the format of password<br>
	 * <b>password</b> should be of 8 to 20 characters, should have at
	 *         least one upper case, one lower case, one number and one special
	 *         character among {!, #, $, %, ^, &, *, (, )}
	 *         
	 * @param password
	 * 
	 * @return true if the password is matching the above conditions, else
	 *         false. <br>
	 *         
	 */
	public static boolean validatePassword(String password) {
		boolean flag = false;
		
		/*
		 * this condition checks the length of password */
		if (password.length() >= 8 && password.length() <= 20) {
			
		/*
		 * this condition checks if password has any upper case character or not */
			if (password.matches(".*[A-Z].*")) {
		/*
		 * this condition check for lower case character in password */
				if (password.matches(".*[a-z].*")) {
		/*
		 * this condition check for lower case character in password */
					if (password.matches(".*[0-9].*")) {
		/*
		 * this condition check for special characters {!, #, $, %, ^, &, *, (, )} in password */
						if (password.matches(".*(!|#|\\$|%|\\^|&|\\*|\\(|\\)).*")) {
							flag = true;
						}
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * This method validates loginName whether they are in proper
	 * format or not. Valid format:- <h4>loginName</h4> should start with 'T' 
	 * followed by 4 digits.
	 *
	 * @param loginName
	 * 
	 * @return true if the login name is matching the above conditions, else
	 *         false. <br>
	 */
	public static boolean validateLoginName(String loginName) {

		Boolean flag = false;
		
		/*
		 * this condition check whether login name start with 'T' 
		 * followed by 4 digits. */
		if (loginName.matches("(T)[0-9]{4}"))
			flag = true;

		return flag;
	}

}
