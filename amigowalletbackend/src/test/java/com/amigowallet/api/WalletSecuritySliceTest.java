package com.amigowallet.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.amigowallet.model.User;
import com.amigowallet.model.UserStatus;
import com.amigowallet.security.JwtService;
import com.amigowallet.security.SecurityConfig;
import com.amigowallet.service.TransactionHistoryService;
import com.amigowallet.service.UserLoginService;
import com.amigowallet.service.WalletToWalletService;

/**
 * Security web slice exercising the real {@link SecurityConfig} filter chain and
 * the {@link com.amigowallet.exception.GlobalExceptionHandler}:
 * <ul>
 * <li>a public endpoint (authenticate) is reachable without a token,</li>
 * <li>a protected endpoint is 401 without a token and 200 with a valid one,</li>
 * <li>a ConcurrencyFailureException maps to 409,</li>
 * <li>a malformed JSON body maps to 400.</li>
 * </ul>
 */
@WebMvcTest(controllers = { UserLoginAPI.class, TransactionHistoryAPI.class, WalletToWalletAPI.class },
		properties = {
				"app.jwt.secret=test-secret-test-secret-test-secret-0123456789",
				"app.jwt.access-ttl-seconds=1800",
				"app.jwt.reset-ttl-seconds=600",
				"app.cors.allowed-origins=http://localhost:4200",
				"app.bank.base-url=http://localhost:3331/EDUBank",
				"app.bank.api-key=test-key"
		})
@Import({ SecurityConfig.class, JwtService.class })
class WalletSecuritySliceTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JwtService jwtService;

	@MockBean
	private UserLoginService userLoginService;

	@MockBean
	private TransactionHistoryService transactionHistoryService;

	@MockBean
	private WalletToWalletService walletToWalletService;

	private String accessToken() {
		return jwtService.createAccessToken(42, "james@walleto.app", "James Butt");
	}

	@Test
	void publicAuthenticate_isReachableWithoutToken() throws Exception {
		User authenticated = new User();
		authenticated.setUserId(42);
		authenticated.setEmailId("james@walleto.app");
		authenticated.setName("James Butt");
		authenticated.setUserStatus(UserStatus.ACTIVE);
		when(userLoginService.authenticate(any())).thenReturn(authenticated);

		mvc.perform(post("/UserLoginAPI/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"emailId\":\"james@walleto.app\",\"password\":\"James#123\"}"))
				.andExpect(status().isOk());
	}

	@Test
	void protectedEndpoint_returns401WithoutToken() throws Exception {
		mvc.perform(post("/TransactionHistoryAPI/getAllTransactions"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void protectedEndpoint_returns200WithValidToken() throws Exception {
		when(transactionHistoryService.getAllTransactionByUserId(42)).thenReturn(List.of());

		mvc.perform(post("/TransactionHistoryAPI/getAllTransactions")
				.header("Authorization", "Bearer " + accessToken()))
				.andExpect(status().isOk());
	}

	@Test
	void malformedJsonBody_returns400() throws Exception {
		mvc.perform(post("/UserLoginAPI/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ this is not valid json"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void concurrencyFailure_maps409() throws Exception {
		when(walletToWalletService.transferToWallet(any(), any(), any()))
				.thenThrow(new ConcurrencyFailureException("row locked"));

		mvc.perform(post("/WalletToWalletAPI/transfertowallet")
				.header("Authorization", "Bearer " + accessToken())
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"recipientEmailId\":\"receiver@walleto.app\",\"amount\":100.00}"))
				.andExpect(status().isConflict());
	}
}
