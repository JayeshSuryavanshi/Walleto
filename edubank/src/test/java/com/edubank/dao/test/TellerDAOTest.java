package com.edubank.dao.test;

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

import com.edubank.dao.TellerDAO;
import com.edubank.model.Teller;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class TellerDAOTest {

	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link TellerDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private TellerDAO tellerDAO;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a negative test case method to test
	 * {@link TellerDAO#getLoginForTeller(String loginName)} method In test data
	 * loginName "T1379" is invalid.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getLoginForTellerInvalidLoginName() throws Exception {
			
		/*
		 * "T1379" is a sample invalid test data in the below statement to
		 * test the method
		 */
		Teller teller = tellerDAO.getLoginForTeller("T1379");
		Assert.assertNull(teller);
			
	}

	/**
	 * This is a positive test case method to test
	 * {@link TellerDAO#getLoginForTeller(String loginName)} method.
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getLoginForTellerValidLoginName() throws Exception {

		/*
		 * "T1378" is a sample valid test data in the below statement to
		 * test the method
		 */
		Teller teller = tellerDAO.getLoginForTeller("T1378");

		/*
		 * The following line of code is to make the test case passed.
		 * If any exception comes from the call to the method
		 * "tellerDAO.getLoginForTeller("T1378")" which is being tested
		 * or return value is null then the test case will not be passed.
		 */
		Assert.assertNotNull(teller);
	}
}
