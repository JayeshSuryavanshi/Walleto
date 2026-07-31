package com.amigowallet.dto;

import java.util.Collections;
import java.util.List;

import com.amigowallet.model.User;
import com.amigowallet.model.UserStatus;

/**
 * Public profile projection returned by the auth endpoints. Deliberately omits
 * the password, security answer, CVV, full card numbers, and the raw
 * transaction ledger.
 */
public record UserProfileResponse(
		Integer userId,
		String name,
		String emailId,
		String mobileNumber,
		UserStatus userStatus,
		Double balance,
		Integer rewardPoints,
		List<CardSummary> cards) {

	public static UserProfileResponse from(User user) {
		List<CardSummary> cards = user.getCards() == null
				? Collections.emptyList()
				: user.getCards().stream().map(CardSummary::from).toList();
		return new UserProfileResponse(
				user.getUserId(),
				user.getName(),
				user.getEmailId(),
				user.getMobileNumber(),
				user.getUserStatus(),
				user.getBalance(),
				user.getRewardPoints(),
				cards);
	}
}
