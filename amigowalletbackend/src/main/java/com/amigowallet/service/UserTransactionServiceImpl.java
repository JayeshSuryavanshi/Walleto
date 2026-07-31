package com.amigowallet.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.bank.BankClient;
import com.amigowallet.bank.BankTransactionResult;
import com.amigowallet.bank.BankVerifyResponse;
import com.amigowallet.dao.WalletLedgerDAO;
import com.amigowallet.dto.BankTransferRequest;
import com.amigowallet.dto.LoadMoneyCardRequest;
import com.amigowallet.dto.LoadMoneyNetBankingRequest;
import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.exception.ApiException;
import com.amigowallet.utility.AmigoWalletConstants;
import com.amigowallet.utility.MoneyUtil;

/**
 * Server-to-server bank orchestration for load-money and transfer-to-bank. This
 * bean is deliberately NOT class-level {@code @Transactional}: the load flows must
 * perform the bank debit OUTSIDE any wallet transaction (so the DB row lock is not
 * held across the network call), then commit the wallet credit in its own tx via
 * {@link WalletLedgerDAO} (whose methods are {@code Propagation.REQUIRED}). Only
 * {@link #sendMoneyToBankAccount} is transactional, so the wallet debit and the
 * bank credit share one boundary and roll back together.
 */
@Service("userTransactionService")
public class UserTransactionServiceImpl implements UserTransactionService {

	private static final Logger logger = LoggerFactory.getLogger(UserTransactionServiceImpl.class);

	private final WalletLedgerDAO walletLedger;
	private final BankClient bankClient;
	private final Environment environment;

	public UserTransactionServiceImpl(WalletLedgerDAO walletLedger, BankClient bankClient, Environment environment) {
		this.walletLedger = walletLedger;
		this.bankClient = bankClient;
		this.environment = environment;
	}

	@Override
	public MoneyTransactionResponse loadMoneyFromDebitCard(Integer userId, LoadMoneyCardRequest request) {
		BigDecimal amount = MoneyUtil.requirePositive(request.amount());
		String idempotencyKey = newIdempotencyKey();

		// 1) Debit the bank via the card FIRST (bank -> wallet). Throws on failure.
		BankTransactionResult bankResult = bankClient.cardDebit(
				request.cardNumber(), request.pin(), request.expiry(), amount,
				request.cardHolderName(), idempotencyKey);
		requireBankSuccess(bankResult);

		// 2) Credit the wallet in its own committed transaction; compensate on failure.
		return creditWalletAfterBankDebit(userId, amount, bankResult, idempotencyKey,
				AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_FROM_BANK_TO_EWALLET_USING_DEBIT_CARD);
	}

	@Override
	public MoneyTransactionResponse loadMoneyFromNetBanking(Integer userId, LoadMoneyNetBankingRequest request) {
		BigDecimal amount = MoneyUtil.requirePositive(request.amount());
		String idempotencyKey = newIdempotencyKey();

		// 1) Debit the bank via net banking FIRST. The amount is the validated body
		//    field — never a client-supplied balance.
		BankTransactionResult bankResult = bankClient.netbankingDebit(
				request.loginName(), request.password(), amount);
		requireBankSuccess(bankResult);

		// 2) Credit the wallet in its own committed transaction; compensate on failure.
		return creditWalletAfterBankDebit(userId, amount, bankResult, idempotencyKey,
				AmigoWalletConstants.TRANSACTION_INFO_MONEY_ADDED_FROM_BANK_TO_EWALLET_USING_NET_BANKING);
	}

	/**
	 * Credits the wallet after a confirmed bank debit. If the wallet-side persist
	 * throws, issues a compensating bank credit back to the debited account (when
	 * the bank returned account details) and surfaces an error.
	 */
	private MoneyTransactionResponse creditWalletAfterBankDebit(Integer userId, BigDecimal amount,
			BankTransactionResult bankResult, String idempotencyKey, String infoPrefix) {
		try {
			WalletLedgerDAO.LedgerEntry entry = walletLedger.credit(
					userId, amount,
					AmigoWalletConstants.PAYMENT_FROM_BANK.charAt(0),
					AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0),
					infoPrefix + " [bankTxnId=" + bankResult.bankTransactionId() + "]",
					0, AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));

			return MoneyTransactionResponse.withBankTxn(
					environment.getProperty("LoadMoneyAPI.LOAD_SUCCESS"),
					amount, entry.newBalance(), bankResult.bankTransactionId());
		} catch (RuntimeException walletFailure) {
			compensateBankDebit(bankResult, amount, idempotencyKey, walletFailure);
			throw new ApiException(HttpStatus.BAD_GATEWAY, "BankService.RECONCILIATION_REQUIRED");
		}
	}

	/**
	 * Best-effort compensation for a bank debit whose wallet credit failed: credit
	 * the amount back to the debited account when the bank returned its details;
	 * otherwise log a reconciliation error (the base contract returns no account,
	 * so an automated reversal is not always possible).
	 */
	private void compensateBankDebit(BankTransactionResult bankResult, BigDecimal amount, String idempotencyKey,
			RuntimeException cause) {
		logger.error("Wallet credit failed after bank debit {} ({}). Attempting compensation.",
				bankResult.bankTransactionId(), cause.getClass().getSimpleName());
		if (bankResult.accountNumber() != null && bankResult.ifsc() != null) {
			try {
				bankClient.creditAccount(bankResult.accountNumber(), bankResult.ifsc(), amount,
						"compensate-" + idempotencyKey);
				logger.error("Compensating bank credit issued for bank txn {}.", bankResult.bankTransactionId());
			} catch (RuntimeException compensationFailure) {
				logger.error("Compensating bank credit FAILED for bank txn {} — manual reconciliation required.",
						bankResult.bankTransactionId());
			}
		} else {
			logger.error("Bank txn {} debited but wallet not credited and no account details to auto-compensate "
					+ "— manual reconciliation required.", bankResult.bankTransactionId());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public MoneyTransactionResponse sendMoneyToBankAccount(Integer userId, BankTransferRequest request) {
		BigDecimal amount = MoneyUtil.requirePositive(request.amount());

		// 1) Verify the destination account first.
		BankVerifyResponse verification = bankClient.verifyAccount(
				request.accountNumber(), request.ifsc(), request.accountHolderName());
		if (verification == null || !verification.verified()) {
			throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "BankService.VERIFY_FAILED");
		}

		// 2) Lock + funds-check + debit the wallet FIRST (in this transaction).
		WalletLedgerDAO.LedgerEntry debit = walletLedger.debit(
				userId, amount,
				AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0),
				AmigoWalletConstants.PAYMENT_TO_BANK.charAt(0),
				AmigoWalletConstants.TRANSACTION_INFO_MONEY_SENT_TO_BANK_ACCOUNT_FROM_EWALLET,
				0);

		// 3) Credit the bank. If this fails it throws, rolling back the wallet debit
		//    (nothing has left the wallet).
		String idempotencyKey = newIdempotencyKey();
		BankTransactionResult bankResult = bankClient.creditAccount(
				request.accountNumber(), request.ifsc(), amount, idempotencyKey);
		requireBankSuccess(bankResult);

		// 4) Bind the bank txn id onto the (managed) ledger row; flushes on commit.
		debit.row().setInfo(debit.row().getInfo() + " [bankTxnId=" + bankResult.bankTransactionId() + "]");

		return MoneyTransactionResponse.withBankTxn(
				environment.getProperty("PayToBankAPI.PAY_TO_BANK_SUCCESS"),
				amount, debit.newBalance(), bankResult.bankTransactionId());
	}

	private void requireBankSuccess(BankTransactionResult result) {
		if (result == null || !result.isSuccess()) {
			throw new ApiException(HttpStatus.BAD_GATEWAY, "BankService.DEBIT_FAILED");
		}
	}

	private String newIdempotencyKey() {
		return UUID.randomUUID().toString();
	}
}
