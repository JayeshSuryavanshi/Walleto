package com.edubank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubank.entity.AccountCustomerMappingEntity;

public interface AccountCustomerMappingRepository extends JpaRepository<AccountCustomerMappingEntity, Integer> {

	Optional<AccountCustomerMappingEntity> findByAccountCustomerMappingId(Integer accountCustomerMappingId);

	Optional<AccountCustomerMappingEntity> findByAccountNumber(String accountNumber);

	Optional<AccountCustomerMappingEntity> findByCustomerId(Integer customerId);
}
