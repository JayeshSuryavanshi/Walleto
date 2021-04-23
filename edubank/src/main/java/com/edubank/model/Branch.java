package com.edubank.model;

/**
 * This is a bean class. Also called as model class has the attributes to keep branch properties
 *

 * @author ETA_JAVA

 **/
public class Branch
{
	private Integer branchId;
	private String ifsc;
	private String branchName;
	private String branchCode;
	
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public String getIfsc() {
		return ifsc;
	}
	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

}
