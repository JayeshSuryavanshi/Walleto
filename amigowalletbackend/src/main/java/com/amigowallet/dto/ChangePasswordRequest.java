package com.amigowallet.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
		@NotBlank String password,
		@NotBlank String newPassword,
		@NotBlank String confirmNewPassword) {
}
