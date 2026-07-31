package com.amigowallet.bank;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.amigowallet.exception.ApiException;
import com.amigowallet.security.AppProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Server-to-server client for the bank-api (edubank). wallet-api owns ALL bank
 * interaction; the browser never calls the bank directly. Every request carries
 * the {@code X-Service-Auth} shared-key header. Base URL and key come from
 * {@code app.bank.*} ({@link AppProperties.Bank}).
 *
 * <p>Never logs PANs, PINs, or credentials. On any transport / non-2xx failure a
 * typed {@link ApiException} (502 BAD_GATEWAY) is raised so callers can react and
 * transactional boundaries roll back cleanly.
 */
@Component
public class BankClient {

	private static final Logger logger = LoggerFactory.getLogger(BankClient.class);
	private static final String SERVICE_AUTH_HEADER = "X-Service-Auth";

	private final RestClient restClient;

	public BankClient(RestClient.Builder builder, AppProperties properties) {
		this.restClient = builder
				.baseUrl(properties.bank().baseUrl())
				.defaultHeader(SERVICE_AUTH_HEADER, properties.bank().apiKey())
				.defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	/** {@code POST /api/accounts/verify} — verify a destination bank account. */
	public BankVerifyResponse verifyAccount(String accountNumber, String ifsc, String accountHolderName) {
		return post("/api/accounts/verify",
				new VerifyRequest(accountNumber, ifsc, accountHolderName),
				BankVerifyResponse.class, "accounts/verify");
	}

	/** {@code POST /api/accounts/credit} — credit a bank account (wallet -> bank). */
	public BankTransactionResult creditAccount(String accountNumber, String ifsc, BigDecimal amount,
			String idempotencyKey) {
		return post("/api/accounts/credit",
				new CreditRequest(accountNumber, ifsc, amount, idempotencyKey),
				BankTransactionResult.class, "accounts/credit");
	}

	/** {@code POST /api/cards/debit} — debit a bank account via card (bank -> wallet). */
	public BankTransactionResult cardDebit(String cardNumber, String pin, LocalDate expiry, BigDecimal amount,
			String cardHolderName, String idempotencyKey) {
		return post("/api/cards/debit",
				new CardDebitRequest(cardNumber, pin, expiry, amount, cardHolderName, idempotencyKey),
				BankTransactionResult.class, "cards/debit");
	}

	/** {@code POST /api/netbanking/debit} — debit a bank account via net banking (bank -> wallet). */
	public BankTransactionResult netbankingDebit(String loginName, String password, BigDecimal amount) {
		return post("/api/netbanking/debit",
				new NetBankingDebitRequest(loginName, password, amount),
				BankTransactionResult.class, "netbanking/debit");
	}

	private <T> T post(String path, Object body, Class<T> responseType, String opName) {
		try {
			T response = restClient.post()
					.uri(path)
					.body(body)
					.retrieve()
					.body(responseType);
			if (response == null) {
				throw new ApiException(HttpStatus.BAD_GATEWAY, "BankService.UNAVAILABLE");
			}
			return response;
		} catch (RestClientException e) {
			// Message may contain the bank's error body; do not surface it to the client.
			logger.error("Bank call '{}' failed: {}", opName, e.getClass().getSimpleName());
			throw new ApiException(HttpStatus.BAD_GATEWAY, "BankService.UNAVAILABLE");
		}
	}

	// --- Wire request bodies (unknown fields tolerated by the bank; nulls omitted). ---

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private record VerifyRequest(String accountNumber, String ifsc, String accountHolderName) {
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private record CreditRequest(String accountNumber, String ifsc, BigDecimal amount, String idempotencyKey) {
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private record CardDebitRequest(String cardNumber, String pin, LocalDate expiry, BigDecimal amount,
			String cardHolderName, String idempotencyKey) {
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private record NetBankingDebitRequest(String loginName, String password, BigDecimal amount) {
	}
}
