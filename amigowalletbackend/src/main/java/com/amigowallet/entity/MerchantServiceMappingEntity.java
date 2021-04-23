package com.amigowallet.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
