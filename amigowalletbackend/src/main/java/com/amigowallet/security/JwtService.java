package com.amigowallet.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.amigowallet.exception.ApiException;

/**
 * Mints and validates stateless HS256 JWTs.
 *
 * <ul>
 * <li><b>Access tokens</b> ({@code purpose="access"}) carry the authenticated
 * user's identity (sub = userId) and are consumed by the resource-server chain.</li>
 * <li><b>Reset tokens</b> ({@code purpose="pwd_reset"}) are short-lived and are
 * the ONLY thing that authorises a password reset. They are validated here with
 * a dedicated decoder that requires {@code purpose == "pwd_reset"} — the
 * resource-server decoder rejects them, so a reset token can never be used as an
 * access token.</li>
 * </ul>
 */
@Service
public class JwtService {

	private final JwtEncoder encoder;
	private final long accessTtlSeconds;
	private final long resetTtlSeconds;
	private final NimbusJwtDecoder resetTokenDecoder;

	public JwtService(JwtEncoder encoder, AppProperties properties) {
		this.encoder = encoder;
		this.accessTtlSeconds = properties.jwt().accessTtlSeconds();
		this.resetTtlSeconds = properties.jwt().resetTtlSeconds();

		SecretKeySpec key = new SecretKeySpec(
				properties.jwt().secret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key)
				.macAlgorithm(MacAlgorithm.HS256)
				.build();
		decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
				JwtValidators.createDefault(),
				new PurposeValidator("pwd_reset")));
		this.resetTokenDecoder = decoder;
	}

	public long getAccessTtlSeconds() {
		return accessTtlSeconds;
	}

	public long getResetTtlSeconds() {
		return resetTtlSeconds;
	}

	/**
	 * Creates an access token whose subject is the userId.
	 */
	public String createAccessToken(Integer userId, String email, String name) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(String.valueOf(userId))
				.issuedAt(now)
				.expiresAt(now.plusSeconds(accessTtlSeconds))
				.claim("email", email)
				.claim("name", name)
				.claim("purpose", "access")
				.build();
		return encode(claims);
	}

	/**
	 * Creates a short-lived single-purpose password-reset token bound to a userId.
	 */
	public String createResetToken(Integer userId) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(String.valueOf(userId))
				.issuedAt(now)
				.expiresAt(now.plusSeconds(resetTtlSeconds))
				.claim("purpose", "pwd_reset")
				.build();
		return encode(claims);
	}

	/**
	 * Validates a password-reset token (signature, expiry, and
	 * {@code purpose == "pwd_reset"}) and returns the userId it is bound to.
	 *
	 * @throws ApiException FORBIDDEN if the token is missing, malformed, expired,
	 *                      or not a reset token.
	 */
	public Integer parseResetToken(String token) {
		if (token == null || token.isBlank()) {
			throw new ApiException(HttpStatus.FORBIDDEN, "ForgotPasswordService.INVALID_RESET_TOKEN");
		}
		try {
			Jwt jwt = resetTokenDecoder.decode(token);
			return Integer.valueOf(jwt.getSubject());
		} catch (JwtException | NumberFormatException e) {
			throw new ApiException(HttpStatus.FORBIDDEN, "ForgotPasswordService.INVALID_RESET_TOKEN");
		}
	}

	private String encode(JwtClaimsSet claims) {
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}
}
