package com.amigowallet.service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import com.amigowallet.exception.ApiException;

/**
 * Runs a money-mutating unit of work in a fresh transaction, with a bounded retry
 * on lock contention.
 *
 * <p>Each attempt runs inside its own {@code PROPAGATION_REQUIRES_NEW} transaction
 * (hence a fresh persistence context that re-reads balances under the row lock), so
 * a loser in a race re-checks funds and typically ends with a clean
 * {@code 422 INSUFFICIENT_BALANCE} rather than an HTTP 500. If a transient lock
 * failure (optimistic version collision / pessimistic lock-acquisition failure)
 * persists across all attempts, a {@code 409 CONFLICT} {@link ApiException} is
 * thrown instead of leaking the raw Hibernate/Spring exception.
 *
 * <p>The wrapped work MUST be idempotent across attempts (e.g. any external bank
 * call inside it must reuse a stable idempotency key), because a retried attempt
 * re-executes the whole unit in a new transaction.
 */
@Component
public class MoneyTxRunner {

	private static final Logger logger = LoggerFactory.getLogger(MoneyTxRunner.class);

	private static final int MAX_ATTEMPTS = 3;
	private static final long BASE_BACKOFF_MILLIS = 15L;

	private final PlatformTransactionManager transactionManager;

	public MoneyTxRunner(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public <T> T runWithRetry(Supplier<T> work) {
		RuntimeException lastLockFailure = null;
		for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
			TransactionTemplate template = new TransactionTemplate(transactionManager);
			template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			try {
				return template.execute(status -> work.get());
			} catch (RuntimeException ex) {
				if (!isLockContention(ex)) {
					throw ex;
				}
				lastLockFailure = ex;
				logger.warn("Lock contention on money mutation (attempt {}/{}): {}",
						attempt, MAX_ATTEMPTS, ex.getClass().getSimpleName());
				backoff(attempt);
			}
		}
		logger.warn("Money mutation abandoned after {} attempts due to lock contention: {}",
				MAX_ATTEMPTS, lastLockFailure == null ? "?" : lastLockFailure.getClass().getSimpleName());
		throw new ApiException(HttpStatus.CONFLICT, "WalletService.CONCURRENT_MODIFICATION");
	}

	/** Scans the throwable + cause chain for a known optimistic/pessimistic lock failure. */
	private boolean isLockContention(Throwable throwable) {
		for (Throwable t = throwable; t != null; t = t.getCause()) {
			String name = t.getClass().getName();
			if (name.equals("org.springframework.orm.ObjectOptimisticLockingFailureException")
					|| name.equals("org.springframework.dao.OptimisticLockingFailureException")
					|| name.equals("org.springframework.dao.PessimisticLockingFailureException")
					|| name.equals("org.springframework.dao.CannotAcquireLockException")
					|| name.equals("org.springframework.dao.ConcurrencyFailureException")
					|| name.equals("org.hibernate.StaleObjectStateException")
					|| name.equals("org.hibernate.dialect.lock.OptimisticEntityLockException")
					|| name.equals("org.hibernate.exception.LockAcquisitionException")
					|| name.equals("jakarta.persistence.OptimisticLockException")
					|| name.equals("jakarta.persistence.PessimisticLockException")
					|| name.equals("jakarta.persistence.LockTimeoutException")) {
				return true;
			}
			if (t.getCause() == t) {
				break;
			}
		}
		return false;
	}

	private void backoff(int attempt) {
		try {
			long jitter = ThreadLocalRandom.current().nextLong(BASE_BACKOFF_MILLIS + 1);
			Thread.sleep((long) attempt * BASE_BACKOFF_MILLIS + jitter);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
