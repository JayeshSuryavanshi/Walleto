package com.amigowallet.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Uniform response for every money-mutation endpoint. {@code newBalance} is the
 * authoritative post-transaction wallet balance. {@code pointsEarned} and
 * {@code bankTransactionId} are populated only where relevant (null otherwise).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MoneyTransactionResponse(
		String message,
		BigDecimal amount,
		BigDecimal newBalance,
		Integer pointsEarned,
		String bankTransactionId) {

	public static MoneyTransactionResponse of(String message, BigDecimal amount, BigDecimal newBalance) {
		return new MoneyTransactionResponse(message, amount, newBalance, null, null);
	}

	public static MoneyTransactionResponse withPoints(String message, BigDecimal amount, BigDecimal newBalance,
			Integer pointsEarned) {
		return new MoneyTransactionResponse(message, amount, newBalance, pointsEarned, null);
	}

	public static MoneyTransactionResponse withBankTxn(String message, BigDecimal amount, BigDecimal newBalance,
			String bankTransactionId) {
		return new MoneyTransactionResponse(message, amount, newBalance, null, bankTransactionId);
	}

	/** Returns a copy with the message replaced (numeric fields unchanged). */
	public MoneyTransactionResponse withMessage(String newMessage) {
		return new MoneyTransactionResponse(newMessage, amount, newBalance, pointsEarned, bankTransactionId);
	}
}
