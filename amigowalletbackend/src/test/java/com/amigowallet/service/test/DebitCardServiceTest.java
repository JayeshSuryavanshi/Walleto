package com.amigowallet.service.test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.amigowallet.dao.DebitCardDAO;
import com.amigowallet.model.Bank;
import com.amigowallet.model.Card;
import com.amigowallet.model.CardStatus;
import com.amigowallet.service.DebitCardService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DebitCardServiceTest 
{

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	/**
	 * This attribute is initialized with a mock object as It is annotated with @Mock
	 * Mockito annotation.
	 */
	@MockBean
	private DebitCardDAO debitCardDAO;
	
	/**
	 * This attribute is used in all the test case methods of
	 * {@link DebitCardServiceTest} to invoke the methods of {@link DebitCardService}. <br>
	 * 
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private DebitCardService debitCardService;

	@Test
	public void fetchAllBankDetailsValid()
	{
		List<Bank> banksListFromDAO=new ArrayList<>();
		Bank bank=new Bank();
		bank.setBankId(101);
		bank.setBankName("Java Bank");
		banksListFromDAO.add(bank);
		when(debitCardDAO.fetchAllBankDetails()).thenReturn(banksListFromDAO);
		List<Bank> banksList=debitCardService.fetchAllBankDetails();
		Assert.assertTrue(banksList.size()==1 && banksList.get(0).getBankId().equals(101) &&
				banksList.get(0).getBankName().equals("Java Bank"));
	}

	@Test
	public void addCardNonExistingValidCard() throws Exception
	{
		Card card=new Card();			
		card.setCardNumber("6642190005012220");
		card.setExpiryDate(LocalDate.now().plusYears(5));
		when(debitCardDAO.fetchCardByUserId(anyInt())).thenReturn(new ArrayList<Card>());
		debitCardService.addCard(card, 12170);
		Assert.assertTrue(true);
	}
	
	@Test
	public void addCardExistingDeactiveCard() throws Exception
	{
		Card card=new Card();
		card.setCardNumber("6642190005012220");
		card.setExpiryDate(LocalDate.now().plusYears(5));
		Card cardFromDAO1=new Card();
		cardFromDAO1.setCardNumber("6642190005012220");
		cardFromDAO1.setExpiryDate(LocalDate.now().plusYears(5));
		Card cardFromDAO2=new Card();
		cardFromDAO2.setCardNumber("6642190005012221");
		cardFromDAO2.setExpiryDate(LocalDate.now().plusYears(5));
		CardStatus cardStatus = CardStatus.INACTIVE;
		cardFromDAO1.setCardStatus(cardStatus);;
		List<Card> cardsListFromDAO=new ArrayList<Card>();
		cardsListFromDAO.add(cardFromDAO2);
		cardsListFromDAO.add(cardFromDAO1);
		when(debitCardDAO.fetchCardByUserId(anyInt())).thenReturn(cardsListFromDAO);
		debitCardService.addCard(card, 12170);
		Assert.assertTrue(true);
	}
	
	@Test
	public void addCardExistingActiveCard() throws Exception
	{
		
		Card card=new Card();
		card.setCardNumber("6642190005012220");
		card.setExpiryDate(LocalDate.now().plusYears(5));
		Card cardFromDAO= new Card();
		cardFromDAO.setCardNumber("6642190005012220");
		cardFromDAO.setExpiryDate(LocalDate.now().plusYears(5));
		CardStatus cardStatus = CardStatus.ACTIVE;
		cardFromDAO.setCardStatus(cardStatus);
		List<Card> cardsListFromDAO=new ArrayList<Card>();
		cardsListFromDAO.add(cardFromDAO);
		when(debitCardDAO.fetchCardByUserId(anyInt())).thenReturn(cardsListFromDAO);
		expectedException.expect(Exception.class);
		expectedException.expectMessage("DebitCardService.CARD_ALREADY_EXIST");
		debitCardService.addCard(card, 12170);
	}
	
	@Test
	public void deleteCardValid()
	{
		Card card=new Card();
		card.setCardId(12346);		
		debitCardService.deleteCard(card);
		Assert.assertTrue(true);
	}

}
