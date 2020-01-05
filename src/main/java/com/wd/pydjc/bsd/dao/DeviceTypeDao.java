package com.wd.pydjc.bsd.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.wd.pydjc.bsd.model.DeviceType;

@Mapper
public interface DeviceTypeDao {

	@Select("select * from bsd_device_type t ")
	List<DeviceType> listAll();

}
