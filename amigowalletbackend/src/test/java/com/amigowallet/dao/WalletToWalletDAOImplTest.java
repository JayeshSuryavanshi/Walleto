package com.amigowallet.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.amigowallet.dto.MoneyTransactionResponse;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.exception.ApiException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 * Wallet-to-wallet orchestration: self-transfer rejection, unknown-recipient
 * rejection, deterministic reward points (floor(amount/100)) and the capped 2%
 * cashback (only for genuine transfers >= 200). The funds check + locking are
 * delegated to a mocked {@link WalletLedgerDAO}, so this focuses on the transfer
 * rules; the recipient lookup {@link EntityManager} query is mocked too.
 */
@ExtendWith(MockitoExtension.class)
class WalletToWalletDAOImplTest {

	private static final Integer SENDER = 42;
	private static final Integer RECEIVER = 99;

	@Mock
	private EntityManager entityManager;

	@Mock
	private TypedQuery<Object[]> recipientQuery;

	@Mock
	private WalletLedgerDAO walletLedger;

	private WalletToWalletDAOImpl dao;

	@BeforeEach
	void setUp() {
		dao = new WalletToWalletDAOImpl();
		ReflectionTestUtils.setField(dao, "entityManager", entityManager);
		ReflectionTestUtils.setField(dao, "walletLedger", walletLedger);
	}

	private void stubRecipientLookup(List<Object[]> rows) {
		when(entityManager.createQuery(anyString(), eq(Object[].class))).thenReturn(recipientQuery);
		when(recipientQuery.setParameter(anyString(), any())).thenReturn(recipientQuery);
		when(recipientQuery.getResultList()).thenReturn(rows);
	}

	private UserEntity user(Integer id, String email) {
		UserEntity u = new UserEntity();
		u.setUserId(id);
		u.setEmailId(email);
		return u;
	}

	/** Stubs the locked loads + ledger postings so the transfer can complete. */
	private void stubLedger(String senderBalanceAfterDebit) {
		when(walletLedger.lockUser(SENDER)).thenReturn(user(SENDER, "sender@x.com"));
		when(walletLedger.lockUser(RECEIVER)).thenReturn(user(RECEIVER, "r@x.com"));
		when(walletLedger.debit(any(), any(), anyChar(), anyChar(), anyString(), anyInt()))
				.thenReturn(new WalletLedgerDAO.LedgerEntry(null, new BigDecimal(senderBalanceAfterDebit)));
		when(walletLedger.credit(any(), any(), anyChar(), anyChar(), anyString(), anyInt(), anyChar()))
				.thenReturn(new WalletLedgerDAO.LedgerEntry(null, new BigDecimal(senderBalanceAfterDebit)));
	}

	@Test
	void selfTransfer_isRejected_400() {
		// Recipient email resolves back to the sender's own id.
		stubRecipientLookup(List.<Object[]>of(new Object[] { SENDER, "self@x.com" }));

		assertThatThrownBy(() -> dao.transferToWallet(SENDER, new BigDecimal("250.00"), "self@x.com"))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> {
					ApiException ae = (ApiException) e;
					assertThat(ae.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
					assertThat(ae.getMessageKey()).isEqualTo("WalletService.PAYING_HIMSELF");
				});

		verify(walletLedger, never()).debit(any(), any(), anyChar(), anyChar(), anyString(), anyInt());
	}

	@Test
	void unknownRecipient_isRejected_404() {
		stubRecipientLookup(Collections.emptyList());

		assertThatThrownBy(() -> dao.transferToWallet(SENDER, new BigDecimal("250.00"), "ghost@x.com"))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
	}

	@Test
	void transfer_awardsDeterministicPoints_andCashbackForAmountAtLeast200() {
		stubRecipientLookup(List.<Object[]>of(new Object[] { RECEIVER, "r@x.com" }));
		stubLedger("1000.00");

		MoneyTransactionResponse response = dao.transferToWallet(SENDER, new BigDecimal("250.00"), "r@x.com");

		// floor(250/100) = 2 points, earned on the sender's debit row.
		assertThat(response.pointsEarned()).isEqualTo(2);
		verify(walletLedger).debit(eq(SENDER), eq(new BigDecimal("250.00")), anyChar(), anyChar(), anyString(), eq(2));
		// 2% cashback = 5.00, credited to the SENDER.
		verify(walletLedger).credit(eq(SENDER), eq(new BigDecimal("5.00")), anyChar(), anyChar(), anyString(),
				anyInt(), anyChar());
		// Receiver is credited exactly the transfer amount.
		verify(walletLedger).credit(eq(RECEIVER), eq(new BigDecimal("250.00")), anyChar(), anyChar(), anyString(),
				anyInt(), anyChar());
	}

	@Test
	void transfer_capsCashbackAt100() {
		stubRecipientLookup(List.<Object[]>of(new Object[] { RECEIVER, "r@x.com" }));
		stubLedger("50000.00");

		MoneyTransactionResponse response = dao.transferToWallet(SENDER, new BigDecimal("10000.00"), "r@x.com");

		assertThat(response.pointsEarned()).isEqualTo(100);
		// 2% of 10000 = 200 -> capped to 100.00.
		verify(walletLedger).credit(eq(SENDER), eq(new BigDecimal("100.00")), anyChar(), anyChar(), anyString(),
				anyInt(), anyChar());
	}

	@Test
	void transfer_below200_earnsNoCashback() {
		stubRecipientLookup(List.<Object[]>of(new Object[] { RECEIVER, "r@x.com" }));
		stubLedger("500.00");

		MoneyTransactionResponse response = dao.transferToWallet(SENDER, new BigDecimal("150.00"), "r@x.com");

		assertThat(response.pointsEarned()).isEqualTo(1);
		// No cashback: the sender is never credited (only the receiver is).
		verify(walletLedger, never()).credit(eq(SENDER), any(BigDecimal.class), anyChar(), anyChar(), anyString(),
				anyInt(), anyChar());
	}
}
