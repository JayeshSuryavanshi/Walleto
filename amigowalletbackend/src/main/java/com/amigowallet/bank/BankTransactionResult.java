package com.amigowallet.bank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Result of a bank credit or debit ({@code /api/accounts/credit},
 * {@code /api/cards/debit}, {@code /api/netbanking/debit}).
 *
 * <p>{@code bankTransactionId} and {@code status} are the documented contract.
 * {@code accountNumber} / {@code ifsc} are OPTIONAL fields the bank may return on
 * a debit so that the wallet can issue a compensating credit if its own post-debit
 * persistence fails; when the bank does not return them (the base contract) they
 * are {@code null} and the wallet falls back to logging a reconciliation error.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BankTransactionResult(
		String bankTransactionId,
		String status,
		String accountNumber,
		String ifsc) {

	public boolean isSuccess() {
		return status != null && status.equalsIgnoreCase("SUCCESS");
	}
}
