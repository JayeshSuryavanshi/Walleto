package com.amigowallet.dao;

import java.util.List;

import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;

/** 
 * This is DAO interface contains the methods responsible for interacting with
 * the database with respect to debit card details like
 * <BR>
 * -deleteCard<br>
 * -fetchCardByUserId<br>
 * -addNewCard<br>
 * -addNewCard<br>
 * -fetchAllBankDetails<br>
 * 
 * @author ETA_JAVA
 *
 */
public interface DebitCardDAO {

	/**
	 * This method receives Card bean object as an argument 
	 * is used to delete the saved card from the database.
	 * It changes the status of the card to deactive.
	 * 
	 * @param card
	 */
	public void deleteCard(Card card);
	
	
	/**
	 * 
	 * This method receives user id as an argument this gets
	 * the user card  details for that user id from the data base table
	 * and returns the list of card for that user id. 
	 * 
	 * @param userId
	 * 
	 * @return list if card
	 */
	public List<Card> fetchCardByUserId(Integer userId);
	
	
	/**
	 * 
	 * This method receives user id and card as an argument this gets
	 * the user card details for that user id from the data base table
	 * and add the new card details into card details for that user.
	 * 
	 * @param card
	 * @param userId
	 * 
	 * @return card
	 */
	public Card addNewCard(Card card, Integer userId);
	
	
	/**
	 * 
	 * This method receives user id and cardId as an argument this gets
	 * the user card details for that user id from the data base table
	 * and add the activate the status for that cardId.
	 * 
	 * @param cardId
	 * @param userId
	 */
	public void activateCard(Integer cardId, Integer userId);
	
	
	/**
	 * this method fetch all the banks from the database.
	 * 
	 * @return list of bank details
	 */
	public List<Bank> fetchAllBankDetails(); 
	
	
}
