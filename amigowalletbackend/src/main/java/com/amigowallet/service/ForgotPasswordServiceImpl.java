package com.amigowallet.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.ForgotPasswordDAO;
import com.amigowallet.dao.RegistrationDAO;
import com.amigowallet.dto.SecurityQuestionResponse;
import com.amigowallet.exception.ApiException;
import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;
import com.amigowallet.security.JwtService;
import com.amigowallet.validator.UserLoginValidator;

/**
 * Hardened implementation of the password-recovery chain.
 *
 * @see ForgotPasswordService
 * @author ETA_JAVA
 */
@Service(value = "forgotPasswordService")
@Transactional
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

	private final ForgotPasswordDAO forgotPasswordDAO;
	private final RegistrationDAO registrationDAO;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public ForgotPasswordServiceImpl(ForgotPasswordDAO forgotPasswordDAO, RegistrationDAO registrationDAO,
			PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.forgotPasswordDAO = forgotPasswordDAO;
		this.registrationDAO = registrationDAO;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@Override
	@Transactional(readOnly = true)
	public SecurityQuestionResponse getSecurityQuestion(String emailId) throws Exception {

		User user = forgotPasswordDAO.authenticateEmailId(emailId);

		if (user != null && user.getSecurityQuestion() != null
				&& user.getSecurityQuestion().getQuestion() != null) {
			return new SecurityQuestionResponse(
					user.getSecurityQuestion().getQuestionId(),
					user.getSecurityQuestion().getQuestion());
		}

		/*
		 * Non-enumerating fallback: return a plausible decoy question drawn from the
		 * real question set, chosen deterministically from the email so that a given
		 * unknown email always maps to the same question. The HTTP status and body
		 * shape are identical to a known account, so existence cannot be inferred.
		 */
		return decoyQuestion(emailId);
	}

	@Override
	public String verifyAnswerAndCreateResetToken(String emailId, String securityAnswer) throws Exception {

		User account = forgotPasswordDAO.authenticateEmailId(emailId);
		if (account == null || account.getUserId() == null) {
			throw new ApiException(HttpStatus.FORBIDDEN, "ForgotPasswordService.INVALID_SECURITY_ANSWER");
		}

		User lookup = new User();
		lookup.setUserId(account.getUserId());
		User userFromDB = forgotPasswordDAO.validateSecurityAnswer(lookup);

		if (userFromDB == null || userFromDB.getSecurityAnswer() == null || securityAnswer == null
				|| !userFromDB.getSecurityAnswer().equalsIgnoreCase(securityAnswer)) {
			throw new ApiException(HttpStatus.FORBIDDEN, "ForgotPasswordService.INVALID_SECURITY_ANSWER");
		}

		/* Answer verified -> mint a short-lived reset token bound to this userId. */
		return jwtService.createResetToken(account.getUserId());
	}

	@Override
	public void resetPassword(Integer userId, String newPassword) throws Exception {

		/* Enforce the password-format policy before storing. */
		UserLoginValidator.validateResetPasswordDetails(newPassword);

		forgotPasswordDAO.resetPassword(userId, passwordEncoder.encode(newPassword));
	}

	private SecurityQuestionResponse decoyQuestion(String emailId) {
		List<SecurityQuestion> questions = registrationDAO.getAllSecurityQuestions();
		if (questions == null || questions.isEmpty()) {
			return new SecurityQuestionResponse(0, "Please contact support to recover your account.");
		}
		String key = emailId == null ? "" : emailId.toLowerCase();
		int index = Math.floorMod(key.hashCode(), questions.size());
		SecurityQuestion decoy = questions.get(index);
		return new SecurityQuestionResponse(decoy.getQuestionId(), decoy.getQuestion());
	}
}
