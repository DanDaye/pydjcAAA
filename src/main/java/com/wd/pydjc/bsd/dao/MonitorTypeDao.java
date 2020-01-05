package com.wd.pydjc.bsd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wd.pydjc.bsd.model.MonitorType;

@Mapper
public interface MonitorTypeDao {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into bsd_monitor_type(name, monitor_type_code,parent_id,level) values(#{name}, #{monitorTypeCode}, #{parentId},1)")
	int save(MonitorType monitortype);
	
	@Update("update bsd_monitor_type set name =#{name},monitor_type_code=#{monitorTypeCode} where id =#{id}")
	int update(MonitorType monitoetype);
	
	@Delete("delete from bsd_monitor_type where id =#{id}")
	int deleteMonitorType(MonitorType monitortype);
	
	@Select("select id,name,monitor_type_code,parent_id,level from bsd_monitor_type ")
	List<MonitorType> listAll();


	@Select("select * from bsd_monitor_type where id =#{monitorTypeId}")
	MonitorType getMonitorTypeById(long monitorTypeId);
	
	@Select("select * from bsd_monitor_type t")
	List<MonitorType> getAllMonitorType();
	

}
