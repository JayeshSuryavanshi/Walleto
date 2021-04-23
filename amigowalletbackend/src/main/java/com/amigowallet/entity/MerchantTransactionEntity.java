package com.amigowallet.entity;

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

import com.amigowallet.model.TransactionStatus;
@Entity
@Table(name="MERCHANT_TRANSACTION")
public class MerchantTransactionEntity {
	
	@Id
	@Column(name="MERCHANT_TRANSACTION_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer merchantTransactionId;
	@Column(name="AMOUNT")
	private Double amount;
	@CreationTimestamp
	@Column(name="TRANSACTION_DATE_TIME")
	private LocalDateTime transactionDateTime;
	@Column(name="PAYMENT_TYPE_ID")
	private Integer paymentTypeId;
	@Column(name="REMARKS")
	private String remarks;
	@Column(name="INFO")
	private String info;
	@Enumerated(EnumType.STRING)
	@Column(name = "TRANSACTION_STATUS")
	private TransactionStatus transactionStatus;
	@Column(name="MERCHANT_ID ")
	private Integer merchantId;
	public Integer getMerchantTransactionId() {
		return merchantTransactionId;
	}
	public void setMerchantTransactionId(Integer merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
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
	public Integer getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
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
	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	
	




}
