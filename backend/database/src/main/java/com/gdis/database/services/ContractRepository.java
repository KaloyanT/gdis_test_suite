package com.gdis.database.services;
	
import org.springframework.data.repository.CrudRepository;
import com.gdis.database.models.Contract;

public interface ContractRepository extends CrudRepository <Contract, Long> {
	
	public Contract findById(long id);
}
