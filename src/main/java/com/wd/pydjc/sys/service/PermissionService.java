package com.wd.pydjc.sys.service;

import com.wd.pydjc.sys.model.Permission;

public interface PermissionService {

	void save(Permission permission);

	void update(Permission permission);

	void delete(Long id);
}
