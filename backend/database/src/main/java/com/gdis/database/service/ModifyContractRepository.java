package com.gdis.database.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gdis.database.model.Contract;
import com.gdis.database.model.ModifyContract;

public interface ModifyContractRepository extends CrudRepository<ModifyContract, Long> {
	
	public ModifyContract findByModifiedContractID(long id);
	
	public ModifyContract findByTestName(String testName);
	
	public List<ModifyContract> findByContractAndTestNameAndChangedMonthlyPremiumAndNewEndDate(Contract contract, 
			String testName, double changedMonthlyPremium, Date newEndDate);

}
