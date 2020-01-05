package com.wd.pydjc.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.wd.pydjc.sys.model.Customer;

@Mapper
public interface CustomerDao {

	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("insert into sys_customer(name, name_abbr, name_en, code, address, tel, status, createTime, updateTime) values(#{name}, #{nameAbbr}, #{nameEn}, #{code}, #{address}, #{tel}, #{status}, now(), now())")
	int save(Customer customer);

	@Select("select * from sys_customer t where t.id = #{id}")
	Customer getById(Long id);
	
	@Select("select * from sys_customer t where t.status = 1")
	List<Customer> getAllCustomer();

	@Select("select * from sys_customer t where t.name = #{name}")
	Customer getCustomer(String name);
	
	@Select("select * from sys_customer t where t.name like '%#{name}%'")
	List<Customer> getCustomerByLikeName(String name);

	Integer count(@Param("params") Map<String, Object> params);

	List<Customer> list(@Param("params") Map<String, Object> params, @Param("offset") Integer offset,
			@Param("limit") Integer limit);

	int update(Customer customer);
}
