package com.edubank.service;

import com.edubank.model.Transaction;

/**
 * This Interface contains methods for business logics related to Net Banking module,
 * like payByNetBanking.
 * 
 * @author ETA_JAVA
 *
 */
public interface NetBankingService {

	/**
	 * This method is used to make payment by net banking, It sets the required
	 * details to the transaction object and sends to the updated object to the
	 * corresponding dao.
	 * 
	 * @param transaction
	 * 
	 * @return transactionId
	 */
	public Long payByNetBanking(Transaction transaction)throws Exception;
}
