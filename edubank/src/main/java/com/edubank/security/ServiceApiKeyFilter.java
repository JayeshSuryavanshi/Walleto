package com.edubank.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Service-to-service authentication filter. Requests carrying a valid
 * {@code X-Service-Auth} header (matching the configured shared key) are
 * authenticated with the {@code SERVICE} authority. Requests without a valid
 * key are left unauthenticated, so the authorization rules decide access
 * (public endpoints still work; {@code /api/**} is rejected by the entry point).
 */
public class ServiceApiKeyFilter extends OncePerRequestFilter {

	public static final String HEADER = "X-Service-Auth";

	private final byte[] expectedKey;

	public ServiceApiKeyFilter(String apiKey) {
		this.expectedKey = apiKey.getBytes(StandardCharsets.UTF_8);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String provided = request.getHeader(HEADER);
		if (provided != null && !provided.isBlank()
				&& MessageDigest.isEqual(expectedKey, provided.getBytes(StandardCharsets.UTF_8))
				&& SecurityContextHolder.getContext().getAuthentication() == null) {

			var authentication = new UsernamePasswordAuthenticationToken(
					"wallet-service", null, List.of(new SimpleGrantedAuthority("SERVICE")));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}
}
