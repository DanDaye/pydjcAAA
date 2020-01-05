package com.wd.pydjc.bsd.model;

import com.wd.pydjc.sys.model.BaseEntity;

public class Vandor extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;

	private String shortName;
	private String longName;
	private String linkman;
	private String phone;
	private String user;
	private String password;
	private String port;
	private Long isStart;
	private Long customerId;
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getLongName() {
		return longName;
	}
	public void setLongName(String longName) {
		this.longName = longName;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
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
	
	
}
