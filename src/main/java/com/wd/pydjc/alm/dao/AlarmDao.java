package com.wd.pydjc.alm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.wd.pydjc.alm.model.AlarmData;

@Mapper
public interface AlarmDao {

	Integer count(@Param("params") Map<String, Object> params);

	List<AlarmData> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	
	List<AlarmData> getCurrentAlarmList(@Param("params") Map<String, Object> params);
	
	@Update("update alm_alarm_data set cfm_flag = 1 where end_time is null and meas_point_id = #{measPointId}")
	int confirmAlarm(@Param("measPointId") Integer measPointId);
}
