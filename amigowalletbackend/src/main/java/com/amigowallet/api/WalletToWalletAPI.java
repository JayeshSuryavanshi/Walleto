package com.amigowallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.dto.WalletTransferRequest;
import com.amigowallet.security.AuthUtil;
import com.amigowallet.service.WalletToWalletService;

import jakarta.validation.Valid;

/**
 * Wallet-to-wallet transfer. The SENDER is the authenticated principal (from the
 * JWT); only the recipient email + amount are supplied in a typed, validated body
 * (the legacy untyped positional {@code Object[]} has been removed).
 */
@RestController
@RequestMapping("WalletToWalletAPI")
public class WalletToWalletAPI {

	private static final Logger logger = LoggerFactory.getLogger(WalletToWalletAPI.class);

	private final WalletToWalletService walletTransferService;

	public WalletToWalletAPI(WalletToWalletService walletTransferService) {
		this.walletTransferService = walletTransferService;
	}

	@PostMapping("transfertowallet")
	public MoneyTransactionResponse transferToWallet(@Valid @RequestBody WalletTransferRequest request) {
		Integer senderUserId = AuthUtil.currentUserId();
		logger.info("Wallet-to-wallet transfer requested by userId {}", senderUserId);

		MoneyTransactionResponse response =
				walletTransferService.transferToWallet(senderUserId, request.amount(), request.recipientEmailId());

		logger.info("Wallet-to-wallet transfer completed for userId {}", senderUserId);
		return response;
	}
}
