package com.edubank.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edubank.dto.AccountVerifyRequest;
import com.edubank.dto.AccountVerifyResponse;
import com.edubank.dto.CreditRequest;
import com.edubank.dto.MoneyMoveResponse;
import com.edubank.service.BankService;

import jakarta.validation.Valid;

/**
 * Account REST API consumed server-to-server by wallet-api. Amounts are
 * {@link java.math.BigDecimal} in the JSON body (never in the URL path).
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountApiController {

	private final BankService bankService;

	public AccountApiController(BankService bankService) {
		this.bankService = bankService;
	}

	@PostMapping("/verify")
	public ResponseEntity<AccountVerifyResponse> verify(@Valid @RequestBody AccountVerifyRequest request) {
		return ResponseEntity.ok(bankService.verifyAccount(request));
	}

	@PostMapping("/credit")
	public ResponseEntity<MoneyMoveResponse> credit(@Valid @RequestBody CreditRequest request) {
		return ResponseEntity.ok(bankService.credit(request));
	}
}
