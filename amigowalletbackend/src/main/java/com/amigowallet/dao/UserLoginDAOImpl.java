package com.amigowallet.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.BankEntity;
import com.amigowallet.entity.CardEntity;
import com.amigowallet.entity.PaymentTypeEntity;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.entity.UserTransactionEntity;
import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;
import com.amigowallet.model.PaymentType;
import com.amigowallet.model.User;
import com.amigowallet.model.UserTransaction;

/**
 * This is a DAO class that contains the methods responsible for interacting the database
 * with respect to Login module like  getUserByEmailId, change user password, getUserByUserId.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "userLoginDAO")
public class UserLoginDAOImpl implements UserLoginDAO {
	
	/** This is a spring auto-wired attribute used to create data base sessions */
	@Autowired
	EntityManager entityManager;

	/**
	 * This method is used to get a User model corresponding to its emailId<br>
	 * The transaction and card details are also fetched along with the User.
	 * 
	 * @param emailId
	 * 
	 * @return User
	 */
	@Override
	@SuppressWarnings("unchecked")
	public User getUserByEmailId(String emailId) throws Exception {
		
		emailId = emailId.toLowerCase();
		User userFromDb = null;
		
		/*
		 * The userEntity with the required emailId is fetched 
		 */
		Query query = entityManager.createQuery("from UserEntity where emailId=:emailId");
		query.setParameter("emailId", emailId.toLowerCase());
		
		List<UserEntity> userEntities = query.getResultList();
		
		/*
		 * A user bean is populated from the data obtained from the 
		 * above fetched userEntity
		 */
		if (!userEntities.isEmpty()) {
			UserEntity userEntity = userEntities.get(0);
			userFromDb = new User();
			userFromDb.setEmailId(userEntity.getEmailId());
			userFromDb.setMobileNumber(userEntity.getMobileNumber());
			userFromDb.setName(userEntity.getName());
			userFromDb.setPassword(userEntity.getPassword());
			userFromDb.setUserId(userEntity.getUserId());
			userFromDb.setUserStatus(userEntity.getUserStatus());
			
			/*
			 * The below lines of code fetches the card details of the user.
			 */
			List<CardEntity> cardEntities = userEntity.getCardEntities();
			
			List<Card> cards = null;
			if (cardEntities != null && !cardEntities.isEmpty()) {
				cards = new ArrayList<>();
				
				/*
				 * For each cardEntity in the list of cardEntities 
				 * a Bank bean is populated with the corresponding properties
				 * of the bankEntity
				 */
				for (CardEntity cardEntity : cardEntities) {
					
					Card card =new Card();
					BankEntity bankEntity = cardEntity.getBankEntity();
					Bank bank = new Bank();
					bank.setBankId(bankEntity.getBankId());
					bank.setBankName(bankEntity.getBankName());
					card.setBank(bank);

					card.setCardId(cardEntity.getCardId());
					card.setCardNumber(cardEntity.getCardNumber());
					card.setExpiryDate(cardEntity.getExpiryDate());
					card.setCardStatus(cardEntity.getUserStatus());
					cards.add(card);
				}
				userFromDb.setCards(cards);
			}

			/*
			 * The below lines of code fetches all the points
			 * earned and transaction details of the user.
			 */
			List<UserTransactionEntity> userTransactionEntities = userEntity.getUserTransactionEntities();
			
			List<UserTransaction> transactions = null;
			if (userTransactionEntities != null && !userTransactionEntities.isEmpty()) {
				transactions = new ArrayList<UserTransaction>();
				for (UserTransactionEntity userTransactionEntity : userTransactionEntities) {
					
					UserTransaction transaction = new UserTransaction();
					transaction.setAmount(userTransactionEntity.getAmount());
					transaction.setInfo(userTransactionEntity.getInfo());
					transaction.setIsRedeemed(userTransactionEntity.getIsRedeemed());
					
					/*
					 * The following code is to set the payment Type model object to transaction model object
					 */
					PaymentTypeEntity paymentTypeEntity = userTransactionEntity.getPaymentTypeEntity();		
					if (paymentTypeEntity != null) {
						
						PaymentType paymentType = new PaymentType();
						paymentType.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
						paymentType.setPaymentTo(paymentTypeEntity.getPaymentTo());
						paymentType.setPaymentType(paymentTypeEntity.getPaymentType());
						paymentType.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());

						transaction.setPaymentType(paymentType);
					}
					transaction.setPointsEarned(userTransactionEntity.getPointsEarned());
					transaction.setRemarks(userTransactionEntity.getRemarks());

					transaction.setTransactionStatus(userTransactionEntity.getTransactionStatus());
					transaction.setTransactionDateTime(userTransactionEntity.getTransactionDateTime());
					transaction.setUserTransactionId(userTransactionEntity.getUserTransactionId());
					transactions.add(transaction);
				}
				userFromDb.setUserTransactions(transactions);
			}
		}
		return userFromDb;
	}
	
	/**
	 * This method is used to get a User model corresponding to its userId<br>
	 * The transaction and card details are also fetched along with the User.
	 * 
	 * @param emailId
	 * 
	 * @return User
	 */
	@Override
	public User getUserByUserId(Integer userId) {
		User userFromDb = null;
		
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);

		if (userEntity != null) {
			
			userFromDb = new User();
			userFromDb.setEmailId(userEntity.getEmailId());
			userFromDb.setMobileNumber(userEntity.getMobileNumber());
			userFromDb.setName(userEntity.getName());
			userFromDb.setPassword(userEntity.getPassword());
			userFromDb.setUserId(userEntity.getUserId());
			userFromDb.setUserStatus(userEntity.getUserStatus());
			
			/*
			 * The below lines of code fetches the card details of the user.
			 */
			List<CardEntity> cardEntities = userEntity.getCardEntities();
			
			List<Card> cards = null;
			if (cardEntities != null && !cardEntities.isEmpty()) {
				cards = new ArrayList<>();
				
				/*
				 * For each cardEntity in the list of cardEntities 
				 * a Bank bean is populated with the corresponding properties
				 * of the bankEntity
				 */
				for (CardEntity cardEntity : cardEntities) {
					Card card = new Card();
					
					BankEntity bankEntity = cardEntity.getBankEntity();
					Bank bank = new Bank();
					bank.setBankId(bankEntity.getBankId());
					bank.setBankName(bankEntity.getBankName());
					card.setBank(bank);
					
					card.setCardId(cardEntity.getCardId());
					card.setCardNumber(cardEntity.getCardNumber());
					card.setExpiryDate(cardEntity.getExpiryDate());
					card.setCardStatus(cardEntity.getUserStatus());
					cards.add(card);
				}
				userFromDb.setCards(cards);
			}

			/*
			 * The below lines of code fetches all the transaction and points earned details of the user.
			 */
			List<UserTransactionEntity> userTransactionEntities = userEntity.getUserTransactionEntities();
			
			List<UserTransaction> transactions = null;
			if (userTransactionEntities != null&& !userTransactionEntities.isEmpty()) {
				transactions = new ArrayList<UserTransaction>();
				for (UserTransactionEntity userTransactionEntity : userTransactionEntities) {
					
					UserTransaction transaction = new UserTransaction();
					transaction.setAmount(userTransactionEntity.getAmount());
					transaction.setInfo(userTransactionEntity.getInfo());
					transaction.setIsRedeemed(userTransactionEntity.getIsRedeemed());
					
					/*
					 * The following code is to set the payment Type model object to transaction model object
					 */
					PaymentTypeEntity paymentTypeEntity = userTransactionEntity.getPaymentTypeEntity();
					if (paymentTypeEntity != null) {
						
						PaymentType paymentType = new PaymentType();
						paymentType.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
						paymentType.setPaymentTo(paymentTypeEntity.getPaymentTo());
						paymentType.setPaymentType(paymentTypeEntity.getPaymentType());
						paymentType.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());
						transaction.setPaymentType(paymentType);
					}
					transaction.setPointsEarned(userTransactionEntity.getPointsEarned());
					transaction.setRemarks(userTransactionEntity.getRemarks());
					
					transaction.setTransactionStatus(userTransactionEntity.getTransactionStatus());
					transaction.setTransactionDateTime(userTransactionEntity.getTransactionDateTime());
					transaction.setUserTransactionId(userTransactionEntity.getUserTransactionId());
					transactions.add(transaction);
				}
				userFromDb.setUserTransactions(transactions);
			}
		}
		return userFromDb;
	}
	
	/**
	 * This method is used to change the password of an existing User. It
	 * takes UserLogin as a parameter, which includes, userId and
	 * newPassword, it fetches an entity on the basis of userId, and updates
	 * the password to newPassword received.<br>
	 * 
	 * @param user
	 */
	@Override
	public void changeUserPassword(User user) {
		
		UserEntity userEntity = entityManager.find(UserEntity.class, user.getUserId());
		userEntity.setPassword(user.getNewPassword());
	}
}
