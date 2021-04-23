package com.edubank.service;

import java.time.LocalDate;
import java.util.List;

import com.edubank.model.Transaction;


/**
 * This is a service interface having methods which contain business logic related to Transactions.
 * 
 * @author ETA_JAVA
 *
 */
public interface TransactionService 
{

	/**
	 * This method is used to add the transaction to the database
	 * by calling the corresponding DAO method
	 * 
	 * @param transaction, createdBy
	 * 
	 * @return transaction
	 */
	public Transaction addTransaction(Transaction transaction, String createdBy)throws Exception;
	
	/**
	 * This method receives account number as argument and makes a call to DAO method getAccountTransactionsForDateRange()
	 * with values  passed account number, from date as 29 days less than todays date and to date as todays date to 
	 * get last 30 days transactions report.  <br>
	 * If transactions list is empty then it throws TransactionsNotFoundException.
	 * 
	 * @param accountNo
	 * 
	 * @return List of transactions
	 * 
	 * @throws Exception
	 */
	public List<Transaction> getLastThirtyDaysTransactionsForAccount(String accountNumber) 
			throws Exception;
	
	/**
	 * This method receives account number, from date and to date as arguments and makes a call to DAO method 
	 * getAccountTransactionsForDateRange() to get transactions report. <br>
	 * If transactions list is empty then it throws TransactionsNotFoundException.
	 * 
	 * @param accountNo
	 * @param fromDate
	 * @param toDate
	 * 
	 * @return List of transactions
	 * 
	 * @throws Exception 
	 * @throws Exception 
	 * @throws Exception
	 */
	public List<Transaction> getAccountTransactionsForDateRange
			(String accountNumber, LocalDate fromDate, LocalDate toDate) 
			throws Exception;
}
