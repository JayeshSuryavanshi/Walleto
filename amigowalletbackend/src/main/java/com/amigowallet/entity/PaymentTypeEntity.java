package com.amigowallet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="PAYMENT_TYPE")
public class PaymentTypeEntity {

	@Id
	@Column(name="PAYMENT_TYPE_ID")
	private Integer paymentTypeId;
	@Column(name="PAYMENT_FROM")
	private Character paymentFrom;
	@Column(name="PAYMENT_TO")
	private Character paymentTo;
	@Column(name="PAYMENT_TYPE")
	private Character paymentType;
	
	public Integer getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public Character getPaymentFrom() {
		return paymentFrom;
	}
	public void setPaymentFrom(Character paymentFrom) {
		this.paymentFrom = paymentFrom;
	}
	public Character getPaymentTo() {
		return paymentTo;
	}
	public void setPaymentTo(Character paymentTo) {
		this.paymentTo = paymentTo;
	}
	public Character getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Character paymentType) {
		this.paymentType = paymentType;
	}
	
	
}
