package com.amigowallet.service;

import com.amigowallet.dto.SecurityQuestionResponse;

/**
 * Business logic for the password-recovery chain.
 *
 * <p>Hardened contract (Phase 3): no full-User disclosure, identity is always
 * derived server-side, and the actual password reset is gated by a short-lived
 * single-purpose token (minted only after the security answer is verified).
 *
 * @author ETA_JAVA
 */
public interface ForgotPasswordService {

	/**
	 * Returns the security question to present for a recovery attempt. Uses a
	 * non-enumerating strategy: an unknown email yields a plausible decoy question
	 * drawn from the same question set, so the response is indistinguishable from
	 * that of a real account.
	 */
	SecurityQuestionResponse getSecurityQuestion(String emailId) throws Exception;

	/**
	 * Verifies the security answer for the account identified by {@code emailId}
	 * (userId is resolved server-side, never trusted from the client). On success
	 * returns a short-lived password-reset token; on failure throws.
	 */
	String verifyAnswerAndCreateResetToken(String emailId, String securityAnswer) throws Exception;

	/**
	 * Resets the password for {@code userId} (derived from the reset token, never
	 * from the request body).
	 */
	void resetPassword(Integer userId, String newPassword) throws Exception;
}
