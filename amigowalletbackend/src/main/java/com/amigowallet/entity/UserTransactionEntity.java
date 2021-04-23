package com.amigowallet.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.amigowallet.model.TransactionStatus;

@Entity
@Table(name="USER_TRANSACTION")
public class UserTransactionEntity {

	@Id
	@Column(name="USER_TRANSACTION_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userTransactionId;
	@Column(name = "AMOUNT")
	private Double amount;
	@CreationTimestamp
	@Column(name="TRANSACTION_DATE_TIME")
	private LocalDateTime transactionDateTime;
	@Column(name = "REMARKS")
	private String remarks;
	@Column(name = "INFO")
	private String info;
	@Column(name="POINTS_EARNED")
	private Integer pointsEarned;
	@Column(name="IS_REDEEMED")
	private Character isRedeemed;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PAYMENT_TYPE_ID")
	private PaymentTypeEntity paymentTypeEntity;

	@Enumerated(EnumType.STRING)
	@Column(name = "TRANSACTION_STATUS")
	private TransactionStatus transactionStatus;

	public Long getUserTransactionId() {
		return userTransactionId;
	}

	public void setUserTransactionId(Long userTransactionId) {
		this.userTransactionId = userTransactionId;
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

	public Integer getPointsEarned() {
		return pointsEarned;
	}

	public void setPointsEarned(Integer pointsEarned) {
		this.pointsEarned = pointsEarned;
	}

	public Character getIsRedeemed() {
		return isRedeemed;
	}

	public void setIsRedeemed(Character isRedeemed) {
		this.isRedeemed = isRedeemed;
	}

	public PaymentTypeEntity getPaymentTypeEntity() {
		return paymentTypeEntity;
	}

	public void setPaymentTypeEntity(PaymentTypeEntity paymentTypeEntity) {
		this.paymentTypeEntity = paymentTypeEntity;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	
}
