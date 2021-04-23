package com.edubank.service.test;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.edubank.dao.NetBankingDAO;
import com.edubank.model.Transaction;
import com.edubank.model.TransactionType;
import com.edubank.service.NetBankingService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NetBankingServiceTest {

	/**
	 * This attribute is initialized with a mock object as It is annotated
	 * with @Mock Mockito annotation.
	 */
	@MockBean
	private NetBankingDAO netBankingDAO;

	/**
	 * This attribute is used in all the test case methods of
	 * {@link NetBankingServiceTest} to invoke the methods of
	 * {@link NetBankingService}. <br>
	 * It is annotated with @InjectMocks Mockito annotation which makes it to
	 * use mocked objects.
	 */
	@Autowired
	private NetBankingService netBankingService;

	/**
	 * This is a positive test case method to test
	 * {@link NetBankingService#payByNetBanking(Transaction transaction)} method
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void payByNetBankingValid() throws Exception {
		/*
		 * The following code is to create a Transaction class object with
		 * sample test data.
		 */
		Transaction transaction = new Transaction();
		transaction.setAccountNumber("632941935815667");
		transaction.setAmount(10.0);
		transaction.setInfo("From:- Account To:- AmigoWallet, Reason:- Payment");
		transaction.setRemarks("RemarksDATA");
		transaction.setTransactionDateTime(LocalDateTime.now());
		transaction.setTransactionMode("Internet-Banking");
		transaction.setType(TransactionType.DEBIT);

		/*
		 * The below statement mocks the DAO class method call in service and
		 * 1234 is returned to the service by the mocked DAO class method
		 */
		when(netBankingDAO.payByNetBanking(transaction)).thenReturn(1234L);

		Assert.assertEquals(new Long(1234L), netBankingService.payByNetBanking(transaction));
	}
}
