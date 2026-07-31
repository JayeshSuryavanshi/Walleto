package com.amigowallet.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.amigowallet.exception.ApiException;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;

/**
 * JWT minting + purpose isolation: an access token validates against the
 * access-only resource-server decoder, a reset token validates only via the
 * reset decoder, and neither can be presented as the other.
 */
class JwtServiceTest {

	private static final String SECRET = "test-secret-test-secret-test-secret-0123456789";
	private static final Integer USER_ID = 12121;

	private JwtService jwtService;
	private JwtDecoder accessDecoder;

	@BeforeEach
	void setUp() {
		SecretKeySpec key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		JwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(key));
		AppProperties properties = new AppProperties(
				new AppProperties.Jwt(SECRET, 1800L, 600L),
				new AppProperties.Cors(List.of("http://localhost:4200")),
				new AppProperties.Bank("http://localhost:3331/EDUBank", "dev-key"));
		jwtService = new JwtService(encoder, properties);

		// Mirror SecurityConfig.jwtDecoder(): access-only resource-server decoder.
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
		decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
				JwtValidators.createDefault(), new PurposeValidator("access")));
		accessDecoder = decoder;
	}

	@Test
	void accessToken_isValidatedByAccessDecoder_withSubjectAndPurpose() {
		String token = jwtService.createAccessToken(USER_ID, "james@walleto.app", "James Butt");

		Jwt decoded = accessDecoder.decode(token);

		assertThat(decoded.getSubject()).isEqualTo("12121");
		assertThat(decoded.getClaimAsString("purpose")).isEqualTo("access");
		assertThat(decoded.getClaimAsString("email")).isEqualTo("james@walleto.app");
	}

	@Test
	void resetToken_isParsedByResetDecoder_returningUserId() {
		String token = jwtService.createResetToken(USER_ID);
		assertThat(jwtService.parseResetToken(token)).isEqualTo(USER_ID);
	}

	@Test
	void resetToken_isRejectedByAccessDecoder() {
		String resetToken = jwtService.createResetToken(USER_ID);
		assertThatThrownBy(() -> accessDecoder.decode(resetToken)).isInstanceOf(JwtException.class);
	}

	@Test
	void accessToken_isRejectedByResetDecoder() {
		String accessToken = jwtService.createAccessToken(USER_ID, "james@walleto.app", "James Butt");
		assertThatThrownBy(() -> jwtService.parseResetToken(accessToken))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
	}

	@Test
	void blankResetToken_isRejected_403() {
		assertThatThrownBy(() -> jwtService.parseResetToken("  "))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.FORBIDDEN));
	}
}
