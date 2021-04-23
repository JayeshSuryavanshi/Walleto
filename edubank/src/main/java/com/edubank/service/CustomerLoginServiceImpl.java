package com.edubank.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.CustomerDAO;
import com.edubank.dao.CustomerLoginDAO;
import com.edubank.model.Customer;
import com.edubank.model.CustomerLogin;
import com.edubank.model.CustomerLoginLockedStatus;
import com.edubank.utility.Hashing;
import com.edubank.validator.CustomerLoginValidator;

/**
 * This class contains the methods for business logics related to Login
 * modules, like authenticate customer login, change password, etc.
 * 
 * @author ETA_JAVA
 *
 */
@Service(value = "customerLoginService")
@Transactional
public class CustomerLoginServiceImpl implements CustomerLoginService {

	@Autowired
	private CustomerLoginDAO customerLoginDao;

	@Autowired
	private CustomerDAO customerDao;

	/**
	 * This method is used to authenticate the login credentials entered by
	 * customer. It checks the loginName and password with input field
	 * validation, if valid it verifies the credential with the database. If
	 * verified, it checks whether the account is locked. <br>
	 * 
	 * @param customerLogin
	 * 
	 * @return customer object if the credentials are valid and the account is
	 *         not locked
	 * 
	 * @throws Exception
	 *             if the loginName is not in proper format
	 * 				or
	 *             if the password is not in proper format
	 *				or
	 *             if the loginName is not matching any record in the database
	 *             or the password entered is incorrect with respect to the
	 *             loginName.
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 * @throws Exception
	 *             if the credentials are proper but the account is locked
	 * @throws Exception
	 */
	@Override
	public Customer authenticateCustomerLogin(CustomerLogin customerLogin)
			throws Exception {
		
		/*
		 * here we are calling the validateLoginMethod method of
		 * customerLoginValidator class
		 * passing the argument customerLogin in it
		 * this is a void method 
		 * which will validate the condition and if things are not accordingly 
		 * this will throw exception */
		CustomerLoginValidator.validateLoginDetails(customerLogin);
		
		/*
		 * After validating we are calling the dao class
		 * we are calling getCustomerLoginByLoginName method of customerLoginDao
		 * this method will return us customerLogin class by passing customer name to it  
		 **/
		CustomerLogin loginFromDAO = customerLoginDao
				.getCustomerLoginByLoginName(customerLogin.getLoginName());
		
		/*
		 * if the above method doesn't return us anything
		 * This will throw InvalidCredentialsException having message 
		 * "LoginService.INVALID_CREDENTIALS" 
		 **/
		if (loginFromDAO == null) {
			throw new Exception(
					"LoginService.INVALID_CREDENTIALS");
		}
		
		/*
		 * here we are generating hash value of the password 
		 */
		String hashValueOfPassword = Hashing.getHashValue(customerLogin
				.getPassword());
		/*
		 * here we are comparing the hash values of password 
		 * that we are passing with the password which we are fetching from database
		 * if password doesn't match exception will be thrown with message
		 * "LoginService.INVALID_CREDENTIALS"
		 */ 
		if (!hashValueOfPassword.equals(loginFromDAO.getPassword())) {
			throw new Exception(
					"LoginService.INVALID_CREDENTIALS");
		}
		
		/* after comparing password if it matches
		 * we are checking whether account is locked or not
		 * if its locked then we are throwing exception with message 
		 * "LoginService.ACCOUNT_LOCKED"
		 */
		if (loginFromDAO.getLockedStatus().equals(CustomerLoginLockedStatus.LOCKED)) {
			throw new Exception("LoginService.ACCOUNT_LOCKED");
		}
		
		/*
		 * after validating everything
		 * we are fetching customer bean from CustomerDao
		 * by passing customerId to it
		 */
		Customer customer = customerDao.getCustomerByCustomerId(loginFromDAO
				.getCustomerId());

		return customer;
	}

	/**
	 * This method is used to change the password of logged in user. It checks
	 * the password, newPassword and confirmNewPassword with input field
	 * validation, if they are in proper format, it checks whether the
	 * newPassword is matching the confirmNewPassword, then it checks if the old
	 * password and the new password are same.
	 * 
	 * @param customerLogin
	 * 
	 * @throws Exception
	 *             if the password is not in proper format or 

	 *             if the newPassword is not in proper format or

	 *             if the confirmNewPassword is not in proper format or 

	 *             if newPassword is same as old password or

	 *             if confirmNewPassword is not same as newPassword or

	 *             if the current password is not matching the record of logged
	 *             in user
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 * @throws Exception
	 */
	@Override
	public void changeCustomerPassword(CustomerLogin customerLogin)
			throws Exception {		
	
		/*
		 * here we are validating the password that is given by the user
		 * in this we are passing customerLogin class to validateChangePasswordDetails method of
		 * CustomerLoginValidator class
		 * if password given by user is not proper exception will be thrown accordingly
		 **/
		CustomerLoginValidator.validateChangePasswordDetails(customerLogin);
		
		/*
		 * here we are fetching CustomerLogin class from
		 * 	customerLoginDao by passing custmerID to getCustomerLoginByCustomerId method of that class
		 **/
		CustomerLogin customerLoginFromDao = customerLoginDao
				.getCustomerLoginByCustomerId(customerLogin.getCustomerId());
		
		/*
		 * here we are comparing the hash values of password 
		 * that we are passing with the password which we are fetching from database
		 * if password doesn't match exception will be thrown with message
		 * "LoginService.INVALID_CREDENTIALS"
		 * */ 
		if (!Hashing.getHashValue(customerLogin.getPassword()).equals(
				customerLoginFromDao.getPassword())) {
			throw new Exception("LoginService.INVALID_PASSWORD");
		}
		
		/*
		 * here we are validating whether new password is equal to old password
		 **/ 
		if (customerLogin.getPassword().equals(customerLogin.getNewPassword())) {
			throw new Exception(
					"LoginService.OLD_PASSWORD_NEW_PASSWORD_SAME");
		}
		
		/*
		 * here after comparing hash value of passwords we are setting the new value
		 * and updating it to the database by passing it to dao class
		 **/
		customerLogin.setNewPassword(Hashing.getHashValue(customerLogin
				.getNewPassword()));
		customerLoginDao.changeCustomerPassword(customerLogin);

	}
	
	/**
	 * This method is used to add a new login details for the newly added 
	 * Customer  with the data from the model object<br>
	 *
	 * it finds the loginName by using entered emailId, i.e. it takes first part of it (before @)
	 * then it is checked in the database for any usage of the same login name
	 * if same loginName present then get number of loginNames with same name
	 * add one to it and append it to the loginName
	 * 
	 * populate all the values to the bean and pass it to the database
	 * 
	 * @param customer
	 * 
	 * @return customerLogin
	 */
	@Override
	public CustomerLogin addCustomerLogin(Customer customer) throws Exception {
		
		/*
		 * here we are getting a customerLogin bean object
		 **/
		CustomerLogin customerLogin = new CustomerLogin();
		
		/*
		 * here we are setting the new values to customerLogin 
		 **/
		customerLogin.setCustomerId(customer.getCustomerId());
		customerLogin.setLockedStatus(CustomerLoginLockedStatus.UNLOCKED);
		
		/*
		 * here we are splitting emailId By '@' to get userId from It
		 **/
		
		String loginName = customer.getEmailId().split("@")[0];
		
		if(loginName.length()>15){
			loginName = loginName.substring(0, 16);
		}
		
		/*
		 * here we are checking the availability of user name
		 * we are passing the login name to checkAvailabilityOfloginName method
		 * which will return the no of rows with same login name
		 **/
		Long noOfCustomersWithSameLoginName = customerLoginDao.checkAvailabilityOfloginName(loginName.toLowerCase());
		
		/* here if no customer have same login name then new login name is set */ 
		if(noOfCustomersWithSameLoginName!=0)
			loginName +=(noOfCustomersWithSameLoginName+1);
		
		String password = loginName.substring(0,1).toUpperCase()+loginName.substring(1).toLowerCase()+"!"+"123";
		
		int i=3;
		while(password.length()<=8){
			password+=(++i);
		}
		
		/*
		 * here we are setting the login name and password to customerLogin 
		 **/
		customerLogin.setLoginName(loginName.toLowerCase());
		customerLogin.setPassword(password);
		
		/*
		 * here we are adding customer to database by passing the customerLogin bean object
		 * to customer login dao
		 **/
		Integer customerLoginId = customerLoginDao.addNewCustomerLogin(customerLogin);
		
		customerLogin.setCustomerLoginId(customerLoginId);
		
		return customerLogin;
	}

}
