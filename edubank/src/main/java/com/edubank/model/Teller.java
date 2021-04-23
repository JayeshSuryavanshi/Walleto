package com.edubank.model;

/**
 *  This is a bean class. Also called as model class has the attributes to keep teller properties
 *
 * @author ETA_JAVA
 *
 **/
public class Teller {
	
	private Integer tellerId;
	private String loginName;
	private String password;
	public Integer getTellerId() {
		return tellerId;
	}
	public void setTellerId(Integer tellerId) {
		this.tellerId = tellerId;
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
	
}
