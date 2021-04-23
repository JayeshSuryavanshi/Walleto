package com.edubank.dao;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.edubank.entity.AccountCustomerMappingEntity;
import com.edubank.entity.CustomerEntity;
import com.edubank.entity.DebitCardEntity;
import com.edubank.model.DebitCard;
import com.edubank.utility.ApplicationConstants;
import com.edubank.utility.Hashing;

/**
 * This is a DAO class having methods to perform CRUD operations on debit card
 * table.
 * 
 * @author ETA_JAVA
 *
 */
@Repository(value = "debitCardDAO")
public class DebitCardDAOImpl implements DebitCardDAO {
	/** This is a spring auto-wired attribute used to create data base sessions */
	@PersistenceContext
	EntityManager entityManager;

	/**
	 * @see com.edubank.dao.DebitCardDAO#getDebitCardDetails(java.lang.String)
	 */
	@Override
	public DebitCard getDebitCardDetails(String debitCardNumber) throws Exception {

		DebitCard debitCard = null;

		Query query = entityManager.createQuery("from DebitCardEntity where debitCardNumber=:debitCardNumber");
		query.setParameter("debitCardNumber", debitCardNumber);

		List<DebitCardEntity> debitCardEntities = query.getResultList();

		if (debitCardEntities.isEmpty())
			return null;
		DebitCardEntity debitCardEntity = debitCardEntities.get(0);
		AccountCustomerMappingEntity accountCustomerMappingEntity = entityManager
				.find(AccountCustomerMappingEntity.class, debitCardEntity.getAccountCustomerMappingId());

		/*
		 * here again we are fetching the entity class on basis of customerId
		 */
		CustomerEntity customerEntity = entityManager.find(CustomerEntity.class,
				accountCustomerMappingEntity.getCustomerId());

		/*
		 * here we are setting values to bean class from entity that we have fetched
		 * from database on given conditions
		 */
		debitCard = new DebitCard();

		debitCard.setDebitCardNumber(debitCardEntity.getDebitCardNumber());
		debitCard.setCardHolderName(customerEntity.getName());
		debitCard.setCvv(debitCardEntity.getCvv());
		debitCard.setPin(debitCardEntity.getPin());
		debitCard.setValidFrom(debitCardEntity.getValidFrom());
		debitCard.setValidThru(debitCardEntity.getValidThru());
		debitCard.setDebitCardStatus(debitCardEntity.getDebitCardStatus());
		debitCard.setLockedStatus(debitCardEntity.getLockedStatus());

		return debitCard;
	}

	/**
	 * This method is used the change the debit card pin. it fetches the debit card
	 * details by the debit card number and updates the pin.
	 * 
	 * @param debitCard
	 */
	@Override
	public void changeDebitCardPin(DebitCard debitCard) throws Exception {

		
		Query query = entityManager.createQuery("from DebitCardEntity where debitCardNumber=:debitCardNumber");
		query.setParameter("debitCardNumber",  debitCard.getDebitCardNumber());
		
		List<DebitCardEntity> debitCardEntities = query.getResultList();
		if (!debitCardEntities.isEmpty()) {
			debitCardEntities.get(0).setPin(debitCard.getNewPin());
		}

	}

	/**
	 * This method takes accountCustomerMappingId and returns the corresponding
	 * Debit Card Details
	 * 
	 * @param accountCustomerMappingId
	 * 
	 * @return debitCard
	 */
	@Override
	public DebitCard getDebitCardDetailsByAccountCustomerMappingId(Integer accountCustomerMappingId) throws Exception {

		DebitCard debitCard = null;
		Query query = entityManager.createQuery("from DebitCardEntity where accountCustomerMappingId=:debitCardNumber");
		query.setParameter("debitCardNumber",  accountCustomerMappingId);
		
		List<DebitCardEntity> debitCardEntities = query.getResultList();

		/*
		 * here we are setting values to bean class from entity class only if the entity
		 * class exists
		 **/
		if (!debitCardEntities.isEmpty()) {
			DebitCardEntity debitCardEntity = debitCardEntities.get(0);
			debitCard = new DebitCard();

			debitCard.setDebitCardNumber(debitCardEntity.getDebitCardNumber());
			debitCard.setCvv(debitCardEntity.getCvv());
			debitCard.setPin(debitCardEntity.getPin());
			debitCard.setValidFrom(debitCardEntity.getValidFrom());
			debitCard.setValidThru(debitCardEntity.getValidThru());
			debitCard.setDebitCardStatus(debitCardEntity.getDebitCardStatus());
			debitCard.setLockedStatus(debitCardEntity.getLockedStatus());
		}

		return debitCard;
	}

	/**
	 * This method returns the maximum of card numbers inserted if no card number
	 * inserted send a default one
	 *
	 * @return debitCardNumber
	 */
	@Override
	public String getLastCardNumber() throws Exception {

		Query query = entityManager.createQuery("select debitCardNumber from DebitCardEntity order by debitCardNumber desc");
		List<String> cardNumbers = query.getResultList();

		if (cardNumbers.isEmpty()) {
			return ApplicationConstants.FIRST_DEBIT_CARD_NUMBER;
		} else {
			return cardNumbers.get(0);
		}

	}

	/**
	 * This method is used to add a debit card record for the newly added customer
	 *
	 * populate the entity from bean and persist using save()
	 *
	 * @param mappingId,
	 *            tellerId
	 */
	@Override
	public void addDebitCard(DebitCard debitCard, Integer tellerId) throws NoSuchAlgorithmException, Exception {

		/*
		 * here we are creating an object for entity class
		 **/
		DebitCardEntity cardEntity = new DebitCardEntity();

		/*
		 * here we are setting values from bean class to entity
		 **/
		cardEntity.setAccountCustomerMappingId(debitCard.getAccountCustomerMappingId());
		cardEntity.setCvv(Hashing.getHashValue(debitCard.getCvv()));
		cardEntity.setDebitCardNumber(debitCard.getDebitCardNumber());
		cardEntity.setDebitCardStatus(debitCard.getDebitCardStatus());
		cardEntity.setLockedStatus(debitCard.getLockedStatus());
		cardEntity.setPin(Hashing.getHashValue(debitCard.getPin()));
		cardEntity.setValidFrom(debitCard.getValidFrom());
		cardEntity.setValidThru(debitCard.getValidThru());
		cardEntity.setCreatedBy(tellerId);

		/*
		 * here we are adding the Entity to table using save method save method will
		 * return primary key of that row
		 */
		entityManager.persist(cardEntity);

	}

	/**
	 * This method is used to get the pin of the debit card and customer account
	 * mappingId
	 * 
	 * @param debitCardNumber
	 * 
	 * @return pinAndMappingId as an object
	 */
	@Override
	public Object[] getPinAndMappingIdOfdebitCard(String debitCardNumber) throws Exception {
		Object[] pinAndMappingId = null;

		Query query = entityManager.createQuery("select pin, accountCustomerMappingId from DebitCardEntity where debitCardNumber=:debitCardNumber");
		query.setParameter("debitCardNumber",  debitCardNumber);
		
		pinAndMappingId = (Object[]) query.getSingleResult();

		return pinAndMappingId;
	}

}
