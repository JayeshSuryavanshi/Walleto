package com.amigowallet.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Load-money-via-debit-card request. The card is debited at the bank server-side
 * (bank -> wallet) and the wallet then credited; the acting userId comes from the
 * JWT. The PIN is used only to authorise the bank debit and is never persisted or
 * logged.
 */
public record LoadMoneyCardRequest(
		@NotBlank String cardNumber,
		@NotBlank String pin,
		@NotNull @JsonDeserialize(using = LocalDateDeserializer.class) LocalDate expiry,
		String cardHolderName,
		@NotNull @Positive @Digits(integer = 13, fraction = 2) BigDecimal amount) {
}
