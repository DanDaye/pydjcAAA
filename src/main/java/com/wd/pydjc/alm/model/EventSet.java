package com.wd.pydjc.alm.model;

import com.wd.pydjc.sys.model.BaseEntity;

public class EventSet extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;

	private String name;
	private String code;
	private String eventexp;
	
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
	public String getEventexp() {
		return eventexp;
	}
	public void setEventexp(String eventexp) {
		this.eventexp = eventexp;
	}
	
}
