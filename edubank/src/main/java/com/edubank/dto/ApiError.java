package com.edubank.dto;

import java.time.OffsetDateTime;

/**
 * Structured JSON error body. Carries no stack traces or internal detail.
 */
public record ApiError(
		OffsetDateTime timestamp,
		int status,
		String error,
		String code,
		String message,
		String path) {

	public static ApiError of(int status, String error, String code, String message, String path) {
		return new ApiError(OffsetDateTime.now(), status, error, code, message, path);
	}
}
