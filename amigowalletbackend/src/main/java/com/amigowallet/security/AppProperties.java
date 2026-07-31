package com.amigowallet.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Strongly-typed binding for the {@code app.*} configuration block
 * (JWT signing/TTL settings and the CORS allow-list).
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Cors cors) {

	public record Jwt(String secret, long accessTtlSeconds, long resetTtlSeconds) {
	}

	public record Cors(List<String> allowedOrigins) {
	}
}
