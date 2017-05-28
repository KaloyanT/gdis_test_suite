package com.gdis.database.service;
	
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gdis.database.model.Contract;
import com.gdis.database.model.Customer;
import com.gdis.database.model.Product;

public interface ContractRepository extends CrudRepository <Contract, Long> {
	
	public Contract findByContractID(long contractID);
	
	public List<Contract> findByPolicyOwner(Customer policyOwner);
	
	public List<Contract> findByPolicyOwnerAndInsuredPersonAndProduct(Customer policyOwner, 
			Customer insuredPerson, Product product);
	
}
