package com.edubank.model;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;


/**
 * This is a bean class. Also called as model class has the attributes to keep Debit card properties
 *

 * @author ETA_JAVA
 *
 **/
public class DebitCard
{	
	private String debitCardNumber;
	private String cardHolderName;
	private String cvv;
	private LocalDate validFrom; 
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	private LocalDate validThru;
	private String pin;
	private DebitCardLockedStatus lockedStatus;
	private DebitCardStatus debitCardStatus;
	private String newPin;
	private String confirmNewPin;
	private Integer accountCustomerMappingId;

	public Integer getAccountCustomerMappingId() {
		return accountCustomerMappingId;
	}
	public void setAccountCustomerMappingId(Integer accountCustomerMappingId) {
		this.accountCustomerMappingId = accountCustomerMappingId;
	}
	public String getNewPin() {
		return newPin;
	}
	public void setNewPin(String newPin) {
		this.newPin = newPin;
	}
	public String getConfirmNewPin() {
		return confirmNewPin;
	}
	public void setConfirmNewPin(String confirmNewPin) {
		this.confirmNewPin = confirmNewPin;
	}
	public String getDebitCardNumber() {
		return debitCardNumber;
	}
	public void setDebitCardNumber(String debitCardNumber) {
		this.debitCardNumber = debitCardNumber;
	}
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public LocalDate getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(LocalDate validFrom) {
		this.validFrom = validFrom;
	}
	public LocalDate getValidThru() {
		return validThru;
	}
	public void setValidThru(LocalDate validThru) {
		this.validThru = validThru;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}
	public DebitCardLockedStatus getLockedStatus() {
		return lockedStatus;
	}
	public void setLockedStatus(DebitCardLockedStatus lockedStatus) {
		this.lockedStatus = lockedStatus;
	}
	public DebitCardStatus getDebitCardStatus() {
		return debitCardStatus;
	}
	public void setDebitCardStatus(DebitCardStatus debitCardStatus) {
		this.debitCardStatus = debitCardStatus;
	}
}
