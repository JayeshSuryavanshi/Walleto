package com.amigowallet.dao;

import java.util.List;

import com.amigowallet.model.UserTransaction;

/**
 * DAO for reading a user's transaction ledger (used by transaction history). The
 * reward-points redemption itself is handled atomically in the service layer via
 * a pessimistic lock on the user + {@code WalletLedgerDAO}.
 *
 * @author ETA_JAVA
 */
public interface RewardPointsDAO {

	/**
	 * Returns all of a user's transactions, or {@code null} if the user has none
	 * (or does not exist) — never throws an NPE on a bad userId.
	 *
	 * @param userId the user
	 * @return list of user transactions, or {@code null}
	 */
	List<UserTransaction> getAllTransactionByUserId(Integer userId);
}
