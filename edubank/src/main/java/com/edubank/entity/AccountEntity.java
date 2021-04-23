package com.edubank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.edubank.model.AccountStatus;

 /**
 * This is an entity class mapped to Database table <q>ACCOUNT</q>
 * 
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="ACCOUNT")
public class AccountEntity
{
	/**
	 * @Id is used to map primary key of this table
	 * 
	 * @column is used to map a property with a column in table where column name is different
	 * in this case 'accountId' is mapped with ACCOUNT_ID column of this table  
	 * */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ACCOUNT_ID ")
	private Integer accountId;
	@Column(name="ACCOUNT_NUMBER")
	private String accountNumber; 
	@Column(name="BRANCH_ID")
	private Integer branchId;
	@Column(name="BALANCE")
	private Double balance;
	@Column(name="LOCKED_BALANCE")
	private Double lockedBalance;
	@Enumerated(EnumType.STRING)
	@Column(name="ACCOUNT_STATUS")
	private AccountStatus accountStatus;
	
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
	public Double getLockedBalance() {
		return lockedBalance;
	}
	public void setLockedBalance(Double lockedBalance) {
		this.lockedBalance = lockedBalance;
	}
	public AccountStatus getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}
}
