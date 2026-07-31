package com.amigowallet.dao;

import java.math.BigDecimal;
import java.util.List;

public interface BillPaymentDAO {

	List<String> displayServiceType();

	List<String> displayMerchantName(String type);

	/**
	 * Resolves a merchant id by name.
	 *
	 * @throws com.amigowallet.exception.ApiException 404 if no such merchant.
	 */
	Integer findMerchantId(String name);

	/**
	 * Records the merchant side of a bill payment as a wallet-&gt;merchant DEBIT
	 * ({@code PAYMENT_TYPE_ID = 6}) MERCHANT_TRANSACTION row. Must be called within
	 * the same transaction as the payer's wallet debit.
	 */
	void registerMerchantTransaction(Integer merchantId, BigDecimal amount, String info);
}
