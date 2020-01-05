package com.wd.pydjc.sys.service;

import com.wd.pydjc.sys.model.SysLogs;

/**
 * 日志service
 * 
 */
public interface SysLogService {

	void save(SysLogs sysLogs);

	void save(Long userId, String module, Boolean flag, String remark);

	void deleteLogs();
}
