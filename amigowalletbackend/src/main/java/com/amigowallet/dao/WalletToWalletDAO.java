package com.amigowallet.dao;

import java.math.BigDecimal;

import com.amigowallet.dto.MoneyTransactionResponse;

public interface WalletToWalletDAO {

	/**
	 * Transfers {@code amount} from the authenticated sender to the wallet owning
	 * {@code recipientEmailId}, as one atomic posting under pessimistic locks on
	 * both users (locked in ascending userId order to avoid deadlock): funds check
	 * + sender debit + receiver credit, plus deterministic reward points and a
	 * capped 2% cashback for genuine transfers >= 200. Self-transfer is rejected.
	 *
	 * @return the posting result (amount, sender's new balance, points) with a
	 *         {@code null} message for the service layer to fill in.
	 */
	MoneyTransactionResponse transferToWallet(Integer senderUserId, BigDecimal amount, String recipientEmailId);
}
