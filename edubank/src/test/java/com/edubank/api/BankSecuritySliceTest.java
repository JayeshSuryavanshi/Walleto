package com.edubank.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.edubank.dto.AccountVerifyResponse;
import com.edubank.security.SecurityConfig;
import com.edubank.security.ServiceApiKeyFilter;
import com.edubank.service.BankService;

/**
 * Security web slice exercising the real service-to-service filter chain:
 * {@code /api/**} requires a valid {@code X-Service-Auth} key (401 otherwise),
 * and the actuator health endpoint is public.
 */
@WebMvcTest(controllers = AccountApiController.class,
		properties = "bank.service.api-key=test-service-key")
@Import(SecurityConfig.class)
class BankSecuritySliceTest {

	private static final String VERIFY_BODY =
			"{\"accountNumber\":\"ACC1\",\"ifsc\":\"EDUB0001\",\"accountHolderName\":\"Martin\"}";

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BankService bankService;

	@Test
	void apiEndpoint_returns401WithoutServiceAuthHeader() throws Exception {
		mvc.perform(post("/api/accounts/verify")
				.contentType(MediaType.APPLICATION_JSON)
				.content(VERIFY_BODY))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void apiEndpoint_returns401WithWrongServiceAuthKey() throws Exception {
		mvc.perform(post("/api/accounts/verify")
				.header(ServiceApiKeyFilter.HEADER, "wrong-key")
				.contentType(MediaType.APPLICATION_JSON)
				.content(VERIFY_BODY))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void apiEndpoint_passesWithCorrectServiceAuthKey() throws Exception {
		when(bankService.verifyAccount(any())).thenReturn(new AccountVerifyResponse(true, "Martin"));

		mvc.perform(post("/api/accounts/verify")
				.header(ServiceApiKeyFilter.HEADER, "test-service-key")
				.contentType(MediaType.APPLICATION_JSON)
				.content(VERIFY_BODY))
				.andExpect(status().isOk());
	}

	@Test
	void actuatorHealth_isPublic() throws Exception {
		// With no X-Service-Auth header the security chain must NOT reject the health
		// path. The actuator endpoint itself is not wired into a web slice, so the
		// request passes authentication and then falls through to the missing handler
		// (not a 401/403) — which is exactly what "public" means here.
		int status = mvc.perform(get("/actuator/health")).andReturn().getResponse().getStatus();
		assertThat(status).isNotIn(401, 403);
	}
}
