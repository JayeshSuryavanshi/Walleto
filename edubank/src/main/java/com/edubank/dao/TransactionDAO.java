package com.edubank.dao;

import java.time.LocalDate;
import java.util.List;

import com.edubank.model.Transaction;

/**
 * This is a DAO interface having methods to perform CRUD operations on
 * transaction table.
 * 
 * @author ETA_JAVA
 *
 */

public interface TransactionDAO {
	/**
	 * This method is used to persist the transaction
	 * 
	 * @param transaction, createdBy
	 *            
	 * 
	 * @return transactionId
	 */
	public Long addTransaction(Transaction transaction, String createdBy)
			throws Exception;

	/**
	 * This method is used to retrieve transactions of the passed account number
	 * between from date and to date in descending order of the transaction date
	 * time. If transactions are not there then it returns null.
	 * 
	 * @param accountNumber
	 * @param fromDate
	 * @param toDate
	 * 
	 * @return List<Transaction>
	 */
	public List<Transaction> getAccountTransactionsForDateRange(
			String accountNumber, LocalDate fromDate, LocalDate toDate)
			throws Exception;

}
