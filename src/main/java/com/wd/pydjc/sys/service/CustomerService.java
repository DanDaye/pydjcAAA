package com.wd.pydjc.sys.service;

import com.wd.pydjc.sys.model.Customer;

public interface CustomerService {

	Customer saveCustomer(Customer customer);
	
	Customer updateCustomer(Customer Customer);

	Customer getCustomer(String name);

}
