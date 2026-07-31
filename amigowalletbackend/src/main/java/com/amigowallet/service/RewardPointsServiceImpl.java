package com.amigowallet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.amigowallet.dao.WalletLedgerDAO;
import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.entity.UserTransactionEntity;
import com.amigowallet.exception.ApiException;
import com.amigowallet.model.TransactionStatus;
import com.amigowallet.utility.AmigoWalletConstants;
import com.amigowallet.utility.MoneyUtil;

/**
 * Reward-points redemption. The user is pessimistically locked for the whole
 * operation, so two concurrent redeem calls cannot both see the same points and
 * double-credit. Only SUCCESS, non-redeemed rows contribute points.
 *
 * @author ETA_JAVA
 */
@Service(value = "rewardPointsService")
public class RewardPointsServiceImpl implements RewardPointsService {

	/**
	 * Minimum redeemable points. The legacy code hard-coded {@code 10} while its
	 * comment said {@code 100}; the user-facing message states 10, so 10 is the
	 * intended threshold, applied consistently here.
	 */
	private static final int REDEEM_THRESHOLD = 10;

	private final WalletLedgerDAO walletLedger;
	private final MoneyTxRunner moneyTxRunner;
	private final Environment environment;

	public RewardPointsServiceImpl(WalletLedgerDAO walletLedger, MoneyTxRunner moneyTxRunner,
			Environment environment) {
		this.walletLedger = walletLedger;
		this.moneyTxRunner = moneyTxRunner;
		this.environment = environment;
	}

	@Override
	public MoneyTransactionResponse redeemRewardPoints(Integer userId) {
		return moneyTxRunner.runWithRetry(() -> doRedeem(userId));
	}

	private MoneyTransactionResponse doRedeem(Integer userId) {
		UserEntity user = walletLedger.lockUser(userId);

		List<UserTransactionEntity> rows = user.getUserTransactionEntities();
		int rewardPoints = 0;
		if (rows != null) {
			for (UserTransactionEntity row : rows) {
				if (isRedeemable(row)) {
					rewardPoints += row.getPointsEarned() == null ? 0 : row.getPointsEarned();
				}
			}
		}

		if (rewardPoints < REDEEM_THRESHOLD) {
			throw new ApiException(HttpStatus.PRECONDITION_FAILED,
					"RewardPointsService.REWARD_POINTS_NOT_ENOUGH_TO_REDEEM");
		}

		/* Amount = points * REDEEM_PERCENTAGE (0.10), rounded HALF_UP to 2dp. */
		BigDecimal redeemRate = BigDecimal.valueOf(AmigoWalletConstants.REDEEM_PERCENTAGE);
		BigDecimal amountToCredit = MoneyUtil.scale(
				BigDecimal.valueOf(rewardPoints).multiply(redeemRate).setScale(MoneyUtil.SCALE, RoundingMode.HALF_UP));

		/* Mark every redeemable row as redeemed (managed entities flush on commit). */
		if (rows != null) {
			for (UserTransactionEntity row : rows) {
				if (isRedeemable(row)) {
					row.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
				}
			}
		}

		/* Credit the redeemed money to the wallet (same locked transaction). */
		WalletLedgerDAO.LedgerEntry entry = walletLedger.credit(
				userId, amountToCredit,
				AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0),
				AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0),
				AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_BY_REDEEMING_REWARD_POINTS,
				0, AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));

		return MoneyTransactionResponse.of(
				environment.getProperty("RewardPointsAPI.REWARD_POINTS_REDEEMED_SUCCESS"),
				amountToCredit, entry.newBalance());
	}

	private boolean isRedeemable(UserTransactionEntity row) {
		return TransactionStatus.SUCCESS.equals(row.getTransactionStatus())
				&& row.getIsRedeemed() != null
				&& AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.equals(row.getIsRedeemed().toString());
	}
}
