package com.amigowallet.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.amigowallet.dao.test.BillPaymentDAOTest;
import com.amigowallet.dao.test.DebitCardDAOTest;
import com.amigowallet.dao.test.ForgotPasswordDAOTest;
import com.amigowallet.dao.test.RegistrationDAOTest;
import com.amigowallet.dao.test.RewardPointsDAOTest;
import com.amigowallet.dao.test.UserLoginDAOTest;
import com.amigowallet.dao.test.UserTransactionDAOTest;
import com.amigowallet.dao.test.WalletToWalletDAOTest;
import com.amigowallet.service.test.BillPaymentServiceTest;
import com.amigowallet.service.test.DebitCardServiceTest;
import com.amigowallet.service.test.ForgotPasswordServiceTest;
import com.amigowallet.service.test.RegistrationServiceTest;
import com.amigowallet.service.test.RewardPointsServiceTest;
import com.amigowallet.service.test.TransactionHistoryServiceTest;
import com.amigowallet.service.test.UserLoginServiceTest;
import com.amigowallet.service.test.UserTransactionServiceTest;

import com.amigowallet.service.test.WalletToWalletServiceTest;
import com.amigowallet.validator.test.RegistrationValidatorTest;
import com.amigowallet.validator.test.UserLoginValidatorTest;

/**
 * this will run all the test cases at a time to include the test
 * cases write all the test class in SuiteClasses

 * @author ETA_JAVA
 *
 */
@RunWith(Suite.class)
/** here we are including all the test cases files which needs to be tested */
@Suite.SuiteClasses({ 
	
	DebitCardDAOTest.class,
	ForgotPasswordDAOTest.class,
	RegistrationDAOTest.class,
	RewardPointsDAOTest.class,
	UserLoginDAOTest.class,
	UserTransactionDAOTest.class,
	WalletToWalletDAOTest.class,
	BillPaymentDAOTest.class,
	
	DebitCardServiceTest.class,
	ForgotPasswordServiceTest.class,
	RegistrationServiceTest.class,
	RewardPointsServiceTest.class,
	UserLoginServiceTest.class,
	UserTransactionServiceTest.class,
	WalletToWalletServiceTest.class,
	TransactionHistoryServiceTest.class,
	BillPaymentServiceTest.class,
	
	UserLoginValidatorTest.class,
	RegistrationValidatorTest.class
})
public class AmigoWalletTestSuite {

}
