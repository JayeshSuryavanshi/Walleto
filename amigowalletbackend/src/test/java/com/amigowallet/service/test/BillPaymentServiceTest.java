package com.amigowallet.service.test;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.BillPaymentDAO;
import com.amigowallet.dao.RewardPointsDAO;
import com.amigowallet.dao.UserLoginDAO;
import com.amigowallet.service.BillPaymentServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class BillPaymentServiceTest {
	
	@Rule
	public ExpectedException ee=ExpectedException.none();
	
	@Mock
	private BillPaymentDAO billPaymentDAO;
	
	@Mock
	UserLoginDAO userLoginDAO;
	
	@Mock
	RewardPointsDAO rewardPointsDAO;
	
	@InjectMocks
	public BillPaymentServiceImpl billPayment=new BillPaymentServiceImpl();
	
	@Test
	public void displayServiceTypeValid() {
		List<String> list=new ArrayList<String>();
		list.add("abc");
		Mockito.when(billPaymentDAO.displayServiceType()).thenReturn(list);
		List<String> list1=billPayment.displayServiceType();
		Assert.assertNotNull(list1);
		
	}
	
	@Test
	public void displayMerchantNameTest() {
		List<String> list=new ArrayList<String>();
		list.add("abc");
		Mockito.when(billPaymentDAO.displayMerchantName(Mockito.anyString())).thenReturn(list);
		List<String> list1=billPayment.displayMerchantName("Electricity Bill Payment");
		Assert.assertNotNull(list1);
		
	}

	
	@Test
	public void payBillTestInvalid1() throws Exception{
		ee.expect(Exception.class);
		ee.expectMessage("No Transaction");
		Mockito.when(rewardPointsDAO.getAllTransactionByUserId(Mockito.anyInt())).thenReturn(null);
		billPayment.payBill(12129, 150d,"BSNL");
		
		
		
	}
	
	
	
//	@Test
//	public void payBillTestValid() throws Exception{
//		
//		UserTransaction ut=new UserTransaction();
//		ut.setAmount(100d);
//		List<UserTransaction> listut=new ArrayList<UserTransaction>();
//		listut.add(ut);
//		Mockito.when(rewardPointsDAO.getAllTransactionByUserId(Mockito.anyInt())).thenReturn(listut);
//		Mockito.when(billPaymentDAO.findMerchantId(Mockito.anyString())).thenReturn(1021);
//		Mockito.when(billPaymentDAO.registerMerchantTransaction(Mockito.anyInt(), Mockito.anyDouble(), Mockito.anyInt())).thenReturn(1);
//		Mockito.when(billPaymentDAO.registerUserTransaction(Mockito.anyInt(), Mockito.anyDouble(), Mockito.anyInt())).thenReturn(1);
//		Integer num=billPayment.payBill(1021, 15d,"BSNL");
//		Assert.assertNotNull(num);
//		
//		
//	}
	
	
	
}
