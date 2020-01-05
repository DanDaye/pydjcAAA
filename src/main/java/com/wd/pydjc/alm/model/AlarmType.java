package com.wd.pydjc.alm.model;

import com.wd.pydjc.sys.model.BaseEntity;

public class AlarmType extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;

	private String name;
	private String code;
	private Long measTypeId;
	private String type;
	private String level;
	private String cfmFlag;
	private Long customerId;
	private Long eventSetId;
	private String delFlag;
	
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
	public Long getMeasTypeId() {
		return measTypeId;
	}
	public void setMeasTypeId(Long measTypeId) {
		this.measTypeId = measTypeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getCfmFlag() {
		return cfmFlag;
	}
	public void setCfmFlag(String cfmFlag) {
		this.cfmFlag = cfmFlag;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getEventSetId() {
		return eventSetId;
	}
	public void setEventSetId(Long eventSetId) {
		this.eventSetId = eventSetId;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
}
