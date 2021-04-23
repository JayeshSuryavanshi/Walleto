package com.edubank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.edubank.model.AccountCustomerMappingStatus;

/**
 * This is an entity class mapped to DataBase table <q>ACCOUNT_CUSTOMER_MAPPING</q>
 *
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="ACCOUNT_CUSTOMER_MAPPING")
public class AccountCustomerMappingEntity
{
	/** @Id is used to map primary key of this table
	 *
	 *
	 *  @column is used to map a property with a column in table where column name is different
	 *  in this case 'accountCustomerMappingId' is mapped with ACCOUNT_CUSTOMER_MAPPING_ID column of this table  
	 */
	@Id       
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ACCOUNT_CUSTOMER_MAPPING_ID")
	private Integer accountCustomerMappingId;
	@Column(name="ACCOUNT_NUMBER")
	private String accountNumber;
	@Column(name="CUSTOMER_ID")
	private Integer customerId;
	@Enumerated(EnumType.STRING)
	@Column(name="MAPPING_STATUS")
	private AccountCustomerMappingStatus mappingStatus;
	
	public Integer getAccountCustomerMappingId() {
		return accountCustomerMappingId;
	}
	public void setAccountCustomerMappingId(Integer accountCustomerMappingId) {
		this.accountCustomerMappingId = accountCustomerMappingId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public AccountCustomerMappingStatus getMappingStatus() {
		return mappingStatus;
	}
	public void setMappingStatus(AccountCustomerMappingStatus mappingStatus) {
		this.mappingStatus = mappingStatus;
	}
}
