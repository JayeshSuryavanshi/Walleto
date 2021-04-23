package com.edubank.validator;

/**
 * This is a validator class used to validate account number and amount as per business requirements.
 * 
 * @author ETA_JAVA
 *
 */
public class AccountValidator 
{
	/**
	 * This method is to validate the accountNumber 
	 * accountNumber should be of 15 digits.
	 * 
	 * @param accountNumber
	 * 
	 * @throws Exception
	 * 							if the accountNumber is invalid
	 */
	public static void validateAccountNumber(String accountNumber) throws Exception
	{
		if(!accountNumber.matches("[0-9]{15}"))
			throw new Exception("AccountValidator.INVALID_ACCOUNT_NUMBER");

	}
	
	/**
	 * This method is to validate the amount 
	 * <b>amount</b> should be greater than zero
	 *  
	 * @param amount
	 * 
	 * @throws Exception if amount is invalid
	 */
	public static void validateAmount(Double amount) throws Exception{

		if(amount<=0){

			throw new Exception("AccountValidator.INVALID_AMOUNT");
		}
		
	}


}
