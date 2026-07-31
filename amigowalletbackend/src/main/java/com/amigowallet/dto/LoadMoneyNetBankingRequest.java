package com.amigowallet.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Load-money-via-net-banking request. Net-banking credentials authorise a bank
 * debit server-side (bank -> wallet), then the wallet is credited. The amount is
 * an explicit validated field — NEVER derived from a client-supplied balance. The
 * acting userId comes from the JWT; the password is never persisted or logged.
 */
public record LoadMoneyNetBankingRequest(
		@NotBlank String loginName,
		@NotBlank String password,
		@NotNull @Positive @Digits(integer = 13, fraction = 2) BigDecimal amount) {
}
