package com.edubank.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountVerifyRequest(
		@NotBlank(message = "is required") String accountNumber,
		@NotBlank(message = "is required") String ifsc,
		@NotBlank(message = "is required") String accountHolderName) {
}
