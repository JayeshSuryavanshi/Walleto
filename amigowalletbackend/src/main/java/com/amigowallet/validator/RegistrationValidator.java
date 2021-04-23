package com.amigowallet.validator;

import com.amigowallet.model.User;

/**
 * This class is a utility class having static methods, used to
 * validate the data entered in the user registration form. <br>
 * <i>Example:-</i> name, email, password, contact number.
 * 
 * @author ETA_JAVA
 *
 */
public class RegistrationValidator {
	
	/**
	 * This method is for validating the user details
	 * if user details are invalid corresponding exception is thrown
	 * 
	 * @param user
	 * 
	 * @throws Exception
	 * 			   with message <i>RegistrationValidator.INVALID_USER_NAME</i>
	 *             if name is invalid
	 * 				or 
	 * 			   with message <i>RegistrationValidator.INVALID_PASSWORD</i> if
	 *             the password is not matching the proper format
	 * 				or 
	 * 				with message <i>RegistrationValidator.INVALID_EMAIL</i> if
	 *             the email is not matching the proper format
	 * 				or 
	 * 			   with message <i>RegistrationValidator.INVALID_CONTACT</i> if
	 *             the contact no is not matching the proper format
	 */
	public static void validateUserDetails(User user) throws Exception
	{
		if (!validateName(user.getName())) {
			throw new Exception("RegistrationValidator.INVALID_USER_NAME");
		}
		if (!validateEmail(user.getEmailId())) {
			throw new Exception("RegistrationValidator.INVALID_EMAIL");
		}
		if (!validateContact(user.getMobileNumber())) {
			throw new Exception("RegistrationValidator.INVALID_CONTACT");
		}
		if (!validatePassword(user.getPassword())) {
			throw new Exception("RegistrationValidator.INVALID_PASSWORD");
		}
	}
	
	/**
	 * This method validates the user name<br>
	 * Name will allow only alphabets and spaces
	 * and names with only spaces is invalid
	 * 
	 * @param name
	 * 
	 * @return boolean
	 */
	public static boolean validateName(String name) {
		boolean flag = false;
		if (name.matches("[A-Za-z ]+") && !name.matches("[ ]*")) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * This method validates the user email id
	 * Email id should follow a valid format
	 * 
	 * @param emailId
	 * 
	 * @return boolean
	 */
	public static boolean validateEmail(String emailId) {
		boolean flag = false;
		if (emailId.matches("[^@]+@[^@]+[.][^@]+")) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * This method validates contact mobile number
	 * Mobile number should be 10 digits
	 * 
	 * @param mobileNumber
	 * 
	 * @return boolean
	 */
	public static boolean validateContact(String mobileNumber) {
		boolean flag = false;
		if (mobileNumber.length()==10) {
			if (mobileNumber.matches("[0-9]*")) {
				flag = true;
			}	
		}
		return flag;
	}
	
	/**
	 * This method validates password
	 * Password should be with in 8-20 characters with at least 
	 * one upper case, one lower case, one digit and 
	 * special character in (!,#,$,%,^,&,*,(,)) 
	 * 
	 * @param password
	 * 
	 * @return boolean
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
						if (password.matches(".*(!|#|\\$|%|\\^|&|\\*|\\(|\\)).*")) {
							flag = true;
						}
					}
				}
			}
		}
		
		return flag;
	}
}

