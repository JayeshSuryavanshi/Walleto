package com.amigowallet.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Delete-saved-card request. Ownership of the card is verified against the
 * authenticated principal before deletion.
 */
public record DeleteCardRequest(
		@NotNull Integer cardId) {
}
