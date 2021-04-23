package com.edubank.dao;

import java.security.NoSuchAlgorithmException;

import com.edubank.model.DebitCard;

/**
 * This is a DAO interface having methods to perform CRUD operations on debit card table
 *
 * @author ETA_JAVA
 *
 */
public interface DebitCardDAO 
{
	
	/**
	 * This method takes debit card number and return debit card details as a
	 * {@link DebitCard} Object
	 * 
	 * @param debitCardNumber
	 * 
	 * @return {@link DebitCard} Object if debit card is found for passed
	 *         debitCardNumber else returns null
	 */
	public DebitCard getDebitCardDetails(String debitCardNumber) throws Exception;

	/**
	 * This method takes accountCustomerMappingId and returns the corresponding Debit Card Details
	 * 
	 * @param accountCustomerMappingId
	 * 
	 * @return debitCard
	 */
	public DebitCard getDebitCardDetailsByAccountCustomerMappingId(
			Integer accountCustomerMappingId) throws Exception;

	/**
	 * This method is used the change the debit card pin. it fetches the debit
	 * card details by the debit card number and updates the pin.
	 * 
	 * @param debitCard
	 */
	public void changeDebitCardPin(DebitCard debitCard) throws Exception;

	/**
	 * This method returns the maximum of card numbers inserted if no card 
	 * number inserted send a default one
	 *
	 * @param 
	 * 
	 * @return debitCardNumber
	 */
	public String getLastCardNumber() throws Exception;

	/**
	 * This method is used to add a debit card record for the newly added customer
	 *
	 * populate the entity from bean and persist using save()
	 *
	 * @param mappingId, tellerId
	 * 
	 * @return debitCard
	 */
	public void addDebitCard(DebitCard debitCard, Integer tellerId) throws NoSuchAlgorithmException, Exception;
	
	/**
	 * This method is used to get the pin of the debit card and
	 * customer account mappingId
	 * 
	 * @param debitCardNumber
	 * 
	 * @return pinAndMappingId as an object
	 */
	public Object[] getPinAndMappingIdOfdebitCard(String debitCardNumber) throws Exception; 
}
