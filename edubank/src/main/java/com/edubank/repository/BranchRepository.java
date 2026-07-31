package com.edubank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edubank.entity.BranchEntity;

public interface BranchRepository extends JpaRepository<BranchEntity, Integer> {

	Optional<BranchEntity> findByBranchId(Integer branchId);
}
