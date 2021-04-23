package com.amigowallet.dao.test;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.BillPaymentDAO;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class BillPaymentDAOTest {

	@Rule
	public ExpectedException ee=ExpectedException.none();
	
	@Autowired
	private BillPaymentDAO billPaymentDAO;
	
	@Test
	public void displayServiceTypeValid() throws Exception{
		List<String> list =billPaymentDAO.displayServiceType();
		Assert.assertNotNull(list);
	}
	
	
	@Test
	public void displayMerchantNameValid() {
		List<String> list =billPaymentDAO.displayMerchantName("Electricity Bill Payment");
		Assert.assertNotNull(list);
		
	}
	
	@Test
	public void registerMerchantTransactionValid() throws Exception{
		Integer num=billPaymentDAO.registerMerchantTransaction(1021, 150d, 12129);
		Integer num1=1;
		Assert.assertEquals(num1, num);
	}
	
	@Test
	public void registerMerchantTransactionInvalid() throws Exception{
		Integer num=billPaymentDAO.registerMerchantTransaction(1021, 150d, 19);
		Integer num1=0;
		Assert.assertEquals(num1, num);
	}
	
	@Test
	public void registerUserTransactionValid()  throws Exception{
		Integer num=billPaymentDAO.registerUserTransaction(12129, 150d, 1021);
		Assert.assertNotNull(num);
		
	}
	
	@Test
	public void findMerchantIdValid() throws Exception{
		Integer num=billPaymentDAO.findMerchantId("BSNL");
		Assert.assertNotNull(num);
	}
	
	
	
}
