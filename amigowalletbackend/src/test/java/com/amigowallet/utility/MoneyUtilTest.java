package com.amigowallet.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.amigowallet.exception.ApiException;

/**
 * Positive-amount validation and BigDecimal exactness/scale guarantees for money.
 */
class MoneyUtilTest {

	@Test
	void requirePositive_rejectsNull() {
		assertThatThrownBy(() -> MoneyUtil.requirePositive(null))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> {
					ApiException ae = (ApiException) e;
					assertThat(ae.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
					assertThat(ae.getMessageKey()).isEqualTo("WalletService.INVALID_AMOUNT");
				});
	}

	@Test
	void requirePositive_rejectsZero() {
		assertThatThrownBy(() -> MoneyUtil.requirePositive(new BigDecimal("0.00")))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
	}

	@Test
	void requirePositive_rejectsNegative() {
		assertThatThrownBy(() -> MoneyUtil.requirePositive(new BigDecimal("-5.00")))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
	}

	@Test
	void requirePositive_rejectsOverPreciseScale() {
		assertThatThrownBy(() -> MoneyUtil.requirePositive(new BigDecimal("1.001")))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
	}

	@Test
	void requirePositive_acceptsAndNormalisesToScaleTwo() {
		BigDecimal result = MoneyUtil.requirePositive(new BigDecimal("10.5"));
		assertThat(result).isEqualByComparingTo("10.50");
		assertThat(result.scale()).isEqualTo(2);
	}

	@Test
	void bigDecimalArithmeticIsExactWithScaleTwo_noFloatDrift() {
		// Blueprint invariant: 3413.60 - 100.00 == 3313.60 at scale 2, no binary-float drift.
		BigDecimal difference = MoneyUtil.scale(new BigDecimal("3413.60").subtract(new BigDecimal("100.00")));
		assertThat(difference).isEqualTo(new BigDecimal("3313.60"));
		assertThat(difference.scale()).isEqualTo(2);

		// A value that a double cannot represent exactly stays exact as BigDecimal.
		BigDecimal sum = MoneyUtil.scale(new BigDecimal("0.10").add(new BigDecimal("0.20")));
		assertThat(sum).isEqualTo(new BigDecimal("0.30"));
	}
}
