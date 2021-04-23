package com.edubank.entity;

import java.time.LocalDate;
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
import org.hibernate.annotations.UpdateTimestamp;

import com.edubank.model.DebitCardLockedStatus;
import com.edubank.model.DebitCardStatus;

/**
 * This is an entity class mapped to DataBase table <q>DEBIT_CARD</q>
 * 
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="DEBIT_CARD")
public class DebitCardEntity
{

	/** @Id is used to map primary key of this table
	 *
	 *
	 *  @column is used to map a property with a column in table where column name is different
	 *  in this case 'debitCardId' is mapped with DEBIT_CARD_ID column of this table
	 */
	@Id       
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="DEBIT_CARD_ID")
	private Integer debitCardId;
	@Column(name="DEBIT_CARD_NUMBER")
	private String debitCardNumber; 
	@Column(name="ACCOUNT_CUSTOMER_MAPPING_ID")
	private Integer accountCustomerMappingId;
	@Column(name="CVV")
	private String cvv;
	@Column(name="VALID_FROM")
	private LocalDate validFrom; 
	@Column(name="VALID_THRU")
	private LocalDate validThru;
	@Column(name="PIN")
	private String pin;
	@Enumerated(EnumType.STRING)
	@Column(name="LOCKED_STATUS")
	private DebitCardLockedStatus lockedStatus;
	@Enumerated(EnumType.STRING)
	@Column(name="DEBIT_CARD_STATUS")
	private DebitCardStatus debitCardStatus;
	@CreationTimestamp
	@Column(name="CREATED_TIMESTAMP")
	private LocalDateTime createdTimeStamp;
	@Column(name="CREATED_BY")
	private Integer createdBy;
	@UpdateTimestamp
	@Column(name="MODIFIED_TIMESTAMP")
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
	/*public LocalDateTime getCreatedTimeStamp() {
		return createdTimeStamp;
	}
	public void setCreatedTimeStamp(LocalDateTime createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}*/
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	/*public LocalDateTime getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}
	public void setModifiedTimeStamp(LocalDateTime modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}*/
}
