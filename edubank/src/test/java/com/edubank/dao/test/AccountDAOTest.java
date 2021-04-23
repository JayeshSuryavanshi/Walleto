package com.edubank.dao.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.AccountDAO;
import com.edubank.model.Account;
import com.edubank.model.AccountCustomerMapping;
import com.edubank.model.AccountStatus;
import com.edubank.model.Branch;
import com.edubank.model.Customer;
import com.edubank.utility.ApplicationConstants;

/**
 * It covers both positive and negative test cases.
 *
 * @author ETA_JAVA
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class AccountDAOTest {

	@Autowired
	private AccountDAO accountDAO;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void addAccountValidTest() throws Exception 
	{

		/*
		 * The following code is to create an Account class object with
		 * sample valid test data.
		 */

		Account account = new Account();
		account.setBalance(0d);
		account.setBranchId(ApplicationConstants.BRANCH_ID);
		account.setAccountStatus(AccountStatus.ACTIVE);

		String accountNumber=accountDAO.addAccount(account);
		
		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "accountDAO.addAccount(account)" which is being tested then the
		 * following line of code will not be executed and the test case
		 * will be failed.
		 */

		Assert.assertNotNull(accountNumber);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#getAccountCustomerMappingByCustomerId(Integer customerId)}
	 * method.
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void getAccountNumberByCustomerIdValidCustomerId() throws Exception 
	{
		/*
		 * The following code is to create an Account class object with
		 * sample valid test data.
		 */
		Integer customerId = 552092;

		AccountCustomerMapping accountCustomerMapping = accountDAO
				.getAccountCustomerMappingByCustomerId(customerId);
			
		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "accountDAO.getAccountCustomerMappingByCustomerId(customerId)"
		 * which is being tested or return value is null then the test case 
		 * will not be passed.
		 */
		Assert.assertNotNull(accountCustomerMapping);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountDAO#getAccountCustomerMappingByCustomerId(Integer customerId)}
	 * method. <br>
	 * In test data customer id 101 is invalid
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void getAccountNumberByCustomerIdInvalidCustomerId() throws Exception 
	{
			
		Integer customerId = 101;

		AccountCustomerMapping accountCustomerMapping = accountDAO.getAccountCustomerMappingByCustomerId(customerId);
		Assert.assertNull(accountCustomerMapping);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#getAccountCustomerMappingByAccountNumber(String accountNumber)}
	 * method.
	 * 
	 * @throws Exception 
	 *
	 */
	@Test
	public void getAccountCustomerMappingByAccountNumberValid() throws Exception 
	{
		/*
		 * This account number is a sample valid test data to test the
		 * method
		 */
		String accountNumber = "443328602688019";

		AccountCustomerMapping accountCustomerMapping = accountDAO
				.getAccountCustomerMappingByAccountNumber(accountNumber);

		/* The following line of code is to make the test case passed. */
		Assert.assertNotNull(accountCustomerMapping);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountDAO#getAccountCustomerMappingByAccountNumber(String accountNumber)}
	 * method. <br>
	 * In test data account number 12345 is invalid
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getAccountCustomerMappingByAccountNumberInValidAccountNumber() throws Exception
	{
		String accountNumber = "12345";

		AccountCustomerMapping accountCustomerMapping = accountDAO.getAccountCustomerMappingByAccountNumber(accountNumber);
		
		Assert.assertNull(accountCustomerMapping);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#getBranchDetails(Integer branchId)} method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getBranchDetailsValid() throws Exception 
	{
		/*
		 * This branch id is a sample valid test data to test the method
		 */
		Integer branchId = 5123;

		Branch branch = accountDAO.getBranchDetails(branchId);

		/* The following line of code is to make the test case passed. */
		Assert.assertNotNull(branch);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountDAO#getBranchDetails(Integer branchId)} method <br>
	 * In test data branch id 101 is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getBranchDetailsInvalidBranchId() throws Exception {
			
		/* This branch id is a sample invalid test data to test the method */
		Integer branchId = 101;

		Branch branch = accountDAO.getBranchDetails(branchId);

		/* The following line of code is to make the test case passed. */
		Assert.assertNull(branch);
		
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#creditMoneyToAccount(String accountNumber, Double amount)}
	 * method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void creditMoneyToAccountValid() throws Exception
	{
		/*
		 * This account number is a sample valid test data to test the 
		 * method
		 */
		String accountNumber = "443328602688019";

		Double amount = 500.0;

		Double updatedBalance=accountDAO.creditMoneyToAccount(accountNumber, amount);

		/* The following line of code is to make the test case passed. */
		Assert.assertNotNull(updatedBalance);
		
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountDAO#creditMoneyToAccount(String accountNumber, Double amount)}
	 * method.<br>
	 * In test data account number 44332860268801 is invalid. <br>
	 * 
	 * @throws Exception 
	 */
	@Test
	public void creditMoneyToAccountInvalidAccountNumber() throws Exception 
	{
		String accountNumber = "44332860268801";

		Double amount = 500.0;
		amount = accountDAO.creditMoneyToAccount(accountNumber, amount);
		Assert.assertNull(amount);

	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#getAccountByAccountNumber(String accountNumber)}
	 * method.
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void getAccountByAccountNumberValidAccountNumber() throws Exception {
			
		/*
		 * This account number is a sample valid test data to test the
		 * method
		 */
		String accountNumber = "443328602688019";

		Account account = accountDAO.getAccountByAccountNumber(accountNumber);

		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "accountDAO.getAccountByAccountNumber(accountNumber)" which
		 * is being tested or return value is null then the test case
		 * will not be passed.
		 */
		Assert.assertNotNull(account);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountDAO#getAccountByAccountNumber(String accountNumber)}
	 * method. <br>
	 * In test data accountNumber 121 is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getAccountByAccountNumberInvalidAccountNumber() throws Exception
	{
			
			String accountNumber = "121";

			Account account = accountDAO.getAccountByAccountNumber(accountNumber);
			Assert.assertNull(account);

	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#creditMoney(String accountNumber, Double amount)}
	 * method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void creditMoneyValidCase() throws Exception 
	{
		/*
		 * This account number is a sample valid test data to test the
		 * method
		 */
		String accountNumber = "581606946469104";

		Double amount = 1000d;

		accountDAO.creditMoney(accountNumber, amount);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "accountDAO.creditMoney(accountNumber, amount)" which is being
		 * tested then the following line of code will not be executed and
		 * the test case will not be passed.
		 */
		Assert.assertTrue(true);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#debitMoney(String accountNumber, Double amount)}
	 * method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void debitMoneyValidCase() throws Exception
	{
			/*
			 * This account number is a sample valid test data to test the
			 * method
			 */
			String accountNumber = "581606946469104";

			Double amount = 1000d;

			accountDAO.debitMoney(accountNumber, amount);

			/*
			 * The following line of code is to make the test case passed. If
			 * any exception comes from the call to the method
			 * "accountDAO.debitMoney(accountNumber, amount)" which is being
			 * tested then the following line of code will not be executed and
			 * the test case will not be passed.
			 */
			Assert.assertTrue(true);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#getBalanceOfAccountNumber(String accountNumber)}
	 * method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getBalanceOfAccountNumberValid() throws Exception
	{
		/*
		 * This account number is a sample valid test data to test the 
		 * method
		 */
		String accountNumber = "443328602688019";

		Double balance = accountDAO.getBalanceOfAccountNumber(accountNumber);

		/* The following line of code is to make the test case passed. */
		Assert.assertNotNull(balance);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#addAccountCustomerMapping(Integer customerId, String accountNumber) }
	 * method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void addAccountCustomerMappingValid() throws Exception
	{
		/*
		 * This account number is a sample valid test data to test the
		 * method
		 */
		String accountNumber = "443328602688019";
		Integer customerId = 106038;

		Integer mappingId = accountDAO.addAccountCustomerMapping(
				customerId, accountNumber);

		/* The following line of code is to make the test case passed. */
		Assert.assertNotNull(mappingId);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountDAO#getAccountNumberForMappingId(Integer mappingId) }
	 * method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getAccountNumberForMappingIdValid() throws Exception 
	{

		/*
		 * This mapping Id is a sample valid test data to test the
		 * method
		 */
		Integer mappingId = 200035;
		
		String accountNumber = accountDAO
				.getAccountNumberForMappingId(mappingId);

		/* The following line of code is to make the test case passed. */
		Assert.assertNotNull(accountNumber);
	}
	
	@Test
	public void getCustomerByCustomerIdInvalidCustomerId()  throws Exception{
		
		/*
		 * This customer id is a sample invalid test data to test the method
		 */
		Integer customerId = 1324;

		Customer customer = accountDAO.getCustomerByCustomerId(customerId);
			
		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "customerDAO.getCustomerByCustomerId(customerId)" which is
		 * being tested or return value is not null then the test case
		 * will not be passed.
		 */
		Assert.assertNull(customer);

		
	}
	
	@Test
	public void getCustomerByCustomerIdValidCustomerId() throws Exception 
	{
		/*
		 * This customer id is a sample invalid test data to test the method
		 */
		Integer customerId = 106038;

		Customer customer = accountDAO.getCustomerByCustomerId(customerId);

		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "accountDAO.getCustomerByCustomerId(customerId)" which is
		 * being tested or return value is null then the test case
		 * will not be passed.
		 */
		Assert.assertNotNull(customer);
	}
	
}
