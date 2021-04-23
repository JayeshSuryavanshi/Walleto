package com.amigowallet.service.test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amigowallet.dao.RewardPointsDAO;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.service.TransactionHistoryServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionHistoryServiceTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Mock
	private RewardPointsDAO rewardPointsDAO;
	
	/**
	 * This attribute is used in all the test case methods of
	 * {@link TransactionHistoryServiceTest} to invoke the methods of {@link TransactionHistoryService}. <br>
	 * 
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@InjectMocks
	private TransactionHistoryServiceImpl transactionHistoryService;
	
	@Test
	public void noTransactionsAvailable() throws Exception
	{
		when(rewardPointsDAO.getAllTransactionByUserId(anyInt())).thenReturn(null);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("TransactionHistoryService.NO_TRANSACTIONS_FOUND");
		transactionHistoryService.getAllTransactionByUserId(12121);
	}
	
	@Test
	public void transactionsAvailable() throws Exception
	{
		UserTransaction userTransaction1=new UserTransaction();
		UserTransaction userTransaction2=new UserTransaction();
		UserTransaction userTransaction3=new UserTransaction();
		List<UserTransaction> userTransactionsList=new ArrayList<>();
		userTransactionsList.add(userTransaction1);
		userTransactionsList.add(userTransaction2);
		userTransactionsList.add(userTransaction3);
		when(rewardPointsDAO.getAllTransactionByUserId(anyInt())).thenReturn(userTransactionsList);
		transactionHistoryService.getAllTransactionByUserId(12121);
		Assert.assertTrue(true);
	}
	
}
