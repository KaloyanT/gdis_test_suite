package com.gdis.database.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.database.model.ModifyContract;
import com.gdis.database.service.ModifyContractRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/modifyContract")
public class ModifyContractController {
	
	@Autowired
	private ModifyContractRepository modifyContractRepository;
	
	//@Autowired
	//private ContractRepository contractRepository;
	
	private ModifyContract modifiedContractToSave;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllModifiedContracts() {
		
		Iterable<ModifyContract> modifiedContractsIterable = modifyContractRepository.findAll();
		
		if(modifiedContractsIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<ModifyContract> modifiedContractsList = new ArrayList<ModifyContract>();
		
		// Java 8 Method Reference is used here
		modifiedContractsIterable.forEach(modifiedContractsList::add);
		
		return new ResponseEntity<>(modifiedContractsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getModifiedContract(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Modified Contract ID can't be negative!");
		
		ModifyContract modifiedContract = modifyContractRepository.findByModifiedContractID(id);
		
		if (modifiedContract == null) {
			return new ResponseEntity<>(new CustomErrorType("Contract with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(modifiedContract, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createModifiedContract(@RequestBody ModifyContract newModifiedContract) {
	    
		
		if(newModifiedContract == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setModifiedContractToSave(newModifiedContract);

		boolean duplicateExists = duplicateModifiedContractFound(getModifiedContractToSave());
		
		if(duplicateExists == true) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		modifyContractRepository.save(newModifiedContract);
			
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	
	private boolean duplicateModifiedContractFound(ModifyContract newModifiedContract) {
		
		
		/*
		boolean contractExists = false;
		
		
		Contract contract = newModifiedContract.getContract();
		
		List<Contract> similarContracts = contractRepository.findByPolicyOwnerAndInsuredPersonAndProduct(
				contract.getPolicyOwner(), contract.getInsuredPerson(), contract.getProduct());
		
		if(contract.contractExistsInDB(similarContracts) > 0) {
			contractExists = true;
			contract = contractRepository.findByContractID(contract.getContractID());
			newModifiedContract.setContract(contract);
		}
		
		//contractRepository.save(contract);
		
		if(contractExists == false) {
			return false;
		}
		*/
		
		/*
		contractRepository.save(newModifiedContract.getContract());
		
		List<ModifyContract> similarModifiedContracts = 
				modifyContractRepository.findByContractAndTestNameAndChangedMonthlyPremiumAndNewEndDate(
						newModifiedContract.getContract(), newModifiedContract.getTestName(), newModifiedContract.getChangedMonthlyPremium(), 
						newModifiedContract.getNewEndDate());
		
		
		
		if(newModifiedContract.modifiedContractExistsInDB(similarModifiedContracts) > 0) {
			return true;
		}
		
		*/
		return false;
	}


	public ModifyContract getModifiedContractToSave() {
		return modifiedContractToSave;
	}


	public void setModifiedContractToSave(ModifyContract modifiedContractToSave) {
		this.modifiedContractToSave = modifiedContractToSave;
	}

}
