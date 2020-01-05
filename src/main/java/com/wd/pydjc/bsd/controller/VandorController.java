package com.wd.pydjc.bsd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wd.pydjc.bsd.dao.VandorDao;
import com.wd.pydjc.bsd.model.Vandor;

import io.swagger.annotations.Api;

@Api(tags = "供应商")
@RestController
@RequestMapping("/Vandor")
public class VandorController {

	@Autowired
	private VandorDao vandorDao;
	

	@GetMapping("/list")
	public List<Vandor> list() {
		List<Vandor> list = vandorDao.listAll();
		return list;
	}
}
