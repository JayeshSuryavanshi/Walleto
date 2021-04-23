package com.edubank.service.test;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.edubank.dao.AccountDAO;
import com.edubank.model.Account;
import com.edubank.model.AccountCustomerMapping;
import com.edubank.model.AccountCustomerMappingStatus;
import com.edubank.model.AccountStatus;
import com.edubank.model.Branch;
import com.edubank.model.Customer;
import com.edubank.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTest {

	@MockBean
	private AccountDAO accountDAO;

	@Autowired
	private AccountService accountService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void getAccountByCustomerIdInvalidCustomerId() throws Exception {

		Integer customerId = 1234;

		when(accountDAO.getAccountCustomerMappingByCustomerId(customerId)).thenReturn(null);

		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.NO_ACCOUNT_FOR_CUSTOMER_ID");

		accountService.getAccountByCustomerId(customerId);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#getAccountByCustomerId(Integer customerId)} method.
	 * <br>
	 * In test data customer is set as inactive.
	 * 
	 */
	@Test
	public void getAccountByCustomerIdValidCustomerIdCustomerAccountInActive() throws Exception {
		/*
		 * The below statements are to set the test data used for testing the
		 * method
		 */
		Integer customerId = 1234;
		String accountNumberFromDao = "101";

		AccountCustomerMapping accountCustomerMapping = new AccountCustomerMapping();
		accountCustomerMapping.setAccountNumber(accountNumberFromDao);

		Account account = new Account();
		account.setAccountStatus(AccountStatus.INACTIVE);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountCustomerMapping object created above is returned to the
		 * service by the mocked DAO class method
		 */
		when(accountDAO.getAccountCustomerMappingByCustomerId(customerId)).thenReturn(accountCustomerMapping);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * account object created above is returned to the service by the mocked
		 * DAO class method
		 */
		when(accountDAO.getAccountByAccountNumber(accountNumberFromDao)).thenReturn(account);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.CUSTOMER_ACCOUNT_NOT_ACTIVE");

		accountService.getAccountByCustomerId(customerId);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService#getAccountByCustomerId(Integer customerId)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void getAccountByCustomerIdValidDetails() throws Exception {
		/*
		 * The below statements are to set the test data used for testing the
		 * method
		 */
		Integer customerId = 1234;
		String accountNumberFromDao = "101";

		AccountCustomerMapping accountCustomerMapping = new AccountCustomerMapping();
		accountCustomerMapping.setAccountNumber(accountNumberFromDao);

		Account account = new Account();
		account.setAccountStatus(AccountStatus.ACTIVE);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountCustomerMapping object created above is returned to the
		 * service by the mocked DAO class method
		 */
		when(accountDAO.getAccountCustomerMappingByCustomerId(customerId)).thenReturn(accountCustomerMapping);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * account object created above is returned to the service by the mocked
		 * DAO class method
		 */
		when(accountDAO.getAccountByAccountNumber(accountNumberFromDao)).thenReturn(account);

		Assert.assertEquals(accountService.getAccountByCustomerId(customerId),account);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService#verifyAccountNumberAndIfsc(Account)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyAccountNumberAndIfscValid() throws Exception {
		/*
		 * The following code is to create an Account class object with sample
		 * valid test data.
		 */
		Account account = new Account();
		account.setAccountNumber("443328602688019");
		account.setAccountHolderName("Martin Luther");
		account.setIfsc("JAVA0000501");

		/*
		 * The below statement is to get the Account class object used to mock
		 * the return value of call to DAO class method in service
		 */
		Account accountFromDAO = createStubAccount(account.getAccountNumber(), 5123, AccountStatus.ACTIVE);

		/*
		 * The below statements are to make the Branch class object used to mock
		 * the return value of call to DAO class method in service
		 */
		Branch branchFromDAO = new Branch();
		branchFromDAO.setIfsc("JAVA0000501");

		/*
		 * The below statements are to make the AccountCustomerMapping class
		 * object used to mock the return value of call to DAO class method in
		 * service
		 */
		AccountCustomerMapping accountCustomerMappingFromDAO = new AccountCustomerMapping();
		accountCustomerMappingFromDAO.setCustomerId(552092);
		accountCustomerMappingFromDAO.setMappingStatus(AccountCustomerMappingStatus.ACTIVE);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountFromDAO object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(accountDAO.getAccountByAccountNumber(anyString())).thenReturn(accountFromDAO);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * branchFromDAO object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(accountDAO.getBranchDetails(anyInt())).thenReturn(branchFromDAO);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountCustomerMappingFromDAO object created above is returned to the
		 * service by the mocked DAO class method
		 */
		when(accountDAO.getAccountCustomerMappingByAccountNumber(anyString()))
				.thenReturn(accountCustomerMappingFromDAO);

		Integer customerId = accountService.verifyAccountNumberAndIfsc(account);

		Assert.assertEquals(customerId.intValue(),552092);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#verifyAccountNumberAndIfsc(Account)} In test data
	 * account number 12345 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyAccountNumberAndIfscInValidAccountNumber() throws Exception {
		/*
		 * The following code is to create an Account class object with sample
		 * test data.
		 */
		Account account = new Account();
		account.setAccountNumber("12345");
		account.setAccountHolderName("Martin Luther");
		account.setIfsc("JAVA0000501");

		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(accountDAO.getAccountByAccountNumber(anyString())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.ACCOUNT_NOT_EXIST");

		accountService.verifyAccountNumberAndIfsc(account);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#verifyAccountNumberAndIfsc(Account)} All the test
	 * data is correct. Account is inactive.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyAccountNumberAndIfscValidAccountNumberAccountInactive() throws Exception {
		/*
		 * The following code is to create an Account class object with sample
		 * test data used to test the method.
		 */
		Account account = new Account();
		account.setAccountNumber("443328602688019");
		account.setAccountHolderName("Martin Luther");
		account.setIfsc("JAVA0000501");

		/*
		 * The below statement is to get the Account class object used to mock
		 * the return value of call to DAO class method in service
		 */
		Account accountFromDAO = createStubAccount(account.getAccountNumber(), 5123, AccountStatus.INACTIVE);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountFromDAO object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(accountDAO.getAccountByAccountNumber(anyString())).thenReturn(accountFromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.CUSTOMER_ACCOUNT_NOT_ACTIVE");

		accountService.verifyAccountNumberAndIfsc(account);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#verifyAccountNumberAndIfsc(Account)} In test data
	 * IFSC code "JEE0000501" is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyAccountNumberAndIfscInvalidIfscCode() throws Exception {
		/*
		 * The following code is to create an Account class object with sample
		 * test data used to test the method.
		 */

		Account account = new Account();
		account.setAccountNumber("443328602688019");
		account.setAccountHolderName("Martin Luther");
		account.setIfsc("JEE0000501");

		/*
		 * The below statement is to get the Account class object used to mock
		 * the return value of call to DAO class method in service
		 */
		Account accountFromDAO = createStubAccount(account.getAccountNumber(), 5123, AccountStatus.ACTIVE);

		/*
		 * The below statements are to get make the Branch class object used to
		 * mock the return value of call to DAO class method in service
		 */
		Branch branchFromDAO = new Branch();
		branchFromDAO.setIfsc("JAVA0000501");

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountFromDAO object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(accountDAO.getAccountByAccountNumber(anyString())).thenReturn(accountFromDAO);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * branchFromDAO object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(accountDAO.getBranchDetails(anyInt())).thenReturn(branchFromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.INVALID_ACCOUNT_IFSC_CODE");

		accountService.verifyAccountNumberAndIfsc(account);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#verifyAccountNumberAndIfsc(Account)} In test data
	 * customer account mapping isActive is set to 'N'
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyAccountNumberAndIfscValidAccountNumberCustomerInactive() throws Exception {
		/*
		 * The following code is to create an Account class object with sample
		 * test data used to test the method.
		 */

		Account account = new Account();
		account.setAccountNumber("443328602688019");
		account.setAccountHolderName("Martin Luther");
		account.setIfsc("JAVA0000501");

		/*
		 * The below statement is to get the Account class object used to mock
		 * the return value of call to DAO class method in service
		 */
		Account accountFromDAO = createStubAccount(account.getAccountNumber(), 5123, AccountStatus.ACTIVE);

		/*
		 * The below statements are to make the Branch class object used to mock
		 * the return value of call to DAO class method in service
		 */
		Branch branchFromDAO = new Branch();
		branchFromDAO.setIfsc("JAVA0000501");

		/*
		 * The below statements are to make the AccountCustomerMapping class
		 * object used to mock the return value of call to DAO class method in
		 * service
		 */
		AccountCustomerMapping accountCustomerMappingFromDAO = new AccountCustomerMapping();
		accountCustomerMappingFromDAO.setCustomerId(552092);
		accountCustomerMappingFromDAO.setMappingStatus(AccountCustomerMappingStatus.INACTIVE);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountFromDAO object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(accountDAO.getAccountByAccountNumber(anyString())).thenReturn(accountFromDAO);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * branchFromDAO object created above is returned to the service by the
		 * mocked DAO class method
		 */
		when(accountDAO.getBranchDetails(anyInt())).thenReturn(branchFromDAO);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountCustomerMappingFromDAO object created above is returned to the
		 * service by the mocked DAO class method
		 */
		when(accountDAO.getAccountCustomerMappingByAccountNumber(anyString()))
				.thenReturn(accountCustomerMappingFromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.CUSTOMER_ACCOUNT_NOT_ACTIVE");

		accountService.verifyAccountNumberAndIfsc(account);
	}

	/**
	 * This method receives account details as arguments and creates Account
	 * bean object which is stub for mocking call to DAO method in service.
	 * 
	 * @param accountNumber
	 * @param branchId
	 * @param isActive
	 * 
	 * @return Account
	 */
	private Account createStubAccount(String accountNumber, Integer branchId, AccountStatus accountStatus) {

		/*
		 * The below code creates an object for Account class with the values
		 * passed and returns the object
		 */
		Account account = new Account();
		account.setAccountNumber(accountNumber);
		account.setBranchId(branchId);
		account.setAccountStatus(accountStatus);

		return account;
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService# verifyAccountHolderName(String accountHolderName, String accountHolderNameForAccount)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyAccountHolderNameValid() throws Exception {
		/*
		 * The below code is to make the sample valid test data used to test the
		 * method
		 */

		String accountHolderName = "Martin Luther";
		String accountHolderNameForAccount = "Martin Luther";
		/* ---------------------------------------------------------------- */

		accountService.verifyAccountHolderName(accountHolderName, accountHolderNameForAccount);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService# verifyAccountHolderName(String accountHolderName, String accountHolderNameForAccount)}
	 * <br>
	 * In test data account holder name "Martin" is invalid and account holder
	 * name for account "Martin Luther" is valid
	 * 
	 * @throws Exception
	 */
	@Test
	public void verifyAccountHolderNameInvalidAccountHolderName() throws Exception {
		/*
		 * The below code is to make the sample test data used to test the
		 * method
		 */
		String accountHolderName = "Martin";
		String accountHolderNameForAccount = "Martin Luther";
		/*
		 * ---------------------------------------------------------------------
		 * --
		 */

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.INVALID_ACCOUNT_HOLDER_NAME");

		accountService.verifyAccountHolderName(accountHolderName, accountHolderNameForAccount);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService#creditMoneyToAccount(String accountNumber, Double amount)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void creditMoneyToAccountValid() throws Exception {
		/*
		 * The below code is to make the sample valid test data used to test the
		 * method
		 */
		String accountNumber = "443328602688019";
		Double amount = 500.0;

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 24068.4306 value is returned to the service by the mocked DAO class
		 * method
		 */
		when(accountDAO.creditMoneyToAccount(anyString(), anyDouble())).thenReturn(24068.4306);

		accountService.creditMoneyToAccount(accountNumber, amount);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#creditMoneyToAccount(String accountNumber, Double amount)}
	 * <br>
	 * In test data account number 12345 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void creditMoneyToAccountInvalidAccountNumber() throws Exception {
		/*
		 * The below code is to make the sample test data used to test the
		 * method
		 */
		String accountNumber = "12345";
		Double amount = 500.0;

		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(accountDAO.creditMoneyToAccount(anyString(), anyDouble())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.ACCOUNT_NOT_EXIST");

		accountService.creditMoneyToAccount(accountNumber, amount);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService#createNewAccount()}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void createNewAccountValid() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * "98989898989" is returned to the service by the mocked DAO class
		 * method
		 */
		when(accountDAO.addAccount(any())).thenReturn("98989898989");

		Branch branch = new Branch();
		branch.setBranchName("JAVA FP");
		branch.setIfsc("JAVA0000501");

		when(accountDAO.getBranchDetails(anyInt())).thenReturn(branch);

		Account account = accountService.createNewAccount();

		/*
		 * The below statement is to make the test case passed. If account
		 * object returned by the called service method has Zero balance then
		 * the test case will be passed else the test case will be failed. If
		 * any exception comes from the call to the method
		 * "accountService.createNewAccount()" which is being tested then the
		 * following line of code will not be executed and the test case will be
		 * failed.
		 */
		Assert.assertEquals(new Double(0), account.getBalance());
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService#mapCustomerWithAccount(Integer customerId, String accountNumber)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void mapCustomerWithAccountValidData() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * 123123 is returned to the service by the mocked DAO class method
		 */
		when(accountDAO.addAccountCustomerMapping(anyInt(), anyString())).thenReturn(123123);

		/*
		 * The below statement makes a call to the service class method which is
		 * being tested with sample valid test data 12121 and "989898912421"
		 */
		Integer mappingId = accountService.mapCustomerWithAccount(12121, "989898912421");

		/*
		 * The below statement is to make the test case passed. If 'mappingId'
		 * variable's value is 123123 then the test case will be passed else the
		 * test case will be failed. If any exception comes from the call to the
		 * method "accountService.mapCustomerWithAccount(12121, "989898912421")"
		 * which is being tested then the following line of code will not be
		 * executed and the test case will be failed.
		 */
		Assert.assertEquals(new Integer(123123), mappingId);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService#addMoney(String accountNumber, Double amount)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addMoneyValid() throws Exception {
		/*
		 * The below statements are to set the sample valid test data used for
		 * testing the method
		 */
		String accountNumber = "443328602688019";
		Double amount = 500.0;

		/*
		 * The below statement is to mock the DAO class method having return
		 * type as void. Note: By default DAO class methods having return type
		 * as void are mocked.
		 */
		doNothing().when(accountDAO).creditMoney(anyString(), anyDouble());

		accountService.addMoney(accountNumber, amount);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#addMoney(String accountNumber, Double amount)}
	 * method. <br>
	 * In test data amount -500.0 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void addMoneyInvalidAmount() throws Exception {
		/*
		 * The below statements are to set the sample test data used for testing
		 * the method
		 */
		String accountNumber = "443328602688019";
		Double amount = -500.0;

		/*
		 * The below statement is to mock the DAO class method having return
		 * type as void. Note: By default DAO class methods having return type
		 * as void are mocked.
		 */
		doNothing().when(accountDAO).creditMoney(anyString(), anyDouble());

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountValidator.INVALID_AMOUNT");

		accountService.addMoney(accountNumber, amount);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService#debitAmount(String accountNumber, Double amount)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void debitAmountValid() throws Exception {
		/*
		 * The below statements are to set the sample valid test data used for
		 * testing the method
		 */
		String accountNumber = "443328602688019";
		Double amount = 500.0;

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 5000.00 is returned to the service by the mocked DAO class method
		 */
		when(accountDAO.getBalanceOfAccountNumber(anyString())).thenReturn(5000.00);

		/*
		 * The below statement is to mock the DAO class method having return
		 * type as void. Note: By default DAO class methods having return type
		 * as void are mocked.
		 */
		doNothing().when(accountDAO).debitMoney(anyString(), anyDouble());

		accountService.debitAmount(accountNumber, amount);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#debitAmount(String accountNumber, Double amount)}
	 * method. <br>
	 * In test data amount -500.0 is invalid.
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void debitAmountInvalidAmount() throws Exception {
		/*
		 * The below statements are to set the sample test data used for testing
		 * the method
		 */
		String accountNumber = "443328602688019";
		Double amount = -500.0;
		/*
		 * ---------------------------------------------------------------------
		 * -----------
		 */

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountValidator.INVALID_AMOUNT");

		accountService.debitAmount(accountNumber, amount);
	}

	/**
	 * This is a negative test case method to test
	 * {@link AccountService#debitAmount(String accountNumber, Double amount)}
	 * method. <br>
	 * Test data is valid and testing the method to throw an exception for
	 * Insufficient Balance
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void debitAmountInsufficientBalance() throws Exception {
		/*
		 * The below statements are to set the sample test data used for testing
		 * the method
		 */
		String accountNumber = "443328602688019";
		Double amount = 5000.0;

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 3000.00 is returned to the service by the mocked DAO class method
		 */
		when(accountDAO.getBalanceOfAccountNumber(anyString())).thenReturn(3000.00);

		/*
		 * The below statement is to mock the DAO class method having return
		 * type as void. Note: By default DAO class methods having return type
		 * as void are mocked.
		 */
		doNothing().when(accountDAO).debitMoney(anyString(), anyDouble());

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.INSUFFICIENT_BALANCE");

		accountService.debitAmount(accountNumber, amount);
	}

	/**
	 * This is a positive test case method to test
	 * {@link AccountService#getAccountNumberFromMappingId(Integer)}
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void getAccountNumberFromMappingIdValid() throws Exception {
		when(accountDAO.getAccountNumberForMappingId(anyInt())).thenReturn("443328602688019");

		String accountNumber = accountService.getAccountNumberFromMappingId(200035);

		Assert.assertEquals(accountNumber, "443328602688019");
	}
	
	@Test
	public void getAccountByAccountNumberValidAccountNumber() throws Exception {
		
		Account account = new Account();
		account.setAccountNumber("443328602688019");
		account.setAccountStatus(AccountStatus.ACTIVE);
		
		AccountCustomerMapping accountCustomerMapping = new AccountCustomerMapping();
		accountCustomerMapping.setCustomerId(12345);
		
		Customer customer = new Customer();
		customer.setName("John");
		
		when(accountDAO.getAccountByAccountNumber(anyString())).thenReturn(account);
		when(accountDAO.getAccountCustomerMappingByAccountNumber(anyString())).thenReturn(accountCustomerMapping);
		when(accountDAO.getCustomerByCustomerId(anyInt())).thenReturn(customer);
		
		Account accountFromService = accountService.getAccountByAccountNumber("443328602688019");
		Assert.assertEquals(accountFromService.getAccountHolderName(),customer.getName());
			
	}
	
	@Test
	public void getAccountByAccountNumberInValidAccountNumber() throws Exception {
		
		when(accountDAO.getAccountByAccountNumber(anyString())).thenReturn(null);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.ACCOUNT_NOT_EXIST");
		accountService.getAccountByAccountNumber("443328602688019");
			
	}
	
	@Test
	public void getAccountByAccountNumberLockedAccount() throws Exception {
		
		Account account = new Account();
		account.setAccountNumber("443328602688019");
		account.setAccountStatus(AccountStatus.INACTIVE);
		
		when(accountDAO.getAccountByAccountNumber(anyString())).thenReturn(account);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("AccountService.CUSTOMER_ACCOUNT_NOT_ACTIVE");
		accountService.getAccountByAccountNumber("443328602688019");
			
	}
	
}
