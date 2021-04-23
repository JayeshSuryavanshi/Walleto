package com.amigowallet.validator;

import com.amigowallet.model.User;


/**
 * The UserLoginValidator class is a utility class having static methods, used to
 * validate the data entered from the login form, change password and reset password.  <br>
 * <i>For Example:-</i> Login Name, Password.
 * 
 * @author ETA_JAVA
 *
 */
public class UserLoginValidator {
	
	/**
	 * This method is for validating the user login details
	 * 
	 * @param user
	 * 
	 * @throws Exception
	 *  			if the loginName is not matching the above format or if the
	 *              password is not matching the above format
	 */
	public static void validateUserLogin(User user)
			throws Exception {
		
		/* here we are checking for valid email id format */
		if (!validateUserEmail(user.getEmailId())) {
			throw new Exception(
					"LoginValidator.INVALID_CREDENTIALS");
		}
		
		/* here we are checking for valid password format */
		if (!validatePassword(user.getPassword())) {
			throw new Exception(
					"LoginValidator.INVALID_CREDENTIALS");
		}
	}
	
	/**
	 * This method validates the user's password
	 * <h4>password</h4> should be of 8 to 20 characters, should have at least
	 * one upper case, one lower case, one number and one special character
	 * among {!, #, $, %, ^, &, *, (, )}
	 * 
	 * @param password
	 * 
	 * @return true if the password is matching the above conditions, else
	 *         false.
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
					 * this condition check for number in password */
					if (password.matches(".*[0-9].*")) {
						
						/*
						 * this condition check for special characters {!, #, $, %, ^, &, *, (, )} in password */
						if (password.matches(".*[!#$%^&*()].*")) {
							flag = true;
						}
					}
				}
			}
		}

		return flag;
	}
	
	/**
	 * This method validates the user's emailId
	 * emailId should be in alphanumeric and may contain "." & "_" followed by "@" then alphabets,"." & alphabets.
	 * 
	 * @param emailId
	 * 
	 * @return true if the emailId is matching the above conditions, else
	 *         false.
	 */
	public static boolean validateUserEmail(String emailId) {

		boolean flag = false;
		if (emailId.matches("[a-zA-Z0-9\\._]+@[a-zA-Z]+\\.[a-zA-Z]+")) {
			flag = true;
		}
		return flag;
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
	 *				or
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
	 * 				or
	 *             with message
	 *             <i>LoginValidator.OLD_PASSWORD_NEW_PASSWORD_SAME</i> if the
	 *             newPassword is same as old password
	 */
	public static void validateChangePasswordDetails(User user)
			throws Exception			
	{
		if (!validatePassword(user.getPassword())) {
			throw new Exception(
					"LoginValidator.INVALID_PASSWORD_FORMAT");
		}
		if (!validatePassword(user.getNewPassword())) {
			throw new Exception(
					"LoginValidator.INVALID_NEW_PASSWORD_FORMAT");
		}
		if (!validatePassword(user.getConfirmNewPassword())) {
			throw new Exception(
					"LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT");
		}
		if (!user.getNewPassword().equals(
				user.getConfirmNewPassword())) {
			throw new Exception(
					"LoginValidator.CONFIRM_NEW_PASSWORD_MISSMATCH");
		}

		if (user.getPassword().equals(user.getNewPassword())) {
			throw new Exception(
					"LoginValidator.OLD_PASSWORD_NEW_PASSWORD_SAME");
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
	 * 				or
	 *             with message
	 *             <i>LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT</i> if
	 *             the confirmNewPassword is not matching the above format
	 * 				or
	 *             with message
	 *             <i>LoginValidator.INVALID_CONFIRM_NEW_PASSWORD_FORMAT</i> if
	 *             the newPassword is not matching the confirmNewPassword
	 */
	public static void validateResetPasswordDetails(String newPassword)
			throws Exception			
	{
		if (!validatePassword(newPassword)) {
			throw new Exception(
					"LoginValidator.INVALID_NEW_PASSWORD_FORMAT");
		}
	}
}
