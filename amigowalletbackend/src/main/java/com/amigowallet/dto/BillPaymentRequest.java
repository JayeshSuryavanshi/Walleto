package com.amigowallet.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Wallet-to-merchant bill-payment request. The payer is the authenticated
 * principal (userId is no longer a path variable).
 */
public record BillPaymentRequest(
		@NotBlank String merchantName,
		@NotNull @Positive @Digits(integer = 13, fraction = 2) BigDecimal amount) {
}
