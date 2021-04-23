package com.edubank.model;

/**
 *  This is a bean class. Also called as model class has the attributes to keep customer login properties
 *

 * @author ETA_JAVA
 *
 **/
public class CustomerLogin {

	private Integer customerLoginId;
	private String loginName;
	private String password;
	private Integer customerId;
	private CustomerLoginLockedStatus lockedStatus;
	private String newPassword;
	private String confirmNewPassword;
	
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}
	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}
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
}
