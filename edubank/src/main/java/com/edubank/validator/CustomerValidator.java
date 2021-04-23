package com.edubank.validator;

import java.time.LocalDate;

import com.edubank.model.Customer;


/**
 * The CustomerValidator class is a utility class having static methods, used to
 * validate the data entered from the add customer form. <br>
 * <i>Example:-</i> name, email, date of birth
 * 
 * @author ETA_JAVA
 *
 */
public class CustomerValidator {

	
	/**
	 * This method validates name, email and age using date of birth whether they are in proper
	 * format/valid or not. Valid format:- <h4>Name</h4> should contain only
	 * alphabets and spaces.
	 * 
	 * <h4>email</h4> should contain @ and followed by at least one '.'
	 *
	 * <h4>date of birth</h4> age of the customer should be greater than 18
	 *
	 * @param customer
	 * 
	 * @throws Exception
	 *             with message <i>CustomerValidator.INVALID_CUSTOMER_NAME</i>
	 *             if the name is not matching the above format
	 *				or
	 *             with message <i>CustomerValidator.INVALID_EMAIL</i> if
	 *             the email is not matching the above format
	 * 				or
	 *             with message <i>CustomerValidator.INVALID_AGE_OF_CUSTOMER</i> if
	 *             the email is not matching the above format
	 */

	public static void validateCustomerDetails(Customer customer) throws Exception
	{

		if (!validateName(customer.getName())) {
			throw new Exception("CustomerValidator.INVALID_CUSTOMER_NAME");
		}
		if (!validateEmail(customer.getEmailId())) {
			throw new Exception("CustomerValidator.INVALID_EMAIL");
		}
		if (!validateAgeFromDOB(customer.getDateOfBirth())) {
			throw new Exception("CustomerValidator.INVALID_AGE_OF_CUSTOMER");
		}
	}

	/**
	 * This method is used to validate the age of customer<br>
	 * <h4>date of birth</h4> age of the customer should be greater than 18
	 * 
	 * @param dateOfBirth
	 * 
	 * @return true if the age of the customer should be greater than 18, else
	 *         false. <br>
	 * 
	 */
	public static boolean validateAgeFromDOB(LocalDate dateOfBirth) {
		boolean flag = false;
		LocalDate date = LocalDate.now();
		date= date.minusYears(18);
		if (dateOfBirth.isBefore(date)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * This method is used to validate the format of mail<br>
	 * <h4>email</h4> should contain @ and followed by at least one '.'
	 * 
	 * @param emailId
	 * 
	 * @return true if the email is matching the above conditions, else
	 *         false. <br>
	 * 
	 */
	public static boolean validateEmail(String emailId) {
		boolean flag = false;
		if (emailId.matches("[^@]+@[^@]+[.][^@]+")) {
			flag = true;
		}
		return flag;
	}

	/**
	 * This method is used to validate the format of name<br>
	 * <h4>Name</h4> should contain only alphabets and spaces.
	 * 
	 * @param name
	 * 
	 * @return true if the name is matching the above conditions, else
	 *         false. <br>
	 * 
	 */
	public static boolean validateName(String name) {
		boolean flag = false;
		if (name.matches("[A-Za-z ]+") && !name.matches("[ ]*")) {
			flag = true;
		}
		return flag;
	}

}
