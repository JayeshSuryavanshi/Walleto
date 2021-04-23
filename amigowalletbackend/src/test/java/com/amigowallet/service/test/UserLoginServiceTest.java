package com.amigowallet.service.test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amigowallet.dao.UserLoginDAO;
import com.amigowallet.model.Card;
import com.amigowallet.model.CardStatus;
import com.amigowallet.model.PaymentType;
import com.amigowallet.model.User;
import com.amigowallet.model.UserStatus;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.service.UserLoginService;
import com.amigowallet.utility.AmigoWalletConstants;
import com.amigowallet.utility.HashingUtility;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserLoginServiceTest 
{

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This attribute is initialized with a mock object as It is annotated with @Mock
	 * Mockito annotation.
	 */
	@MockBean
	private UserLoginDAO userLoginDAO;
	
	/**
	 * This attribute is used in all the test case methods of
	 * {@link UserLoginServiceTest} to invoke the methods of {@link UserLoginService}. <br>
	 * 
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private UserLoginService userLoginService;

	@Test
	public void authenticateValidUser() throws Exception
	{
		User user=new User();
		user.setEmailId("james@infy.com");
		user.setPassword("James#123");
		
		User userFromDAO=new User();
		userFromDAO.setEmailId("james@infy.com");
		userFromDAO.setPassword(HashingUtility.getHashValue("James#123"));
		UserStatus userStatus=UserStatus.ACTIVE;
		userFromDAO.setUserStatus(userStatus);
		
		CardStatus cardStatus1=CardStatus.ACTIVE;
		
		CardStatus cardStatus2=CardStatus.INACTIVE;
		
		Card card1=new Card();
		card1.setCardStatus(cardStatus1);
		
		Card card2=new Card();
		card2.setCardStatus(cardStatus2);
		
		List<Card> cardsList=new ArrayList<>();
		cardsList.add(card1);
		cardsList.add(card2);
		
		PaymentType paymentType1=new PaymentType();
		paymentType1.setPaymentType(AmigoWalletConstants.PAYMENT_TYPE_DEBIT.charAt(0));
		
		PaymentType paymentType2=new PaymentType();
		paymentType2.setPaymentType(AmigoWalletConstants.PAYMENT_TYPE_CREDIT.charAt(0));
		
		UserTransaction userTransaction1=new UserTransaction();
		userTransaction1.setAmount(500.0);
		userTransaction1.setPaymentType(paymentType1);
		userTransaction1.setPointsEarned(60);
		userTransaction1.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		
		UserTransaction userTransaction2=new UserTransaction();
		userTransaction2.setAmount(1500.0);
		userTransaction2.setPaymentType(paymentType2);
		userTransaction2.setPointsEarned(30);
		userTransaction2.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
		
		List<UserTransaction> userTransactionsList=new ArrayList<>();
		userTransactionsList.add(userTransaction1);
		userTransactionsList.add(userTransaction2);
		
		userFromDAO.setCards(cardsList);
		userFromDAO.setUserTransactions(userTransactionsList);
		
		when(userLoginDAO.getUserByEmailId(anyString())).thenReturn(userFromDAO);
		
		User userFromService=userLoginService.authenticate(user);
		
		Assert.assertTrue(userFromService.getBalance().equals(1000.0) && userFromDAO.getRewardPoints().equals(60));
	}
	
	@Test
	public void authenticateInvalidUser() throws Exception
	{
		User user=new User();			
		user.setEmailId("joseph@infy.com");
		user.setPassword("Joseph#123");	

		when(userLoginDAO.getUserByEmailId(anyString())).thenReturn(null);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.INVALID_CREDENTIALS");
		userLoginService.authenticate(user);
	}
	
	@Test
	public void authenticateInactiveUser() throws Exception
	{
		User user=new User();	
		user.setEmailId("james@infy.com");
		user.setPassword("James#123");	
		
		User userFromDAO=new User();
		userFromDAO.setEmailId("james@infy.com");
		userFromDAO.setPassword(HashingUtility.getHashValue("James#123"));
		
		userFromDAO.setUserStatus(UserStatus.INACTIVE);
		
		when(userLoginDAO.getUserByEmailId(anyString())).thenReturn(userFromDAO);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("UserLoginService.INACTIVE_USER");
		userLoginService.authenticate(user);
	}
	
	@Test
	public void authenticateIncorrectPassword() throws Exception
	{
		User user=new User();	
		user.setEmailId("james@infy.com");
		user.setPassword("James$123");	
		
		User userFromDAO=new User();
		userFromDAO.setEmailId("james@infy.com");
		userFromDAO.setPassword(HashingUtility.getHashValue("James#123"));
		
		when(userLoginDAO.getUserByEmailId(anyString())).thenReturn(userFromDAO);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.INVALID_CREDENTIALS");
		userLoginService.authenticate(user);
	}
	
	@Test
	public void getUserbyUserIdValidUser() throws Exception
	{
		User userFromDAO=new User();
		userFromDAO.setEmailId("james@infy.com");
		userFromDAO.setPassword(HashingUtility.getHashValue("James#123"));
		
		CardStatus cardStatus1=CardStatus.ACTIVE;
		
		CardStatus cardStatus2=CardStatus.INACTIVE;
		
		Card card1=new Card();
		card1.setCardStatus(cardStatus1);
		
		Card card2=new Card();
		card2.setCardStatus(cardStatus2);
		
		List<Card> cardsList=new ArrayList<>();
		cardsList.add(card1);
		cardsList.add(card2);
		
		PaymentType paymentType1=new PaymentType();
		paymentType1.setPaymentType(AmigoWalletConstants.PAYMENT_TYPE_DEBIT.charAt(0));
		
		PaymentType paymentType2=new PaymentType();
		paymentType2.setPaymentType(AmigoWalletConstants.PAYMENT_TYPE_CREDIT.charAt(0));
		
		UserTransaction userTransaction1=new UserTransaction();			
		userTransaction1.setAmount(500.0);
		userTransaction1.setPaymentType(paymentType1);
		userTransaction1.setPointsEarned(60);
		userTransaction1.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		
		UserTransaction userTransaction2=new UserTransaction();
		userTransaction2.setAmount(1500.0);
		userTransaction2.setPaymentType(paymentType2);
		userTransaction2.setPointsEarned(30);
		userTransaction2.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
		
		List<UserTransaction> userTransactionsList=new ArrayList<>();
		userTransactionsList.add(userTransaction1);
		userTransactionsList.add(userTransaction2);
		
		userFromDAO.setCards(cardsList);
		userFromDAO.setUserTransactions(userTransactionsList);
		
		when(userLoginDAO.getUserByUserId(anyInt())).thenReturn(userFromDAO);
		
		User userFromService=userLoginService.getUserbyUserId(12121);
		
		Assert.assertTrue(userFromService.getBalance().equals(1000.0) && userFromDAO.getRewardPoints().equals(60));
	}
	
	@Test
	public void getUserbyUserIdInvalidUser() throws Exception
	{
		when(userLoginDAO.getUserByUserId(anyInt())).thenReturn(null);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.INVALID_CREDENTIALS");
		userLoginService.getUserbyUserId(12345);
	}
	
	@Test
	public void changeUserPasswordValidUser() throws Exception
	{
		User user=new User();
		user.setUserId(1234);
		user.setEmailId("james@infy.com");
		user.setPassword("James#123");	
		user.setNewPassword("James$123");
		user.setConfirmNewPassword("James$123");
		
		User userFromDAO=new User();	
		userFromDAO.setEmailId("james@infy.com");
		userFromDAO.setPassword(HashingUtility.getHashValue("James#123"));
		
		when(userLoginDAO.getUserByUserId(anyInt())).thenReturn(userFromDAO);
		
		userLoginService.changeUserPassword(user);
		
		Assert.assertTrue(true);
	}
	
	@Test
	public void changeUserPasswordInvalidUser() throws Exception
	{
		User user=new User();
		user.setEmailId("james@infy.com");
		user.setPassword("James#123");	
		user.setNewPassword("James$123");
		user.setConfirmNewPassword("James$123");
		
		when(userLoginDAO.getUserByUserId(anyInt())).thenReturn(null);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.USER_NOT_FOUND");
		userLoginService.changeUserPassword(user);
	}
	
	@Test
	public void changeUserPasswordIncorrectCurrentPassword() throws Exception
	{
		User user=new User();	
		user.setUserId(1234);
		user.setEmailId("james@infy.com");
		user.setPassword("James!123");	
		user.setNewPassword("James$123");
		user.setConfirmNewPassword("James$123");
		
		User userFromDAO=new User();		
		userFromDAO.setEmailId("james@infy.com");
		userFromDAO.setPassword(HashingUtility.getHashValue("James#123"));
		
		when(userLoginDAO.getUserByUserId(anyInt())).thenReturn(userFromDAO);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("LoginService.INVALID_PASSWORD");
		userLoginService.changeUserPassword(user);
	}

}
