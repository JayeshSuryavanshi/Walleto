package com.amigowallet.service;

import java.math.BigDecimal;
import java.util.List;

import com.amigowallet.dto.MoneyTransactionResponse;

public interface BillPaymentService {

	List<String> displayServiceType();

	List<String> displayMerchantName(String type);

	/**
	 * Pays {@code amount} from the authenticated user's wallet to the named
	 * merchant, as one atomic, funds-checked, locked transaction: wallet debit
	 * (wallet-&gt;merchant) + merchant transaction row. Returns the deterministic
	 * points earned and the new balance.
	 */
	MoneyTransactionResponse payBill(Integer userId, BigDecimal amount, String merchantName);
}
