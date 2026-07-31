package com.amigowallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.BankTransferRequest;
import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.security.AuthUtil;
import com.amigowallet.service.UserTransactionService;

import jakarta.validation.Valid;

/**
 * Transfer money from the wallet to a bank account (withdraw). Orchestrated
 * server-to-server: verify destination, lock + funds-check + debit the wallet,
 * then credit the bank — all so that a failed bank leg rolls the debit back.
 *
 * <p>The mapping keeps the historical (misspelled) {@code BankTrasnferAPI} path
 * for frontend-contract continuity; the amount has moved out of the URL into the
 * validated JSON body and the userId now comes from the JWT.
 */
@RestController
@RequestMapping("BankTrasnferAPI")
public class BankTransferAPI {

	private static final Logger logger = LoggerFactory.getLogger(BankTransferAPI.class);

	private final UserTransactionService userTransactionService;

	public BankTransferAPI(UserTransactionService userTransactionService) {
		this.userTransactionService = userTransactionService;
	}

	@PostMapping("sendMoneyBankAccount")
	public MoneyTransactionResponse sendMoneyToBankAccount(@Valid @RequestBody BankTransferRequest request) {
		Integer userId = AuthUtil.currentUserId();
		logger.info("Transfer-to-bank requested by userId {}", userId);

		MoneyTransactionResponse response = userTransactionService.sendMoneyToBankAccount(userId, request);

		logger.info("Transfer-to-bank completed for userId {}", userId);
		return response;
	}
}
