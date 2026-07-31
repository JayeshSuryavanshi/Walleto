package com.amigowallet.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response of {@code POST /api/accounts/verify}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BankVerifyResponse(boolean verified, String accountHolderName) {
}
