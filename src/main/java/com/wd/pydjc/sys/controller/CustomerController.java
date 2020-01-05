package com.wd.pydjc.sys.controller;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wd.pydjc.common.annotation.LogAnnotation;
import com.wd.pydjc.common.page.table.PageTableHandler;
import com.wd.pydjc.common.page.table.PageTableHandler.CountHandler;
import com.wd.pydjc.common.page.table.PageTableHandler.ListHandler;
import com.wd.pydjc.common.page.table.PageTableRequest;
import com.wd.pydjc.common.page.table.PageTableResponse;
import com.wd.pydjc.sys.dao.CustomerDao;
import com.wd.pydjc.sys.model.Customer;
import com.wd.pydjc.sys.model.Role;
import com.wd.pydjc.sys.service.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 客户相关接口
 * 
 *
 */
@Api(tags = "客户")
@RestController
@RequestMapping("/customer")
public class CustomerController {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerDao customerDao;

	@LogAnnotation
	@PostMapping
	@ApiOperation(value = "保存客户")
	public Customer saveUser(@RequestBody Customer customer) {
		
		if(customer.getId() != null){
			customerService.updateCustomer(customer);
		}else{
			Customer c = customerService.getCustomer(customer.getName());
			if (c != null) {
				throw new IllegalArgumentException(customer.getName() + "已存在");
			}
			customerService.saveCustomer(customer);
		}

		return customer;
	}


	@GetMapping
	@ApiOperation(value = "用户列表")
	public PageTableResponse listUsers(PageTableRequest request) {
		return new PageTableHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return customerDao.count(request.getParams());
			}
		}, new ListHandler() {

			@Override
			public List<Customer> list(PageTableRequest request) {
				List<Customer> list = customerDao.list(request.getParams(), request.getOffset(), request.getLimit());
				return list;
			}
		}).handle(request);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取")
	public Customer get(@PathVariable Long id) {
		return customerDao.getById(id);
	}
	
	@GetMapping("/getAllCustomer")
	public List<Customer> getAllCustomer() {
		return customerDao.getAllCustomer();
	}
}
