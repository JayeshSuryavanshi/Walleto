package com.edubank.service.test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.edubank.dao.AccountDAO;
import com.edubank.dao.DebitCardDAO;
import com.edubank.model.AccountCustomerMapping;
import com.edubank.model.DebitCard;
import com.edubank.model.DebitCardLockedStatus;
import com.edubank.model.DebitCardStatus;
import com.edubank.service.DebitCardService;
import com.edubank.service.DebitCardServiceImpl;
import com.edubank.utility.ApplicationConstants;
import com.edubank.utility.Hashing;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DebitCardServiceTest {

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private DebitCardDAO debitCardDAO;

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private AccountDAO accountDAO;

	/**
	 * This attribute is used in all the test case methods of
	 * {@link DebitCardServiceTest} to invoke the methods of
	 * {@link DebitCardService}. <br>
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private DebitCardService debitCardService = new DebitCardServiceImpl();

	/**
	 * Creating expected exception rule for testing exceptions in negative test
	 * case methods.
	 */
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This is a positive test case method to test
	 * {@link DebitCardService#verifyCardDetails(DebitCard)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyCardDetailsValid() throws Exception {
		/*
		 * The following code is to create a DebitCard class object with sample
		 * valid test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("6642110005012149");
		debitCard.setCardHolderName("Martin Luther");
		debitCard.setCvv("367");
		debitCard.setValidThru(LocalDate.of(2027, 4, 6));

		/*
		 * The below statement is to get the DebitCard class object used to mock
		 * the return value of call to DAO class method in service
		 */
		DebitCard debitCardfromDAO = createStubDebitCard(debitCard.getDebitCardNumber(), debitCard.getCardHolderName(),
				debitCard.getCvv(), debitCard.getValidThru(), DebitCardStatus.ACTIVE, DebitCardLockedStatus.UNLOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardfromDAO object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO.getDebitCardDetails(anyString())).thenReturn(debitCardfromDAO);

		debitCardService.verifyCardDetails(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#verifyCardDetails(DebitCard)} method <br>
	 * In test data Debit Card Number 664211000501214 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyCardDetailsInValidDebitCardNumber() throws Exception {
		/*
		 * The following code is to create a DebitCard class object with sample
		 * test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("664211000501214");
		debitCard.setCardHolderName("Martin Luther");
		debitCard.setCvv("367");
		debitCard.setValidThru(LocalDate.of(2027, 4, 6));

		/*
		 * The below statement mocks the DAO class method call in service and
		 * null is returned to the service by the mocked DAO class method
		 */
		when(debitCardDAO.getDebitCardDetails(anyString())).thenReturn(null);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.DEBITCARD_NOT_FOUND");

		debitCardService.verifyCardDetails(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#verifyCardDetails(DebitCard)} method <br>
	 * In test data Debit Card valid thru 2020-4-6 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyCardDetailsInValidExpiryDate() throws Exception {
		/*
		 * The following code is to create a DebitCard class object with sample
		 * test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("6642110005012149");
		debitCard.setCardHolderName("Martin Luther");
		debitCard.setCvv("367");
		debitCard.setValidThru(LocalDate.of(2020, 4, 6));

		/*
		 * The below statement is to get the DebitCard class object used to mock
		 * the return value of call to DAO class method in service
		 */
		DebitCard debitCardfromDAO = createStubDebitCard(debitCard.getDebitCardNumber(), debitCard.getCardHolderName(),
				debitCard.getCvv(), LocalDate.of(2027, 4, 6), DebitCardStatus.ACTIVE, DebitCardLockedStatus.UNLOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardfromDAO object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO.getDebitCardDetails(anyString())).thenReturn(debitCardfromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.INVALID_VALID_THRU");

		debitCardService.verifyCardDetails(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#verifyCardDetails(DebitCard)} method <br>
	 * In test data Debit Card holder name is null
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyCardDetailsDebitCardHolderNameNull() throws Exception {
		/*
		 * The following code is to create a DebitCard class object with sample
		 * test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("6642110005012149");
		debitCard.setCardHolderName(null);
		debitCard.setCvv("367");
		debitCard.setValidThru(LocalDate.of(2027, 4, 6));

		/*
		 * The below statement is to get the DebitCard class object used to mock
		 * the return value of call to DAO class method in service
		 */
		DebitCard debitCardfromDAO = createStubDebitCard(debitCard.getDebitCardNumber(), "Martin Luther",
				debitCard.getCvv(), debitCard.getValidThru(), DebitCardStatus.ACTIVE, DebitCardLockedStatus.UNLOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardfromDAO object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO.getDebitCardDetails(anyString())).thenReturn(debitCardfromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.INVALID_CARDHOLDER_NAME");

		debitCardService.verifyCardDetails(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#verifyCardDetails(DebitCard)} method <br>
	 * In test data Debit Card valid thru 2020-4-6 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyCardDetailsInValidCvv() throws Exception {
		/*
		 * The following code is to create a DebitCard class object with sample
		 * test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("6642110005012149");
		debitCard.setCardHolderName("Martin Luther");
		debitCard.setCvv("123");
		debitCard.setValidThru(LocalDate.of(2027, 4, 6));

		/*
		 * The below statement is to get the DebitCard class object used to mock
		 * the return value of call to DAO class method in service
		 */
		DebitCard debitCardfromDAO = createStubDebitCard(debitCard.getDebitCardNumber(), debitCard.getCardHolderName(),
				"367", LocalDate.of(2027, 4, 6), DebitCardStatus.ACTIVE, DebitCardLockedStatus.UNLOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardfromDAO object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO.getDebitCardDetails(anyString())).thenReturn(debitCardfromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.INVALID_CVV");

		debitCardService.verifyCardDetails(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#verifyCardDetails(DebitCard)} method <br>
	 * In test data Debit Card valid thru 2020-4-6 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyCardDetailsExpiredDebitCard() throws Exception {
		/*
		 * The following code is to create a DebitCard class object with sample
		 * test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("6642110005012149");
		debitCard.setCardHolderName("Martin Luther");
		debitCard.setCvv("367");
		debitCard.setValidThru(LocalDate.of(2016, 4, 6));

		/*
		 * The below statement is to get the DebitCard class object used to mock
		 * the return value of call to DAO class method in service
		 */
		DebitCard debitCardfromDAO = createStubDebitCard(debitCard.getDebitCardNumber(), debitCard.getCardHolderName(),
				debitCard.getCvv(), LocalDate.of(2016, 4, 6), DebitCardStatus.INACTIVE, DebitCardLockedStatus.UNLOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardfromDAO object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO.getDebitCardDetails(anyString())).thenReturn(debitCardfromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.DEBITCARD_EXPIRED");

		debitCardService.verifyCardDetails(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#verifyCardDetails(DebitCard)} method <br>
	 * In test data Debit Card valid thru 2020-4-6 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyCardDetailsInactiveDebitCard() throws Exception {
		/*
		 * The following code is to create a DebitCard class object with sample
		 * test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("6642110005012149");
		debitCard.setCardHolderName("Martin Luther");
		debitCard.setCvv("367");
		debitCard.setValidThru(LocalDate.of(2027, 4, 6));

		/*
		 * The below statement is to get the DebitCard class object used to mock
		 * the return value of call to DAO class method in service
		 */
		DebitCard debitCardfromDAO = createStubDebitCard(debitCard.getDebitCardNumber(), debitCard.getCardHolderName(),
				debitCard.getCvv(), LocalDate.of(2027, 4, 6), DebitCardStatus.INACTIVE, DebitCardLockedStatus.UNLOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardfromDAO object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO.getDebitCardDetails(anyString())).thenReturn(debitCardfromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.DEBITCARD_NOT_ACTIVE");

		debitCardService.verifyCardDetails(debitCard);
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#verifyCardDetails(DebitCard)} method <br>
	 * In test data Debit Card valid thru 2020-4-6 is invalid
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyCardDetailsLockedDebitCard() throws Exception {
		/*
		 * The following code is to create a DebitCard class object with sample
		 * test data.
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber("6642110005012149");
		debitCard.setCardHolderName("Martin Luther");
		debitCard.setCvv("367");
		debitCard.setValidThru(LocalDate.of(2027, 4, 6));

		/*
		 * The below statement is to get the DebitCard class object used to mock
		 * the return value of call to DAO class method in service
		 */
		DebitCard debitCardfromDAO = createStubDebitCard(debitCard.getDebitCardNumber(), debitCard.getCardHolderName(),
				debitCard.getCvv(), LocalDate.of(2027, 4, 6), DebitCardStatus.ACTIVE, DebitCardLockedStatus.LOCKED);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardfromDAO object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO.getDebitCardDetails(anyString())).thenReturn(debitCardfromDAO);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.DEBITCARD_LOCKED");

		debitCardService.verifyCardDetails(debitCard);
	}

	/**
	 * This method receives debit card details as arguments and creates
	 * DebitCard bean which is stub for mocking call to DAO method
	 * 
	 * @param debitCardNumber
	 * @param cardHolderName
	 * @param cvv
	 * @param validThru
	 * @param isActive
	 * @param isLocked
	 * 
	 * @return DebitCard
	 * 
	 */
	private DebitCard createStubDebitCard(String debitCardNumber, String cardHolderName, String cvv,
			LocalDate validThru, DebitCardStatus debitCardStatus, DebitCardLockedStatus lockedStatus) throws NoSuchAlgorithmException {

		/*
		 * The below code creates an object for DebitCard class with the values
		 * passed and returns the object
		 */
		DebitCard debitCard = new DebitCard();
		debitCard.setDebitCardNumber(debitCardNumber);
		debitCard.setCardHolderName(cardHolderName);
		debitCard.setCvv(Hashing.getHashValue(cvv));
		debitCard.setValidThru(validThru);
		debitCard.setDebitCardStatus(debitCardStatus);
		debitCard.setLockedStatus(lockedStatus);

		return debitCard;
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#changeDebitCardPin(Integer customerId, DebitCard debitCard)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void changeDebitCardPinInvalidCurrentPin() throws Exception {
		/*
		 * The following code is to set customer id 101 as sample valid test
		 * data and to create a DebitCard class object with sample test data. In
		 * debit card test data current PIN "5522" is invalid.
		 */
		Integer customerId = 101;

		DebitCard debitCard = new DebitCard();
		debitCard.setPin("5522");
		debitCard.setNewPin("8855");
		debitCard.setConfirmNewPin("8855");
		// ----------------------------------------------------------------------------------

		/*
		 * The below statements are to make the AccountCustomerMapping class
		 * object used to mock the return value of call to DAO class method in
		 * service
		 */
		AccountCustomerMapping accountCustomerMapping = new AccountCustomerMapping();
		accountCustomerMapping.setAccountCustomerMappingId(201);

		/*
		 * The below statements are to make the DebitCard class object used to
		 * mock the return value of call to DAO class method in service
		 */
		DebitCard debitCardFromDao = new DebitCard();
		debitCardFromDao.setPin(Hashing.getHashValue("4524"));

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountCustomerMapping object created above is returned to the
		 * service by the mocked DAO class method
		 */
		when(accountDAO.getAccountCustomerMappingByCustomerId(customerId)).thenReturn(accountCustomerMapping);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardFromDao object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO
				.getDebitCardDetailsByAccountCustomerMappingId(accountCustomerMapping.getAccountCustomerMappingId()))
						.thenReturn(debitCardFromDao);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.INVALID_CURRENT_PIN");

		debitCardService.changeDebitCardPin(customerId, debitCard);
	}

	/**
	 * This is a positive test case method to test
	 * {@link DebitCardService#changeDebitCardPin(Integer customerId, DebitCard debitCard)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void changeDebitCardPinValidCurrentPin() throws Exception {
		/*
		 * The following code is to set customer id 101 as sample valid test
		 * data and to create a DebitCard class object with sample valid test
		 * data.
		 */
		Integer customerId = 101;

		DebitCard debitCard = new DebitCard();
		debitCard.setPin("5522");
		debitCard.setNewPin("8855");
		debitCard.setConfirmNewPin("8855");

		/*
		 * The below statements are to make the AccountCustomerMapping class
		 * object used to mock the return value of call to DAO class method in
		 * service
		 */
		AccountCustomerMapping accountCustomerMapping = new AccountCustomerMapping();
		accountCustomerMapping.setAccountCustomerMappingId(201);

		/*
		 * The below statements are to make the DebitCard class object used to
		 * mock the return value of call to DAO class method in service
		 */
		DebitCard debitCardFromDao = new DebitCard();
		debitCardFromDao.setPin(Hashing.getHashValue("5522"));

		/*
		 * The below statement mocks the DAO class method call in service and
		 * accountCustomerMapping object created above is returned to the
		 * service by the mocked DAO class method
		 */
		when(accountDAO.getAccountCustomerMappingByCustomerId(customerId)).thenReturn(accountCustomerMapping);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * debitCardFromDao object created above is returned to the service by
		 * the mocked DAO class method
		 */
		when(debitCardDAO
				.getDebitCardDetailsByAccountCustomerMappingId(accountCustomerMapping.getAccountCustomerMappingId()))
						.thenReturn(debitCardFromDao);

		debitCardService.changeDebitCardPin(customerId, debitCard);
	}

	/**
	 * This is a positive test case method to test
	 * {@link DebitCardService#createNewDebitCard(Integer mappingId, Integer tellerId)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void createNewDebitCardValidData() throws Exception {
		/*
		 * The below statement mocks the DAO class method call in service and
		 * FIRST_DEBIT_CARD_NUMBER (6642100005012130) from ApplicationConstants
		 * is returned to the service by the mocked DAO class method
		 */
		when(debitCardDAO.getLastCardNumber()).thenReturn(ApplicationConstants.FIRST_DEBIT_CARD_NUMBER);

		/*
		 * The below statement makes a call to
		 * debitCardService.createNewDebitCard(123456, 123) method which is
		 * being tested with sample test data 123456 as mapping Id and 123 as
		 * teller Id.
		 */
		DebitCard debitCard = debitCardService.createNewDebitCard(123456, 123);

		/*
		 * The following line of code is to make the test case passed. If
		 * returned debitCard object's debitCard number first 4 digits are same
		 * as first 4 digits of FIRST_DEBIT_CARD_NUMBER (6642100005012130) from
		 * ApplicationConstants then the test case will be passed else the test
		 * case will be failed. If any exception comes from the call to the
		 * method "debitCardService.createNewDebitCard(123456, 123)" which is
		 * being tested then the following line of code will not be executed and
		 * the test case will be failed.
		 */
		Assert.assertEquals(debitCard.getDebitCardNumber().substring(0, 4),
				ApplicationConstants.FIRST_DEBIT_CARD_NUMBER.substring(0, 4));
	}

	/**
	 * This is a positive test case method to test
	 * {@link DebitCardService#verifyPinAndGetAccountCustomerMappingId(String, String)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyPinAndGetAccountCustomerMappingIdValid() throws Exception {
		/*
		 * This debitCardNumber is a sample valid test data to test the method
		 */
		String debitCardNumber = "6642110005012149";

		Object[] pinMappingId = { Hashing.getHashValue("5709"), 200035 };
		when(debitCardDAO.getPinAndMappingIdOfdebitCard(debitCardNumber)).thenReturn(pinMappingId);

		Integer mappingId = debitCardService.verifyPinAndGetAccountCustomerMappingId(debitCardNumber, "5709");
		Assert.assertTrue(pinMappingId[1].equals(mappingId));
	}

	/**
	 * This is a negative test case method to test
	 * {@link DebitCardService#verifyPinAndGetAccountCustomerMappingId(String, String)}
	 * method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void verifyPinAndGetAccountCustomerMappingIdInvalidPin() throws Exception {
		/*
		 * This debitCardNumber is a sample valid test data to test the method
		 */
		String debitCardNumber = "6642110005012149";

		Object[] pinMappingId = { Hashing.getHashValue("5709"), 200035 };
		when(debitCardDAO.getPinAndMappingIdOfdebitCard(debitCardNumber)).thenReturn(pinMappingId);

		/*
		 * Populating expected exception rule to expect exceptions of Exception
		 * type with a specific message.
		 */
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.INVALID_PIN_EXCEPTION");

		debitCardService.verifyPinAndGetAccountCustomerMappingId(debitCardNumber, "5708");
	}
}
