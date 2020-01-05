package com.wd.pydjc.bsd.model;

import java.util.List;
import com.wd.pydjc.bsd.model.Vandor;
import com.wd.pydjc.sys.model.BaseEntity;

public class Device extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;

	private String name;
	private String no;
	private Long deviceTypeId;
	private Long parentId;
	private Long isStart;
	private Long customerId;
	private Long vandorId;
	private Integer sort; 
	private String rpdNo;
	
	private List<Device> child;
	private Device parent;
	private DeviceType deviceType;
	private Vandor vandor;
	
	private List<MeasPoint> measPoints;
	
	//虚拟字段
	private String icon;
	
	
	public List<MeasPoint> getMeasPoints() {
		return measPoints;
	}
	public void setMeasPoints(List<MeasPoint> measPoints) {
		this.measPoints = measPoints;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(Long deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	public Long getVandorId() {
		return vandorId;
	}
	public void setVandorId(Long vandorId) {
		this.vandorId = vandorId;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public List<Device> getChild() {
		return child;
	}
	public void setChild(List<Device> child) {
		this.child = child;
	}
	public Device getParent() {
		return parent;
	}
	public void setParent(Device parent) {
		this.parent = parent;
	}
	public String getRdpNo() {
		return rpdNo;
	}
	public void setRpdNo(String rdpNo) {
		this.rpdNo = rdpNo;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public DeviceType getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	public Vandor getVandor() {
		return vandor;
	}
	public void setVandor(Vandor vandor) {
		this.vandor = vandor;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	
}
