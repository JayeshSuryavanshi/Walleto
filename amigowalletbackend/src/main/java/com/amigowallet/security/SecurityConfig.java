package com.amigowallet.security;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Central Spring Security configuration for wallet-api.
 *
 * <p>Stateless, token-only. CSRF is disabled (there are no cookies/sessions);
 * CORS is centralised here (the per-controller {@code @CrossOrigin} annotations
 * have all been removed). Identity for protected endpoints comes exclusively
 * from the access JWT.
 *
 * <p>Matchers use the servlet path (the {@code /AmigoWallet} context path is
 * stripped by the container before Spring Security sees the request).
 */
@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class SecurityConfig {

	private static final String[] PUBLIC_PATHS = {
			"/UserLoginAPI/authenticate",
			"/RegistrationAPI/**",
			"/ForgotPasswordAPI/**",
			"/actuator/health/**",
			"/actuator/info",
			"/swagger-ui/**",
			"/swagger-ui.html",
			"/v3/api-docs/**"
	};

	private final AppProperties properties;

	public SecurityConfig(AppProperties properties) {
		this.properties = properties;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers(PUBLIC_PATHS).permitAll()
						.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2
						// The password-reset endpoint carries a pwd_reset token in the
						// Authorization header and validates it itself; the resource-server
						// decoder (access-only) must NOT attempt to authenticate it.
						.bearerTokenResolver(new ResetAwareBearerTokenResolver())
						.jwt(jwt -> jwt
								.decoder(jwtDecoder)
								.jwtAuthenticationConverter(jwtAuthenticationConverter())));
		return http.build();
	}

	/**
	 * Grants a single {@code SCOPE_access} authority and uses the {@code sub}
	 * claim (userId) as the principal name.
	 */
	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setPrincipalClaimName("sub");
		converter.setJwtGrantedAuthoritiesConverter(
				jwt -> List.<GrantedAuthority>of(new SimpleGrantedAuthority("SCOPE_access")));
		return converter;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(properties.cors().allowedOrigins());
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		config.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	private SecretKeySpec hmacKey() {
		return new SecretKeySpec(properties.jwt().secret().getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	}

	@Bean
	public JwtEncoder jwtEncoder() {
		return new NimbusJwtEncoder(new ImmutableSecret<SecurityContext>(hmacKey()));
	}

	/**
	 * Resource-server decoder: validates signature + expiry AND requires
	 * {@code purpose == "access"}, so reset tokens are rejected as access tokens.
	 */
	@Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(hmacKey())
				.macAlgorithm(MacAlgorithm.HS256)
				.build();
		decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
				JwtValidators.createDefault(),
				new PurposeValidator("access")));
		return decoder;
	}

	/**
	 * Passwords: new passwords are stored BCrypt(strength 12) with a
	 * {@code {bcrypt}} prefix; legacy bare-hex SHA-256 hashes still verify via the
	 * default-for-matches encoder and are re-encoded on successful login.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		String idForEncode = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder(12));
		DelegatingPasswordEncoder delegating = new DelegatingPasswordEncoder(idForEncode, encoders);
		delegating.setDefaultPasswordEncoderForMatches(new Sha256PasswordEncoder());
		return delegating;
	}

	/**
	 * Bearer-token resolver that declines to resolve a token for the
	 * password-reset endpoint (so that a pwd_reset token in the Authorization
	 * header does not trip the access-only resource-server chain). All other
	 * requests use the default resolution.
	 */
	static final class ResetAwareBearerTokenResolver
			implements org.springframework.security.oauth2.server.resource.web.BearerTokenResolver {

		private final org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver delegate =
				new org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver();

		@Override
		public String resolve(HttpServletRequest request) {
			String uri = request.getRequestURI();
			if (uri != null && uri.contains("/ForgotPasswordAPI/resetPassword")) {
				return null;
			}
			return delegate.resolve(request);
		}
	}
}
