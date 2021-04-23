package com.amigowallet.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amigowallet.entity.BankEntity;
import com.amigowallet.entity.CardEntity;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;
import com.amigowallet.model.CardStatus;

/**
 * This is a DAO class having methods to perform CRUD operations on debit card table.
 *
 * @author ETA_JAVA
 *
 */
@Repository(value = "debitCardDao")
public class DebitCardDAOImpl implements DebitCardDAO {
	
	@Autowired
	EntityManager entityManager;
	
	/**
	 * 
	 * This method is used to delete the saved card<br>
	 * It changes the status of the card to deactive.
	 * 
	 * @param card
	 */
	@Override
	public void deleteCard(Card card) {
		
		
		/*
		 * Card Entity is fetched using the cardId
		 */
		CardEntity cardEntity = entityManager.find(CardEntity.class, card.getCardId());
	
		/*
		 * Here we are setting the statusEntity only if the cardEntity exists 
		 * 
		 */
		if (cardEntity != null) {
			cardEntity.setUserStatus(CardStatus.INACTIVE);
		}

	}
	/**
	 * 
	 * This method is used to fetch card details of a given user
	 * 
	 * @param userId
	 * 
	 * @return List of cards
	 */
	@Override
	public List<Card> fetchCardByUserId(Integer userId) {
		
		/*
		 * User entity is fetched using the userId and card entities
		 * for the user entity is fetched and populated to a list
		 */
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		List<CardEntity> cardList = userEntity.getCardEntities();
		
		/*
		 * For each cardEntity in the list,the details are populated to a
		 * card bean and added to another list
		 */
		List<Card> cards = new ArrayList<>();
		for (CardEntity cardEntity : cardList) {
			
			Card card1 = new Card();
			card1.setCardId(cardEntity.getCardId());
			card1.setCardNumber(cardEntity.getCardNumber());
			card1.setExpiryDate(cardEntity.getExpiryDate());
			card1.setCardStatus(cardEntity.getUserStatus());
			
		
			/*
			 * Bank bean properties are set here 
			 */
			Bank bank = new Bank();
			bank.setBankId(cardEntity.getBankEntity().getBankId());
			bank.setBankName(cardEntity.getBankEntity().getBankName());
			
			/*
			 * Status and Bank details are populated to card bean
			 */
			card1.setBank(bank);
			cards.add(card1);
		}
		return cards;
	}
	
	/**
	 * 
	 * This method is used for adding a new card
	 * 
	 * @param card
	 * @param userId
	 */
	@Override
	public Card addNewCard(Card card, Integer userId) {
		
		/*
		 * User entity is fetched using the userId and card entities
		 * for the user entity is fetched and populated to a list
		 */
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		List<CardEntity> cardList = userEntity.getCardEntities();	
		
		/*
		 * A new cardEntity is populated using the properties of the card
		 * to be added.
		 */
		
		CardEntity cardEntity = new CardEntity();
		cardEntity.setCardNumber(card.getCardNumber());
		cardEntity.setExpiryDate(card.getExpiryDate());
		
	
		/*
		 * Fetching the Bank entity using bankId
		 */
		BankEntity bankEntity = entityManager.find(BankEntity.class, card.getBank().getBankId());

	
		/*
		 * The bankEntity and statusEntity fetched above are set to the cardEntity
		 */
		cardEntity.setBankEntity(bankEntity);
		cardEntity.setUserStatus(CardStatus.ACTIVE);
		
		/*
		 * The cardEntity is added to the card list of the user
		 * and is set to the userEntit
		 */
		cardList.add(cardEntity);
		userEntity.setCardEntities(cardList);

		
		card.setCardId(cardEntity.getCardId());		
		
		return card;

	}
	
	/**
	 * 
	 * This method is activating an already existing card
	 * 
	 * @param userId
	 * @param cardId
	 */
	@Override
	public void activateCard(Integer cardId, Integer userId) {
		
		/*
		 * User entity is fetched using the userId and card entities
		 * for the user entity is fetched and populated to a list
		 */
		UserEntity userEntity = entityManager.find(UserEntity.class, userId);
		
		List<CardEntity> cardList = userEntity.getCardEntities();
		
		/*
		 * CardEntity having the same cardId as the card to be activated
		 * is found from the card list
		 */
		for (CardEntity cardEntity : cardList) {
			if (cardEntity.getCardId().equals(cardId)) {
				
				/*
				 * The active status is set to the cardEnity
				 */
				cardEntity.setUserStatus(CardStatus.ACTIVE);
				break;
			}
		}
	}
	
	/**
	 * 
	 * This method is for fetching all the bank details
	 * 
	 * @return List of banks
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Bank> fetchAllBankDetails() {
		
		List<BankEntity> bankEntityList = entityManager.createQuery("from BankEntity").getResultList();
		
		/*
		 * For each bankEntity in the list,a bank bean is populated with
		 * the corresponding properties and added to another list
		 */
		List<Bank> bankList = new ArrayList<>();
		for (BankEntity bankEntity : bankEntityList) {
			
			Bank bank = new  Bank();
			bank.setBankId(bankEntity.getBankId());
			bank.setBankName(bankEntity.getBankName());
			bankList.add(bank);
		}
		
		return bankList;
	}
}