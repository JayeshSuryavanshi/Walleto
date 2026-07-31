package com.amigowallet.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.MerchantEntity;
import com.amigowallet.entity.MerchantTransactionEntity;
import com.amigowallet.exception.ApiException;
import com.amigowallet.model.TransactionStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

@Repository("billPaymentDAO")
public class BillPaymentDAOImpl implements BillPaymentDAO {

	/** Wallet -> Merchant DEBIT payment type (PAYMENT_TYPE_ID = 6). */
	private static final Integer WALLET_TO_MERCHANT_DEBIT_TYPE_ID = 6;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<String> displayServiceType() {
		return entityManager
				.createQuery("select c.serviceType from MerchantServiceTypeEntity c", String.class)
				.getResultList();
	}

	@Override
	public List<String> displayMerchantName(String type) {
		Integer serviceId;
		try {
			serviceId = entityManager
					.createQuery("select c.serviceId from MerchantServiceTypeEntity c where c.serviceType = :st",
							Integer.class)
					.setParameter("st", type)
					.getSingleResult();
		} catch (NoResultException e) {
			return new ArrayList<>();
		}

		List<Integer> merchantIds = entityManager
				.createQuery("select c.merchantId from MerchantServiceMappingEntity c where c.serviceId = :id",
						Integer.class)
				.setParameter("id", serviceId)
				.getResultList();

		List<MerchantEntity> merchants = entityManager
				.createQuery("select c from MerchantEntity c", MerchantEntity.class)
				.getResultList();

		List<String> names = new ArrayList<>();
		for (MerchantEntity merchant : merchants) {
			if (merchantIds.contains(merchant.getMerchantId())) {
				names.add(merchant.getName());
			}
		}
		return names;
	}

	@Override
	public Integer findMerchantId(String name) {
		try {
			return entityManager
					.createQuery("select c.merchantId from MerchantEntity c where c.name = :mname", Integer.class)
					.setParameter("mname", name)
					.getSingleResult();
		} catch (NoResultException e) {
			throw new ApiException(HttpStatus.NOT_FOUND, "WalletToMerchantTransferAPI.INVALID_MERCHANT");
		}
	}

	@Override
	public void registerMerchantTransaction(Integer merchantId, BigDecimal amount, String info) {
		MerchantTransactionEntity merchantTransaction = new MerchantTransactionEntity();
		merchantTransaction.setAmount(amount);
		merchantTransaction.setPaymentTypeId(WALLET_TO_MERCHANT_DEBIT_TYPE_ID);
		merchantTransaction.setRemarks("D");
		merchantTransaction.setInfo(info);
		merchantTransaction.setTransactionStatus(TransactionStatus.SUCCESS);
		merchantTransaction.setMerchantId(merchantId);
		entityManager.persist(merchantTransaction);
	}
}
