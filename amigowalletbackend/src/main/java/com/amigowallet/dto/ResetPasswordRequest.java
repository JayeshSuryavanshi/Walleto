package com.amigowallet.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Reset payload. The reset token itself is NOT part of the body — it is read
 * from the {@code Authorization: Bearer <resetToken>} header and the userId is
 * derived from it server-side.
 */
public record ResetPasswordRequest(
		@NotBlank String newPassword,
		@NotBlank String confirmNewPassword) {
}
