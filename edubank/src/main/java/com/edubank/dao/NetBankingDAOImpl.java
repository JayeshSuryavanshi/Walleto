package com.edubank.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.edubank.entity.AccountEntity;
import com.edubank.entity.TransactionEntity;
import com.edubank.model.Transaction;

/**
 * This is a DAO class, used to interact with database, contains the methods
 * related to net banking module like payByNetBanking.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "netBankingDAO")
public class NetBankingDAOImpl implements NetBankingDAO {

	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * This method is used to get a persist a transaction record and update the
	 * amount in the corresponding account. 
	 * 
	 * @param transaction
	 * 
	 * @return transactionId
	 */
	@Override
	public Long payByNetBanking(Transaction transaction) throws Exception {

		Long transactionId = null;
		
			TransactionEntity entity = new TransactionEntity();
			
			/*
			 * here we are setting values from bean class to entity class
			 **/
			entity.setAccountNumber(transaction.getAccountNumber());
			entity.setAmount(transaction.getAmount());
			entity.setCreatedBy("AmigoWallet");
			entity.setInfo(transaction.getInfo());
			entity.setRemarks(transaction.getRemarks());
			entity.setTransactionMode(transaction.getTransactionMode());
			entity.setType(transaction.getType());
			
			/*
			 * here we are adding the Entity to  table using save method 
			 * save method will return primary key of that row
			 */
			entityManager.persist(entity);
			
			transactionId = entity.getTransactionId();
			
			Query query = entityManager.createQuery("from AccountEntity a where a.accountNumber = :acNo");
			query.setParameter("acNo", transaction.getAccountNumber());

			AccountEntity accountEntity = (AccountEntity) query.getResultList().get(0);

			accountEntity.setBalance(accountEntity.getBalance()
					- transaction.getAmount());
			
			

		return transactionId;
	}

}
