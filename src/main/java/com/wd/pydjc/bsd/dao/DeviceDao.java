package com.wd.pydjc.bsd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.wd.pydjc.bsd.model.Device;

@Mapper
public interface DeviceDao {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into bsd_device(name, no, device_type_id, parent_id, is_start, customer_id, vandor_id, sort, rdp_no,del_flag, create_time, update_time) values(#{name}, #{no}, #{deviceTypeId}, #{parentId}, #{isStart}, #{customerId}, #{vandorId}, #{sort}, #{rpdNo}, #{delFlag},now(), now())")
	int save(Device device);
	
	int update(Device device);
	
	int deleteDevice(Device device);
	
	List<Device> listAll();
	
	Device getDeviceById(@Param("params") Map<String, Object> params);
	
	List<Device> getDeviceIncludeMeasPoint(@Param("params") Map<String, Object> params);
}

