package com.edubank.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.edubank.model.DebitCardLockedStatus;
import com.edubank.model.DebitCardStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity mapped to the {@code DEBIT_CARD} table.
 * <p>
 * NOTE: the CVV column has been intentionally removed. CVV is never accepted or
 * stored by bank-api; the PIN (encoded with the application password encoder)
 * is the authentication factor for card debits.
 */
@Entity
@Table(name = "DEBIT_CARD")
public class DebitCardEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DEBIT_CARD_ID")
	private Integer debitCardId;

	@Column(name = "DEBIT_CARD_NUMBER")
	private String debitCardNumber;

	@Column(name = "ACCOUNT_CUSTOMER_MAPPING_ID")
	private Integer accountCustomerMappingId;

	@Column(name = "VALID_FROM")
	private LocalDate validFrom;

	@Column(name = "VALID_THRU")
	private LocalDate validThru;

	@Column(name = "PIN")
	private String pin;

	@Enumerated(EnumType.STRING)
	@Column(name = "LOCKED_STATUS")
	private DebitCardLockedStatus lockedStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "DEBIT_CARD_STATUS")
	private DebitCardStatus debitCardStatus;

	@CreationTimestamp
	@Column(name = "CREATED_TIMESTAMP")
	private LocalDateTime createdTimeStamp;

	@Column(name = "CREATED_BY")
	private Integer createdBy;

	@UpdateTimestamp
	@Column(name = "MODIFIED_TIMESTAMP")
	private LocalDateTime modifiedTimeStamp;

	public Integer getDebitCardId() {
		return debitCardId;
	}

	public void setDebitCardId(Integer debitCardId) {
		this.debitCardId = debitCardId;
	}

	public String getDebitCardNumber() {
		return debitCardNumber;
	}

	public void setDebitCardNumber(String debitCardNumber) {
		this.debitCardNumber = debitCardNumber;
	}

	public Integer getAccountCustomerMappingId() {
		return accountCustomerMappingId;
	}

	public void setAccountCustomerMappingId(Integer accountCustomerMappingId) {
		this.accountCustomerMappingId = accountCustomerMappingId;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
}
