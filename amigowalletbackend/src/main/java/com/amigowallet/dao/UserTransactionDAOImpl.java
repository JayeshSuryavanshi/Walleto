package com.amigowallet.dao;

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
 * This is a DAO class having method to perform CRUD operations on user and transactions
 * on performing different transactions like loadMoney, debitMoney etc.
 * 
 * @author ETA_JAVA
 * 
 */
@Repository("userTransactionDAO")
public class UserTransactionDAOImpl implements UserTransactionDAO {
	
	/** This is a spring auto-wired attribute used to create data base sessions */
	@Autowired
	EntityManager entityManager;
	
	/**
	 * This method is used for adding the transaction for given userId for loading money to amigo wallet.
	 * 
	 * @param userTransaction
	 * @param userId
	 * 
	 * @return userTransaction
	 */
	@Override
	public UserTransaction loadMoney(UserTransaction userTransaction, Integer userId) {
		
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		List<UserTransactionEntity> transactionEntities = userEntity.getUserTransactionEntities();
		
		UserTransactionEntity transactionEntity = new UserTransactionEntity();
		transactionEntity.setAmount(userTransaction.getAmount());
		transactionEntity.setIsRedeemed(userTransaction.getIsRedeemed());
		transactionEntity.setInfo(userTransaction.getInfo());	
		
		/*
		 * The following code is to set the paymentTypeEntity to transactionEntity
		 */

		Query query = entityManager.createQuery(
				"from PaymentTypeEntity where paymentFrom = :paymentFrom and paymentTo = :paymentTo and paymentType = :paymentType");
		query.setParameter("paymentFrom", AmigoWalletConstants.PAYMENT_FROM_BANK.charAt(0));
		query.setParameter("paymentTo", AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0));
		query.setParameter("paymentType", AmigoWalletConstants.PAYMENT_TYPE_CREDIT.charAt(0));
		PaymentTypeEntity paymentTypeEntity = (PaymentTypeEntity) query.getSingleResult();
		
		transactionEntity.setPaymentTypeEntity(paymentTypeEntity);
		transactionEntity.setPointsEarned(userTransaction.getPointsEarned());
		transactionEntity.setRemarks(userTransaction.getRemarks());
		
		/*
		 * The following code is to set the StatusEntity to transactionEntity
		 */

		transactionEntity.setTransactionStatus(TransactionStatus.SUCCESS);

		transactionEntities.add(transactionEntity);
		entityManager.persist(transactionEntity);
		userEntity.setUserTransactionEntities(transactionEntities);
		
		/*
		 * The userEntity is saved to the database
		 */
		entityManager.persist(userEntity);
		userTransaction.setUserTransactionId(transactionEntity.getUserTransactionId());
		
		/*
		 * The following code is to set the paymenType model object to transaction model object
		 */
		if (paymentTypeEntity != null) {
			
			PaymentType paymentType = new PaymentType();
			paymentType.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
			paymentType.setPaymentTo(paymentTypeEntity.getPaymentTo());
			paymentType.setPaymentType(paymentTypeEntity.getPaymentType());
			paymentType.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());

			userTransaction.setPaymentType(paymentType);
		}
		
		userTransaction.setTransactionStatus(transactionEntity.getTransactionStatus());
		return userTransaction;
	}

	/**
	 * This method is used to add the transaction for given userId for sending the money to a bank account.
	 * 
	 * @param userTransaction
	 * @param userId
	 * 
	 * @return userTransaction
	 */
	public UserTransaction sendMoneyToBankAccount(
			UserTransaction userTransaction, Integer userId) {
		
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		List<UserTransactionEntity> transactionEntities = userEntity.getUserTransactionEntities();
		
		UserTransactionEntity transactionEntity = new UserTransactionEntity();
		transactionEntity.setAmount(userTransaction.getAmount());
		transactionEntity.setIsRedeemed(userTransaction.getIsRedeemed());
		transactionEntity.setInfo(userTransaction.getInfo());	
		
		/*
		 * The following code is to set the paymentTypeEntity to transactionEntity
		 */

		Query query = entityManager.createQuery(
				"from PaymentTypeEntity where paymentFrom = :paymentFrom and paymentTo = :paymentTo and paymentType = :paymentType");
		
		query.setParameter("paymentFrom", AmigoWalletConstants.PAYMENT_FROM_WALLET.charAt(0));
		query.setParameter("paymentTo", AmigoWalletConstants.PAYMENT_TO_BANK.charAt(0));
		query.setParameter("paymentType", AmigoWalletConstants.PAYMENT_TYPE_DEBIT.charAt(0));
		
		PaymentTypeEntity paymentTypeEntity = (PaymentTypeEntity) query.getSingleResult();
		
		transactionEntity.setPaymentTypeEntity(paymentTypeEntity);
		transactionEntity.setPointsEarned(userTransaction.getPointsEarned());
		transactionEntity.setRemarks(userTransaction.getRemarks());
		
		/*
		 * The following code is to set the StatusEntity to transactionEntity
		 */

		transactionEntity.setTransactionStatus(TransactionStatus.SUCCESS);

		transactionEntities.add(transactionEntity);
		entityManager.persist(transactionEntity);
		userEntity.setUserTransactionEntities(transactionEntities);
		
		/*
		 * The userEntity is saved to the database
		 */
		entityManager.persist(userEntity);
		userTransaction.setUserTransactionId(transactionEntity.getUserTransactionId());
		
		/*
		 * The following code is to set the paymenType model object to transaction model object
		 */
		if (paymentTypeEntity != null) {
			
			PaymentType paymentType = new PaymentType();
			paymentType.setPaymentFrom(paymentTypeEntity.getPaymentFrom());
			paymentType.setPaymentTo(paymentTypeEntity.getPaymentTo());
			paymentType.setPaymentType(paymentTypeEntity.getPaymentType());
			paymentType.setPaymentTypeId(paymentTypeEntity.getPaymentTypeId());

			userTransaction.setPaymentType(paymentType);
		}
		
		userTransaction.setTransactionStatus(transactionEntity.getTransactionStatus());
		return userTransaction;
	}
}
