package com.amigowallet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BANK")
public class BankEntity {

	@Id
	@Column(name="BANK_ID")
	private Integer bankId;
	@Column(name="BANK_NAME")
	private String bankName;
	
	public Integer getBankId() {
		return bankId;
	}
	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
}
