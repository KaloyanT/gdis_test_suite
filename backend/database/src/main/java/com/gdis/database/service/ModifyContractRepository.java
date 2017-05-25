package com.gdis.database.service;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.ModifyContract;

public interface ModifyContractRepository extends CrudRepository<ModifyContract, Long> {
	
	public ModifyContract findById(long id);
	
	public ModifyContract findByTestName(String testName);

}
