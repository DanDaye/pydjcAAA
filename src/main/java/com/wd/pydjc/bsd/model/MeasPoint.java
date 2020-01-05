package com.wd.pydjc.bsd.model;

import java.math.BigDecimal;
import com.wd.pydjc.sys.model.BaseEntity;

public class MeasPoint extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;
	
	private String name;
	private Long measTypeId;
	private Long deviceId;
	private String status;
	private String unit;
	private Long isStart;
	private Long customerId;
	private BigDecimal standardValue;
	private BigDecimal oneLowerLimit;
	private BigDecimal oneUpperLimit;
	private BigDecimal twoLowerLimit;
	private BigDecimal twoUpperLimit;
	private BigDecimal threeLowerLimit;
	private BigDecimal threeUpperLimit;
	private String limitUnit;
	private long continueTime;
	
	//虚拟字段
	private String measTypeCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getMeasTypeId() {
		return measTypeId;
	}
	public void setMeasTypeId(Long measTypeId) {
		this.measTypeId = measTypeId;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getIsStart() {
		return isStart;
	}
	public void setIsStart(Long isStart) {
		this.isStart = isStart;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getStandardValue() {
		return standardValue;
	}
	public void setStandardValue(BigDecimal standardValue) {
		this.standardValue = standardValue;
	}
	public BigDecimal getOneLowerLimit() {
		return oneLowerLimit;
	}
	public void setOneLowerLimit(BigDecimal oneLowerLimit) {
		this.oneLowerLimit = oneLowerLimit;
	}
	public BigDecimal getOneUpperLimit() {
		return oneUpperLimit;
	}
	public void setOneUpperLimit(BigDecimal oneUpperLimit) {
		this.oneUpperLimit = oneUpperLimit;
	}
	public BigDecimal getTwoLowerLimit() {
		return twoLowerLimit;
	}
	public void setTwoLowerLimit(BigDecimal twoLowerLimit) {
		this.twoLowerLimit = twoLowerLimit;
	}
	public BigDecimal getTwoUpperLimit() {
		return twoUpperLimit;
	}
	public void setTwoUpperLimit(BigDecimal twoUpperLimit) {
		this.twoUpperLimit = twoUpperLimit;
	}
	public BigDecimal getThreeLowerLimit() {
		return threeLowerLimit;
	}
	public void setThreeLowerLimit(BigDecimal threeLowerLimit) {
		this.threeLowerLimit = threeLowerLimit;
	}
	public BigDecimal getThreeUpperLimit() {
		return threeUpperLimit;
	}
	public void setThreeUpperLimit(BigDecimal threeUpperLimit) {
		this.threeUpperLimit = threeUpperLimit;
	}
	public String getLimitUnit() {
		return limitUnit;
	}
	public void setLimitUnit(String limitUnit) {
		this.limitUnit = limitUnit;
	}
	public long getContinueTime() {
		return continueTime;
	}
	public void setContinueTime(long continueTime) {
		this.continueTime = continueTime;
	}
	public String getMeasTypeCode() {
		return measTypeCode;
	}
	public void setMeasTypeCode(String measTypeCode) {
		this.measTypeCode = measTypeCode;
	}

}
