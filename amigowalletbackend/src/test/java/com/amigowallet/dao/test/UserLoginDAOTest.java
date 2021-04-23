package com.amigowallet.dao.test;

import java.security.NoSuchAlgorithmException;

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

import com.amigowallet.dao.UserLoginDAO;
import com.amigowallet.model.User;
import com.amigowallet.utility.HashingUtility;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class UserLoginDAOTest 
{

	/**
	 * This attribute is used in all the test case methods to invoke the methods of {@link CustomerLoginDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private UserLoginDAO userLoginDAO;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	/**
	 * This is a positive test case method to test
	 * {@link UserLoginDAO#changeUserPassword(User user)}
	 * 
	 */
	@Test
	public void changeUserPasswordValidDetails() throws NoSuchAlgorithmException
	{
		User user = new User();
		user.setUserId(12121);
		user.setNewPassword(HashingUtility.getHashValue("James$123"));
		userLoginDAO.changeUserPassword(user);
		Assert.assertTrue(true);
	}
	
	@Test
	public void getUserByEmailIdValidEmail() throws Exception
	{
		User user = userLoginDAO.getUserByEmailId("james@infy.com");		
		if(user==null)
			Assert.assertTrue(false);
		else
			Assert.assertTrue(true);
	}
	
	@Test
	public void getUserByEmailIdInvalidEmail() throws Exception
	{
		
		User user = userLoginDAO.getUserByEmailId("abcdef");
		Assert.assertNull(user);
	}
	
	@Test
	public void getUserByUserIdValidUserId()
	{
		User user = userLoginDAO.getUserByUserId(12121);
		if(user==null)
			Assert.assertTrue(false);
		else
			Assert.assertTrue(true);
	}
	
	@Test
	public void getUserByUserIdInvalidUserId()
	{
		User user = userLoginDAO.getUserByUserId(100);
		if(user==null)
			Assert.assertTrue(true);
		else
			Assert.assertTrue(false);
	}
}
