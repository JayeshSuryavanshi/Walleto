package com.edubank.exception;

import org.springframework.http.HttpStatus;

/**
 * Domain exception carrying a machine-readable {@code code}, an HTTP status and
 * a safe, human-readable message. Never wraps or exposes stack traces to
 * callers.
 */
public class BankException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final HttpStatus status;
	private final String code;

	public BankException(HttpStatus status, String code, String message) {
		super(message);
		this.status = status;
		this.code = code;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getCode() {
		return code;
	}

	// --- factory helpers for the common banking failure modes ---

	public static BankException accountNotFound() {
		return new BankException(HttpStatus.NOT_FOUND, "ACCOUNT_NOT_FOUND", "Account not found");
	}

	public static BankException accountInactive() {
		return new BankException(HttpStatus.BAD_REQUEST, "ACCOUNT_INACTIVE", "Account is not active");
	}

	public static BankException invalidIfsc() {
		return new BankException(HttpStatus.BAD_REQUEST, "INVALID_IFSC", "IFSC does not match the account");
	}

	public static BankException invalidAccountHolderName() {
		return new BankException(HttpStatus.BAD_REQUEST, "INVALID_ACCOUNT_HOLDER_NAME",
				"Account holder name does not match");
	}

	public static BankException cardNotFound() {
		return new BankException(HttpStatus.NOT_FOUND, "CARD_NOT_FOUND", "Debit card not found");
	}

	public static BankException cardInactive() {
		return new BankException(HttpStatus.BAD_REQUEST, "CARD_INACTIVE", "Debit card is not active");
	}

	public static BankException cardLocked() {
		return new BankException(HttpStatus.BAD_REQUEST, "CARD_LOCKED", "Debit card is locked");
	}

	public static BankException cardExpired() {
		return new BankException(HttpStatus.BAD_REQUEST, "CARD_EXPIRED", "Debit card has expired");
	}

	public static BankException invalidExpiry() {
		return new BankException(HttpStatus.BAD_REQUEST, "INVALID_EXPIRY", "Card expiry does not match");
	}

	public static BankException invalidPin() {
		return new BankException(HttpStatus.UNAUTHORIZED, "INVALID_PIN", "Invalid card credentials");
	}

	public static BankException invalidCredentials() {
		return new BankException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Invalid net-banking credentials");
	}

	public static BankException accountLocked() {
		return new BankException(HttpStatus.BAD_REQUEST, "ACCOUNT_LOCKED", "Net-banking access is locked");
	}

	public static BankException insufficientFunds() {
		return new BankException(HttpStatus.UNPROCESSABLE_ENTITY, "INSUFFICIENT_FUNDS", "Insufficient funds");
	}

	public static BankException invalidAmount() {
		return new BankException(HttpStatus.BAD_REQUEST, "INVALID_AMOUNT",
				"Amount must be greater than zero with at most 4 decimal places");
	}
}
