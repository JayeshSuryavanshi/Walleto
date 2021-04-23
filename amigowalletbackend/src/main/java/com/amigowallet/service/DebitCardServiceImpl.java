package com.amigowallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.DebitCardDAO;
import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;
import com.amigowallet.model.CardStatus;

/**
 * This is a service class contains methods for business
 * logics related to debit Card.
 * 
 * @author ETA_JAVA
 *
 */
@Service(value = "debitCardService")
@Transactional
public class DebitCardServiceImpl implements DebitCardService {

	@Autowired
	private DebitCardDAO debitCardDao;

	/**
	 * This method is used for deleting a saved card<br>
	 * A Card bean is passed as the argument and the appropriate DAO method is
	 * called
	 * 
	 * @param card
	 */
	@Override
	public void deleteCard(Card card) {

		debitCardDao.deleteCard(card);
	}

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
	@Override
	public Card addCard(Card card,Integer userId) throws Exception
	{
		
		/*
		 * here we are passing user id as an argument which gets us the user
		 * card details for that user id from the data base table and returns
		 * the list of card for that user id
		 */
		List<Card> cards = debitCardDao.fetchCardByUserId(userId);

		Boolean isExists = false;

		/*
		 * here from the list of cards we are checking for the card no which is
		 * equal to card which we want
		 */
		for (Card cardFromDAO : cards) {
			if (cardFromDAO.getCardNumber().equals(card.getCardNumber())) {
				isExists = true;
				
				/*
				 * here we are checking if that card's status is active or not
				 * if card status is active exception is thrown with message
				 * 'DebitCardService.CARD_ALREADY_EXIST' else activateCard
				 * method of debit card dao is called which activate the card
				 * status
				 */
				if (cardFromDAO.getCardStatus().equals(CardStatus.INACTIVE)) {
					Integer cardId=cardFromDAO.getCardId();
					debitCardDao.activateCard(cardId, userId);
					card.setCardId(cardFromDAO.getCardId());
				} else {
					throw new Exception(
							"DebitCardService.CARD_ALREADY_EXIST");
				}

				break;
			}
		}

		/* if card with that no does not exist then new card is added */
		if (isExists == false) {
			card = debitCardDao.addNewCard(card, userId);
		}
		
		return card;
	}

	/**
	 * This method returns us the list of banks having banks details this calls
	 * fetchAllBankDetails method of debitCardDao to fetch the bank details
	 * 
	 * @return list of banks
	 * 
	 */
	@Override
	public List<Bank> fetchAllBankDetails() {
		List<Bank> bankList = debitCardDao.fetchAllBankDetails();
		return bankList;
	}

}
