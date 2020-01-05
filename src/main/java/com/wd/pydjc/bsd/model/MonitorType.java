package com.wd.pydjc.bsd.model;

import java.util.List;

import com.wd.pydjc.sys.model.BaseEntity;


public class MonitorType extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8332281295374443374L;
	
	private Long id;
//	private Long monitorTypeId;
	private String name;
	private String monitorTypeCode;
	private Long parentId;
	private Long level;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMonitorTypeCode() {
		return monitorTypeCode;
	}
	public void setMonitorTypeCode(String monitorTypeCode) {
		this.monitorTypeCode = monitorTypeCode;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getLevel() {
		return level;
	}
	public void setLevel(Long level) {
		this.level = level;
	}
	public static List<MonitorType> getAllMonitorType() {
		// TODO Auto-generated method stub
		return null;
	}
}
