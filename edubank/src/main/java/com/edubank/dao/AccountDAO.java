package com.edubank.dao;

import com.edubank.model.Account;
import com.edubank.model.AccountCustomerMapping;
import com.edubank.model.Branch;
import com.edubank.model.Customer;

/**
 * This is DAO interface contains the methods responsible for interacting with the database with
 * respect to Account module like verifyAccountDetails,
 * getAccountCustomerMappingByCustomerId, getAccountByAccountNumber
 * 
 * @author ETA_JAVA
 *
 */
public interface AccountDAO
{
	
	/**
	 * This method receives branch id as an argument and gets the branch details from the data base table 
	 * and then returns Branch model object populated with retrieved branch details.<br>
	 * If branch is not found in the data base then it returns null
	 * 
	 * @param branchId
	 * 
	 * @return Branch
	 */
	public Branch getBranchDetails(Integer branchId) throws Exception;

	
	/**
	 * This method is used to fetch the AccountCustomerMapping object
	 * corresponding to customerId
	 * 
	 * @param customerId
	 * 
	 * @return accountCustomerMapping
	 */
	public AccountCustomerMapping getAccountCustomerMappingByCustomerId(
			Integer customerId) throws Exception;

	/**
	 * This method is used to fetch the account object corresponding to
	 * accountNumber
	 * 
	 * @param accountNumber
	 * 
	 * @return Account
	 */
	public Account getAccountByAccountNumber(String accountNumber) throws Exception;
	
	/**
	 * This method is used to persist the new account details for the newly added 
	 * Customer  with the data from the model object<br>
	 *
	 * populate all the values to the entity from bean and persist using save() method
	 * 
	 * @param account
	 * 
	 * @return accountNumber
	 */
	public String addAccount(Account account) throws Exception;

	/**
	 * This method is used to persist a mapping record i.e. customerId and the account
	 * number to be mapped<br>
	 *
	 * populate the entity using parameters and persist it using save() method
	 *
	 * @param customerId, accountNumber
	 * 
	 * @return accountCustomerMappingId
	 */

	public Integer addAccountCustomerMapping(Integer customerId, String accountNumber) throws Exception;
	
	
	/**
	 * This method is used to credit the amount to 
	 * the customer account
	 * It adds the amount to existing balance
	 * 
	 * @param accountNumber,amount
	 */
	public void creditMoney(String accountNumber, Double amount) throws Exception;

	/**
	 * This method is used to fetch the accountNumber using the mappingId
	 * 
	 * @param mappingId
	 * 
	 * @return accountNumber
	 */
	public String getAccountNumberForMappingId(Integer mappingId) throws Exception;
	
	/**
	 * This method is used to debit money from the given account
	 * 
	 * @param accountNumber
	 * @param amount
	 */
	public void debitMoney(String accountNumber, Double amount)  throws Exception;
	
	/**
	 * This method is used to fetch the AccountCustomerMapping details from the data base and returns AccountCustomerMapping object
	 * corresponding to account number.
	 * If details are not found for the given account number it returns null
	 * 
	 * @param accountNumber
	 * 
	 * @return accountCustomerMapping
	 */
	public AccountCustomerMapping getAccountCustomerMappingByAccountNumber(String accountNumber) throws Exception;
	
	/**
	 * This method receives account number and amount as arguments. It updates the balance of the given account by crediting
	 * the given money in the account table <br>
	 * If account exists then it returns the updated balance else it returns null. 
	 * 
	 * @param accountNumber
	 * @param amount
	 * 
	 * @return Double
	 */
	public Double creditMoneyToAccount(String accountNumber, Double amount) throws Exception;

	/**
	 * This method is used to fetch balance of the given account
	 * 
	 * @param accountNumber
	 * 
	 * @return balance
	 */
	public Double getBalanceOfAccountNumber(String accountNumber) throws Exception;
	
	/**
	 * This method is used to get a Customer model corresponding to its
	 * customerId<br>
	 * It uses session.get() method to interact with database for fetching the
	 * data
	 * 
	 * @param customerId
	 * 
	 * @return Customer
	 */
	public Customer getCustomerByCustomerId(Integer customerId) throws Exception;
}
