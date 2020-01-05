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
import com.wd.pydjc.bsd.dao.MonitorTypeDao;
import com.wd.pydjc.bsd.model.Device;
import com.wd.pydjc.bsd.model.MonitorType;
import com.wd.pydjc.bsd.service.MonitorTypeService;
import com.wd.pydjc.sys.model.Customer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "监测类型")
@RestController
@RequestMapping("/monitorType")
public class MonitorTypeController {

	@Autowired
	private MonitorTypeDao MonitorTypeDao;
	@Autowired
	private  MonitorTypeService  MonitorTypeService;

	@GetMapping("/tree")
	@ApiOperation(value = "所有菜单")
	public JSONArray monitorTypeAll() {
		
		List<MonitorType> monitorTypeAll = MonitorTypeDao.listAll();
		JSONArray array = new JSONArray();
		setMonitorTypeTree(0L, monitorTypeAll, array);

		return array;
	}
	
	@PostMapping("/save")
	public MonitorType saveMonitorType(@RequestBody MonitorType monitortype) {
		System.out.println("hahahahaha"+monitortype.getName()+monitortype.getMonitorTypeCode());
		if(monitortype != null && monitortype.getId() != null){
			monitortype = MonitorTypeService.updateMonitorType(monitortype);
			 return getMonitorTypeById(monitortype.getId());
		}else{
			monitortype = MonitorTypeService.saveMonitorType(monitortype);
			 return getMonitorTypeById(monitortype.getId());
		}
	}
	
	@PostMapping("/delete")
	public MonitorType deleteMonitorType(@RequestBody MonitorType monitortype) {
		return MonitorTypeService.deleteMonitorType(monitortype);
	}

	@GetMapping("/getMonitorTypeById")
	private MonitorType getMonitorTypeById(Long id) {
		// TODO Auto-generated method stub
		
		MonitorType monitortype = MonitorTypeService.getMonitorTypeById(id);
	
		return monitortype;
	}

	private void setMonitorTypeTree(long pId, List<MonitorType> monitorTypeAll, JSONArray array) {
		// TODO Auto-generated method stub
		for (MonitorType monitortype : monitorTypeAll) {
			if (monitortype.getParentId().equals(pId)) {
//				monitortype.setIcon(monitortype.getDeviceType().getIcon());
				String string = JSONObject.toJSONString(monitortype);
				JSONObject parent = (JSONObject) JSONObject.parse(string);
				array.add(parent);

				if (monitorTypeAll.stream().filter(p -> p.getParentId().equals(monitortype.getId())).findAny() != null) {
					JSONArray child = new JSONArray();
					parent.put("child", child);
					setMonitorTypeTree(monitortype.getId(), monitorTypeAll, child);
				}
			}
		}
	}
	
	@GetMapping("/getAllMonitorType")
	public List<MonitorType> getAllMonitorType() {
		return MonitorTypeDao.getAllMonitorType();
	}
}
