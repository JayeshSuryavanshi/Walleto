package com.edubank.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Legacy password encoder that reproduces the original edubank hashing
 * (unsalted, single-round SHA-256, lowercase hex). Used ONLY as the
 * default-for-matches encoder inside a {@link org.springframework.security.crypto.password.DelegatingPasswordEncoder}
 * so that pre-existing bare-hex hashes (seed data) still verify while all newly
 * encoded secrets use BCrypt. Comparison is constant-time.
 */
public class Sha256PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return sha256Hex(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (rawPassword == null || encodedPassword == null) {
			return false;
		}
		String computed = sha256Hex(rawPassword.toString());
		return MessageDigest.isEqual(
				computed.getBytes(StandardCharsets.UTF_8),
				encodedPassword.getBytes(StandardCharsets.UTF_8));
	}

	private static String sha256Hex(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
			return HexFormat.of().formatHex(digest);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 not available", e);
		}
	}
}
