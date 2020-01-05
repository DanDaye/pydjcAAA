package com.wd.pydjc.bsd.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.wd.pydjc.bsd.model.DeviceTotleConf;

@Mapper
public interface DeviceTotleConfDao {

	
	@Select("select * from bsd_device_totle_conf t where t.customer_id = #{customerId}")
	List<DeviceTotleConf> listAll(Integer customerId);
	
}

