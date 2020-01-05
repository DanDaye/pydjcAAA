package com.wd.pydjc.bsd.model;

import java.math.BigDecimal;

import com.wd.pydjc.sys.model.BaseEntity;

public class MeasType extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;

	private String name;
	private String code;
	private String desc;
	private Long isStart;
	private String unit;
	private Long parentId;
	private Long monitorTypeId;
	private BigDecimal standardValue;
	private BigDecimal oneLowerLimit;
	private BigDecimal oneUpperLimit;
	private BigDecimal twoLowerLimit;
	private BigDecimal twoUpperLimit;
	private BigDecimal threeLowerLimit;
	private BigDecimal threeUpperLimit;
	private String fc;
	private String limitUnit;
	private long continueTime;
	private String code645;
	private String monitorTypeName;
	private String parentCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Long getIsStart() {
		return isStart;
	}
	public void setIsStart(Long isStart) {
		this.isStart = isStart;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	public String getCode645() {
		return code645;
	}
	public void setCode645(String code645) {
		this.code645 = code645;
	}
	public String getFc() {
		return fc;
	}
	public void setFc(String fc) {
		this.fc = fc;
	}
	public Long getMonitorTypeId() {
		return monitorTypeId;
	}
	public void setMonitorTypeId(Long monitorTypeId) {
		this.monitorTypeId = monitorTypeId;
	}
	public String getMonitorTypeName() {
		return monitorTypeName;
	}
	public void setMonitorTypeName(String monitorTypeName) {
		this.monitorTypeName = monitorTypeName;
	}
	
	
}
