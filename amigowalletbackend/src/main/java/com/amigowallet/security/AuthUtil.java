package com.amigowallet.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.amigowallet.exception.ApiException;

/**
 * Helper for deriving the caller's identity from the authenticated JWT.
 * Protected endpoints MUST obtain the userId from here (the token subject),
 * never from the request body or path.
 */
public final class AuthUtil {

	private AuthUtil() {
	}

	/**
	 * @return the authenticated user's id (the JWT {@code sub} claim).
	 * @throws ApiException UNAUTHORIZED if there is no authenticated JWT.
	 */
	public static Integer currentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
			try {
				return Integer.valueOf(jwt.getSubject());
			} catch (NumberFormatException e) {
				throw new ApiException(HttpStatus.UNAUTHORIZED, null);
			}
		}
		throw new ApiException(HttpStatus.UNAUTHORIZED, null);
	}
}
