package com.edubank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.AccountDAO;
import com.edubank.model.Account;
import com.edubank.model.AccountCustomerMapping;
import com.edubank.model.AccountCustomerMappingStatus;
import com.edubank.model.AccountStatus;
import com.edubank.model.Branch;
import com.edubank.model.Customer;
import com.edubank.utility.ApplicationConstants;
import com.edubank.validator.AccountValidator;

/**
 * This is a service class implements {@link AccountService} interface, it contains methods for business logics related 
 * to operations on account of customer such as create account, add money to account, verifyAccountDetail, debit amount from
 * account, get account details etc.
 * 
 * @author ETA_JAVA
 *
 */
@Service(value = "accountService")
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	private AccountDAO accountDAO;

	/**
	 * This method is used to add a new account details for the newly added
	 * Customer with the data from the model object<br>
	 *
	 * populate all the values to the bean and pass it to the database
	 * 
	 * @return accountFromDao
	 */
	@Override
	public Account createNewAccount() throws Exception{

		/*
		 * here we are creating a bean object and adding the details to it
		 */
		Account account = new Account();

		account.setBalance(0d);
		account.setBranchId(ApplicationConstants.BRANCH_ID);
		account.setAccountStatus(AccountStatus.ACTIVE);

		/*
		 * here we are passing the bean object to dao to update it in datebase
		 */
		String accountNumber = accountDAO.addAccount(account);
		account.setAccountNumber(accountNumber);
		
		Branch accountBranch=accountDAO.getBranchDetails(account.getBranchId());
		account.setBranchName(accountBranch.getBranchName());
		account.setIfsc(accountBranch.getIfsc());
		
		return account;
	}

	/**
	 * This method is used to add a mapping record i.e. customerId and the
	 * account number to be mapped<br>
	 *
	 * @param customerId, accountNumber            
	 * 
	 * @return accountCustomerMappingId
	 */
	@Override
	public Integer mapCustomerWithAccount(Integer customerId,
			String accountNumber) throws Exception {

		Integer mappingId = accountDAO.addAccountCustomerMapping(customerId,
				accountNumber);

		return mappingId;
	}

	/**
	 * This method is used to add money to a customer account
	 * 
	 * @param accountNumber, amount
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	@Override
	public void addMoney(String accountNumber, Double amount)
			throws Exception{

		/* This below method call is to validate amount */
		AccountValidator.validateAmount(amount);

		accountDAO.creditMoney(accountNumber, amount);

	}

	/**
	 * This method is used to get the accountNumber using the mappingId
	 * 
	 * @param mappingId
	 * 
	 * @return accountNumber
	 */
	@Override
	public String getAccountNumberFromMappingId(Integer mappingId) throws Exception {

		String accountNumber = accountDAO
				.getAccountNumberForMappingId(mappingId);
		return accountNumber;
	}

	/**
	 * This method is used to debit the amount from the given customer account
	 * It first validates the account number and amount
	 * 
	 * @param accountNumber, amount
	 * 
	 * @throws Exception
	 *             , if balance is insufficient
	 * @throws Exception
	 *             , if amount is invalid
	 */
	@Override
	public void debitAmount(String accountNumber, Double amount)
			throws Exception {


		/* This below method call is to validate amount */
		AccountValidator.validateAmount(amount);
		
		/* This method is used to fetch balance of the given account */
		Double balance = accountDAO.getBalanceOfAccountNumber(accountNumber);
		if (balance < amount) {
			throw new Exception(
					"AccountService.INSUFFICIENT_BALANCE");
		}
		
		/*
		 * This method is used to debit money from the given account
		 */
		accountDAO.debitMoney(accountNumber, amount);
	}

	/**
	 * @see com.edubank.service.AccountService#verifyAccountDetails(com.edubank.model.Account)
	 */
	@Override
	public Integer verifyAccountNumberAndIfsc(Account account)
			throws Exception {
		/*
		 * This method is used to fetch the account object corresponding to
		 * accountNumber
		 */
		Account accountFromDAO = accountDAO.getAccountByAccountNumber(account
				.getAccountNumber());

		/*
		 * here we are validating account which we received from database and
		 * throwing corresponding exception
		 */
		if (accountFromDAO == null)
			throw new Exception(
					"AccountService.ACCOUNT_NOT_EXIST");

		if (accountFromDAO.getAccountStatus().equals(AccountStatus.INACTIVE))
			throw new Exception(
					"AccountService.CUSTOMER_ACCOUNT_NOT_ACTIVE");

		Branch branchFromDAO = accountDAO.getBranchDetails(accountFromDAO
				.getBranchId());
		if (!branchFromDAO.getIfsc().equalsIgnoreCase(account.getIfsc()))
			throw new Exception(
					"AccountService.INVALID_ACCOUNT_IFSC_CODE");

		/*
		 * This method is used to fetch the AccountCustomerMapping details from
		 * the data base and returns AccountCustomerMapping object corresponding
		 * to account number. If details are not found for the given account
		 * number it returns null
		 */
		AccountCustomerMapping accountCustomerMappingFromDAO = accountDAO
				.getAccountCustomerMappingByAccountNumber(account
						.getAccountNumber());
		
		/* here we are checking if account is active or not */
		if (accountCustomerMappingFromDAO.getMappingStatus().equals(AccountCustomerMappingStatus.INACTIVE))
			throw new Exception(
					"AccountService.CUSTOMER_ACCOUNT_NOT_ACTIVE");

		return accountCustomerMappingFromDAO.getCustomerId();
	}

	/**
	 * @see com.edubank.service.AccountService#verifyAccountHolderName(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public void verifyAccountHolderName(String accountHolderName,
			String accountHolderNameForAccount)
			throws Exception {

		/* here we are validating the account holder name */
		if (!accountHolderNameForAccount.equalsIgnoreCase(accountHolderName))
			throw new Exception(
					"AccountService.INVALID_ACCOUNT_HOLDER_NAME");

	}

	/**
	 * @see com.edubank.service.AccountService#creditMoneyToAccount(java.lang.String,
	 *      java.lang.Double)
	 */
	@Override
	public void creditMoneyToAccount(String accountNumber, Double amount)
			throws Exception {

		/*
		 * This method receives account number and amount as arguments. It
		 * updates the balance of the given account by crediting the given money
		 * in the account table If account exists then it returns the updated
		 * balance else it returns null.
		 */
		Double updatedBalance = accountDAO.creditMoneyToAccount(accountNumber,
				amount);
		
		
		if (updatedBalance == null)
			throw new Exception(
					"AccountService.ACCOUNT_NOT_EXIST");
	}

	/**
	 * This method is used to get the account object corresponding to the
	 * customerId
	 * 
	 * @param customerId
	 * 
	 * @return account object
	 * 
	 * @throws Exception
	 *             if the accountCustomerMapping object is not found for the
	 *             given customerId
					or
	 *             if the account is in active for the given customerId
	 * @throws Exception 
	 */
	@Override
	public Account getAccountByCustomerId(Integer customerId)
			throws Exception {

		/* This method is used to fetch the AccountCustomerMapping object corresponding to customerId */
		AccountCustomerMapping accountCustomerMapping = accountDAO
				.getAccountCustomerMappingByCustomerId(customerId);

		if (accountCustomerMapping == null) {
			throw new Exception(
					"AccountService.NO_ACCOUNT_FOR_CUSTOMER_ID");
		}
		
		String accountNumber = accountCustomerMapping.getAccountNumber();
		
		/*
		 * This method is used to fetch the account object corresponding to accountNumber */
		Account account = accountDAO.getAccountByAccountNumber(accountNumber);
		
		/* here we are checking if account is active or not */
		if (account.getAccountStatus().equals(AccountStatus.INACTIVE)) {
			throw new Exception(
					"AccountService.CUSTOMER_ACCOUNT_NOT_ACTIVE");
		}

		return account;
	}
	

	/**
	 * This method is used to get the account details corresponding to account number.
	 * 
	 * @param accountNumber
	 * 
	 * @return account
	 * 
	 * @throws Exception
	 * @throws Exception 
	 * @throws Exception 
	 */
	@Override
	public Account getAccountByAccountNumber(String accountNumber)
			throws  Exception {


		/* This below method call is to validate account number entered by the teller */
		AccountValidator.validateAccountNumber(accountNumber);
		
		/*
		 * This method is used to fetch the account object corresponding to accountNumber */
		Account account = accountDAO.getAccountByAccountNumber(accountNumber);
		
		if(account==null)
		{
			throw new Exception(
					"AccountService.ACCOUNT_NOT_EXIST");
		}
		
		/* here we are checking if account is active or not */
		if (account.getAccountStatus().equals(AccountStatus.INACTIVE)) {
			throw new Exception(
					"AccountService.CUSTOMER_ACCOUNT_NOT_ACTIVE");
		}

		AccountCustomerMapping accountCustomerMapping = accountDAO.getAccountCustomerMappingByAccountNumber(accountNumber);
		
		Customer customer = accountDAO.getCustomerByCustomerId(accountCustomerMapping.getCustomerId());
		
		account.setAccountHolderName(customer.getName());
		
		return account;
	}
	
}
