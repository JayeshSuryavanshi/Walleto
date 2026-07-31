package com.amigowallet.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Legacy password encoder that verifies bare 64-hex, unsalted single-round
 * SHA-256 digests produced by the original application. It is wired as the
 * {@code DelegatingPasswordEncoder}'s default-for-matches so that pre-migration
 * hashes in the database still authenticate during the transition window.
 *
 * <p>{@link #encode(CharSequence)} returns the bare hex digest for completeness,
 * but it is <b>never</b> used to store new passwords (the delegating encoder
 * encodes with BCrypt). {@link #matches(CharSequence, String)} performs a
 * constant-time comparison and uses an explicit UTF-8 charset, fixing the two
 * defects of the original {@code HashingUtility}.
 */
public class Sha256PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return toHex(sha256(rawPassword));
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (rawPassword == null || encodedPassword == null) {
			return false;
		}
		String computed = toHex(sha256(rawPassword));
		return MessageDigest.isEqual(
				computed.getBytes(StandardCharsets.UTF_8),
				encodedPassword.getBytes(StandardCharsets.UTF_8));
	}

	private static byte[] sha256(CharSequence data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return md.digest(data.toString().getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			// SHA-256 is a mandated algorithm on every JVM; this cannot happen.
			throw new IllegalStateException("SHA-256 algorithm not available", e);
		}
	}

	private static String toHex(byte[] bytes) {
		StringBuilder hex = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			String h = Integer.toHexString(0xff & b);
			if (h.length() == 1) {
				hex.append('0');
			}
			hex.append(h);
		}
		return hex.toString();
	}
}
