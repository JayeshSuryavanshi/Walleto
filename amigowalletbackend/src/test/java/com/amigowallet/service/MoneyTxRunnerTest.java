package com.amigowallet.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import com.amigowallet.exception.ApiException;

/**
 * Bounded retry-on-lock-contention behaviour. The transaction manager is mocked
 * (each attempt runs the supplied unit of work directly, no real DB).
 */
class MoneyTxRunnerTest {

	private PlatformTransactionManager transactionManager;
	private MoneyTxRunner runner;

	@BeforeEach
	void setUp() {
		transactionManager = mock(PlatformTransactionManager.class);
		when(transactionManager.getTransaction(any())).thenReturn(mock(TransactionStatus.class));
		runner = new MoneyTxRunner(transactionManager);
	}

	@Test
	void returnsValueOnSuccess() {
		String result = runner.runWithRetry(() -> "ok");
		assertThat(result).isEqualTo("ok");
	}

	@Test
	void retriesOnStaleObjectStateExceptionThenSucceeds() {
		AtomicInteger attempts = new AtomicInteger();
		Supplier<String> work = () -> {
			if (attempts.incrementAndGet() < 3) {
				throw new StaleObjectStateException("UserEntity", 1);
			}
			return "done";
		};

		String result = runner.runWithRetry(work);

		assertThat(result).isEqualTo("done");
		assertThat(attempts.get()).isEqualTo(3);
	}

	@Test
	void throws409AfterExhaustingRetriesOnOptimisticLockFailure() {
		AtomicInteger attempts = new AtomicInteger();
		Supplier<String> work = () -> {
			attempts.incrementAndGet();
			throw new ObjectOptimisticLockingFailureException("conflict", new RuntimeException());
		};

		assertThatThrownBy(() -> runner.runWithRetry(work))
				.isInstanceOf(ApiException.class)
				.satisfies(e -> {
					ApiException ae = (ApiException) e;
					assertThat(ae.getStatus()).isEqualTo(HttpStatus.CONFLICT);
					assertThat(ae.getMessageKey()).isEqualTo("WalletService.CONCURRENT_MODIFICATION");
				});

		assertThat(attempts.get()).isEqualTo(3);
	}

	@Test
	void nonLockRuntimeExceptionPropagatesWithoutRetry() {
		AtomicInteger attempts = new AtomicInteger();
		Supplier<String> work = () -> {
			attempts.incrementAndGet();
			throw new IllegalStateException("boom");
		};

		assertThatThrownBy(() -> runner.runWithRetry(work))
				.isInstanceOf(IllegalStateException.class);

		assertThat(attempts.get()).isEqualTo(1);
	}
}
