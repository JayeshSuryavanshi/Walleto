package com.amigowallet.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.exception.ApiException;
import com.amigowallet.utility.AmigoWalletConstants;
import com.amigowallet.utility.MoneyUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository("WalletToWalletDAO")
public class WalletToWalletDAOImpl implements WalletToWalletDAO {

	/** Cashback rate (2%) and cap for genuine transfers of >= CASHBACK_MIN. */
	private static final BigDecimal CASHBACK_RATE = new BigDecimal("0.02");
	private static final BigDecimal CASHBACK_MIN = new BigDecimal("200");
	private static final BigDecimal CASHBACK_CAP = new BigDecimal("100.00");
	/** 1 reward point per this many currency units (floored, deterministic). */
	private static final BigDecimal POINTS_PER_UNIT = new BigDecimal("100");

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private WalletLedgerDAO walletLedger;

	@Override
	public MoneyTransactionResponse transferToWallet(Integer senderUserId, BigDecimal amount, String recipientEmailId) {
		BigDecimal safeAmount = MoneyUtil.requirePositive(amount);

		/*
		 * Resolve the recipient's identity via a SCALAR projection. This deliberately
		 * does NOT load the recipient UserEntity into the persistence context, so the
		 * subsequent locked find (lockUser) is the FIRST managed load of that row — a
		 * PESSIMISTIC_WRITE from the outset, not a lock upgrade of an already-loaded
		 * entity (which, under concurrency, raced the @Version check and produced a
		 * StaleObjectStateException).
		 */
		String normalizedEmail = recipientEmailId == null ? null : recipientEmailId.toLowerCase();
		List<Object[]> matches = entityManager.createQuery(
				"select u.userId, u.emailId from UserEntity u where u.emailId = :email", Object[].class)
				.setParameter("email", normalizedEmail)
				.getResultList();
		if (matches.isEmpty()) {
			throw new ApiException(HttpStatus.NOT_FOUND, "WalletToWalletService.INVALID_EMAIL");
		}
		Integer receiverUserId = (Integer) matches.get(0)[0];

		/*
		 * Fixed self-transfer guard: compare identities (the original reference
		 * comparison of two separately-constructed objects was always false, which
		 * combined with the missing funds check to print money).
		 */
		if (senderUserId.equals(receiverUserId)) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "WalletService.PAYING_HIMSELF");
		}

		/*
		 * Lock BOTH users up front in ascending userId order (deadlock avoidance),
		 * before any balance mutation. These locked finds are the first managed loads
		 * of each row. Subsequent ledger debit/credit re-find the same locked entities.
		 */
		Integer first = Math.min(senderUserId, receiverUserId);
		Integer second = Math.max(senderUserId, receiverUserId);
		UserEntity firstEntity = walletLedger.lockUser(first);
		UserEntity secondEntity = walletLedger.lockUser(second);
		UserEntity senderEntity = senderUserId.equals(first) ? firstEntity : secondEntity;
		String senderEmail = senderEntity.getEmailId();

		/* Deterministic reward points: floor(amount / 100), earned by the sender. */
		int points = safeAmount.divide(POINTS_PER_UNIT, 0, RoundingMode.DOWN).intValue();

		/* Sender debit (funds-checked inside the ledger) + receiver credit — one posting. */
		WalletLedgerDAO.LedgerEntry senderDebit = walletLedger.debit(
				senderUserId, safeAmount,
				AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0),
				AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0),
				AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_DEBIT + recipientEmailId,
				points);

		walletLedger.credit(
				receiverUserId, safeAmount,
				AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0),
				AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0),
				AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_CREDIT + senderEmail,
				0, AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));

		BigDecimal senderBalance = senderDebit.newBalance();

		/*
		 * Cashback: only on genuine transfers (never self — guarded above) of at
		 * least CASHBACK_MIN, credited to the SENDER as a promo credit, 2% of the
		 * amount, deterministically rounded and capped.
		 */
		if (safeAmount.compareTo(CASHBACK_MIN) >= 0) {
			BigDecimal cashback = safeAmount.multiply(CASHBACK_RATE).setScale(MoneyUtil.SCALE, RoundingMode.HALF_UP);
			if (cashback.compareTo(CASHBACK_CAP) > 0) {
				cashback = CASHBACK_CAP;
			}
			if (cashback.compareTo(BigDecimal.ZERO) > 0) {
				WalletLedgerDAO.LedgerEntry cashbackEntry = walletLedger.credit(
						senderUserId, cashback,
						AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0),
						AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0),
						AmigoWalletConstants.TRANSACTION_INFO_MONEY_CASHBACK_TO_WALLET_CREDIT,
						0, AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
				senderBalance = cashbackEntry.newBalance();
			}
		}

		return MoneyTransactionResponse.withPoints(null, safeAmount, senderBalance, points);
	}
}
