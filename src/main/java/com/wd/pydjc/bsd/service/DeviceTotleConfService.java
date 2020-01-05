package com.wd.pydjc.bsd.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wd.pydjc.bsd.dao.DeviceTotleConfDao;
import com.wd.pydjc.bsd.model.DeviceTotleConf;
import com.wd.pydjc.common.utils.UserUtil;

@Service
public class DeviceTotleConfService {

	@Autowired
	private DeviceTotleConfDao deviceTotleConfDao;

	public List<DeviceTotleConf> getDeviceTotleConf(){
		return deviceTotleConfDao.listAll(UserUtil.getCurrentUser().getCustomerId());
	}

	
	
}
