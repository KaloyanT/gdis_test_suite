package com.gdis.database.services;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.models.NewContract;

public interface NewContractRepository extends CrudRepository<NewContract, Long> {
	
	public NewContract findById(long id);
}
