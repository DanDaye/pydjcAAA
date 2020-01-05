package com.wd.pydjc.bsd.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wd.pydjc.bsd.dao.MeasTypeDao;
import com.wd.pydjc.bsd.model.MeasType;
import com.wd.pydjc.bsd.service.MeasTypeService;
import com.wd.pydjc.common.annotation.LogAnnotation;
import com.wd.pydjc.common.dto.MeasTypeDto;
import com.wd.pydjc.common.dto.UserDto;

import com.wd.pydjc.common.page.table.PageTableRequest;
import com.wd.pydjc.common.page.table.PageTableResponse;
//import com.wd.pydjc.common.page.table.MeasTypePageTableHandler;
//import com.wd.pydjc.common.page.table.MeasTypePageTableHandler.MeasTypeCountHandler;
//import com.wd.pydjc.common.page.table.MeasTypePageTableHandler.MeasTypeListHandler;
import com.wd.pydjc.common.page.table.PageTableHandler;
import com.wd.pydjc.common.page.table.PageTableHandler.CountHandler;
import com.wd.pydjc.common.page.table.PageTableHandler.ListHandler;
import com.wd.pydjc.sys.model.Customer;
import com.wd.pydjc.sys.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "测点类型")
@RestController
@RequestMapping("/measType")
public class MeasTypeController {

	@Autowired
	private MeasTypeDao measTypeDao;
	@Autowired
	private MeasTypeService measTypeService;

	@GetMapping
	@ApiOperation(value = "测点类型列表")
	// @RequiresPermissions("bsd:measType:query")
	public PageTableResponse listMeasType(PageTableRequest request) {
		return new PageTableHandler(new CountHandler(){
			
			@Override
			public int count(PageTableRequest request) {
				return measTypeDao.count(request.getParams());
			}
		}, new ListHandler(){
			@Override
			public List<MeasType> list(PageTableRequest request) {
				List<MeasType> list = measTypeDao.list(request.getParams(), request.getOffset(), request.getLimit());

				return list;
			}
		}).handle(request);

	}
/*	@ApiOperation(value = "根据监测类型id获取测点类型")
	@GetMapping("/{monitorTypeId}")
	//@RequiresPermissions("sys:user:query")
	public MeasType measType(@PathVariable Long monitorTypeId ) {
		return measTypeDao.getByMonitorTypeId(monitorTypeId);
	}*/

	@GetMapping("/getAllMeasType")
	public List<MeasType> getAllMeasType() {
		return measTypeDao.getAllMeasType();
	}
	
	@ApiOperation(value = "根据id获取测点类型")
	@GetMapping("/{id}")
	@RequiresPermissions("sys:user:query")
	public MeasType measType(@PathVariable Long id ) {
		System.out.println("hahaha"+id);
		return measTypeDao.getById(id);
	}

	@LogAnnotation
	@PostMapping
	@ApiOperation(value = "保存用户")
	@RequiresPermissions("bsd:measType:add")
	public MeasType saveMeasType(@RequestBody MeasTypeDto measTypeDto) {
		MeasType m = measTypeService.getMeasType(measTypeDto.getName());
		if (m != null) {
			throw new IllegalArgumentException(measTypeDto.getName() + "已存在");
		}

		return measTypeService.saveMeasType(measTypeDto);
	}

}
