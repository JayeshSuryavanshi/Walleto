package com.amigowallet.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="MERCHANT_SERVICE_TYPE")
public class MerchantServiceTypeEntity {
	
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SERVICE_ID")
	private Integer serviceId;
	@Id
	@Column(name="SERVICE_TYPE")
	private String serviceType;
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	
	

}
