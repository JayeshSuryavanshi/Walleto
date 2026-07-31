package com.amigowallet.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Wallet-to-wallet transfer request. The SENDER is the authenticated principal
 * (never carried in the body); only the recipient and amount are supplied.
 */
public record WalletTransferRequest(
		@NotBlank @Email String recipientEmailId,
		@NotNull @Positive @Digits(integer = 13, fraction = 2) BigDecimal amount) {
}
