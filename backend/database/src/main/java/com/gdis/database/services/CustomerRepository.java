package com.gdis.database.services;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.gdis.database.models.Customer;

public interface CustomerRepository  extends CrudRepository <Customer, Long> {
	public List<Customer> findByLastName(String lastName);
	
	public Customer findById(long id);
}
