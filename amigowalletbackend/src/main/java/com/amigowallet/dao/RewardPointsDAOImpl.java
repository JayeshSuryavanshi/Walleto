package com.amigowallet.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.PaymentTypeEntity;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.entity.UserTransactionEntity;
import com.amigowallet.model.PaymentType;
import com.amigowallet.model.TransactionStatus;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;

/**
 * This is a DAO class having methods to perform CRUD operations related to reward points.
 *
 * @author ETA_JAVA
 * 
 */
@Repository(value = "rewardPointsDAO")
public class RewardPointsDAOImpl implements RewardPointsDAO {
	
	/** This is a spring auto-wired attribute used to create data base sessions */
	@Autowired
	EntityManager entityManager;
	
	/**
	 * This method receives user id as argument and gets the transaction
	 * details for that user
	 * It returns user transactions list
	 * If no transaction is not found in the data base then it returns
	 * null
	 * 
	 * @param userId
	 * 
	 * @return list of user transaction
	 */
	@Override
	public List<UserTransaction> getAllTransactionByUserId(Integer userId) {
		
		/*
		 * All the userTransaction entities for the user is fetched 
		 */
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		List<UserTransactionEntity> transactionEntities = userEntity.getUserTransactionEntities();
		List<UserTransaction> userTransactions = null;
		
		/*
		 * For each userTransactionEntity in the list of transactions 
		 * a user transaction bean is populated with the corresponding properties
		 * of the userTransaction Entity
		 */
		if (transactionEntities != null && !transactionEntities.isEmpty()) {
			userTransactions = new ArrayList<UserTransaction>();
			for (UserTransactionEntity userTransactionEntity : transactionEntities) {
				
				UserTransaction userTransaction = new UserTransaction();
				userTransaction.setAmount(userTransactionEntity.getAmount());
				userTransaction.setInfo(userTransactionEntity.getInfo());
				userTransaction.setIsRedeemed(userTransactionEntity.getIsRedeemed());
				
				/*
				 * The properties of a  paymentType bean is populated by 
				 * getting the  corresponding properties of paymentTypeEntity of 
				 * userTransaction Entity
				 */
				PaymentTypeEntity paymentTypeEntity = userTransactionEntity.getPaymentTypeEntity();
				if (paymentTypeEntity != null) {
					
					PaymentType paymentType = new PaymentType();
					paymentType.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
					paymentType.setPaymentTo(paymentTypeEntity.getPaymentTo());
					paymentType.setPaymentType(paymentTypeEntity.getPaymentType());
					paymentType.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());
					userTransaction.setPaymentType(paymentType);
				}
				userTransaction.setPointsEarned(userTransactionEntity.getPointsEarned());
				userTransaction.setRemarks(userTransactionEntity.getRemarks());
				
				userTransaction.setTransactionStatus(userTransactionEntity.getTransactionStatus());
				userTransaction.setTransactionDateTime(userTransactionEntity.getTransactionDateTime());
				userTransaction.setUserTransactionId(userTransactionEntity.getUserTransactionId());
				userTransactions.add(userTransaction);
			}
		}
		return userTransactions;
	}
	
	/**
	 * This method is used for redeeming all the reward points
	 * It updates the isRedeemed field of the userTransactionEntity
	 * 
	 * @param userId
	 */
	@Override
	public void redeemAllRewardPoints(Integer userId) {
		
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		List<UserTransactionEntity> transactionEntities = userEntity.getUserTransactionEntities();
		if (transactionEntities != null && !transactionEntities.isEmpty()) {
			for (UserTransactionEntity userTransactionEntity : transactionEntities) {
				if (AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.equals(userTransactionEntity.getIsRedeemed().toString())){
					userTransactionEntity.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
				}
			}
		}
	}
	
	/**
	 * This method adds the redeemed amount to the wallet
	 * and the userTransaction list is updated
	 * 
	 * @param userId
	 * @param userTranscation
	 */
	@Override
	public void addRedeemedMoneyToWallet(Integer userId,UserTransaction userTransaction) {
		
		/*
		 * User entity is fetched using the userId and UserTransaction Entities
		 * for the user entity is fetched and populated to a list
		 */
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		List<UserTransactionEntity> transactionEntities = userEntity.getUserTransactionEntities();
		
		UserTransactionEntity transactionEntity = new UserTransactionEntity();
		transactionEntity.setAmount(userTransaction.getAmount());
		transactionEntity.setIsRedeemed(userTransaction.getIsRedeemed());
		transactionEntity.setInfo(userTransaction.getInfo());
		
		/*
		 * Payment type entity is fetched and set to transaction entity 
		 */
		Query query = entityManager.createQuery(
				"from PaymentTypeEntity where paymentFrom = :paymentFrom and paymentTo = :paymentTo and paymentType = :paymentType");
		query.setParameter("paymentFrom", AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0));
		query.setParameter("paymentTo", AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0));
		query.setParameter("paymentType", AmigoWalletConstants.PAYMENT_TYPE_CREDIT.charAt(0));
		PaymentTypeEntity paymentTypeEntity = (PaymentTypeEntity) query.getSingleResult();
		transactionEntity.setPaymentTypeEntity(paymentTypeEntity);
		
		transactionEntity.setPointsEarned(userTransaction.getPointsEarned());
		transactionEntity.setRemarks(userTransaction.getRemarks());
		
		/*
		 * StatusEntity is fetched and set to transaction entity 
		 */
		transactionEntity.setTransactionStatus(TransactionStatus.SUCCESS);
		
		/*
		 * The transaction is added to user transactions list
		 */
		transactionEntities.add(transactionEntity);
		userEntity.setUserTransactionEntities(transactionEntities);
	
		/*
		 * The userEntity is saved to the database
		 */
		entityManager.persist(userEntity);	
	}
}
