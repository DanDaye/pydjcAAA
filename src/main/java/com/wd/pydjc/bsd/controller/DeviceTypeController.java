package com.wd.pydjc.bsd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wd.pydjc.bsd.dao.DeviceTypeDao;
import com.wd.pydjc.bsd.model.DeviceType;

import io.swagger.annotations.Api;

@Api(tags = "设备类型")
@RestController
@RequestMapping("/deviceType")
public class DeviceTypeController {

	@Autowired
	private DeviceTypeDao deviceTypeDao;
	

	@GetMapping("/list")
	public List<DeviceType> list() {
		List<DeviceType> list = deviceTypeDao.listAll();
		return list;
	}
}
