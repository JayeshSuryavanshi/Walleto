package com.amigowallet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="MERCHANT_SERVICE_MAPPING")
public class MerchantServiceMappingEntity {
	
	
	@Column(name="SERVICE_ID")
	private Integer serviceId;
	@Id
	@Column(name="MERCHANT_ID")
	private Integer merchantId;
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}
	
	

}
