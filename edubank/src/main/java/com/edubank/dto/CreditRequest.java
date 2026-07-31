package com.edubank.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreditRequest(
		@NotBlank(message = "is required") String accountNumber,
		@NotBlank(message = "is required") String ifsc,
		@NotNull(message = "is required") @Positive(message = "must be greater than zero") BigDecimal amount,
		String idempotencyKey) {
}
