package com.edubank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubank.entity.TransactionEntity;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

	Optional<TransactionEntity> findByIdempotencyKey(String idempotencyKey);
}
