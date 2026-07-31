package com.amigowallet.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.DeleteCardRequest;
import com.amigowallet.dto.LoadMoneyCardRequest;
import com.amigowallet.dto.MessageResponse;
import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;
import com.amigowallet.security.AuthUtil;
import com.amigowallet.service.DebitCardService;
import com.amigowallet.service.UserTransactionService;

import jakarta.validation.Valid;

/**
 * Debit-card endpoints. Every operation derives the acting user from the JWT
 * (the {@code {userId}} path variable and client-supplied card ownership have
 * been removed); load-money is orchestrated server-to-server against the bank.
 *
 * @author ETA_JAVA
 */
@RestController
@RequestMapping("DebitCardAPI")
public class DebitCardAPI {

	private static final Logger logger = LoggerFactory.getLogger(DebitCardAPI.class);

	private final Environment environment;
	private final DebitCardService debitcardService;
	private final UserTransactionService userTransactionService;

	public DebitCardAPI(Environment environment, DebitCardService debitcardService,
			UserTransactionService userTransactionService) {
		this.environment = environment;
		this.debitcardService = debitcardService;
		this.userTransactionService = userTransactionService;
	}

	@PostMapping("deleteCard")
	public MessageResponse deleteCard(@Valid @RequestBody DeleteCardRequest request) {
		Integer userId = AuthUtil.currentUserId();
		logger.info("Delete card requested by userId {} for cardId {}", userId, request.cardId());

		debitcardService.deleteCard(request.cardId(), userId);

		return new MessageResponse(environment.getProperty("DebitCardAPI.SUCCESSFULLY_DELETED"));
	}

	@PostMapping("addCard")
	public ResponseEntity<Card> addCard(@RequestBody Card card) {
		Integer userId = AuthUtil.currentUserId();
		logger.info("Add card requested by userId {}", userId);

		Card added = debitcardService.addCard(card, userId);

		// Return only non-sensitive fields (never echo the PAN or CVV back).
		Card response = new Card();
		response.setCardId(added.getCardId());
		response.setSuccessMessage(environment.getProperty("DebitCardAPI.SUCCESSFULLY_ADDED"));
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("fetchBankDetails")
	public List<Bank> fetchAllBankDetails() {
		return debitcardService.fetchAllBankDetails();
	}

	@PostMapping("loadMoneyDebitCard")
	public MoneyTransactionResponse loadMoneyFromDebitCard(@Valid @RequestBody LoadMoneyCardRequest request) {
		Integer userId = AuthUtil.currentUserId();
		logger.info("Load-money via debit card requested by userId {}", userId);

		MoneyTransactionResponse response = userTransactionService.loadMoneyFromDebitCard(userId, request);

		logger.info("Load-money via debit card completed for userId {}", userId);
		return response;
	}
}
