package com.edubank.model;

/**
 *  This is a bean class. Also called as model class has the attributes to keep account properties
 *  
 * @author ETA_JAVA

 **/
public class Account
{
	private String accountNumber; 
	private String accountHolderName;
	private String branchName;
	private String ifsc;
	private Integer branchId;
	private Double balance;
	private Double lockedBalance;
	private AccountStatus accountStatus;
		
	public Double getLockedBalance() {
		return lockedBalance;
	}
	public void setLockedBalance(Double lockedBalance) {
		this.lockedBalance = lockedBalance;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountHolderName() {
		return accountHolderName;
	}
	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getIfsc() {
		return ifsc;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
}
