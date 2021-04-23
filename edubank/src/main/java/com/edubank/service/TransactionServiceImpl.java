package com.edubank.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.TransactionDAO;
import com.edubank.model.Transaction;
import com.edubank.validator.TransactionValidator;

/**
 * This is a service class having methods which contain business logic related
 * to Transactions.
 * 
 * @author ETA_JAVA
 *
 */
@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionDAO transactionDao;

	/**
	 * This method is used to add the transaction
	 * 
	 * @param transaction, createdBy
	 * 
	 * @return transaction
	 */
	@Override
	public Transaction addTransaction(Transaction transaction, String createdBy)throws Exception {

		transaction.setTransactionDateTime(LocalDateTime.now());

		/* the following code is used to persist the transaction in database */
		Long transactionId = transactionDao.addTransaction(transaction,
				createdBy);

		transaction.setTransactionId(transactionId);
		return transaction;
	}

	/**
	 * @see com.edubank.service.TransactionService#getLastThirtyDaysTransactionsForAccount(java.lang.String)
	 */
	@Override
	public List<Transaction> getLastThirtyDaysTransactionsForAccount(
			String accountNumber)
			throws Exception {
		LocalDate fromDate = LocalDate.now();
		fromDate = fromDate.minusDays(29);
		LocalDate toDate = LocalDate.now();

		/*
		 * This code is used to retrieve transactions of the passed account
		 * number between from date and to date in descending order of the
		 * transaction date time. If transactions are not there then it returns
		 * null.
		 */
		List<Transaction> transactionsList = transactionDao
				.getAccountTransactionsForDateRange(accountNumber, fromDate,
						toDate);

		/* if transaction is not their between the dates exception is thrown */
		if (transactionsList == null)
			throw new Exception(
					"TransactionService.NO_TRANSACTIONS_IN_LAST_THIRTY_DAYS");

		return transactionsList;
	}

	/** 
	 * @see com.edubank.service.TransactionService#getAccountTransactionsForDateRange
	 *      (java.lang.String, java.time.LocalDate, java.time.LocalDate)
	 */
	@Override
	public List<Transaction> getAccountTransactionsForDateRange(
			String accountNumber, LocalDate fromDate, LocalDate toDate)
			throws Exception {
		
		/*
		 * this code validates the passed transaction date range and throws
		 * exception if validation fails.
		 */

		TransactionValidator.validateTransactionsDateRange(fromDate, toDate);

		List<Transaction> tranList = transactionDao
				.getAccountTransactionsForDateRange(accountNumber, fromDate,
						toDate);

		if (tranList == null)
			throw new Exception(
					"TransactionService.NO_TRANSACTIONS_IN_DATE_RANGE");

		return tranList;
	}

}
