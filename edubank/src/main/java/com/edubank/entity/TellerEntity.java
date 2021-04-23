package com.edubank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an entity class mapped to DataBase table <q>TELLER</q>
 *
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="TELLER")
public class TellerEntity {
	
	/** @Id is used to map primary key of this table
	 *
	 *
	 *  @column is used to map a property with a column in table where column name is different
	 *  in this case 'tellerId' is mapped with TELLER_ID column of this table
	 */
	@Id      
	@Column(name="TELLER_ID")
	private Integer tellerId;
	@Column(name="LOGIN_NAME")
	private String loginName;
	@Column(name="PASSWORD")
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
