package com.edubank.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.edubank.model.TransactionType;

/**
 * This is an entity class mapped to DataBase table <q>TRANSACTION</q>
 *
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="TRANSACTION")
public class TransactionEntity {
 
	/** @Id is used to map primary key of this table
	 *
	 *
	 *  @column is used to map a property with a column in table where column name is different
	 *  in this case 'transactionId' is mapped with TRANSACTION_ID column of this table 
	 */
	@Id       
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TRANSACTION_ID")
	private Long transactionId;
	@Column(name="ACCOUNT_NUMBER")
	private String accountNumber;
	@Column(name="AMOUNT")
	private Double amount;
	@CreationTimestamp
	@Column(name="TRANSACTION_DATE_TIME")
	private LocalDateTime transactionDateTime;
	@Enumerated(EnumType.STRING)
	private TransactionType  type;
	@Column(name="TRANSACTION_MODE")
	private String transactionMode;
	@Column(name="REMARKS")
	private String remarks;
	@Column(name="INFO")
	private String info;
	@Column(name="CREATED_BY")
	private String createdBy;
	
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
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
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
}
