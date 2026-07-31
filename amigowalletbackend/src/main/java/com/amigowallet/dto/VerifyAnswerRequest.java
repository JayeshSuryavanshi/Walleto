package com.amigowallet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyAnswerRequest(
		@NotBlank @Email String emailId,
		@NotBlank String securityAnswer) {
}
