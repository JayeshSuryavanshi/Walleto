package com.amigowallet.dto;

import java.time.LocalDate;

import com.amigowallet.model.Card;
import com.amigowallet.model.CardStatus;

/**
 * Safe card projection: the PAN is masked to its last four digits and the CVV
 * is never included.
 */
public record CardSummary(
		Integer cardId,
		String maskedCardNumber,
		String bankName,
		LocalDate expiryDate,
		CardStatus cardStatus) {

	public static CardSummary from(Card card) {
		String bankName = card.getBank() != null ? card.getBank().getBankName() : null;
		return new CardSummary(
				card.getCardId(),
				mask(card.getCardNumber()),
				bankName,
				card.getExpiryDate(),
				card.getCardStatus());
	}

	private static String mask(String cardNumber) {
		if (cardNumber == null || cardNumber.length() < 4) {
			return "**** **** **** ****";
		}
		String last4 = cardNumber.substring(cardNumber.length() - 4);
		return "**** **** **** " + last4;
	}
}
