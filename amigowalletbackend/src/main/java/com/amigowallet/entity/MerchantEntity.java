package com.amigowallet.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.amigowallet.model.MerchantStatus;


@Entity
@Table(name="MERCHANT")
public class MerchantEntity {
	
	@Id
	@Column(name="MERCHANT_ID")
	private Integer merchantId;
	@Column(name="EMAIL_ID")
	private String emailId;
	@Column(name="MOBILE_NUMBER")
	private String mobileNumber;
	@Column(name="NAME")
	private String name;
	@Column(name="PASSWORD")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "MERCHANT_STATUS")
	private MerchantStatus merchantStatus;
	
	@CreationTimestamp
	@Column(name = "CREATED_TIMESTAMP")
	private LocalDateTime createdTimestamp;
	
	@UpdateTimestamp
	@Column(name = "MODIFIED_TIMESTAMP")
	private LocalDateTime modifiedTimestamp;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MerchantStatus getMerchantStatus() {
		return merchantStatus;
	}

	public void setMerchantStatus(MerchantStatus merchantStatus) {
		this.merchantStatus = merchantStatus;
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