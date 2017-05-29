package com.gdis.database.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.NewContract;

public interface NewContractRepository extends CrudRepository<NewContract, Long> {
	
	public NewContract findByNewContractID(long id);
	
	public List<NewContract> findByTestName(String testName);
}
