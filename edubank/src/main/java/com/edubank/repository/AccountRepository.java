package com.edubank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edubank.entity.AccountEntity;

import jakarta.persistence.LockModeType;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

	Optional<AccountEntity> findByAccountNumber(String accountNumber);

	/**
	 * Loads the account under a pessimistic write lock (SELECT ... FOR UPDATE) so
	 * that balance read-modify-write happens atomically across concurrent debits.
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select a from AccountEntity a where a.accountNumber = :accountNumber")
	Optional<AccountEntity> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);
}
