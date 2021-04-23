package com.edubank.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.NetBankingDAO;
import com.edubank.model.Transaction;
import com.edubank.model.TransactionType;

/**
 * This class contains the methods for business logics related to Net Banking
 * module, like payByNetBanking.
 * 
 * @author ETA_JAVA
 *
 */
@Service(value = "netBankingService")
@Transactional
public class NetBankingServiceImpl implements NetBankingService {

	@Autowired
	private NetBankingDAO netBankingDAO;

	/**
	 * This method is used to make payment by net banking, It sets the required
	 * details to the transaction object and sends to the updated object to the
	 * corresponding dao.
	 * 
	 * @param transaction
	 * 
	 * @return transactionId
	 * 
	 * @throws Exception
	 */
	@Override
	public Long payByNetBanking(Transaction transaction) throws Exception {

		transaction.setInfo("From:- Account To:- AmigoWallet, Reason:- Payment");
		transaction.setType(TransactionType.DEBIT);
		transaction.setTransactionDateTime(LocalDateTime.now());
		transaction.setTransactionMode("Internet-Banking");

		/*
		 * the following code is to  persist a transaction record and update
		 * the amount in the corresponding account.
		 */
		Long transactionId = netBankingDAO.payByNetBanking(transaction);

		return transactionId;
	}
}
