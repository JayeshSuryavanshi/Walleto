package com.edubank.entity;

import java.math.BigDecimal;

import com.edubank.model.AccountStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * Entity mapped to the {@code ACCOUNT} table. Holds the authoritative,
 * persisted balance for a bank account. Money columns are {@link BigDecimal}
 * (DECIMAL(19,4)) and a {@link Version} column guards against lost updates.
 */
@Entity
@Table(name = "ACCOUNT")
public class AccountEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ACCOUNT_ID")
	private Integer accountId;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "BRANCH_ID")
	private Integer branchId;

	@Column(name = "BALANCE")
	private BigDecimal balance;

	@Column(name = "LOCKED_BALANCE")
	private BigDecimal lockedBalance;

	@Enumerated(EnumType.STRING)
	@Column(name = "ACCOUNT_STATUS")
	private AccountStatus accountStatus;

	@Version
	@Column(name = "VERSION")
	private Long version;

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getLockedBalance() {
		return lockedBalance;
	}

	public void setLockedBalance(BigDecimal lockedBalance) {
		this.lockedBalance = lockedBalance;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
