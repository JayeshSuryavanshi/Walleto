package com.amigowallet.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.amigowallet.model.CardStatus;

@Entity
@Table(name="CARD")
public class CardEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CARD_ID")
	private Integer cardId;
	@Column(name="CARD_NUMBER")
	private String cardNumber;
	@Column(name="EXPIRY_DATE")
	private LocalDate expiryDate;
	@CreationTimestamp
	@Column(name="CREATED_TIMESTAMP")
	private LocalDateTime createdTimestamp;
	@UpdateTimestamp
	@Column(name="MODIFIED_TIMESTAMP")
	private LocalDateTime modifiedTimestamp;
	
	@Enumerated(EnumType.STRING)
	@Column(name="CARD_STATUS")
	private CardStatus userStatus;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "BANK_ID")
	private BankEntity bankEntity;

	
	public BankEntity getBankEntity() {
		return bankEntity;
	}
	public void setBankEntity(BankEntity bankEntity) {
		this.bankEntity = bankEntity;
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
	
	public CardStatus getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(CardStatus userStatus) {
		this.userStatus = userStatus;
	}
	public LocalDateTime getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public LocalDateTime getModifiedTimestamp() {
		return modifiedTimestamp;
	}
	public void setModifiedTimestamp(LocalDateTime modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}
	
}
