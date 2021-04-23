package com.edubank.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.UpdateTimestamp;

import com.edubank.model.CustomerLoginLockedStatus;

/**
 * This is an entity class mapped to DataBase table <q>CUSTOMER_LOGIN</q>
 *
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="CUSTOMER_LOGIN")
public class CustomerLoginEntity {

	/** @Id is used to map primary key of this table
	 *
	 *
	 *  @column is used to map a property with a column in table where column name is different
	 *  in this case 'CustomerLoginId' is mapped with CUSTOMER_LOGIN_ID column of this table  
	 */
	@Id       
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CUSTOMER_LOGIN_ID")
	private Integer customerLoginId;
	@Column(name="LOGIN_NAME")
	private String loginName;
	@Column(name="PASSWORD")
	private String password;
	@Column(name="CUSTOMER_ID")
	private Integer customerId;
	@Column(name="LOCKED_STATUS ")
	@Enumerated(EnumType.STRING)
	private CustomerLoginLockedStatus lockedStatus;
	@UpdateTimestamp
	@Column(name="MODIFIED_TIMESTAMP")
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
