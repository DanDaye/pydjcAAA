package com.wd.pydjc.bsd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.wd.pydjc.bsd.model.Vandor;

@Mapper
public interface VandorDao {

	@Select("select * from bsd_vandor t")
	List<Vandor> listAll();

}
