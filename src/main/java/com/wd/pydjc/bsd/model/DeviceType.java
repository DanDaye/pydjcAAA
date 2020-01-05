package com.wd.pydjc.bsd.model;

import com.wd.pydjc.sys.model.BaseEntity;

public class DeviceType extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;

	private String name;
	private String code;
	private String desc;
	private String icon;
	private Long isStart;
	private Long orgId;
	
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
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	
}
