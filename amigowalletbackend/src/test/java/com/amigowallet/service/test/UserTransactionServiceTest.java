package com.amigowallet.service.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amigowallet.dao.UserTransactionDAO;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.service.UserTransactionService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTransactionServiceTest 
{
	/**
	 * This attribute is initialized with a mock object as It is annotated with @Mock
	 * Mockito annotation.
	 */
	@MockBean
	private UserTransactionDAO userTransactionDAO;
	
	/**
	 * This attribute is used in all the test case methods of
	 * {@link UserTransactionServiceTest} to invoke the methods of {@link UserTransactionService}. <br>
	 * 
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private UserTransactionService userTransactionService;
	
	@Test
	public void loadMoneyFromDebitCardValid()
	{
		UserTransaction userTransaction =new UserTransaction();
		
		when(userTransactionDAO.loadMoney(any(UserTransaction.class),anyInt())).thenReturn(userTransaction);
		
		UserTransaction userTransactionFromService=userTransactionService.loadMoneyFromDebitCard(500.0, 12121, null);
		
		Assert.assertNotNull(userTransactionFromService);
	}
	
	@Test
	public void loadMoneyFromNetBankingValid()
	{
		UserTransaction userTransaction =new UserTransaction();
		
		when(userTransactionDAO.loadMoney(any(UserTransaction.class),anyInt())).thenReturn(userTransaction);
		
		UserTransaction userTransactionFromService=userTransactionService.loadMoneyFromNetBanking(500.0, 12121, null);
		
		Assert.assertNotNull(userTransactionFromService);
	}
	
	@Test
	public void sendMoneyToBankAccount()
	{
		UserTransaction userTransaction =new UserTransaction();
		
		when(userTransactionDAO.sendMoneyToBankAccount(any(UserTransaction.class),anyInt())).thenReturn(userTransaction);
		
		UserTransaction userTransactionFromService=userTransactionService.sendMoneyToBankAccount(500.0, 12121, null);
		
		Assert.assertNotNull(userTransactionFromService);

		
	}

}
