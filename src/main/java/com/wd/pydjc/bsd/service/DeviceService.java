package com.wd.pydjc.bsd.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.pydjc.bsd.dao.DeviceDao;
import com.wd.pydjc.bsd.model.Device;
import com.wd.pydjc.common.utils.UserUtil;
import com.wd.pydjc.sys.model.User;

@Service
public class DeviceService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private DeviceDao deviceDao;

	public Device getDeviceById(Long id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return deviceDao.getDeviceById(params);
	}

	
	@Transactional
	public Device saveDevice(Device device) {

		deviceDao.save(device);
		
		log.debug("新增设备{}", device.getName());
		return device;
	}
	
	@Transactional
	public Device updateDevice(Device device) {
		deviceDao.update(device);

		return device;
	}
	
	@Transactional
	public Device deleteDevice(Device device) {
		deviceDao.deleteDevice(device);

		return device;
	}
	
	/**
	 * 获取有测点的设备
	 */
	public List<Device> getDeviceIncludeMeasPoint(Long deviceId){
		Map<String, Object> params = new HashMap<String, Object>();
		
		/**
		 * 设备ID 为空 取根节点数据
		 */
		params.put("id", deviceId);
		params.put("customerId", UserUtil.getCurrentUser().getCustomerId());
		return deviceDao.getDeviceIncludeMeasPoint(params);
	}
}
