package com.edubank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.edubank.model.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity mapped to the {@code TRANSACTION} table: the append-only ledger.
 * A ledger row is always written in the same transaction as the balance
 * mutation that produced it. Money is {@link BigDecimal} (DECIMAL(19,4)).
 */
@Entity
@Table(name = "TRANSACTION")
public class TransactionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TRANSACTION_ID")
	private Long transactionId;

	@Column(name = "ACCOUNT_NUMBER")
	private String accountNumber;

	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@CreationTimestamp
	@Column(name = "TRANSACTION_DATE_TIME")
	private LocalDateTime transactionDateTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE")
	private TransactionType type;

	@Column(name = "TRANSACTION_MODE")
	private String transactionMode;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "INFO")
	private String info;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "IDEMPOTENCY_KEY")
	private String idempotencyKey;

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public LocalDateTime getTransactionDateTime() {
		return transactionDateTime;
	}

	public void setTransactionDateTime(LocalDateTime transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public String getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getIdempotencyKey() {
		return idempotencyKey;
	}

	public void setIdempotencyKey(String idempotencyKey) {
		this.idempotencyKey = idempotencyKey;
	}
}
