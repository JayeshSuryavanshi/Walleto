package com.amigowallet.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.exception.ApiException;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.security.AuthUtil;
import com.amigowallet.service.TransactionHistoryService;

import org.springframework.http.HttpStatus;

/**
 * Transaction history. The user is resolved from the JWT (no body userId), so a
 * caller can only read their own history.
 *
 * @author KARAN RAJ SINGH
 */
@RestController
@RequestMapping("TransactionHistoryAPI")
public class TransactionHistoryAPI {

	private static final Logger logger = LoggerFactory.getLogger(TransactionHistoryAPI.class);

	private final TransactionHistoryService transactionHistoryService;

	public TransactionHistoryAPI(TransactionHistoryService transactionHistoryService) {
		this.transactionHistoryService = transactionHistoryService;
	}

	@PostMapping("getAllTransactions")
	public List<UserTransaction> getAllTransactions() {
		Integer userId = AuthUtil.currentUserId();
		logger.info("Transaction history requested by userId {}", userId);

		try {
			return transactionHistoryService.getAllTransactionByUserId(userId);
		} catch (ApiException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ApiException(HttpStatus.NOT_FOUND, "TransactionHistoryService.NO_TRANSACTIONS_FOUND");
		}
	}
}
