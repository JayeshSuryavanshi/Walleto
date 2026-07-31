package com.amigowallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.DebitCardDAO;
import com.amigowallet.exception.ApiException;
import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;
import com.amigowallet.model.CardStatus;

/**
 * Business logic related to debit cards.
 *
 * @author ETA_JAVA
 */
@Service(value = "debitCardService")
@Transactional(rollbackFor = Exception.class)
public class DebitCardServiceImpl implements DebitCardService {

	@Autowired
	private DebitCardDAO debitCardDao;

	@Override
	public void deleteCard(Integer cardId, Integer userId) {
		/*
		 * Ownership check: only cards belonging to the authenticated user may be
		 * deleted. A non-owned or non-existent card yields 404 (no existence leak).
		 */
		boolean owned = debitCardDao.fetchCardByUserId(userId).stream()
				.anyMatch(card -> cardId.equals(card.getCardId()));
		if (!owned) {
			throw new ApiException(HttpStatus.NOT_FOUND, "DebitCardService.CARD_NOT_FOUND");
		}

		Card card = new Card();
		card.setCardId(cardId);
		debitCardDao.deleteCard(card);
	}

	@Override
	public Card addCard(Card card, Integer userId) {
		List<Card> cards = debitCardDao.fetchCardByUserId(userId);

		for (Card existing : cards) {
			if (existing.getCardNumber() != null && existing.getCardNumber().equals(card.getCardNumber())) {
				if (CardStatus.INACTIVE.equals(existing.getCardStatus())) {
					debitCardDao.activateCard(existing.getCardId(), userId);
					card.setCardId(existing.getCardId());
					return card;
				}
				throw new ApiException(HttpStatus.CONFLICT, "DebitCardService.CARD_ALREADY_EXIST");
			}
		}

		return debitCardDao.addNewCard(card, userId);
	}

	@Override
	public List<Bank> fetchAllBankDetails() {
		return debitCardDao.fetchAllBankDetails();
	}
}
