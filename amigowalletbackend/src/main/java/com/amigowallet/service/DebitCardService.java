package com.amigowallet.service;

import java.util.List;

import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;

/**
 * Business logic for debit cards saved in the wallet.
 *
 * @author ETA_JAVA
 */
public interface DebitCardService {

	/**
	 * Deactivates a saved card, but only after verifying that the card belongs to
	 * the authenticated user (closes the IDOR where any caller could delete any
	 * card).
	 *
	 * @throws com.amigowallet.exception.ApiException 404 if the card does not exist
	 *                                                or is not owned by the user.
	 */
	void deleteCard(Integer cardId, Integer userId);

	/**
	 * Adds/activates a card for the authenticated user.
	 *
	 * @throws com.amigowallet.exception.ApiException 409 if the card already exists.
	 */
	Card addCard(Card card, Integer userId);

	/**
	 * @return the list of banks (for the card / account forms).
	 */
	List<Bank> fetchAllBankDetails();
}
