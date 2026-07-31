package com.amigowallet.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.amigowallet.entity.PaymentTypeEntity;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.entity.UserTransactionEntity;
import com.amigowallet.model.PaymentType;
import com.amigowallet.model.UserTransaction;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * DAO for reading a user's transaction ledger.
 *
 * @author ETA_JAVA
 */
@Repository(value = "rewardPointsDAO")
public class RewardPointsDAOImpl implements RewardPointsDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<UserTransaction> getAllTransactionByUserId(Integer userId) {
		if (userId == null) {
			return null;
		}
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		if (userEntity == null) {
			return null;
		}
		List<UserTransactionEntity> transactionEntities = userEntity.getUserTransactionEntities();
		if (transactionEntities == null || transactionEntities.isEmpty()) {
			return null;
		}

		List<UserTransaction> userTransactions = new ArrayList<>();
		for (UserTransactionEntity userTransactionEntity : transactionEntities) {
			UserTransaction userTransaction = new UserTransaction();
			userTransaction.setAmount(userTransactionEntity.getAmount());
			userTransaction.setInfo(userTransactionEntity.getInfo());
			userTransaction.setIsRedeemed(userTransactionEntity.getIsRedeemed());

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
		return userTransactions;
	}
}
