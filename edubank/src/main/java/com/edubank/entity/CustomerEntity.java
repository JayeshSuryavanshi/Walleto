package com.edubank.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * This is an entity class mapped to DataBase table <q>CUSTOMER</q>
 *
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="CUSTOMER")
public class CustomerEntity{
	
	/** @Id is used to map primary key of this table
	 *
	 *
	 *  @column is used to map a property with a column in table where column name is different
	 *  in this case 'customerId' is mapped with CUSTOMER_ID column of this table 
	 */
	@Id       
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CUSTOMER_ID")
	private Integer customerId;
	@Column(name="EMAILID")
	private String emailId;
	@Column(name="NAME")
	private String name;
	@Column(name="DATE_OF_BIRTH")
	private LocalDate dateOfBirth;
	
	@Column(name="SECURITY_QUESTION_ID")
	private Integer securityQuestionId;
	@Column(name="SECURITY_ANSWER")
	private String securityAnswer;
	@CreationTimestamp
	@Column(name="CREATED_TIMESTAMP")
	private LocalDateTime createdTimeStamp;
	@Column(name="CREATED_BY")
	private Integer createdBy;
	@UpdateTimestamp
	@Column(name="MODIFIED_TIMESTAMP")
	private LocalDateTime modifiedTimeStamp;
	@Column(name="MODIFIED_BY")
	private Integer modifiedBy;
	
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public Integer getSecurityQuestionId() {
		return securityQuestionId;
	}
	public void setSecurityQuestionId(Integer securityQuestionId) {
		this.securityQuestionId = securityQuestionId;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	public LocalDateTime getCreatedTimeStamp() {
		return createdTimeStamp;
	}
	public void setCreatedTimeStamp(LocalDateTime createdTimeStamp) {
		this.createdTimeStamp = createdTimeStamp;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}
	public void setModifiedTimeStamp(LocalDateTime modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
