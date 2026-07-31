package com.edubank.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Legacy SHA-256 verification. The known hash is the seed debit-card PIN
 * ({@code 1234}) from V2__seed.sql — a bare, unsalted, single-round SHA-256 hex
 * digest that must still verify via the DelegatingPasswordEncoder default-for-matches.
 */
class Sha256PasswordEncoderTest {

	private static final String PIN_1234_HASH =
			"03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";

	private final Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();

	@Test
	void matchesKnownLegacySeedHash() {
		assertThat(encoder.matches("1234", PIN_1234_HASH)).isTrue();
	}

	@Test
	void rejectsWrongSecret() {
		assertThat(encoder.matches("0000", PIN_1234_HASH)).isFalse();
	}

	@Test
	void rejectsNullInputs() {
		assertThat(encoder.matches(null, PIN_1234_HASH)).isFalse();
		assertThat(encoder.matches("1234", null)).isFalse();
	}

	@Test
	void encodeReproducesTheBareHexDigest() {
		assertThat(encoder.encode("1234")).isEqualTo(PIN_1234_HASH);
	}
}
