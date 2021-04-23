package com.edubank.validator;

import java.time.LocalDate;

/**
 * This is a validator class having method to validate transaction date range.

 * @author ETA_JAVA
 *
 */
public class TransactionValidator
{
	/**
	 * This is a static method receives from date and to date. It validates the passed transaction date range and 
	 * throws exception if validation fails. 
	 * 
	 * @param fromDate
	 * @param toDate
	 * 
	 * @throws Exception
	 */
	public static void validateTransactionsDateRange(LocalDate fromDate, LocalDate toDate)
			throws Exception
	{
		LocalDate today=LocalDate.now();
		
		if(fromDate.isAfter(today) && !toDate.isAfter(today))
			throw new Exception("TransactionValidator.FROM_DATE_GREATER_THAN_TODAY");
		
		if(toDate.isAfter(today) && !fromDate.isAfter(today))
			throw new Exception("TransactionValidator.TO_DATE_GREATER_THAN_TODAY");
		
		if(fromDate.isAfter(today) && toDate.isAfter(today))
			throw new Exception
				("TransactionValidator.TO_AND_FROM__DATE_GREATER_THAN_TODAY");
		
		if(fromDate.isAfter(toDate))
			throw new Exception
				("TransactionValidator.FROM_DATE_GREATER_THAN_TO_DATE");
	}
}
