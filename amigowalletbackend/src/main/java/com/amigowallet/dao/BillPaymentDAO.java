package com.amigowallet.dao;

import java.util.List;

public interface BillPaymentDAO {
	
	public List<String> displayServiceType();
	public List<String> displayMerchantName(String type);
	public Integer registerMerchantTransaction(Integer merchantId, Double amount,Integer userId)  throws Exception;
	public Integer registerUserTransaction(Integer userId, Double amount,Integer merchantId)  throws Exception;
	public Integer findMerchantId(String name)  throws Exception;
	
	

}
