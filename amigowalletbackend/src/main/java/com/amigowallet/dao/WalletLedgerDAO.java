package com.amigowallet.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.entity.PaymentTypeEntity;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.entity.UserTransactionEntity;
import com.amigowallet.exception.ApiException;
import com.amigowallet.model.TransactionStatus;
import com.amigowallet.utility.MoneyUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

/**
 * The single, authoritative money-posting helper for wallet-api.
 *
 * <p>Every money mutation goes through here so that, in ONE transaction:
 * <ol>
 * <li>the acting {@link UserEntity} is loaded under a {@code PESSIMISTIC_WRITE}
 * row lock (preventing double-spend / lost-update races),</li>
 * <li>debits are funds-checked ({@code balance >= amount}, else 422),</li>
 * <li>the authoritative {@code WALLET_USER.BALANCE} is updated with
 * {@link BigDecimal} arithmetic, and</li>
 * <li>exactly one SUCCESS ledger row is appended to {@code USER_TRANSACTION}.</li>
 * </ol>
 *
 * <p>Methods use {@code Propagation.REQUIRED}: they join the caller's transaction
 * when one exists (wallet-to-wallet, bill pay, transfer-to-bank — so the whole
 * posting is atomic and rolls back together) and open their own transaction when
 * called standalone (the post-bank-debit wallet credit of the load-money flows).
 */
@Repository
public class WalletLedgerDAO {

	@PersistenceContext
	private EntityManager entityManager;

	/** A posted ledger row plus the resulting authoritative balance. */
	public record LedgerEntry(UserTransactionEntity row, BigDecimal newBalance) {
	}

	/**
	 * Loads a user under a {@code PESSIMISTIC_WRITE} lock held until the current
	 * transaction ends.
	 *
	 * @throws ApiException 400 if {@code userId} is null; 404 if no such user.
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public UserEntity lockUser(Integer userId) {
		if (userId == null) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "WalletService.TRANSACTION_FAILURE");
		}
		UserEntity userEntity = entityManager.find(UserEntity.class, userId, LockModeType.PESSIMISTIC_WRITE);
		if (userEntity == null) {
			throw new ApiException(HttpStatus.NOT_FOUND, "LoginService.USER_NOT_FOUND");
		}
		return userEntity;
	}

	/**
	 * Credits {@code amount} to the user's balance and appends a SUCCESS credit
	 * ledger row. Used for load-money, wallet-to-wallet receiver credit, cashback,
	 * and redeemed reward points.
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public LedgerEntry credit(Integer userId, BigDecimal amount, char paymentFrom, char paymentTo,
			String info, int pointsEarned, char isRedeemed) {
		BigDecimal safeAmount = MoneyUtil.requirePositive(amount);
		UserEntity user = lockUser(userId);

		BigDecimal newBalance = MoneyUtil.scale(user.getBalance().add(safeAmount));
		user.setBalance(newBalance);

		UserTransactionEntity row = post(user, safeAmount, paymentFrom, paymentTo, "C", info, pointsEarned, isRedeemed);
		return new LedgerEntry(row, newBalance);
	}

	/**
	 * Funds-checks and debits {@code amount} from the user's balance and appends a
	 * SUCCESS debit ledger row. Used for wallet-to-wallet sender debit, bill pay,
	 * and transfer-to-bank.
	 *
	 * @throws ApiException 422 WalletService.INSUFFICIENT_BALANCE if funds are short.
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public LedgerEntry debit(Integer userId, BigDecimal amount, char paymentFrom, char paymentTo,
			String info, int pointsEarned) {
		BigDecimal safeAmount = MoneyUtil.requirePositive(amount);
		UserEntity user = lockUser(userId);

		if (user.getBalance().compareTo(safeAmount) < 0) {
			throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "WalletService.INSUFFICIENT_BALANCE");
		}

		BigDecimal newBalance = MoneyUtil.scale(user.getBalance().subtract(safeAmount));
		user.setBalance(newBalance);

		UserTransactionEntity row = post(user, safeAmount, paymentFrom, paymentTo, "D", info, pointsEarned, 'N');
		return new LedgerEntry(row, newBalance);
	}

	/** Appends one managed, SUCCESS ledger row to the (already locked) user. */
	private UserTransactionEntity post(UserEntity user, BigDecimal amount, char paymentFrom, char paymentTo,
			String remarks, String info, int pointsEarned, char isRedeemed) {
		PaymentTypeEntity paymentType = resolvePaymentType(paymentFrom, paymentTo, remarks.charAt(0));

		UserTransactionEntity row = new UserTransactionEntity();
		row.setAmount(amount);
		row.setInfo(info);
		row.setRemarks(remarks);
		row.setPointsEarned(pointsEarned);
		row.setIsRedeemed(isRedeemed);
		row.setPaymentTypeEntity(paymentType);
		row.setTransactionStatus(TransactionStatus.SUCCESS);

		List<UserTransactionEntity> rows = user.getUserTransactionEntities();
		if (rows == null) {
			rows = new ArrayList<>();
			user.setUserTransactionEntities(rows);
		}
		rows.add(row);
		entityManager.persist(row);
		return row;
	}

	/** Resolves a reference PAYMENT_TYPE row by (from, to, type). */
	private PaymentTypeEntity resolvePaymentType(char paymentFrom, char paymentTo, char paymentType) {
		try {
			return entityManager.createQuery(
					"from PaymentTypeEntity where paymentFrom = :from and paymentTo = :to and paymentType = :type",
					PaymentTypeEntity.class)
					.setParameter("from", paymentFrom)
					.setParameter("to", paymentTo)
					.setParameter("type", paymentType)
					.getSingleResult();
		} catch (NoResultException e) {
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "WalletService.TRANSACTION_FAILURE");
		}
	}
}
