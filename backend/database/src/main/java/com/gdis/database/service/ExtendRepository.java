package com.gdis.database.service;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.ExtendContract;

public interface ExtendRepository extends CrudRepository<ExtendContract, Long> {
	
	public ExtendContract findById(long id);
	
	public ExtendContract findByTestName(String testName);

}
