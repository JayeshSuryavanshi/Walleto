package com.amigowallet.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.UserLoginDAO;
import com.amigowallet.model.Card;
import com.amigowallet.model.CardStatus;
import com.amigowallet.model.TransactionStatus;
import com.amigowallet.model.User;
import com.amigowallet.model.UserStatus;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;
import com.amigowallet.validator.UserLoginValidator;

/**
 * Business logic for the Login module (authenticate, change password, fetch
 * profile). Passwords are handled exclusively via the Spring Security
 * {@link PasswordEncoder} (BCrypt for new hashes, SHA-256 legacy verification
 * with upgrade-on-login). {@code HashingUtility} is no longer used.
 *
 * @author ETA_JAVA
 */
@Service(value = "userLoginService")
@Transactional
public class UserLoginServiceImpl implements UserLoginService {

	private final UserLoginDAO userLoginDAO;
	private final PasswordEncoder passwordEncoder;

	public UserLoginServiceImpl(UserLoginDAO userLoginDAO, PasswordEncoder passwordEncoder) {
		this.userLoginDAO = userLoginDAO;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User authenticate(User user) throws Exception {

		/* Input-field format validation (email + password format). */
		UserLoginValidator.validateUserLogin(user);

		User userFromDao = userLoginDAO.getUserByEmailId(user.getEmailId());

		if (userFromDao == null) {
			throw new Exception("LoginService.INVALID_CREDENTIALS");
		}

		String storedHash = userFromDao.getPassword();
		if (storedHash == null || !passwordEncoder.matches(user.getPassword(), storedHash)) {
			throw new Exception("LoginService.INVALID_CREDENTIALS");
		}

		if (!UserStatus.ACTIVE.equals(userFromDao.getUserStatus())) {
			throw new Exception("UserLoginService.INACTIVE_USER");
		}

		/*
		 * Upgrade-on-login: if the stored hash is a legacy (non-bcrypt) encoding,
		 * transparently re-encode the just-verified password to BCrypt and persist.
		 */
		if (passwordEncoder.upgradeEncoding(storedHash)) {
			User upgrade = new User();
			upgrade.setUserId(userFromDao.getUserId());
			upgrade.setNewPassword(passwordEncoder.encode(user.getPassword()));
			userLoginDAO.changeUserPassword(upgrade);
		}

		/* Never return the password hash. */
		userFromDao.setPassword(null);

		filterInactiveCards(userFromDao);
		computeRewardPoints(userFromDao);

		return userFromDao;
	}

	@Override
	public void changeUserPassword(User user) throws Exception {

		UserLoginValidator.validateChangePasswordDetails(user);

		User userFromDao = userLoginDAO.getUserByUserId(user.getUserId());

		if (userFromDao == null) {
			throw new Exception("LoginService.USER_NOT_FOUND");
		}

		if (userFromDao.getPassword() == null
				|| !passwordEncoder.matches(user.getPassword(), userFromDao.getPassword())) {
			throw new Exception("LoginService.INVALID_PASSWORD");
		}

		/* Store the new password as a BCrypt hash. */
		user.setNewPassword(passwordEncoder.encode(user.getNewPassword()));

		userLoginDAO.changeUserPassword(user);
	}

	@Override
	public User getUserbyUserId(Integer userId) throws Exception {

		User userFromDao = userLoginDAO.getUserByUserId(userId);

		if (userFromDao == null) {
			throw new Exception("LoginService.INVALID_CREDENTIALS");
		}

		userFromDao.setPassword(null);

		filterInactiveCards(userFromDao);
		computeRewardPoints(userFromDao);

		return userFromDao;
	}

	/** Removes deactivated cards from the user's card list. */
	private void filterInactiveCards(User user) {
		List<Card> cards = user.getCards();
		if (cards == null) {
			return;
		}
		List<Card> cardsToBeRemoved = new ArrayList<>();
		for (Card card : cards) {
			if (CardStatus.INACTIVE.equals(card.getCardStatus())) {
				cardsToBeRemoved.add(card);
			}
		}
		if (cards.removeAll(cardsToBeRemoved)) {
			user.setCards(cards);
		}
	}

	/**
	 * Phase 4: the balance is now the authoritative {@code WALLET_USER.BALANCE}
	 * column (set by the DAO) — no longer folded from the ledger on every read.
	 * Only the non-redeemed reward-point total is still derived, and only over
	 * SUCCESS rows.
	 */
	private void computeRewardPoints(User user) {
		List<UserTransaction> transactions = user.getUserTransactions();
		int nonRedeemedPoints = 0;

		if (transactions != null) {
			for (UserTransaction userTransaction : transactions) {
				boolean success = TransactionStatus.SUCCESS.equals(userTransaction.getTransactionStatus());
				boolean notRedeemed = userTransaction.getIsRedeemed() != null
						&& AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO
								.equals(userTransaction.getIsRedeemed().toString());
				if (success && notRedeemed && userTransaction.getPointsEarned() != null) {
					nonRedeemedPoints += userTransaction.getPointsEarned();
				}
			}
		}

		user.setRewardPoints(nonRedeemedPoints);
	}
}
