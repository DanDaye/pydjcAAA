package com.wd.pydjc.bsd.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.pydjc.bsd.dao.MonitorTypeDao;
import com.wd.pydjc.bsd.model.Device;
import com.wd.pydjc.bsd.model.MonitorType;


@Service
public class MonitorTypeService {
	private static final Logger log = LoggerFactory.getLogger("adminLogger");
	
	@Autowired
	private MonitorTypeDao monitorTypeDao;
	
	public MonitorType getMonitorTypeById(Long id){
		/*Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);*/
		return monitorTypeDao.getMonitorTypeById(id);
	}

	@Transactional
	public MonitorType updateMonitorType(MonitorType monitortype) {
		// TODO Auto-generated method stub
		monitorTypeDao.update(monitortype);
		return monitortype;
	}
	
	@Transactional
	public MonitorType saveMonitorType(MonitorType monitortype) {
		// TODO Auto-generated method stub
		monitorTypeDao.save(monitortype);
		log.debug("新增设备{}", monitortype.getName());
		return monitortype;
	}
	
	@Transactional
	public MonitorType deleteMonitorType(MonitorType monitortype) {
		// TODO Auto-generated method stub
		monitorTypeDao.deleteMonitorType(monitortype);
		return monitortype;
	}
}
