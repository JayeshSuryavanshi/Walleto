package com.edubank.dao.test;

import java.time.LocalDate;

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

import com.edubank.dao.DebitCardDAO;
import com.edubank.model.DebitCard;
import com.edubank.model.DebitCardLockedStatus;
import com.edubank.model.DebitCardStatus;
import com.edubank.utility.Hashing;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(true)
public class DebitCardDAOTest {
	/**
	 * This attribute is used in all the test case methods to invoke the methods
	 * of {@link DebitCardDAO}
	 * 
	 * The annotation @Autowired helps to get the object from Spring container,
	 * we do not have to explicitly create the object to use it.
	 */
	@Autowired
	private DebitCardDAO debitCardDAO;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a positive test case method to test
	 * {@link DebitCardDAO#getDebitCardDetails(String)} method.
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void getDebitCardDetailsValid() throws Exception {
			
		/*
		 * This debitCardNumber is a sample valid test data to test the method
		 */
		String debitCardNumber = "6642110005012149";

		DebitCard debitCard = debitCardDAO
				.getDebitCardDetails(debitCardNumber);

		/*
		 * The following line of code is to make the test case passed. If
		 * 'debitCard' variable's value is not null then the test case will
		 * be passed. If 'debitCard' variable's value is null then the test
		 * case will be failed. If any exception comes from the call to the
		 * method "debitCardDAO.getDebitCardDetails(debitCardNumber)" which
		 * is being tested then the following line of code will not be
		 * executed and the test case will not be passed.
		 */
		Assert.assertNotNull(debitCard);
		
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardDAO#getDebitCardDetails(String)} method. <br>
	 * In test data Debit Card Number "664211000501214" is invalid.
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void getDebitCardDetailsInValidDebitCardNumber() throws Exception {
			
		/*
		 * This debitCardNumber is a sample invalid test data to test the method
		 */
		
		String debitCardNumber = "664211000501214";

		DebitCard card = debitCardDAO.getDebitCardDetails(debitCardNumber);
		Assert.assertNull(card);

	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardDAO#getDebitCardDetails(String)} method. <br>
	 * In test data Debit Card Number is set to null.
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void getDebitCardDetailsDebitCardNumberNull() throws Exception {
		
		/* This debitCardNumber is set to null to test the method */
		
		String debitCardNumber = null;

		DebitCard card = debitCardDAO.getDebitCardDetails(debitCardNumber);
		Assert.assertNull(card);

	}

	/**
	 * This is a positive test case method to test
	 * {@link DebitCardDAO#getDebitCardDetailsByAccountCustomerMappingId(Integer)}
	 * method. <br>
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getDebitCardDetailsByAccountCustomerMappingIdValidMappingId() throws Exception {
			
			/*
			 * This accountCustomerMappingId id is a sample valid test data to
			 * test the method
			 */
			Integer accountCustomerMappingId = 200101;

			DebitCard debitCard = debitCardDAO
					.getDebitCardDetailsByAccountCustomerMappingId(accountCustomerMappingId);

			/*
			 * The following line of code is to make the test case passed.
			 * If any exception comes from the call to the method
			 * "debitCardDAO.getDebitCardDetailsByAccountCustomerMappingId
			 * (accountCustomerMappingId)" which is being tested or return
			 * value is null then the test case will not be passed.
			 */
			Assert.assertNotNull(debitCard);
	}

	/**
	 * This is a positive test method to test
	 * {@link DebitCardDAO#changeDebitCardPin(DebitCard)}
	 * 
	 * @throws Exception 
	 * 
	 */
	@Test
	public void changeDebitCardPinValidDetails() throws Exception {

		/*
		 * The following code is to create a DebitCard class object with
		 * sample valid test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("6642120005012155");
		debitCard.setNewPin(Hashing.getHashValue("8742"));

		debitCardDAO.changeDebitCardPin(debitCard);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "debitCardDAO.changeDebitCardPin(debitCard)" which is being
		 * tested then the following line of code will not be executed and
		 * the test case will not be passed.
		 */
		Assert.assertTrue(true);
	}

	/**
	 * This is a positive test method to test
	 * {@link DebitCardDAO#addDebitCard(DebitCard debitCard,Integer tellerId)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void addDebitCardValidData() throws Exception {
			
		/*
		 * The following code is to create a DebitCard class object with
		 * sample valid test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setAccountCustomerMappingId(200274);
		debitCard.setCardHolderName("Nidhi");
		debitCard.setCvv("111");
		debitCard.setPin("1234");
		debitCard.setDebitCardNumber("6642160005012185");
		debitCard.setDebitCardStatus(DebitCardStatus.ACTIVE);
		debitCard.setLockedStatus(DebitCardLockedStatus.UNLOCKED);
		debitCard.setValidFrom(LocalDate.now());
		debitCard.setValidThru(LocalDate.now().plusYears(5));

		debitCardDAO.addDebitCard(debitCard, 102);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "debitCardDAO.addDebitCard(debitCard, 102)" which is being tested
		 * then the following line of code will not be executed and the test
		 * case will not be passed.
		 */
		Assert.assertTrue(true);
	}

	/**
	 * This is a positive test method to test
	 * {@link DebitCardDAO#getLastCardNumber()}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getLastCardNumberValid() throws Exception {
		
		String lastCardNumber = debitCardDAO.getLastCardNumber();

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "debitCardDAO.getLastCardNumber()" which is being tested or return value is null
		 * then the test case will not be passed.
		 */
		Assert.assertNotNull(lastCardNumber);
	}

	/**
	 * This is a positive test method to test
	 * {@link DebitCardDAO#getPinAndMappingIdOfdebitCard(String debitCardNumber)}
	 * 
	 * @throws Exception 
	 */
	@Test
	public void getPinAndMappingIdOfDebitCardValid() throws Exception {
			
		/*
		 * This debitCardNumber is a sample valid test data to test the
		 * method
		 */
		String debitCardNumber = "6642110005012149";

		Object[] pinMappingId = debitCardDAO.getPinAndMappingIdOfdebitCard(debitCardNumber);

		/*
		 * The following line of code is to make the test case passed. If
		 * any exception comes from the call to the method
		 * "debitCardDAO.getPinAndMappingIdOfdebitCard(debitCardNumber)" which is being tested or 
		 * return value is null then the test case will not be passed.
		 */
		Assert.assertNotNull(pinMappingId);
	}
}
