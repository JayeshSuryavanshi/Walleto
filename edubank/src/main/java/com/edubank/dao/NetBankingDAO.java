package com.edubank.dao;

import com.edubank.model.Transaction;

/**
 * This is a DAO Interface, used to interact with database, contains the methods
 * related to net banking module like payByNetBanking.
 * 
 * @author ETA_JAVA
 *
 */
public interface NetBankingDAO {

	/**
	 * 
	 * @param transaction
	 * 
	 * @return transactionId
	 */
	public Long payByNetBanking(Transaction transaction) throws Exception;
}
