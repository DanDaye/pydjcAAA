package com.wd.pydjc.bsd.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.pydjc.bsd.dao.MeasTypeDao;
import com.wd.pydjc.bsd.model.MeasType;
import com.wd.pydjc.bsd.service.MeasTypeService;
import com.wd.pydjc.common.dto.MeasTypeDto;

@Service
public class MeasTypeServiceImpl implements MeasTypeService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");
	
	@Autowired
	private MeasTypeDao measTypeDao;

	@Override
	public MeasType getMeasType(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public MeasType saveMeasType(MeasTypeDto measTypeDto) {
		// TODO Auto-generated method stub
		MeasType measType = measTypeDto;
		if (measType.getId() != null) {//UPDATE
			MeasType mt = measTypeDao.getMeasType(measType.getName());
			if (mt != null && mt.getId() != measType.getId()) {
				throw new IllegalArgumentException(measType.getName() + "已存在");
			}
			measTypeDao.update(measType);
		}else{//ADD
			MeasType mt = measTypeDao.getMeasType(measType.getName());
			if(mt!=null){
				throw new IllegalArgumentException(measType.getName() + "已存在");
			}
			measTypeDao.save(measType);
			log.debug("新增测点类型{}", measType.getName());
		}
		return measType;
	}

}
