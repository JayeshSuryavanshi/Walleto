package com.edubank.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Card debit request. NOTE: no CVV field — CVV is never accepted or stored.
 * The PIN is the authentication factor. {@code expiry} is "MM/yyyy".
 */
public record CardDebitRequest(
		@NotBlank(message = "is required") String cardNumber,
		@NotBlank(message = "is required") String pin,
		@NotBlank(message = "is required") String expiry,
		@NotNull(message = "is required") @Positive(message = "must be greater than zero") BigDecimal amount,
		String cardHolderName) {
}
