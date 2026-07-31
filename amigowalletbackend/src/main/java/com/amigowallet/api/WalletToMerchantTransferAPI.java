package com.amigowallet.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.BillPaymentRequest;
import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.security.AuthUtil;
import com.amigowallet.service.BillPaymentService;

import jakarta.validation.Valid;

/**
 * Wallet-to-merchant (bill payment) endpoints. The payer is the authenticated
 * principal (the {@code {amount}/{userId}} path variables have been removed; the
 * amount is now a validated body field and the userId comes from the JWT).
 */
@RestController
@RequestMapping("WalletToMerchantTransferAPI")
public class WalletToMerchantTransferAPI {

	private static final Logger logger = LoggerFactory.getLogger(WalletToMerchantTransferAPI.class);

	private final BillPaymentService billPaymentService;

	public WalletToMerchantTransferAPI(BillPaymentService billPaymentService) {
		this.billPaymentService = billPaymentService;
	}

	@GetMapping("serviceType")
	public List<String> displayServiceType() {
		return billPaymentService.displayServiceType();
	}

	@PostMapping("merchantType")
	public List<String> displayMerchantName(@RequestBody String name) {
		return billPaymentService.displayMerchantName(name);
	}

	@PostMapping("payBill")
	public MoneyTransactionResponse payBill(@Valid @RequestBody BillPaymentRequest request) {
		Integer userId = AuthUtil.currentUserId();
		logger.info("Bill payment requested by userId {} to merchant {}", userId, request.merchantName());

		MoneyTransactionResponse response =
				billPaymentService.payBill(userId, request.amount(), request.merchantName());

		logger.info("Bill payment completed for userId {}", userId);
		return response;
	}
}
