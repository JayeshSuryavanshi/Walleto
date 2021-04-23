package com.amigowallet.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amigowallet.dao.BillPaymentDAO;
import com.amigowallet.dao.RewardPointsDAO;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.utility.AmigoWalletConstants;

@Service(value = "billPaymentServiceImpl")
@Transactional
public class BillPaymentServiceImpl implements BillPaymentService{

	@Autowired
	private BillPaymentDAO billPaymentDAO;
	
	@Autowired
	RewardPointsDAO rewardPointsDAO;
	
	
	@Override
	public List<String> displayServiceType() 
	{
		return billPaymentDAO.displayServiceType();
	}
	
	
	@Override
	public List<String> displayMerchantName(String type)
	{
		return billPaymentDAO.displayMerchantName(type);
	}
	
	
	@Override
	public Integer payBill(Integer userId,Double amount,String name) throws Exception
	{
		
		Integer res2=null;
		List<UserTransaction> utlist=rewardPointsDAO.getAllTransactionByUserId(userId);
		if(utlist==null)
		{
	
			throw new Exception("No Transaction");
	}
		else {
		Double balance=0.0;
		for(UserTransaction ut:utlist) {
			
			
			if (AmigoWalletConstants.PAYMENT_TYPE_DEBIT
					.equals(ut.getPaymentType()
							.getPaymentType().toString())) {
				balance -= ut.getAmount();
			} else {
				balance += ut.getAmount();
			}
            
		}
		if(balance<amount)
		{  
			throw new Exception("UserToMerchantPaymentService.INSUFFICIENT_BALANCE");
		}
		else {
			Integer merchantId=billPaymentDAO.findMerchantId(name);
			Integer res1=billPaymentDAO.registerMerchantTransaction(merchantId, amount, userId);
			res2=billPaymentDAO.registerUserTransaction(userId, amount, merchantId);
			if(res1==0||res2==null)				
			{
				
				throw new Exception("UserToMerchantPaymentService.INSUFFICIENT_BALANCE");
			}	
		}
	}
		return res2;
	}
	
	
}
