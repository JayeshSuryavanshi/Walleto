package com.edubank.service;

import com.edubank.model.Account;

/**
 * This is a service interface contains methods for business logics related 
 * to operations on account of customer such as create account, add money to account, verifyAccountDetail, debit amount 
 * from account, get account details etc.
 * 
 * @author ETA_JAVA
 *
 */
public interface AccountService 
{
	/**
	 * This method receives {@link Account} model object as an argument, calls corresponding DAO class method to get 
	 * the account details and then verifies the account details (account number, IFSC, account activeness status) returned 
	 * from DAO method with the account details passed in Account object. <br>
	 * If verification success then it returns customer Id of the passed account
	 * else it throws appropriate exception.
	 * 
	 * @param account
	 *
	 * @return customerId of the account passed as argument
	 *
	 * @throws Exception
	 * @throws Exception
	 */
	public Integer verifyAccountNumberAndIfsc(Account account) throws Exception;
	
	/**
	 * This method receives account holder name received in web service method of controller and account holder name
	 * retrieved for the account number which is received in web service method of controller as arguments. 
	 * It compares both the account holder names. <br>
	 * If verification fails then it throws InvalidAccountHolderNameException
	 * 
	 * @param accountHolderName
	 * @param accountHolderNameForAccount
	 *
	 * @throws Exception
	 * @throws Exception 
	 */
	public void verifyAccountHolderName(String accountHolderName, String accountHolderNameForAccount) 
			throws Exception;

	/**
	 * This method receives account number and amount as arguments. It credits the amount to the given account by making a call to 
	 * corresponding DAO method. <br>
	 * If account does not exist then it returns an appropriate exception. 
	 * 
	 * @param accountNumber
	 * @param amount
	 * 
	 * @throws Exception
	 * @throws Exception 
	 */
	public void creditMoneyToAccount(String accountNumber, Double amount) 
			throws Exception;
	
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
	public Account getAccountByCustomerId(Integer customerId)
			throws Exception;
	
	/**
	 * This method is used to add a new account details for the newly added 
	 * Customer  with the data from the model object<br>
	 *
	 * populate all the values to the bean and pass it to the database
	 * 
	 * @return accountFromDao
	 */
	public Account createNewAccount()throws Exception;

	/**
	 * This method is used to add a mapping record i.e. customerId and the account
	 * number to be mapped<br>
	 *
	 * @param customerId, accountNumber
	 * 
	 * @return accountCustomerMappingId
	 */
	public Integer mapCustomerWithAccount(Integer customerId, String accountNumber) throws Exception;
	
	/**
	 * This method is used to add money to a customer account
	 * 
	 * @param accountNumber,amount
	 */
	public void addMoney(String accountNumber, Double amount) throws 
	Exception;

	/**
	 * This method is used to get the accountNumber using the mappingId
	 * 
	 * @param mappingId
	 * 
	 * @return accountNumber
	 */
	public String getAccountNumberFromMappingId(Integer mappingId)throws Exception;
	
	/**
	 * This method is used to debit the amount from the given customer account
	 * 
	 * @param accountNumber,amount
	 * 
	 * @throws Exception,
	 * @throws Exception,
	 */
	public void debitAmount(String accountNumber, Double amount)
			throws Exception;
	
	
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
	public Account getAccountByAccountNumber(String accountNumber)
			throws Exception;
			
}
