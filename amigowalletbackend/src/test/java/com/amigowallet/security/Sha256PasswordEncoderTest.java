package com.amigowallet.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Legacy SHA-256 verification. The known hash is the seed value for user
 * {@code james@example.com} (plaintext {@code James#123}) from V3__demo_seed.sql —
 * a bare, unsalted, single-round SHA-256 hex digest.
 */
class Sha256PasswordEncoderTest {

	private static final String JAMES_HASH =
			"5228de85b31cf1a2529a12800b41b211bfbea9a464cdf5a07dac56f27a468996";

	private final Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();

	@Test
	void matchesKnownLegacySeedHash() {
		assertThat(encoder.matches("James#123", JAMES_HASH)).isTrue();
	}

	@Test
	void rejectsWrongPassword() {
		assertThat(encoder.matches("wrong-password", JAMES_HASH)).isFalse();
	}

	@Test
	void rejectsNullInputs() {
		assertThat(encoder.matches(null, JAMES_HASH)).isFalse();
		assertThat(encoder.matches("James#123", null)).isFalse();
	}

	@Test
	void encodeReproducesTheBareHexDigest() {
		assertThat(encoder.encode("James#123")).isEqualTo(JAMES_HASH);
	}
}
