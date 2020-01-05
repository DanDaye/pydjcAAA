package com.wd.pydjc.alm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wd.pydjc.alm.dao.AlarmDao;
import com.wd.pydjc.alm.model.AlarmData;
import com.wd.pydjc.alm.service.AlarmService;
import com.wd.pydjc.common.page.table.PageTableHandler;
import com.wd.pydjc.common.page.table.PageTableHandler.CountHandler;
import com.wd.pydjc.common.page.table.PageTableHandler.ListHandler;
import com.wd.pydjc.common.page.table.PageTableRequest;
import com.wd.pydjc.common.page.table.PageTableResponse;
import com.wd.pydjc.common.utils.UserUtil;
import com.wd.pydjc.sys.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

/**
 * 告警
 * 
 *
 */
@Api(tags = "告警")
@RestController
@RequestMapping("/alm/alarm")
public class AlarmController {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private AlarmDao hisAlarmDao;

	@Autowired
	private AlarmService alarmService;

	/**
	 * 获取高级列表
	 * @param request 告警列表请求
	 * @return 告警列表响应
	 */
	@GetMapping
	@ApiOperation(value = "告警列表")
	public PageTableResponse list(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return hisAlarmDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<AlarmData> list(PageTableRequest request) {
				List<AlarmData> list = hisAlarmDao.list(request.getParams(), request.getOffset(), request.getLimit());
				return list;
			}
		}).handle(request);
	}

	/**
	 * 获取当前用户
	 * @return 当前用户
	 */
	@ApiOperation(value = "当前登录用户")
	@GetMapping("/current")
	public User currentUser() {
		return UserUtil.getCurrentUser();
	}

	/**
	 * 获取当前所有告警列表
	 * @return 告警列表数据
	 */
	@GetMapping("/getCurrentAlarmList")
	public List<AlarmData> getCurrentAlarmList(){
		Map<String, Object> params = new HashMap<String, Object>();
		return alarmService.getCurrentAlarmList(params);
	}

	/**
	 * 确认告警
	 * @param measPointId
	 * @return
	 */
	@GetMapping("/confirm")
	public JSONObject confirm(Integer measPointId) {
		JSONObject rows = new JSONObject();
		
		alarmService.confirmAlarm(measPointId);
		
		rows.put("status", "1");
		return rows;
	}
}
