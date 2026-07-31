package com.edubank.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edubank.dto.CardDebitRequest;
import com.edubank.dto.MoneyMoveResponse;
import com.edubank.service.BankService;

import jakarta.validation.Valid;

/**
 * Debit-card REST API (bank -> wallet load via card). Verifies the card and
 * debits the linked account in one atomic operation. CVV is never accepted.
 */
@RestController
@RequestMapping("/api/cards")
public class CardApiController {

	private final BankService bankService;

	public CardApiController(BankService bankService) {
		this.bankService = bankService;
	}

	@PostMapping("/debit")
	public ResponseEntity<MoneyMoveResponse> debit(@Valid @RequestBody CardDebitRequest request) {
		return ResponseEntity.ok(bankService.cardDebit(request));
	}
}
