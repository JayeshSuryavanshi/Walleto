package com.amigowallet.service;

import java.util.List;

import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;

/**
 * This is a service interface contains methods for business
 * logics related to debit Card.
 * 
 * @author ETA_JAVA
 * 
 */
public interface DebitCardService {
	
	/**
	 * This method is used for deleting a saved  card<br>
	 * A Card bean is passed as the argument
	 * 
	 * @param card
	 */
	public void deleteCard(Card card);
	
	/**
	 * This method is for adding/activating the card. This method takes card and userId
	 * as arguments and retrieves all the cards for that user.
	 * 
	 * @param 
	 * 
	 * @return Card

	 * @throws Exception 
	 * 
	 */
	public Card addCard(Card card,Integer userId) throws Exception;

	/**
	 * This method returns us the list of banks having banks details, this calls
	 * fetchAllBankDetails method of debitCardDao to fetch the bank details
	 * 
	 * @return list of banks
	 * 
	 */
	public List<Bank> fetchAllBankDetails();
}
