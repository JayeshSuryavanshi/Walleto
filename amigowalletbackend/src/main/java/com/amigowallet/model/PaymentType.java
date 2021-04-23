package com.amigowallet.model;

public class PaymentType {

	private Integer paymentTypeId;
	private Character paymentFrom;
	private Character paymentTo;
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
