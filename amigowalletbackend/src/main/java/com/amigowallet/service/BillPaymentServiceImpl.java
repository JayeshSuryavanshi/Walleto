package com.amigowallet.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amigowallet.dao.BillPaymentDAO;
import com.amigowallet.dao.WalletLedgerDAO;
import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.utility.AmigoWalletConstants;
import com.amigowallet.utility.MoneyUtil;

@Service(value = "billPaymentServiceImpl")
public class BillPaymentServiceImpl implements BillPaymentService {

	/** 1 reward point per this many currency units (floored, deterministic). */
	private static final BigDecimal POINTS_PER_UNIT = new BigDecimal("100");

	private final BillPaymentDAO billPaymentDAO;
	private final WalletLedgerDAO walletLedger;
	private final MoneyTxRunner moneyTxRunner;
	private final Environment environment;

	public BillPaymentServiceImpl(BillPaymentDAO billPaymentDAO, WalletLedgerDAO walletLedger,
			MoneyTxRunner moneyTxRunner, Environment environment) {
		this.billPaymentDAO = billPaymentDAO;
		this.walletLedger = walletLedger;
		this.moneyTxRunner = moneyTxRunner;
		this.environment = environment;
	}

	@Override
	public List<String> displayServiceType() {
		return billPaymentDAO.displayServiceType();
	}

	@Override
	public List<String> displayMerchantName(String type) {
		return billPaymentDAO.displayMerchantName(type);
	}

	@Override
	public MoneyTransactionResponse payBill(Integer userId, BigDecimal amount, String merchantName) {
		BigDecimal safeAmount = MoneyUtil.requirePositive(amount);

		/*
		 * Whole payment (locked funds check + wallet->merchant debit + merchant txn
		 * row) runs in one fresh transaction with a bounded retry on lock contention.
		 */
		return moneyTxRunner.runWithRetry(() -> {
			Integer merchantId = billPaymentDAO.findMerchantId(merchantName);

			int points = safeAmount.divide(POINTS_PER_UNIT, 0, RoundingMode.DOWN).intValue();

			/* Locked, funds-checked wallet -> merchant debit. */
			WalletLedgerDAO.LedgerEntry debit = walletLedger.debit(
					userId, safeAmount,
					AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0),
					AmigoWalletConstants.PAYMENT_TO_MERCHANT.charAt(0),
					AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_MERCHANTPAYMENT_DEBIT + merchantName,
					points);

			/* Merchant-side transaction row, in the same transaction as the debit. */
			billPaymentDAO.registerMerchantTransaction(merchantId, safeAmount,
					AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_MERCHANTPAYMENT_DEBIT + merchantName);

			String message = environment.getProperty("WalletToMerchantTransferAPI.SUCCESSFUL_TRANSACTION1")
					+ " " + points + " "
					+ environment.getProperty("WalletToMerchantTransferAPI.SUCCESSFUL_TRANSACTION2");

			return MoneyTransactionResponse.withPoints(message, safeAmount, debit.newBalance(), points);
		});
	}
}
