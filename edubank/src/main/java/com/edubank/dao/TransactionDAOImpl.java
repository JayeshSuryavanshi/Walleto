package com.edubank.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.edubank.entity.TransactionEntity;
import com.edubank.model.Transaction;

/**
 * This is a DAO class having methods to perform CRUD operations on transaction
 * table.
 * 
 * @author ETA_JAVA
 *
 */
@Repository("transactionDAO")
public class TransactionDAOImpl implements TransactionDAO {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * This method is used to persist the transaction
	 * 
	 * @param transaction,
	 *            createdBy
	 * 
	 * @return transactionId
	 */
	@Override
	public Long addTransaction(Transaction transaction, String createdBy) throws Exception {

		Long transactionId = null;

		/*
		 * here we are creating an entity object and setting the values to that entity
		 * object from model object
		 **/
		TransactionEntity transactionEntity = new TransactionEntity();
		transactionEntity.setAccountNumber(transaction.getAccountNumber());
		transactionEntity.setAmount(transaction.getAmount());
		transactionEntity.setCreatedBy(createdBy);
		transactionEntity.setInfo(transaction.getInfo());
		transactionEntity.setRemarks(transaction.getRemarks());
		transactionEntity.setTransactionMode(transaction.getTransactionMode());
		transactionEntity.setType(transaction.getType());

		/*
		 * here we are adding the Entity to table using save method save method will
		 * return primary key of that row
		 */
		entityManager.persist(transactionEntity);

		transactionId = transactionEntity.getTransactionId();

		return transactionId;
	}

	/**
	 * @see com.edubank.dao.TransactionDAO#getAccountTransactionsForDateRange
	 *      (java.lang.String, java.time.LocalDate, java.time.LocalDate)
	 */
	@Override
	public List<Transaction> getAccountTransactionsForDateRange(String accountNumber, LocalDate fromDate,
			LocalDate toDate) throws Exception {
		List<Transaction> transactionsList = null;

		LocalDateTime fromDateTime = LocalDateTime.of(fromDate, LocalTime.of(0, 0, 0, 0));
		LocalDateTime toDateTime = LocalDateTime.of(toDate, LocalTime.now());

		Query query = entityManager.createQuery(
				"from TransactionEntity where accountNumber=:accNo and transactionDateTime between :timeLowLimit and :timeUpLimit order by transactionDateTime desc");
		query.setParameter("accNo", accountNumber);
		query.setParameter("timeLowLimit", fromDateTime);
		query.setParameter("timeUpLimit", toDateTime);

		List<TransactionEntity> transactionsListFromDAO = query.getResultList();

		/*
		 * here if the list of entities length is more than 1 then we are creating a
		 * bean object using getContext and setting the values into bean object from
		 * entities that we received from database to iterate over the entity list we
		 * are using for loop then after setting the values to bean object we are adding
		 * the values to the transaction list
		 */
		if (!transactionsListFromDAO.isEmpty()) {
			transactionsList = new ArrayList<Transaction>();

			for (TransactionEntity transactionEnt : transactionsListFromDAO) {
				Transaction tran = new Transaction();
				tran.setTransactionId(transactionEnt.getTransactionId());
				tran.setTransactionDateTime(transactionEnt.getTransactionDateTime());
				tran.setType(transactionEnt.getType());
				tran.setInfo(transactionEnt.getInfo());
				tran.setAmount(transactionEnt.getAmount());

				transactionsList.add(tran);
			}
		}

		return transactionsList;
	}
}
