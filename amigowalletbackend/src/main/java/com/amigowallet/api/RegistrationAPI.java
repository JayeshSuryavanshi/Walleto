package com.amigowallet.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.MessageResponse;
import com.amigowallet.dto.RegisterRequest;
import com.amigowallet.dto.RegisterResponse;
import com.amigowallet.dto.SecurityQuestionResponse;
import com.amigowallet.exception.ApiException;
import com.amigowallet.model.SecurityQuestion;
import com.amigowallet.model.User;
import com.amigowallet.service.RegistrationService;

import jakarta.validation.Valid;

/**
 * Public registration endpoints (validate, list security questions, register).
 *
 * @author ETA_JAVA
 */
@RestController
@RequestMapping("RegistrationAPI")
public class RegistrationAPI {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationAPI.class);

	private final Environment environment;
	private final RegistrationService registrationService;

	public RegistrationAPI(Environment environment, RegistrationService registrationService) {
		this.environment = environment;
		this.registrationService = registrationService;
	}

	@PostMapping("validateForRegistration")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public MessageResponse validateForRegistration(@Valid @RequestBody RegisterRequest request) {
		logger.info("Validating registration details");
		try {
			registrationService.validateUser(toUser(request));
		} catch (Exception e) {
			throw mapRegistrationException(e);
		}
		return new MessageResponse(environment.getProperty("RegistrationAPI.SUCCESSFULLY_VALIDATED"));
	}

	@GetMapping("getAllQuestions")
	public List<SecurityQuestionResponse> getAllQuestions() {
		logger.info("Fetching all security questions");
		return registrationService.getAllSecurityQuestions().stream()
				.map(q -> new SecurityQuestionResponse(q.getQuestionId(), q.getQuestion()))
				.toList();
	}

	@PostMapping("register")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
		try {
			User user = toUser(request);
			registrationService.validateUser(user);
			Integer registrationId = registrationService.registerUser(user);
			logger.info("User registered successfully with id {}", registrationId);
			return new RegisterResponse(registrationId,
					environment.getProperty("RegistrationAPI.SUCCESSFUL_REGISTRATION") + registrationId);
		} catch (ApiException ae) {
			throw ae;
		} catch (Exception e) {
			throw mapRegistrationException(e);
		}
	}

	private User toUser(RegisterRequest request) {
		User user = new User();
		user.setName(request.name());
		user.setEmailId(request.emailId());
		user.setMobileNumber(request.mobileNumber());
		user.setPassword(request.password());
		user.setSecurityAnswer(request.securityAnswer());

		SecurityQuestion securityQuestion = new SecurityQuestion();
		securityQuestion.setQuestionId(request.securityQuestion().questionId());
		user.setSecurityQuestion(securityQuestion);

		return user;
	}

	/**
	 * Preserves the original status semantics: validator failures -> 406
	 * NOT_ACCEPTABLE, everything else (e.g. email/mobile already present) -> 409
	 * CONFLICT.
	 */
	private ApiException mapRegistrationException(Exception e) {
		String key = e.getMessage();
		if (key != null && key.contains("Validator")) {
			return new ApiException(HttpStatus.NOT_ACCEPTABLE, key);
		}
		return new ApiException(HttpStatus.CONFLICT, key);
	}
}
