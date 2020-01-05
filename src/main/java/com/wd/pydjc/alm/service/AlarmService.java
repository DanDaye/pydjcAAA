package com.wd.pydjc.alm.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wd.pydjc.alm.dao.AlarmDao;
import com.wd.pydjc.alm.model.AlarmData;

@Service
public class AlarmService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private AlarmDao alarmDao;

	
	/**
	 * 获取当前告警
	 */
	public List<AlarmData> getCurrentAlarmList(Map<String, Object> params){
		return alarmDao.getCurrentAlarmList(params);
	}
	
	public int confirmAlarm(Integer measPointId){
		return alarmDao.confirmAlarm(measPointId);
	}
}
