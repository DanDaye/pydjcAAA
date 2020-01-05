package com.wd.pydjc.bsd.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wd.pydjc.bsd.dao.MeasPointDao;
import com.wd.pydjc.bsd.model.MeasPoint;
import com.wd.pydjc.common.utils.UserUtil;

@Service
public class MeasPointService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private MeasPointDao measPointDao;

	public List<MeasPoint> getDeviceTotleMeasPoint(String measTypeCode){
		return measPointDao.getDeviceTotleMeasPoint(measTypeCode, UserUtil.getCurrentUser().getCustomerId());
	}

}
