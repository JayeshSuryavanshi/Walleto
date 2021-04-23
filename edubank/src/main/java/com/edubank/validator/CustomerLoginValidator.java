package com.edubank.validator;

import com.edubank.model.CustomerLogin;

/**
 * The LoginValidator class is a utility class having static methods, used to
 * validate the data entered from the login form, change password and reset password.  <br>
 * <i>Example:-</i> Login Name, Password
 * 
 * @author ETA_JAVA
 *
 */
public class CustomerLoginValidator {

	/**
	 * This method validates loginName and password whether they are in proper
	 * format or not. Valid format:- <h4>loginName</h4> should contain only
	 * alphabets or number, no special characters or spaces.
	 * 
	 * <h4>password</h4> should be of 8 to 20 characters, should have at least
	 * one upper case, one lower case, one number and one special character
	 * among {!, #, $, %, ^, &, *, (, )}
	 *
	 * @param customerLogin
	 * 
	 * @throws Exception
	 *             if the loginName is not matching the above format or if the
	 *             password is not matching the above format
	 */
	public static void validateLoginDetails(CustomerLogin customerLogin)
			throws Exception {
		
		/*
		 * here if customerLoginName has any character other than
		 * lower case or upper case alphabet or number
		 * then it will throw InvalidCustomerCredentialsException
		 **/
	 	if (!customerLogin.getLoginName().matches("[a-zA-Z0-9.\\_]+")) {

			throw new Exception(
					"LoginValidator.INVALID_CREDENTIALS");
		}
	 	
	 	/* here we are checking for valid password */
		if (!validatePassword(customerLogin.getPassword())) {
			throw new Exception(
					"LoginValidator.INVALID_CREDENTIALS");
		}

	}

	/**
	 * This method is used to validate the format of password<br>
	 * <b>password</b> should be of 8 to 20 characters, should have at least one
	 * upper case, one lower case, one number and one special character among
	 * {!, #, $, %, ^, &, *, (, )}
	 * 
	 * @param password
	 * 
	 * @return true if the password is matching the above conditions, else
	 *         false. <br>
	 * 
	 */
	public static boolean validatePassword(String password) {
		
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
		 * this condition check for number in password */
					if (password.matches(".*[0-9].*")) {
		/*
		 * this condition check for special characters {!, #, $, %, ^, &, *, (, )} in password */				
						if (password
								.matches(".*(!|#|\\$|%|\\^|&|\\*|\\(|\\)).*")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * This method validates password, newPassword and confirmNewPassword
	 * whether they are in proper format or not. Valid format:-
	 * 
	 * <h4>passwords</h4> should be of 8 to 20 characters, should have at least
	 * one upper case, one lower case, one number and one special character
	 * among {!, #, $, %, ^, &, *, (, )}
	 *
	 * @param customerLogin
	 * 
	 * @throws Exception
	 *             with message <i>LoginValidator.INVALID_PASSWORD_FORMAT</i> if
	 *             the password is not matching the above format
	 * 				or
	 *             with message
	 *             <i>LoginValidator.INVALID_NEW_PASSWORD_FORMAT</i> if the
	 *             newPassword is not matching the above format
	 * 				or
	 *             with message
	 *             <i>LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT</i> if
	 *             the confirmNewPassword is not matching the above format
	 * 				or
	 *             with message
	 *             <i>LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT</i> if
	 *             the newPassword is not matching the confirmNewPassword
	 * 
	 * 				or
	 *             with message
	 *             <i>LoginValidator.OLD_PASSWORD_NEW_PASSWORD_SAME</i> if the
	 *             newPassword is same as old password
	 */
	public static void validateChangePasswordDetails(CustomerLogin customerLogin)
			throws Exception {
		
		/* here we are validating the password
		 * passwords should be of 8 to 20 characters, should have at least
		 * one upper case, one lower case, one number and one special character
		 * among {!, #, $, %, ^, &, *, (, )}
		 **/
		if (!validatePassword(customerLogin.getPassword())) {
			throw new Exception(
					"LoginValidator.INVALID_PASSWORD_FORMAT");
		}
		
		/* here we are validating the new password
		 * passwords should be of 8 to 20 characters, should have at least
		 * one upper case, one lower case, one number and one special character
		 * among {!, #, $, %, ^, &, *, (, )}
		 **/
		if (!validatePassword(customerLogin.getNewPassword())) {
			throw new Exception(
					"LoginValidator.INVALID_NEW_PASSWORD_FORMAT");
		}
		
		/* here we are validating the confirm new password
		 * passwords should be of 8 to 20 characters, should have at least
		 * one upper case, one lower case, one number and one special character
		 * among {!, #, $, %, ^, &, *, (, )}
		 **/	
		if (!validatePassword(customerLogin.getConfirmNewPassword())) {
			throw new Exception(
					"LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT");
		}
		
		/*
		 * here we are validating whether new password and confirm new password are equal or not
		 **/
		if (!customerLogin.getNewPassword().equals(
				customerLogin.getConfirmNewPassword())) {
			throw new Exception(
					"LoginValidator.CONFIRM_NEW_PASSWORD_MISSMATCH");
		}

	}

	/**
	 * This method validates newPassword and confirmNewPassword
	 * whether they are in proper format or not. Valid format:-
	 * 
	 * <h4>passwords</h4> should be of 8 to 20 characters, should have at least
	 * one upper case, one lower case, one number and one special character
	 * among {!, #, $, %, ^, &, *, (, )}
	 *
	 * @param newPassword
	 * @param confirmNewPassword
	 *  
	 * @throws Exception
	 *             with message
	 *             <i>LoginValidator.INVALID_NEW_PASSWORD_FORMAT</i> if the
	 *             newPassword is not matching the above format
	 *				or 
	 *             with message
	 *             <i>LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT</i> if
	 *             the confirmNewPassword is not matching the above format
	 * 				or 
	 *             with message
	 *             <i>LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT</i> if
	 *             the newPassword is not matching the confirmNewPassword
	 */
	public static void validateResetPasswordDetails(String newPassword, String confirmNewPassword)
			throws 	Exception
			
	{
		
		/* here we are validating the newPassword
		 * passwords should be of 8 to 20 characters, should have at least
		 * one upper case, one lower case, one number and one special character
		 * among {!, #, $, %, ^, &, *, (, )}
		 **/
		if (!validatePassword(newPassword)) {
			throw new Exception(
					"LoginValidator.INVALID_NEW_PASSWORD_FORMAT");
		}
		
		/* here we are validating the confirmNewPassword
		 * passwords should be of 8 to 20 characters, should have at least
		 * one upper case, one lower case, one number and one special character
		 * among {!, #, $, %, ^, &, *, (, )}
		 **/
		if (!validatePassword(confirmNewPassword)) {
			throw new Exception(
					"LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT");
		}
		
		/*
		 * here we are validating whether new password and confirm new password are equal or not
		 **/
		if (!newPassword.equals(confirmNewPassword)) {
			throw new Exception(
					"LoginValidator.CONFIRM_NEW_PASSWORD_MISSMATCH");
		}
	}
}
