package com.edubank.model;

/**
 *  This is a bean class. Also called as model class has the attributes to keep accountCustomerMapping properties
 *

 * @author ETA_JAVA

 **/

public class AccountCustomerMapping {

	private Integer accountCustomerMappingId;
	private String accountNumber;
	private Integer customerId;
	private AccountCustomerMappingStatus mappingStatus;
	
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
	public Integer getAccountCustomerMappingId() {
		return accountCustomerMappingId;
	}
	public void setAccountCustomerMappingId(Integer accountCustomerMappingId) {
		this.accountCustomerMappingId = accountCustomerMappingId;
	}
}
