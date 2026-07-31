package com.amigowallet.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Registration payload. Preserves the original nested
 * {@code securityQuestion: { questionId }} shape the flow reads.
 */
public record RegisterRequest(
		@NotBlank String name,
		@NotBlank @Email String emailId,
		@NotBlank String mobileNumber,
		@NotBlank String password,
		@NotNull @Valid SecurityQuestionRef securityQuestion,
		@NotBlank String securityAnswer) {

	public record SecurityQuestionRef(@NotNull Integer questionId) {
	}
}
