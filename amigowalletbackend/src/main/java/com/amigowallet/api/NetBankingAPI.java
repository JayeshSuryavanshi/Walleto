package com.amigowallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.LoadMoneyNetBankingRequest;
import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.security.AuthUtil;
import com.amigowallet.service.UserTransactionService;

import jakarta.validation.Valid;

/**
 * Net-banking load-money endpoint. The credited amount is an explicit validated
 * body field (NOT {@code user.getBalance()} as before), the userId comes from the
 * JWT, and the bank debit is performed server-to-server before the wallet credit.
 *
 * @author ETA_JAVA
 */
@RestController
@RequestMapping("NetBankingAPI")
public class NetBankingAPI {

	private static final Logger logger = LoggerFactory.getLogger(NetBankingAPI.class);

	private final UserTransactionService userTransactionService;

	public NetBankingAPI(UserTransactionService userTransactionService) {
		this.userTransactionService = userTransactionService;
	}

	@PostMapping("loadMoneyNetBanking")
	public MoneyTransactionResponse loadMoneyThroughNetBanking(
			@Valid @RequestBody LoadMoneyNetBankingRequest request) {
		Integer userId = AuthUtil.currentUserId();
		logger.info("Load-money via net banking requested by userId {}", userId);

		MoneyTransactionResponse response = userTransactionService.loadMoneyFromNetBanking(userId, request);

		logger.info("Load-money via net banking completed for userId {}", userId);
		return response;
	}
}
