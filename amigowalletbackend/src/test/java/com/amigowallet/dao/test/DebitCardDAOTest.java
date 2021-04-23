package com.amigowallet.dao.test;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.amigowallet.dao.DebitCardDAO;
import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;

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

	@Test
	public void deleteCardValidCardId() {
		Card card = new Card();
		card.setCardId(12346);
		debitCardDAO.deleteCard(card);
		Assert.assertTrue(true);
	}

	@Test
	public void fetchCardByUserIdValidUserId() {
		List<Card> list = debitCardDAO.fetchCardByUserId(12121);
		if (list.isEmpty()) {
			Assert.assertTrue(false);
		} else {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void addNewCard() {
		Card card = new Card();
		card.setCardNumber("1234567891201023");
		card.setExpiryDate(LocalDate.now().plusYears(10));
		Bank bank = new Bank();
		bank.setBankId(101);
		card.setBank(bank);
		debitCardDAO.addNewCard(card, 12121);
		Assert.assertTrue(true);
	}

	@Test
	public void addNewCardInvalidBankId() {
		Card card = new Card();
		card.setCardNumber("1234567891201023");
		card.setExpiryDate(LocalDate.now().plusYears(10));
		Bank bank = new Bank();
		bank.setBankId(22);
		card.setBank(bank);
		debitCardDAO.addNewCard(card, 12121);
		Assert.assertTrue(true);
	}

	@Test
	public void activateCardValidCardId() {
		debitCardDAO.activateCard(12346, 12121);
		Assert.assertTrue(true);
	}

	@Test
	public void fetchAllBankDetails() {
		List<Bank> banks = debitCardDAO.fetchAllBankDetails();
		if (banks.isEmpty())
			Assert.assertTrue(false);
		else
			Assert.assertTrue(true);
	}
}
