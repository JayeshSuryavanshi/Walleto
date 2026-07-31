package com.edubank.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Stateless service-to-service security. No sessions, no CSRF (token auth), no
 * browser CORS ({@code /api/**} is called only server-to-server by wallet-api).
 * Only health/info/OpenAPI endpoints are public; every {@code /api/**} call
 * must present a valid {@code X-Service-Auth} key.
 */
@Configuration
public class SecurityConfig {

	private final String serviceApiKey;

	public SecurityConfig(@Value("${bank.service.api-key}") String serviceApiKey) {
		this.serviceApiKey = serviceApiKey;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, ObjectMapper objectMapper) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.cors(AbstractHttpConfigurer::disable)
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/actuator/health/**",
								"/actuator/info",
								"/swagger-ui.html",
								"/swagger-ui/**",
								"/v3/api-docs/**")
						.permitAll()
						.requestMatchers("/api/**").authenticated()
						.anyRequest().denyAll())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(new JsonAuthenticationEntryPoint(objectMapper)))
				.addFilterBefore(new ServiceApiKeyFilter(serviceApiKey),
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * BCrypt (strength 12) for all newly encoded secrets, with a legacy SHA-256
	 * fallback so pre-existing bare-hex hashes still verify (transition window).
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder(12));
		encoders.put("sha256", new Sha256PasswordEncoder());
		DelegatingPasswordEncoder delegating = new DelegatingPasswordEncoder(encodingId, encoders);
		delegating.setDefaultPasswordEncoderForMatches(new Sha256PasswordEncoder());
		return delegating;
	}
}
