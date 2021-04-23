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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amigowallet.dao.RewardPointsDAO;
import com.amigowallet.model.UserTransaction;
import com.amigowallet.service.RewardPointsService;
import com.amigowallet.utility.AmigoWalletConstants;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RewardPointsServiceTest 
{

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This attribute is initialized with a mock object as It is annotated with @Mock
	 * Mockito annotation.
	 */
	@MockBean
	private RewardPointsDAO rewardPointsDAO;
	
	/**
	 * This attribute is used in all the test case methods of
	 * {@link RewardPointsServiceTest} to invoke the methods of {@link RewardPointsService}. <br>
	 * 
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private RewardPointsService rewardPointsService;

	@Test
	public void redeemRewardPointsMoreThan10() throws Exception
	{
		UserTransaction userTransaction1=new UserTransaction();
		userTransaction1.setPointsEarned(5);
		userTransaction1.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		UserTransaction userTransaction2=new UserTransaction();
		userTransaction2.setPointsEarned(6);
		userTransaction2.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		UserTransaction userTransaction3=new UserTransaction();
		userTransaction3.setPointsEarned(3);
		userTransaction3.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_YES.charAt(0));
		List<UserTransaction> userTransactionsList=new ArrayList<>();
		userTransactionsList.add(userTransaction1);
		userTransactionsList.add(userTransaction2);
		userTransactionsList.add(userTransaction3);
		when(rewardPointsDAO.getAllTransactionByUserId(anyInt())).thenReturn(userTransactionsList);
		rewardPointsService.redeemRewardPoints(12121);
		Assert.assertTrue(true);
	}
	
	@Test
	public void redeemRewardPointsLessThan10() throws Exception
	{
		UserTransaction userTransaction1=new UserTransaction();
		userTransaction1.setPointsEarned(3);
		userTransaction1.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		UserTransaction userTransaction2=new UserTransaction();
		userTransaction2.setPointsEarned(6);
		userTransaction2.setIsRedeemed(AmigoWalletConstants.REWARD_POINTS_REDEEMED_NO.charAt(0));
		List<UserTransaction> userTransactionsList=new ArrayList<>();
		userTransactionsList.add(userTransaction1);
		userTransactionsList.add(userTransaction2);
		when(rewardPointsDAO.getAllTransactionByUserId(anyInt())).thenReturn(userTransactionsList);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("RewardPointsService.REWARD_POINTS_NOT_ENOUGH_TO_REDEEM");
		rewardPointsService.redeemRewardPoints(12170);
	}

}
