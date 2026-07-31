package com.edubank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubank.entity.CustomerLoginEntity;

public interface CustomerLoginRepository extends JpaRepository<CustomerLoginEntity, Integer> {

	Optional<CustomerLoginEntity> findByLoginName(String loginName);
}
