package com.edubank.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.edubank.dao.test.AccountDAOTest;
import com.edubank.dao.test.CustomerDAOTest;
import com.edubank.dao.test.CustomerLoginDAOTest;
import com.edubank.dao.test.DebitCardDAOTest;
import com.edubank.dao.test.ForgotPasswordDAOTest;
import com.edubank.dao.test.NetBankingDAOTest;
import com.edubank.dao.test.SecurityQuestionDAOTest;
import com.edubank.dao.test.TellerDAOTest;
import com.edubank.dao.test.TransactionDAOTest;
import com.edubank.service.test.AccountServiceTest;
import com.edubank.service.test.CustomerLoginServiceTest;
import com.edubank.service.test.CustomerServiceTest;
import com.edubank.service.test.DebitCardServiceTest;
import com.edubank.service.test.ForgotPasswordServiceTest;
import com.edubank.service.test.NetBankingServiceTest;
import com.edubank.service.test.SecurityQuestionServiceTest;
import com.edubank.service.test.TellerServiceTest;
import com.edubank.service.test.TransactionServiceTest;
import com.edubank.validator.test.AccountValidatorTest;
import com.edubank.validator.test.CustomerLoginValidatorTest;
import com.edubank.validator.test.CustomerValidatorTest;
import com.edubank.validator.test.DebitCardValidatorTest;
import com.edubank.validator.test.TellerValidatorTest;
import com.edubank.validator.test.TransactionValidatorTest;

/**
 * the below code will run all the test cases at a time to include the test
 * cases write all the test class in SuiteClasses

 * @author ETA_JAVA
 *
 */
@RunWith(Suite.class)
/** 
 * here we are including all the test cases files which needs to be tested 
 */
@Suite.SuiteClasses({ 
	
		AccountServiceTest.class,
		CustomerLoginServiceTest.class, 
		CustomerServiceTest.class,
		DebitCardServiceTest.class,
		ForgotPasswordServiceTest.class,
		NetBankingServiceTest.class,
		SecurityQuestionServiceTest.class,
		TellerServiceTest.class, 
		TransactionServiceTest.class, 
		
		
		AccountDAOTest.class,
		CustomerDAOTest.class,
		CustomerLoginDAOTest.class, 
		DebitCardDAOTest.class,
		ForgotPasswordDAOTest.class, 
		NetBankingDAOTest.class, 
		TellerDAOTest.class,
		TransactionDAOTest.class,
		SecurityQuestionDAOTest.class,
		
		AccountValidatorTest.class,
		CustomerLoginValidatorTest.class,
		CustomerValidatorTest.class,
		DebitCardValidatorTest.class,
		TellerValidatorTest.class,
		TransactionValidatorTest.class })
public class EDUBankTestSuite 
{
	
}