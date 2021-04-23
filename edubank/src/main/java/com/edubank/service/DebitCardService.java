package com.edubank.service;

import java.security.NoSuchAlgorithmException;

import com.edubank.model.DebitCard;

/**
 * This is a service interface having methods which contain business logic
 * related to Debit Cards.
 * 
 * @author ETA_JAVA
 *
 */
public interface DebitCardService 
{
	
	/**
	 * This method receives a {@link DebitCard} object reference and verifies
	 * the details by calling corresponding DAO class method and throws
	 * exceptions if verification of debit card details fails
	 * 
	 * @param debitCard
	 * 
	 * @throws Exception 
	 * @throws Exception 
	 * @throws NoSuchAlgorithmException 
	 */
	public void verifyCardDetails(DebitCard debitCard) throws Exception; 


	/**
	 * This method is used to change the debit card pin, it receives a
	 * {@link DebitCard} object reference, validates the details by calling
	 * corresponding validator class method, Then it verifies the current pin.
	 * If the data is valid, it updates the debit card pin for the current
	 * customer.
	 * 
	 * @param customerId
	 * @param debitCard
	 * 
	 * @throws Exception  
	 * @throws Exception
	 * @throws NoSuchAlgorithmException 
	 */
	public void changeDebitCardPin(Integer customerId, DebitCard debitCard) throws Exception;
	
	/**
	 * This method is used to add a debit card record for the newly added customer
	 *
	 * create the debitcardnumber using the <b>Luhn algorithm</b>
	 *
	 * populate the bean and pass it on to the database
	 *
	 * @param mappingId, tellerId
	 * 
	 * @return debitCard
	 */
	public DebitCard createNewDebitCard(Integer mappingId, Integer tellerId) throws Exception;
	
	/**
	 * This method is used to verify the pin of the debit card
	 * and get the AccountCustomer MappingId
	 * 
	 * @param debitCardNumber,pin
	 * 
	 * @return mappingId
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 * @throws Exception 
	 */
	public Integer verifyPinAndGetAccountCustomerMappingId(String debitCardNumber, String pin) 
			throws Exception; 

}
