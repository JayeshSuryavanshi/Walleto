package com.edubank.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edubank.dao.AccountDAO;
import com.edubank.dao.DebitCardDAO;
import com.edubank.model.AccountCustomerMapping;
import com.edubank.model.DebitCard;
import com.edubank.model.DebitCardLockedStatus;
import com.edubank.model.DebitCardStatus;
import com.edubank.utility.Hashing;
import com.edubank.validator.DebitCardValidator;

/**
 * This is service class implementing the service interface
 * {@link DebitCardService}. It has methods which contain business logic related
 * to Debit Cards.
 * 
 * @author ETA_JAVA
 *
 */
@Service(value = "debitCardService")
@Transactional
public class DebitCardServiceImpl implements DebitCardService {
	@Autowired
	private DebitCardDAO debitCardDAO;

	@Autowired
	private AccountDAO accountDAO;

	/**
	 * This method receives a {@link DebitCard} object reference and verifies
	 * the details by calling corresponding DAO class method and throws
	 * exceptions if verification of debit card details fails
	 * 
	 * @param debitCard
	 * 
	 * @throws Exception
	 * @throws Exception
	 * @throws NoSuchAlgorithmException
	 */
	@Override
	public void verifyCardDetails(DebitCard debitCard)
			throws Exception {
		DebitCard debitCardFromDAO = debitCardDAO.getDebitCardDetails(debitCard.getDebitCardNumber());

		if (debitCardFromDAO == null)
			throw new Exception("DebitCardService.DEBITCARD_NOT_FOUND");

		if (!"SAVED_CARD_PAYMENT".equals(debitCard.getCardHolderName())) {
			if (!debitCardFromDAO.getCardHolderName().equalsIgnoreCase(debitCard.getCardHolderName()))
				throw new Exception("DebitCardService.INVALID_CARDHOLDER_NAME");
		}
		String hashValueOfCvv = Hashing.getHashValue(debitCard.getCvv());

		if (!debitCardFromDAO.getCvv().equals(hashValueOfCvv))
			throw new Exception("DebitCardService.INVALID_CVV");
			
		if (!(debitCardFromDAO.getValidThru().getMonth().equals(debitCard.getValidThru().getMonth())
				&& debitCardFromDAO.getValidThru().getYear() == debitCard.getValidThru().getYear()))
			throw new Exception("DebitCardService.INVALID_VALID_THRU");

		if (debitCardFromDAO.getValidThru().isBefore(LocalDate.now()))
			throw new Exception("DebitCardService.DEBITCARD_EXPIRED");

		if (debitCardFromDAO.getLockedStatus().equals(DebitCardLockedStatus.LOCKED))
			throw new Exception("DebitCardService.DEBITCARD_LOCKED");

		if (debitCardFromDAO.getDebitCardStatus().equals(DebitCardStatus.INACTIVE))
			throw new Exception("DebitCardService.DEBITCARD_NOT_ACTIVE");
	}

	/**
	 * This method is used to change the debit card pin, it receives a
	 * {@link DebitCard} object reference, validates the details by calling
	 * corresponding validator class method, Then it verifies the current pin.
	 * If the data is valid, it updates the debit card pin for the current
	 * customer.
	 * 
	 * @param customerId
	 * @param debitCard
	 * 
	 * @throws Exception
	 * @throws Exception
	 * @throws NoSuchAlgorithmException
	 */
	@Override
	public void changeDebitCardPin(Integer customerId, DebitCard debitCard)
			throws Exception {

		/*
		 * This method validates the debit card pin , along while new pin and
		 * confirm new pin while changing the debit card pin. Valid debit card
		 * pin format :- Pin should be of four digit, digits should not be in
		 * sequence
		 */
		DebitCardValidator.validateChangeDebitCardPin(debitCard);

		/*
		 * This method is used to fetch the AccountCustomerMapping object
		 * corresponding to
		 */
		AccountCustomerMapping accountCustomerMapping = accountDAO.getAccountCustomerMappingByCustomerId(customerId);

		/*
		 * This method takes accountCustomerMappingId and returns the
		 * corresponding Debit Card Details
		 */
		DebitCard debitCardFromDao = debitCardDAO
				.getDebitCardDetailsByAccountCustomerMappingId(accountCustomerMapping.getAccountCustomerMappingId());

		/* here we are comparing the hash values of pin */
		if (!debitCardFromDao.getPin().equals(Hashing.getHashValue(debitCard.getPin()))) {
			throw new Exception("DebitCardService.INVALID_CURRENT_PIN");
		}

		/* here we are setting the new pin number for the debit card number */
		debitCard.setNewPin(Hashing.getHashValue(debitCard.getNewPin()));
		debitCard.setDebitCardNumber(debitCardFromDao.getDebitCardNumber());

		/*
		 * This method is used the change the debit card pin. it fetches the
		 * debit card details by the debit card number and updates the pin.
		 */
		debitCardDAO.changeDebitCardPin(debitCard);
	}

	/**
	 * This method is used to add a debit card record for the newly added
	 * customer
	 *
	 * create the debitcardnumber using the <b>Luhn algorithm</b>
	 *
	 * populate the bean and pass it on to the database
	 *
	 * @param mappingId
	 *            , tellerId
	 * 
	 * @return debitCard
	 */
	@Override
	public DebitCard createNewDebitCard(Integer mappingId, Integer tellerId)
			throws Exception {

		DebitCard debitCard = new DebitCard();

		/* here we are setting the values for new debit card */
		String debitCardNumber = createDebitCardNumber();

		/* here we are creating new cvv and pin no using random method */
		String cvv = "" + (int) (100 + Math.random() * 899);
		String pin = "" + (int) (1000 + Math.random() * 8999);
		LocalDate validFrom = LocalDate.now();
		LocalDate validThru = LocalDate.now().plusYears(5);

		debitCard.setAccountCustomerMappingId(mappingId);
		debitCard.setDebitCardNumber(debitCardNumber);
		debitCard.setCvv(cvv);
		debitCard.setDebitCardStatus(DebitCardStatus.ACTIVE);
		debitCard.setLockedStatus(DebitCardLockedStatus.UNLOCKED);
		debitCard.setPin(pin);
		debitCard.setValidFrom(validFrom);
		debitCard.setValidThru(validThru);

		/*
		 * This method is used to add a debit card record for the newly added
		 * customer populate the entity from bean and persist using save()
		 */
		debitCardDAO.addDebitCard(debitCard, tellerId);

		return debitCard;
	}

	/**
	 * The method used to create the debitcardnumber using the <b>Luhn
	 * algorithm</b>
	 * 
	 * The MOD 10 algorithm is a checksum (detection of errors) formula which is
	 * the common name for the Luhn algorithm. This formula has been in use to
	 * validate a lot of identification numbers besides debir cards since its
	 * development by scientist Hans Peter Luhn from IBM
	 *
	 * @return String
	 */
	private String createDebitCardNumber() throws Exception {

		/*
		 * This method is used to add a debit card record for the newly added
		 * customer populate the entity from bean and persist using save()
		 */
		String lastCard = debitCardDAO.getLastCardNumber();

		/*
		 * here we are creating a new no for debit card using previous card no
		 */
		int firstFourDigt = Integer.parseInt(lastCard.substring(0, 4));
		int twoDigSeq = Integer.parseInt(lastCard.substring(4, 6));
		String sixDigIfsc = lastCard.substring(6, 12);
		int threeDigSeq = Integer.parseInt(lastCard.substring(12, 15));

		twoDigSeq++;
		threeDigSeq++;
		String debitCardNo = "" + firstFourDigt + twoDigSeq + sixDigIfsc + threeDigSeq;

		/* The method returns us the last digit of debitCardNo */
		Integer lastDigit = findLastDigit(debitCardNo);

		debitCardNo += lastDigit;
		return debitCardNo;
	}

	/**
	 * The method is a helping method for createDebitCardNumber()
	 * 
	 * @param debitCardNo
	 * 
	 * @return Integer
	 */
	private Integer findLastDigit(String debitCardNo) {

		boolean flag = true;
		Integer first15DigitSum = 0;
		for (int i = debitCardNo.length() - 1; i >= 0; i--) {

			int addingDigit = (int) debitCardNo.charAt(i);
			if (flag) {
				addingDigit *= 2;
			}

			int sum = 0;
			while (addingDigit > 9) {
				sum = 0;
				while (addingDigit > 0) {
					int rem;
					rem = addingDigit % 10;
					sum = sum + rem;
					addingDigit = addingDigit / 10;
				}
				addingDigit = sum;
			}
			first15DigitSum += addingDigit;

		}

		int number = first15DigitSum * 9;

		return number % 10;
	}

	/**
	 * This method is used to verify the pin of the debit card and get the
	 * AccountCustomer MappingId
	 * 
	 * @param debitCardNumber,pin
	 * 
	 * @return mappingId
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 * @throws Exception
	 */
	@Override
	public Integer verifyPinAndGetAccountCustomerMappingId(String debitCardNumber, String pin)
			throws Exception {

		/*
		 * This method is used to get the pin of the debit card and customer
		 * account mappingId
		 */
		Object[] pinAndMappingId = debitCardDAO.getPinAndMappingIdOfdebitCard(debitCardNumber);

		/* here we are comparing the hash values of pin */
		if (!Hashing.getHashValue(pin).equals(pinAndMappingId[0].toString())) {
			throw new Exception("DebitCardService.INVALID_PIN_EXCEPTION");
		}
		return (int) pinAndMappingId[1];
	}
}
