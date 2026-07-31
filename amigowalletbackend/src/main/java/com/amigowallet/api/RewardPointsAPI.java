package com.amigowallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.security.AuthUtil;
import com.amigowallet.service.RewardPointsService;

/**
 * Reward-points redemption endpoint. The acting user comes from the JWT (no body
 * userId), preventing redemption of another user's points.
 *
 * @author ETA_JAVA
 */
@RestController
@RequestMapping("RewardPointsAPI")
public class RewardPointsAPI {

	private static final Logger logger = LoggerFactory.getLogger(RewardPointsAPI.class);

	private final RewardPointsService rewardPointsService;

	public RewardPointsAPI(RewardPointsService rewardPointsService) {
		this.rewardPointsService = rewardPointsService;
	}

	@PostMapping("redeemRewardPoints")
	public MoneyTransactionResponse redeemRewardPoints() {
		Integer userId = AuthUtil.currentUserId();
		logger.info("Redeem reward points requested by userId {}", userId);

		MoneyTransactionResponse response = rewardPointsService.redeemRewardPoints(userId);

		logger.info("Redeem reward points completed for userId {}", userId);
		return response;
	}
}
