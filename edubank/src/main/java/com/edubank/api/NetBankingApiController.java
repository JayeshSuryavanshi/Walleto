package com.edubank.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edubank.dto.MoneyMoveResponse;
import com.edubank.dto.NetBankingDebitRequest;
import com.edubank.service.BankService;

import jakarta.validation.Valid;

/**
 * Net-banking REST API (bank -> wallet load via net banking). Stateless,
 * credential-checked debit that replaces the entire JSP redirect flow (no
 * window.location, no open-redirect).
 */
@RestController
@RequestMapping("/api/netbanking")
public class NetBankingApiController {

	private final BankService bankService;

	public NetBankingApiController(BankService bankService) {
		this.bankService = bankService;
	}

	@PostMapping("/debit")
	public ResponseEntity<MoneyMoveResponse> debit(@Valid @RequestBody NetBankingDebitRequest request) {
		return ResponseEntity.ok(bankService.netBankingDebit(request));
	}
}
