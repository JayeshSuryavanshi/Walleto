package com.amigowallet.service;

import java.util.List;

public interface BillPaymentService {
	
	public List<String> displayServiceType();
	public List<String> displayMerchantName(String type);
	public Integer payBill(Integer userId,Double amount,String name) throws Exception;
	

}
