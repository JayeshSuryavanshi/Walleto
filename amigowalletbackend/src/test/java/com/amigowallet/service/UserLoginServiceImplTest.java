package com.amigowallet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.amigowallet.dao.UserLoginDAO;
import com.amigowallet.model.User;
import com.amigowallet.model.UserStatus;
import com.amigowallet.security.Sha256PasswordEncoder;

/**
 * Password upgrade-on-login: a legacy bare SHA-256 hash still authenticates and
 * is transparently re-encoded to BCrypt; a hash that is already BCrypt is not
 * re-encoded. Uses the real production {@link DelegatingPasswordEncoder}.
 */
@ExtendWith(MockitoExtension.class)
class UserLoginServiceImplTest {

	private static final String LEGACY_SHA256 =
			"5228de85b31cf1a2529a12800b41b211bfbea9a464cdf5a07dac56f27a468996"; // sha256("James#123")

	@Mock
	private UserLoginDAO userLoginDAO;

	private PasswordEncoder passwordEncoder;
	private UserLoginServiceImpl service;

	@BeforeEach
	void setUp() {
		// Mirror SecurityConfig.passwordEncoder(): BCrypt for new hashes, SHA-256 legacy match.
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder(12));
		DelegatingPasswordEncoder delegating = new DelegatingPasswordEncoder("bcrypt", encoders);
		delegating.setDefaultPasswordEncoderForMatches(new Sha256PasswordEncoder());
		passwordEncoder = delegating;

		service = new UserLoginServiceImpl(userLoginDAO, passwordEncoder);
	}

	private User storedUser(String storedHash) {
		User u = new User();
		u.setUserId(12121);
		u.setEmailId("james@example.com");
		u.setName("James Butt");
		u.setPassword(storedHash);
		u.setUserStatus(UserStatus.ACTIVE);
		return u;
	}

	private User loginAttempt() {
		User u = new User();
		u.setEmailId("james@example.com");
		u.setPassword("James#123");
		return u;
	}

	@Test
	void authenticate_withLegacyHash_reEncodesToBcrypt() throws Exception {
		when(userLoginDAO.getUserByEmailId("james@example.com")).thenReturn(storedUser(LEGACY_SHA256));

		User result = service.authenticate(loginAttempt());

		assertThat(result).isNotNull();
		assertThat(result.getPassword()).isNull(); // hash never returned to the caller

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(userLoginDAO).changeUserPassword(captor.capture());
		String reEncoded = captor.getValue().getNewPassword();
		assertThat(reEncoded).startsWith("{bcrypt}");
		assertThat(passwordEncoder.matches("James#123", reEncoded)).isTrue();
	}

	@Test
	void authenticate_withBcryptHash_doesNotReEncode() throws Exception {
		String bcrypt = passwordEncoder.encode("James#123"); // "{bcrypt}$2a$12$..."
		when(userLoginDAO.getUserByEmailId("james@example.com")).thenReturn(storedUser(bcrypt));

		User result = service.authenticate(loginAttempt());

		assertThat(result).isNotNull();
		verify(userLoginDAO, never()).changeUserPassword(any());
	}

	@Test
	void authenticate_withWrongPassword_isRejected() throws Exception {
		when(userLoginDAO.getUserByEmailId("james@example.com")).thenReturn(storedUser(LEGACY_SHA256));

		User attempt = new User();
		attempt.setEmailId("james@example.com");
		attempt.setPassword("Wrong#999");

		assertThatThrownBy(() -> service.authenticate(attempt)).isInstanceOf(Exception.class);
		verify(userLoginDAO, never()).changeUserPassword(any());
	}
}
