package com.amigowallet.dao;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.PaymentTypeEntity;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.entity.UserTransactionEntity;
import com.amigowallet.model.PaymentType;
import com.amigowallet.model.TransactionStatus;
import com.amigowallet.model.User;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;

@Repository("WalletToWalletDAO")
public class WalletToWalletDAOImpl implements WalletToWalletDAO{
	
	@Autowired
	UserLoginDAO userLoginDAO;

	
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public Integer transferToWallet(Integer userId, Double amountToTransfer, String emailIdToTransfer) throws Exception{
		
		// TODO Auto-generated method stub
		
		User receiver=userLoginDAO.getUserByEmailId(emailIdToTransfer);
		User sender=userLoginDAO.getUserByUserId(userId);
		Double number=0.0;
		if(sender==receiver) {
			throw new Exception("WalletService.PAYING_HIMSELF");
		}
		
		if(sender==null){
			return 0;
		}
		
		if(receiver==null){
			throw new Exception("WalletToWalletService.INVALID_EMAIL");
		}
		
		if(amountToTransfer>=200){
			number=amountToTransfer*0.02;
		}
		
		Random random=new Random();

		Integer rewardpoint=random.nextInt(5);

		UserTransaction userTransactionSender = new UserTransaction();
		
		/*
		 * A new userTransaction is created here and all the properties are populated
		 */
		userTransactionSender.setAmount(amountToTransfer);
		userTransactionSender.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_DEBIT+" "+emailIdToTransfer);
		userTransactionSender.setPointsEarned(rewardpoint);
		userTransactionSender.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransactionSender.setRemarks("D");
		
		
		UserTransaction userTransactionReceiver = new UserTransaction();
		userTransactionReceiver.setAmount(amountToTransfer);
		userTransactionReceiver.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_WALLET_TO_WALLET_CREDIT+" "+sender.getEmailId());
		userTransactionReceiver.setPointsEarned(0);
		userTransactionReceiver.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransactionReceiver.setRemarks("C");
		
		if(walletDebit(userTransactionSender, userId)==0 || walletCredit(userTransactionReceiver, receiver.getUserId())==0){
			return 0;
		}
		
		if(number!=0.0){
		UserTransaction userTransactionSender1 = new UserTransaction();
		
		/*
		 * A new userTransaction is created here for cashback and all the properties are populated
		 */
		userTransactionSender1.setAmount(number);
		userTransactionSender1.setInfo(AmigoWalletConstants.TRANSACTION_INFO_MONEY_CASHBACK_TO_WALLET_CREDIT+" "+number);
		userTransactionSender1.setPointsEarned(0);
		userTransactionSender1.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		userTransactionSender1.setRemarks("C");
		
		walletCredit(userTransactionSender1, userId);
		}
		return 1;
	}
	/**
	 * This method is used to Debit money for given user transaction with userID passed as parameter.
	 * 
	 * @param userTransaction,userID
	 * 
	 * @return Integer 
	 */
	public Integer walletDebit(UserTransaction userTransaction, Integer userId) {
		
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		
		if(userEntity==null){
			return 0;
		}
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
		query.setParameter("paymentTo", AmigoWalletConstants.PAYMENT_TO_WALLET.charAt(0));
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
		return 1;
	}
	/**
	 * This method is used to Credit money for given user transaction with userID passed as parameter.
	 * 
	 * @param userTransaction,userID
	 * 
	 * @return Integer 
	 */
public Integer walletCredit(UserTransaction userTransaction, Integer userId) {
		
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		if(userEntity==null){
			return 0;
		}
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
		return 1;
	}

	

}
