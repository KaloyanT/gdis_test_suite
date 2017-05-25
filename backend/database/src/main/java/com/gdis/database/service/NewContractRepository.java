package com.gdis.database.service;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.NewContract;

public interface NewContractRepository extends CrudRepository<NewContract, Long> {
	
	public NewContract findById(long id);
	
	public NewContract findByTestName(String testName);
}
