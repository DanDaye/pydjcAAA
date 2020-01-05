package com.wd.pydjc.common.dto;

import java.util.List;

import com.wd.pydjc.bsd.model.MeasType;

public class MeasTypeDto extends MeasType {

	private static final long serialVersionUID = -4065965739148834001L;
	
	private List<Long> permissionIds;

	public List<Long> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(List<Long> permissionIds) {
		this.permissionIds = permissionIds;
	}
}
