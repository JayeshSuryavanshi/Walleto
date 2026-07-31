package com.amigowallet.service;

import java.math.BigDecimal;

import com.amigowallet.dto.MoneyTransactionResponse;

public interface WalletToWalletService {

	/**
	 * Transfers {@code amount} from the authenticated sender to the wallet owning
	 * {@code recipientEmailId}. Atomic (single transaction), funds-checked, and
	 * locked; self-transfer is rejected.
	 */
	MoneyTransactionResponse transferToWallet(Integer senderUserId, BigDecimal amount, String recipientEmailId);
}
