package com.amigowallet.service.test;
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

import com.amigowallet.dao.UserLoginDAO;
import com.amigowallet.dao.WalletToWalletDAO;
import com.amigowallet.model.User;
import com.amigowallet.service.WalletToWalletServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)

public class WalletToWalletServiceTest {
	@Rule
	public ExpectedException ee=ExpectedException.none();
	/**
	 * This attribute is initialized with a mock object as It is annotated with @Mock
	 
	 */
	@Mock
	private WalletToWalletDAO walletTransferDAO;
	@Mock
	UserLoginDAO userLoginDAO;
	/**
	 * This attribute is used in all the test case methods of
	 * {@link WalletToWalletServiceTest} to invoke the methods of {@link WalletToWalletService}. <br>
	 * 
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	
	@InjectMocks
	public WalletToWalletServiceImpl transferToWalletService=new WalletToWalletServiceImpl();
	
	@Test
	public void transferToWalletValidTest() throws Exception{
		User user=new User();
		Mockito.when(userLoginDAO.getUserByEmailId(Mockito.anyString())).thenReturn(user);
		Mockito.when(walletTransferDAO.transferToWallet(Mockito.anyInt(), Mockito.anyDouble(), Mockito.anyString())).thenReturn(1);
		Integer expected=1;
		Integer result=transferToWalletService.transferToWallet(12121, 20d, "josephine@infy.com");
		Assert.assertEquals(expected, result);
		
		
		
	}
	@Test
	public void transferToWalletInvalidTest() throws Exception{
		ee.expect(Exception.class);
		ee.expectMessage("WalletService.TRANSACTION_FAILURE");
		Mockito.when(userLoginDAO.getUserByEmailId("josephina@infy.com")).thenReturn(null);
		transferToWalletService.transferToWallet(12121, 20d, "josephina@infy.com");
		
		
	}
	
	
	
	

}
