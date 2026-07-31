package com.edubank.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.edubank.model.CustomerLoginLockedStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity mapped to the {@code CUSTOMER_LOGIN} table (net-banking credentials).
 */
@Entity
@Table(name = "CUSTOMER_LOGIN")
public class CustomerLoginEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CUSTOMER_LOGIN_ID")
	private Integer customerLoginId;

	@Column(name = "LOGIN_NAME")
	private String loginName;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "CUSTOMER_ID")
	private Integer customerId;

	@Enumerated(EnumType.STRING)
	@Column(name = "LOCKED_STATUS")
	private CustomerLoginLockedStatus lockedStatus;

	@UpdateTimestamp
	@Column(name = "MODIFIED_TIMESTAMP")
	private LocalDateTime modifiedTimeStamp;

	public Integer getCustomerLoginId() {
		return customerLoginId;
	}

	public void setCustomerLoginId(Integer customerLoginId) {
		this.customerLoginId = customerLoginId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public CustomerLoginLockedStatus getLockedStatus() {
		return lockedStatus;
	}

	public void setLockedStatus(CustomerLoginLockedStatus lockedStatus) {
		this.lockedStatus = lockedStatus;
	}

	public LocalDateTime getModifiedTimeStamp() {
		return modifiedTimeStamp;
	}

	public void setModifiedTimeStamp(LocalDateTime modifiedTimeStamp) {
		this.modifiedTimeStamp = modifiedTimeStamp;
	}
}
