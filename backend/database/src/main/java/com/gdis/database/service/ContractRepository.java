package com.gdis.database.service;
	
import org.springframework.data.repository.CrudRepository;

import com.gdis.database.model.Contract;

public interface ContractRepository extends CrudRepository <Contract, Long> {
	
	public Contract findById(long id);
}
