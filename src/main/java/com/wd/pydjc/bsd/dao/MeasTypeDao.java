package com.wd.pydjc.bsd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.wd.pydjc.bsd.model.MeasType;


@Mapper
public interface MeasTypeDao {
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into bsd_meas_type (name,code,monitor_type_id,code645,fc,unit,is_start,one_lower_limit,one_upper_limit,two_lower_limit,two_upper_limit,three_lower_limit,three_upper_limit,limit_unit,create_time,update_time)Values(#{name},#{code},#{monitorTypeId},#{code645},#{fc},#{unit},#{isStart},#{oneLowerLimit},#{oneUpperLimit},#{twoLowerLimit},#{twoUpperLimit}, #{threeLowerLimit},#{threeUpperLimit},#{limitUnit},now(),now())")
	int save(MeasType measType);
	
	@Select("select * from bsd_meas_type t ")
	List<MeasType> listAll();

	List<MeasType> getChildList(@Param("params") Map<String, Object> params);

	@Select("select count(1) from bsd_meas_type t ")
	Integer count(@Param("params") Map<String, Object> params);

	List<MeasType> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	@Select("select * from bsd_meas_type t ")
	List<MeasType> getAllMeasType();

	@Select("select * from bsd_meas_type t where t.name = #{name}")
	MeasType getMeasType(String name);

	@Update("update bsd_meas_type t set t.name = #{name}, t.code = #{code}, t.monitor_type_id = #{monitorTypeId},t.code645 = #{code645},t.fc = #{fc},t.unit = #{unit},t.is_start = #{isStart},t.one_lower_limit = #{oneLowerLimit},t.one_upper_limit = #{oneUpperLimit},t.two_lower_limit = #{twoLowerLimit},t.two_upper_limit = #{twoUpperLimit},t.three_lower_limit = #{threeLowerLimit},t.three_upper_limit = #{threeUpperLimit},t.limit_unit = #{limitUnit},update_time = now() where t.id = #{id}")
	int update(MeasType measType);
	
	
	MeasType getByMonitorTypeId(Long monitorTypeId);
	
	@Select("select * from bsd_meas_type t where t.id = #{id} ")
	MeasType getById(Long id);

	

}
