package com.amigowallet.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Strongly-typed binding for the {@code app.*} configuration block
 * (JWT signing/TTL settings, the CORS allow-list, and the server-to-server
 * bank-api connection).
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Cors cors, Bank bank) {

	public record Jwt(String secret, long accessTtlSeconds, long resetTtlSeconds) {
	}

	public record Cors(List<String> allowedOrigins) {
	}

	/**
	 * Server-to-server bank-api connection. {@code baseUrl} is the bank-api root
	 * (e.g. {@code http://localhost:3331/EDUBank}); {@code apiKey} is sent as the
	 * {@code X-Service-Auth} header on every call.
	 */
	public record Bank(String baseUrl, String apiKey) {
	}
}
