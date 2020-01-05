package com.wd.pydjc.bsd.service;

import org.springframework.stereotype.Service;

import com.wd.pydjc.bsd.model.MeasType;
import com.wd.pydjc.common.dto.MeasTypeDto;

@Service
public interface MeasTypeService {

	MeasType getMeasType(String name);

	MeasType saveMeasType(MeasTypeDto measTypeDto);

}
