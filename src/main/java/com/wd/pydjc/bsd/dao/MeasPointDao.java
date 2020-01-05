package com.wd.pydjc.bsd.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.wd.pydjc.bsd.model.MeasPoint;

@Mapper
public interface MeasPointDao {


	@Select("SELECT t.* FROM bsd_meas_point t,bsd_meas_type mt,bsd_device_totle_conf c WHERE t.device_id = c.device_id and t.meas_type_id = mt.id AND t.meas_type_id = c.meas_type_id and mt.code = #{measTypeCode} and c.customer_id = #{customerId}")
	List<MeasPoint> getDeviceTotleMeasPoint(@Param("measTypeCode") String measTypeCode,@Param("customerId") Integer customerId);
}

