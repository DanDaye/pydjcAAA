package com.wd.pydjc.bsd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wd.pydjc.bsd.dao.DeviceDao;
import com.wd.pydjc.bsd.model.Device;
import com.wd.pydjc.bsd.service.DeviceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 设备相关接口
 * 
 */
@Api(tags = "设备")
@RestController
@RequestMapping("/device")
public class DeviceController {

	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private DeviceService deviceService;

	@GetMapping("/tree")
	@ApiOperation(value = "所有菜单")
	public JSONArray deviceAll() {
		
		List<Device> deviceAll = deviceDao.listAll();
		JSONArray array = new JSONArray();
		setDeviceTree(0L, deviceAll, array);

		return array;
	}
	
	@PostMapping("/save")
	public Device saveDevice(@RequestBody Device device) {
		if(device != null && device.getId() != null){
			 device = deviceService.updateDevice(device);
			 return getDeviceById(device.getId());
		}else{
			 device = deviceService.saveDevice(device);
			 return getDeviceById(device.getId());
		}
	}
	
	@PostMapping("/delete")
	public Device deleteDevice(@RequestBody Device device) {
		return deviceService.deleteDevice(device);
	}
	
	@GetMapping("/getDeviceById")
	public Device getDeviceById(Long id) {
		Device device = deviceService.getDeviceById(id);
		
		return device;
	}


	/**
	 * 菜单树
	 * 
	 * @param pId
	 * @param permissionsAll
	 * @param array
	 */
	private void setDeviceTree(Long pId, List<Device> deviceAll, JSONArray array) {
		for (Device device : deviceAll) {
			if (device.getParentId().equals(pId)) {
				device.setIcon(device.getDeviceType().getIcon());
				String string = JSONObject.toJSONString(device);
				JSONObject parent = (JSONObject) JSONObject.parse(string);
				array.add(parent);

				if (deviceAll.stream().filter(p -> p.getParentId().equals(device.getId())).findAny() != null) {
					JSONArray child = new JSONArray();
					parent.put("child", child);
					setDeviceTree(device.getId(), deviceAll, child);
				}
			}
		}
	}

	
}
