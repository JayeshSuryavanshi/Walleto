package com.amigowallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.AuthResponse;
import com.amigowallet.dto.ChangePasswordRequest;
import com.amigowallet.dto.LoginRequest;
import com.amigowallet.dto.MessageResponse;
import com.amigowallet.dto.UserProfileResponse;
import com.amigowallet.exception.ApiException;
import com.amigowallet.model.User;
import com.amigowallet.security.AuthUtil;
import com.amigowallet.security.JwtService;
import com.amigowallet.service.UserLoginService;

import jakarta.validation.Valid;

/**
 * Login, profile refresh, and change-password endpoints.
 * Identity for the authenticated endpoints is derived from the JWT, never from
 * the request body.
 *
 * @author ETA_JAVA
 */
@RestController
@RequestMapping("UserLoginAPI")
public class UserLoginAPI {

	private static final Logger logger = LoggerFactory.getLogger(UserLoginAPI.class);

	private final UserLoginService loginService;
	private final JwtService jwtService;

	public UserLoginAPI(UserLoginService loginService, JwtService jwtService) {
		this.loginService = loginService;
		this.jwtService = jwtService;
	}

	/**
	 * Public login. Returns an access JWT plus a safe profile projection.
	 * All failures collapse to a generic 401 so the response never reveals which
	 * field was wrong (no user enumeration).
	 */
	@PostMapping("authenticate")
	public AuthResponse authenticate(@Valid @RequestBody LoginRequest request) {
		logger.info("Authentication attempt received");

		User user = new User();
		user.setEmailId(request.emailId());
		user.setPassword(request.password());

		User authenticated;
		try {
			authenticated = loginService.authenticate(user);
		} catch (Exception e) {
			throw new ApiException(HttpStatus.UNAUTHORIZED, "LoginService.INVALID_CREDENTIALS");
		}

		String accessToken = jwtService.createAccessToken(
				authenticated.getUserId(), authenticated.getEmailId(), authenticated.getName());

		logger.info("Authentication successful for userId {}", authenticated.getUserId());

		return new AuthResponse(accessToken, jwtService.getAccessTtlSeconds(),
				UserProfileResponse.from(authenticated));
	}

	/**
	 * Authenticated profile refresh. Any body is ignored; the userId comes from
	 * the JWT.
	 */
	@PostMapping("getUser")
	public UserProfileResponse getUser() {
		Integer userId = AuthUtil.currentUserId();
		try {
			return UserProfileResponse.from(loginService.getUserbyUserId(userId));
		} catch (Exception e) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "LoginService.INVALID_CREDENTIALS");
		}
	}

	/**
	 * Authenticated change-password. userId comes from the JWT; the old password
	 * is verified and the new one stored as BCrypt.
	 */
	@PostMapping("customerChangePassword")
	public MessageResponse customerChangePassword(@Valid @RequestBody ChangePasswordRequest request) {
		Integer userId = AuthUtil.currentUserId();

		User user = new User();
		user.setUserId(userId);
		user.setPassword(request.password());
		user.setNewPassword(request.newPassword());
		user.setConfirmNewPassword(request.confirmNewPassword());

		try {
			loginService.changeUserPassword(user);
		} catch (ApiException ae) {
			throw ae;
		} catch (Exception e) {
			throw new ApiException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		logger.info("Password changed for userId {}", userId);
		return new MessageResponse("Password Successfully changed");
	}
}
