package com.edubank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is an entity class mapped to DataBase table <q>BRANCH</q>
 * 
 * @GenericGenerator is used for auto generating primary key value
 * 
 * @author ETA_JAVA
 *
 */
@Entity
@Table(name="BRANCH")
public class BranchEntity
{
	/** @Id is used to map primary key of this table
	 *
	 *
	 *  @column is used to map a property with a column in table where column name is different
	 *  in this case 'branchId' is mapped with BRANCH_ID column of this table  
	 * */
	@Id       
	@Column(name="BRANCH_ID")
	private Integer branchId;
	@Column(name="IFSC")
	private String ifsc;
	@Column(name="BRANCH_NAME")
	private String branchName;
	@Column(name="BRANCH_CODE")
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
