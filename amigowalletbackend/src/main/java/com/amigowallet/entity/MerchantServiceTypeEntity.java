package com.amigowallet.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="MERCHANT_SERVICE_TYPE")
public class MerchantServiceTypeEntity {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SERVICE_ID")
	private Integer serviceId;
	@Column(name="SERVICE_TYPE", unique = true, nullable = false)
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
