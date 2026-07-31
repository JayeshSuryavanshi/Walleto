package com.amigowallet.exception;

import java.time.OffsetDateTime;

/**
 * Uniform error body returned by {@link GlobalExceptionHandler}.
 * Never carries stack traces or raw exception messages.
 */
public record ApiError(
		OffsetDateTime timestamp,
		int status,
		String error,
		String message,
		String path) {
}
