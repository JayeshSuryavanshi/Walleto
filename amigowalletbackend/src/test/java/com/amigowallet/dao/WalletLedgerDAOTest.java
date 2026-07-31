package com.amigowallet.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.amigowallet.entity.PaymentTypeEntity;
import com.amigowallet.entity.UserEntity;
import com.amigowallet.exception.ApiException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;

/**
 * The single authoritative money-posting helper. Verifies the funds check
 * (INSUFFICIENT_BALANCE) and exact BigDecimal debit/credit arithmetic without a
 * real database (the {@link EntityManager} is mocked).
 */
@ExtendWith(MockitoExtension.class)
class WalletLedgerDAOTest {

	@Mock
	private EntityManager entityManager;

	@Mock
	private TypedQuery<PaymentTypeEntity> paymentTypeQuery;

	private WalletLedgerDAO ledger;

	@BeforeEach
	void setUp() {
		ledger = new WalletLedgerDAO();
		ReflectionTestUtils.setField(ledger, "entityManager", entityManager);
	}

	private UserEntity userWithBalance(Integer userId, String balance) {
		UserEntity user = new UserEntity();
		user.setUserId(userId);
		user.setBalance(new BigDecimal(balance));
		return user;
	}

	/** Wires the PAYMENT_TYPE lookup + persist so a posting can complete. */
	private void stubPostingPlumbing() {
		lenient().when(entityManager.createQuery(anyString(), eq(PaymentTypeEntity.class)))
				.thenReturn(paymentTypeQuery);
		lenient().when(paymentTypeQuery.setParameter(anyString(), any())).thenReturn(paymentTypeQuery);
		lenient().when(paymentTypeQuery.getSingleResult()).thenReturn(new PaymentTypeEntity());
	}

	@Test
	void debit_rejectsWhenFundsInsufficient_422() {
		UserEntity user = userWithBalance(1, "50.00");
		when(entityManager.find(UserEntity.class, 1, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);

		assertThatThrownBy(() -> ledger.debit(1, new BigDecimal("100.00"), 'W', 'W', "info", 0))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> {
					ApiException ae = (ApiException) e;
					assertThat(ae.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
					assertThat(ae.getMessageKey()).isEqualTo("WalletService.INSUFFICIENT_BALANCE");
				});

		// Balance untouched; no ledger row persisted.
		assertThat(user.getBalance()).isEqualByComparingTo("50.00");
	}

	@Test
	void debit_subtractsExactlyAtScaleTwo() {
		UserEntity user = userWithBalance(1, "3413.60");
		when(entityManager.find(UserEntity.class, 1, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);
		stubPostingPlumbing();

		WalletLedgerDAO.LedgerEntry entry = ledger.debit(1, new BigDecimal("100.00"), 'W', 'W', "info", 0);

		assertThat(entry.newBalance()).isEqualTo(new BigDecimal("3313.60"));
		assertThat(entry.newBalance().scale()).isEqualTo(2);
		assertThat(user.getBalance()).isEqualTo(new BigDecimal("3313.60"));
	}

	@Test
	void debit_withExactlyEqualFunds_succeedsToZero() {
		UserEntity user = userWithBalance(1, "100.00");
		when(entityManager.find(UserEntity.class, 1, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);
		stubPostingPlumbing();

		WalletLedgerDAO.LedgerEntry entry = ledger.debit(1, new BigDecimal("100.00"), 'W', 'W', "info", 0);

		assertThat(entry.newBalance()).isEqualByComparingTo("0.00");
	}

	@Test
	void credit_addsExactlyAtScaleTwo() {
		UserEntity user = userWithBalance(2, "3313.60");
		when(entityManager.find(UserEntity.class, 2, LockModeType.PESSIMISTIC_WRITE)).thenReturn(user);
		stubPostingPlumbing();

		WalletLedgerDAO.LedgerEntry entry = ledger.credit(2, new BigDecimal("100.00"), 'B', 'W', "info", 0, 'N');

		assertThat(entry.newBalance()).isEqualTo(new BigDecimal("3413.60"));
		assertThat(entry.newBalance().scale()).isEqualTo(2);
		assertThat(user.getBalance()).isEqualTo(new BigDecimal("3413.60"));
	}

	@Test
	void lockUser_rejectsNullUserId_400() {
		assertThatThrownBy(() -> ledger.lockUser(null))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST));
	}

	@Test
	void lockUser_rejectsUnknownUser_404() {
		when(entityManager.find(UserEntity.class, 99, LockModeType.PESSIMISTIC_WRITE)).thenReturn(null);
		assertThatThrownBy(() -> ledger.lockUser(99))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> assertThat(((ApiException) e).getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
	}
}
