package com.amigowallet.exception;

import org.springframework.http.HttpStatus;

/**
 * Application-level exception carrying an HTTP status and a message key.
 * The message key is resolved (null-safe) to a human message by
 * {@link GlobalExceptionHandler}. A {@code null} key yields a generic message.
 */
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final HttpStatus status;
	private final String messageKey;

	public ApiException(HttpStatus status, String messageKey) {
		super(messageKey);
		this.status = status;
		this.messageKey = messageKey;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getMessageKey() {
		return messageKey;
	}
}
