package com.wd.pydjc.elec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wd.pydjc.bsd.model.Device;
import com.wd.pydjc.bsd.service.DeviceService;
import io.swagger.annotations.Api;

/**
 * 实时用电
 * 
 */
@Api(tags = "设备")
@RestController
@RequestMapping("/elec/realtime")
public class RealTimeController {

	@Autowired
	private DeviceService deviceService;

	@GetMapping("/list")
	public List<Device> list(Long deviceId) {

		/**
		 * deviceId 为空时 取根节点
		 */
		if (deviceId == null) {
			deviceId = 1L;
		}
		List<Device> list = deviceService.getDeviceIncludeMeasPoint(deviceId);
		return list;
	}
	
}
