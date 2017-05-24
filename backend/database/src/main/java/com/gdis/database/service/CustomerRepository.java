package com.gdis.database.service;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.gdis.database.model.Customer;

public interface CustomerRepository  extends CrudRepository <Customer, Long> {
	
	public List<Customer> findByLastName(String lastName);
	
	public Customer findByCustomerID(long customerID);
}
