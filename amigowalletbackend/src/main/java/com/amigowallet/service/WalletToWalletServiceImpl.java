package com.amigowallet.service;

import java.math.BigDecimal;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amigowallet.dao.WalletToWalletDAO;
import com.amigowallet.dto.MoneyTransactionResponse;

@Service(value = "walletTransferService")
public class WalletToWalletServiceImpl implements WalletToWalletService {

	private final WalletToWalletDAO walletTransferDAO;
	private final MoneyTxRunner moneyTxRunner;
	private final Environment environment;

	public WalletToWalletServiceImpl(WalletToWalletDAO walletTransferDAO, MoneyTxRunner moneyTxRunner,
			Environment environment) {
		this.walletTransferDAO = walletTransferDAO;
		this.moneyTxRunner = moneyTxRunner;
		this.environment = environment;
	}

	/**
	 * Runs the transfer in a fresh transaction with a bounded retry on lock
	 * contention: the DAO performs the locked funds check, sender debit, receiver
	 * credit, deterministic points, and capped cashback. A loser in a concurrent
	 * race re-reads the (now-drained) balance in a fresh attempt and ends with a
	 * clean 422 INSUFFICIENT_BALANCE; genuinely persistent contention becomes 409.
	 */
	@Override
	public MoneyTransactionResponse transferToWallet(Integer senderUserId, BigDecimal amount, String recipientEmailId) {
		MoneyTransactionResponse result = moneyTxRunner.runWithRetry(
				() -> walletTransferDAO.transferToWallet(senderUserId, amount, recipientEmailId));
		return result.withMessage(environment.getProperty("WalletAPI.SUCCESSFUL_TRANSACTION"));
	}
}
