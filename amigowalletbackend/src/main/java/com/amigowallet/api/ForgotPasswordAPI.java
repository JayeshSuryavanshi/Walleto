package com.amigowallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.ForgotPasswordRequest;
import com.amigowallet.dto.MessageResponse;
import com.amigowallet.dto.ResetPasswordRequest;
import com.amigowallet.dto.ResetTokenResponse;
import com.amigowallet.dto.SecurityQuestionResponse;
import com.amigowallet.dto.VerifyAnswerRequest;
import com.amigowallet.exception.ApiException;
import com.amigowallet.security.JwtService;
import com.amigowallet.service.ForgotPasswordService;

import jakarta.validation.Valid;

/**
 * Public (but token-gated where noted) password-recovery endpoints.
 *
 * <p>The chain is: forgotPassword (get question) -> validateAnswer (mint reset
 * token) -> resetPassword (consume the reset token from the Authorization
 * header). This closes the original unauthenticated account-takeover: the reset
 * is authorised only by a short-lived pwd_reset token, and the userId is derived
 * from that token, never from the body.
 *
 * @author ETA_JAVA
 */
@RestController
@RequestMapping("ForgotPasswordAPI")
public class ForgotPasswordAPI {

	private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordAPI.class);

	private final Environment environment;
	private final ForgotPasswordService forgotPasswordService;
	private final JwtService jwtService;

	public ForgotPasswordAPI(Environment environment, ForgotPasswordService forgotPasswordService,
			JwtService jwtService) {
		this.environment = environment;
		this.forgotPasswordService = forgotPasswordService;
		this.jwtService = jwtService;
	}

	/**
	 * Step 1: return the security question for the account (non-enumerating).
	 * Does NOT return the User, userId, password, or the security answer.
	 */
	@PostMapping("forgotPassword")
	public SecurityQuestionResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		logger.info("Password recovery: security question requested");
		try {
			return forgotPasswordService.getSecurityQuestion(request.emailId());
		} catch (ApiException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, null);
		}
	}

	/**
	 * Step 2: verify the security answer and, on success, return a short-lived
	 * reset token. Failures return a generic 403.
	 */
	@PostMapping("validateAnswer")
	public ResetTokenResponse validateAnswer(@Valid @RequestBody VerifyAnswerRequest request) {
		logger.info("Password recovery: security answer verification");
		try {
			String resetToken = forgotPasswordService.verifyAnswerAndCreateResetToken(
					request.emailId(), request.securityAnswer());
			return new ResetTokenResponse(resetToken, jwtService.getResetTtlSeconds());
		} catch (ApiException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ApiException(HttpStatus.FORBIDDEN, "ForgotPasswordService.INVALID_SECURITY_ANSWER");
		}
	}

	/**
	 * Step 3: reset the password. The reset token is read from the
	 * {@code Authorization: Bearer <token>} header; the userId is derived from it.
	 * Any userId in the body is ignored.
	 */
	@PostMapping("resetPassword")
	public MessageResponse resetPassword(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
			@Valid @RequestBody ResetPasswordRequest request) {

		if (!request.newPassword().equals(request.confirmNewPassword())) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "LoginValidator.CONFIRM_NEW_PASSWORD_MISSMATCH");
		}

		String resetToken = extractBearerToken(authorizationHeader);
		Integer userId = jwtService.parseResetToken(resetToken);

		try {
			forgotPasswordService.resetPassword(userId, request.newPassword());
		} catch (ApiException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		logger.info("Password reset completed for a user via reset token");
		return new MessageResponse(environment.getProperty("ForgotPasswordAPI.RESET_PASSWORD_SUCCESS"));
	}

	private String extractBearerToken(String authorizationHeader) {
		if (authorizationHeader == null) {
			throw new ApiException(HttpStatus.FORBIDDEN, "ForgotPasswordService.INVALID_RESET_TOKEN");
		}
		String trimmed = authorizationHeader.trim();
		if (trimmed.regionMatches(true, 0, "Bearer ", 0, 7)) {
			return trimmed.substring(7).trim();
		}
		return trimmed;
	}
}
