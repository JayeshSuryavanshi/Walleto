package com.amigowallet.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Transfer-to-bank (withdraw) request. The wallet is debited server-side and the
 * destination bank account credited via server-to-server bank-api calls; the
 * acting userId comes from the JWT.
 */
public record BankTransferRequest(
		@NotBlank String accountNumber,
		@NotBlank String ifsc,
		@NotBlank String accountHolderName,
		@NotNull @Positive @Digits(integer = 13, fraction = 2) BigDecimal amount) {
}
