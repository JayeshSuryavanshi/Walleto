package com.amigowallet.utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.http.HttpStatus;

import com.amigowallet.exception.ApiException;

/**
 * Central money helpers. Money is {@link BigDecimal} with a fixed scale of 2 and
 * {@link RoundingMode#HALF_UP} wherever rounding is required. All amounts that
 * enter a money mutation must first pass {@link #requirePositive(BigDecimal)} so
 * that null, non-positive, or over-precise values are rejected uniformly (never
 * a checked-exception / return-code sentinel that {@code @Transactional} would
 * fail to roll back).
 */
public final class MoneyUtil {

	/** Fixed money scale (2 decimal places). */
	public static final int SCALE = 2;

	private MoneyUtil() {
	}

	/**
	 * Validates that {@code amount} is non-null, strictly positive, and carries at
	 * most 2 decimal places, and returns it normalised to scale 2.
	 *
	 * @throws ApiException 400 WalletService.INVALID_AMOUNT otherwise.
	 */
	public static BigDecimal requirePositive(BigDecimal amount) {
		if (amount == null || amount.scale() > SCALE || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ApiException(HttpStatus.BAD_REQUEST, "WalletService.INVALID_AMOUNT");
		}
		return amount.setScale(SCALE, RoundingMode.HALF_UP);
	}

	/** Normalises a computed amount to money scale (2dp, HALF_UP). */
	public static BigDecimal scale(BigDecimal amount) {
		return amount.setScale(SCALE, RoundingMode.HALF_UP);
	}
}
