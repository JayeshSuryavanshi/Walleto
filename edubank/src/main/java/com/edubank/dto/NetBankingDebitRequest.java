package com.edubank.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NetBankingDebitRequest(
		@NotBlank(message = "is required") String loginName,
		@NotBlank(message = "is required") String password,
		@NotNull(message = "is required") @Positive(message = "must be greater than zero") BigDecimal amount) {
}
