package com.edubank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubank.entity.DebitCardEntity;

public interface DebitCardRepository extends JpaRepository<DebitCardEntity, Integer> {

	Optional<DebitCardEntity> findByDebitCardNumber(String debitCardNumber);
}
