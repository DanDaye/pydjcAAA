package com.wd.pydjc.sys.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wd.pydjc.sys.dao.CustomerDao;
import com.wd.pydjc.sys.model.Customer;
import com.wd.pydjc.sys.model.Customer.Status;
import com.wd.pydjc.sys.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger log = LoggerFactory.getLogger("adminLogger");

	@Autowired
	private CustomerDao customerDao;

	@Override
	@Transactional
	public Customer saveCustomer(Customer customer) {
		customer.setStatus(Status.VALID);
		customerDao.save(customer);
		
		log.debug("新增客户{}", customer.getName());
		return customer;
	}

	@Override
	public Customer getCustomer(String name) {
		return customerDao.getCustomer(name);
	}


	@Override
	public Customer updateCustomer(Customer customer) {
		customerDao.update(customer);
		return customer;
	}

}
