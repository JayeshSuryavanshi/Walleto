package com.amigowallet.model;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class Card {

	private Integer cardId;
	private String cardNumber;
	
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@JsonSerialize(using=LocalDateSerializer.class)
	private LocalDate expiryDate;
	
	private CardStatus cardStatus;
	private String successMessage;
	private String errorMessage;
	private String userName;
	private String cvv;
	
	private Bank bank;
	
	
	public Bank getBank() {
		return bank;
	}
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cVV) {
		cvv = cVV;
	}
	public Integer getCardId() {
		return cardId;
	}
	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public LocalDate getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}
	public CardStatus getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(CardStatus cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	
}
