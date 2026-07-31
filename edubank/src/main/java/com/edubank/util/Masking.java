package com.edubank.util;

/**
 * Masks sensitive identifiers (account / card numbers) to their last four
 * digits for logging and responses.
 */
public final class Masking {

	private Masking() {
	}

	public static String last4(String value) {
		if (value == null || value.isBlank()) {
			return "****";
		}
		String trimmed = value.trim();
		if (trimmed.length() <= 4) {
			return "****";
		}
		return "****" + trimmed.substring(trimmed.length() - 4);
	}
}
