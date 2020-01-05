package com.wd.pydjc.sys.service;

import com.wd.pydjc.common.dto.RoleDto;

public interface RoleService {

	void saveRole(RoleDto roleDto);

	void deleteRole(Long id);
}
