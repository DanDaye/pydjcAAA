package com.wd.pydjc.bsd.model;

import com.wd.pydjc.sys.model.BaseEntity;

public class DeviceTotleConf extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;

	private Long deviceId;
	private Long measTypeId;
	private Long customerId;
	
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public Long getMeasTypeId() {
		return measTypeId;
	}
	public void setMeasTypeId(Long measTypeId) {
		this.measTypeId = measTypeId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
}
