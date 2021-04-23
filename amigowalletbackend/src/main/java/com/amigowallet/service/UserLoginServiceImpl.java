package com.amigowallet.service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.UserLoginDAO;
import com.amigowallet.model.Card;
import com.amigowallet.model.CardStatus;
import com.amigowallet.model.User;
import com.amigowallet.model.UserStatus;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;
import com.amigowallet.utility.HashingUtility;
import com.amigowallet.validator.UserLoginValidator;

/**
 * This class contains the methods for business logics related to Login
 * module, like authenticate, change user password, get user by userId.
 * 
 * @author ETA_JAVA
 *
 */
@Service(value = "userLoginService")
@Transactional
public class UserLoginServiceImpl implements UserLoginService {

	@Autowired
	private UserLoginDAO userLoginDAO;

	/**
	 * This method is used to authenticate the login credentials entered by
	 * user. It checks the loginName and password with input field validation,
	 * if valid it verifies the credential with the database. If verified, it
	 * checks whether the account is locked.
	 * 
	 * @param user
	 *           
	 * @return user object if the credentials are valid and the account is not
	 *         locked
	 *         
	 * @throws Exception
	 *             if the loginName is not matching any record in the database
	 *             or the password entered is incorrect with respect to the
	 *             loginName.
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 */
	@Override
	public User authenticate(User user) throws Exception {

		/*
		 * here we are validating user by passing user object as argument to
		 * validateUserLogin method of UserLoginValidator if user is not valid
		 * corresponding exception will be thrown
		 */
		UserLoginValidator.validateUserLogin(user);

		/*
		 * here we are retrieving user by passing emialId as argument to
		 * getUserByEmailId method of UserLoginDAO.
		 */
		User userFromDao = userLoginDAO.getUserByEmailId(user.getEmailId());

		/*
		 * here if no user exist for the email id, exception is thrown with
		 * message LoginService.INVALID_CREDENTIALS
		 */
		if (userFromDao == null) {
			throw new Exception(
					"LoginService.INVALID_CREDENTIALS");
		}

		/*
		 * here we are generating the hash value for the password by passing
		 * password as argument to getHashValue method of HashingUtility
		 */
		String hashedPassword = HashingUtility.getHashValue(user.getPassword());

		/*
		 * if hash value of hash value of password does not matches exception is
		 * thrown with message "LoginService.INVALID_CREDENTIALS"
		 */
		if (!hashedPassword.equals(userFromDao.getPassword())) {
			throw new Exception(
					"LoginService.INVALID_CREDENTIALS");
		}
		
		/*
		 * Checking if the user is not active then throwing the Exception
		 */
		if(!userFromDao.getUserStatus().equals(UserStatus.ACTIVE))
		{
			throw new Exception("UserLoginService.INACTIVE_USER");
		}
		
		userFromDao.setPassword(null);

		/* Filtering the user cards */
		/*
		 * Removing the deleted/deactive cards
		 */
		List<Card> cards = userFromDao.getCards();

		List<Card> cardsToBeRemoved = new ArrayList<Card>();

		/*
		 * here if card status is deactive then then it is added to the cards to
		 * be removed list
		 */
		if (cards != null) {
			for (Card card : cards) {
				if (CardStatus.INACTIVE.equals(card.getCardStatus())) {
					cardsToBeRemoved.add(card);
				}
			}
			if (cards.removeAll(cardsToBeRemoved)) {
				userFromDao.setCards(cards);
			}
		}

		/* Calculating Balance and Reward points */

		/* here we are receiving all the transaction for the user */
		List<UserTransaction> transactions = userFromDao.getUserTransactions();

		Double balance = 0.0;
		Integer nonRedeemedPoints = 0;

		if (transactions != null) {

			/*
			 * here we are calculating the balance and award points for the user
			 */
			for (UserTransaction userTransaction : transactions) {
				
				/*
				 * if payment type is debit money is reduced from balance else
				 * it is added
				 */
				if (AmigoWalletConstants.PAYMENT_TYPE_DEBIT
						.equals(userTransaction.getPaymentType()
								.getPaymentType().toString())) {
					balance -= userTransaction.getAmount();
				} else {
					balance += userTransaction.getAmount();
				}
				
				/* here reward points is calculated which is yet t be redeemed */
				if (AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO
						.equals(userTransaction.getIsRedeemed().toString())) {
					nonRedeemedPoints += userTransaction.getPointsEarned();
				}
			}
		}

		userFromDao.setBalance(balance);
		userFromDao.setRewardPoints(nonRedeemedPoints);
		
		return userFromDao;

	}

	/**
	 * This method is used to change the password of logged in user. It checks
	 * the password, newPassword and confirmNewPassword with input field
	 * validation, if they are in proper format, it checks whether the
	 * newPassword is matching the confirmNewPassword, then it checks if the old
	 * password and the new password are same.
	 * 
	 * @param user
	 * 
	 * @throws Exception
	 *             if the password is not in proper format
	 * 				or
	 *             if the newPassword is not in proper format
	 * 				or
	 *             if the confirmNewPassword is not in proper format
	 * 				or
	 *             if newPassword is same as old password
	 * 				or
	 *             if confirmNewPassword is not same as newPassword
	 * 				or
	 *             if the current password is not matching the record of logged
	 *             in user
	 * @throws NoSuchAlgorithmException
	 *             if the name of algorithm is incorrect used in password
	 *             hashing mechanism.
	 */
	@Override
	public void changeUserPassword(User user)
			throws Exception {
	
		/*
		 * here we are validating changed password details by passing user
		 * object as argument to validateChangePasswordDetails method of
		 * UserLoginValidator if password is not valid exception
		 * will be thrown
		 */
		UserLoginValidator.validateChangePasswordDetails(user);
	
		/*
		 * here we are retrieving user by passing userIdt as argument to
		 * getUserByUserId method of userLoginDAO if user is not valid then null
		 * value will be return
		 */
		User userFromDao = userLoginDAO.getUserByUserId(user.getUserId());
	
		/*
		 * here if user is not found exception will be thrown with message
		 * LoginService.USER_NOT_FOUND
		 */
		if (userFromDao == null)
			throw new Exception("LoginService.USER_NOT_FOUND");
	
		/*
		 * if hash value of hash value of password doesn't matches exception is
		 * thrown with message "LoginService.INVALID_PASSWORD"
		 */
		if (!HashingUtility.getHashValue(user.getPassword()).equals(
				userFromDao.getPassword())) {
			throw new Exception("LoginService.INVALID_PASSWORD");
		}
	
		/* here we are setting the hash value of the password to the user */
		user.setNewPassword(HashingUtility.getHashValue(user.getNewPassword()));
	
		/*
		 * here we are  changing the password of an existing Customer.
		 * It takes user bean object as a parameter, which includes, customerId and
		 * newPassword, it fetches an entity on the basis of customerId, and
		 * updates the password to newPassword received
		 */
		userLoginDAO.changeUserPassword(user);
	
	}

	/**
	 * This method returns the bean object by passing user id as argument this
	 * checks the user id in database and returns that user.
	 * 
	 * @param userId
	 * 
	 * @return user
	 * 
	 * @throws Exception
	 */
	@Override
	public User getUserbyUserId(Integer userId)
			throws Exception {

		/*
		 * here we are retrieving user by passing userId as argument to
		 * getUserByUserId method of UserLoginDAO if user is not valid null will
		 * be returned
		 */
		User userFromDao = userLoginDAO.getUserByUserId(userId);

		/*
		 * if no user exist for the user id then exception is thrown with
		 * message LoginService.INVALID_CREDENTIALS
		 */
		if (userFromDao == null) {
			throw new Exception(
					"LoginService.INVALID_CREDENTIALS");
		}

		userFromDao.setPassword(null);

		/* Filtering the user cards */
		/*
		 * Removing the deleted/deactive cards
		 */
		List<Card> cards = userFromDao.getCards();

		List<Card> cardsToBeRemoved = new ArrayList<Card>();
		if (cards != null) {

			/*
			 * here if card status is deactive then the it is added to the cards
			 * to be removed list
			 */
			for (Card card : cards) {
				if (CardStatus.INACTIVE.equals(card.getCardStatus())) {
					cardsToBeRemoved.add(card);
				}
			}
			if (cards.removeAll(cardsToBeRemoved)) {
				userFromDao.setCards(cards);
			}

		}
		
		/* Calculating Balance and Reward points */

		/*
		 * here we are fetching all the transaction for the user from database
		 * getUserTransaction method of userFromDAO
		 */
		List<UserTransaction> transactions = userFromDao.getUserTransactions();
		Double balance = 0.0;
		Integer nonRedeemedPoints = 0;

		/*
		 * here we are adding money and reward point to wallet according to the
		 * transactions
		 **/
		if (transactions != null) {
			for (UserTransaction userTransaction : transactions) {
				
				/*
				 * if payment type is debit money is reduced from balance else
				 * it is added
				 */
				if (AmigoWalletConstants.PAYMENT_TYPE_DEBIT
						.equals(userTransaction.getPaymentType()
								.getPaymentType().toString())) {
					balance -= userTransaction.getAmount();
				} else {
					balance += userTransaction.getAmount();
				}
				
				/* here reward points is calculated which is yet t be redeemed */
				if (AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO
						.equals(userTransaction.getIsRedeemed().toString())) {
					nonRedeemedPoints += userTransaction.getPointsEarned();
				}
			}
		}
		userFromDao.setBalance(balance);
		userFromDao.setRewardPoints(nonRedeemedPoints);

		return userFromDao;
	}
}
