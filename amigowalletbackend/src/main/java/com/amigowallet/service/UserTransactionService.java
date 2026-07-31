package com.amigowallet.service;

import com.amigowallet.dto.BankTransferRequest;
import com.amigowallet.dto.LoadMoneyCardRequest;
import com.amigowallet.dto.LoadMoneyNetBankingRequest;
import com.amigowallet.dto.MoneyTransactionResponse;

/**
 * Money movement between the wallet and the bank. All bank interaction is
 * server-to-server (via {@code BankClient}); the browser never contacts the bank.
 * Each method returns only after the bank leg is confirmed, and either commits
 * the wallet side atomically or compensates.
 */
public interface UserTransactionService {

	/**
	 * Load money via debit card (bank -&gt; wallet): debit the card at the bank
	 * FIRST, then credit the wallet; compensate the bank on wallet-credit failure.
	 */
	MoneyTransactionResponse loadMoneyFromDebitCard(Integer userId, LoadMoneyCardRequest request);

	/**
	 * Load money via net banking (bank -&gt; wallet): debit at the bank FIRST, then
	 * credit the wallet; compensate the bank on wallet-credit failure.
	 */
	MoneyTransactionResponse loadMoneyFromNetBanking(Integer userId, LoadMoneyNetBankingRequest request);

	/**
	 * Transfer to bank / withdraw (wallet -&gt; bank): verify the destination, then
	 * in one transaction lock + funds-check + debit the wallet FIRST and credit the
	 * bank; if the bank call fails, the wallet debit rolls back (nothing leaves).
	 */
	MoneyTransactionResponse sendMoneyToBankAccount(Integer userId, BankTransferRequest request);
}
