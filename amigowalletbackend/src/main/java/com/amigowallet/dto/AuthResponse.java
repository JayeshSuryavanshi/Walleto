package com.amigowallet.dto;

public record AuthResponse(
		String accessToken,
		String tokenType,
		long expiresIn,
		UserProfileResponse user) {

	public AuthResponse(String accessToken, long expiresIn, UserProfileResponse user) {
		this(accessToken, "Bearer", expiresIn, user);
	}
}
