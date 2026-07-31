package com.amigowallet.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Rejects any JWT whose {@code purpose} claim does not match the required value.
 * The resource-server decoder requires {@code purpose == "access"} so that a
 * short-lived password-reset token (purpose {@code "pwd_reset"}) can never be
 * presented as an access token, and vice-versa.
 */
public class PurposeValidator implements OAuth2TokenValidator<Jwt> {

	private final String requiredPurpose;

	public PurposeValidator(String requiredPurpose) {
		this.requiredPurpose = requiredPurpose;
	}

	@Override
	public OAuth2TokenValidatorResult validate(Jwt token) {
		String purpose = token.getClaimAsString("purpose");
		if (requiredPurpose.equals(purpose)) {
			return OAuth2TokenValidatorResult.success();
		}
		return OAuth2TokenValidatorResult.failure(
				new OAuth2Error("invalid_token", "The token purpose is not valid for this endpoint", null));
	}
}
