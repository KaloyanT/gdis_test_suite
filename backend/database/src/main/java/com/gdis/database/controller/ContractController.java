package com.gdis.database.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.database.model.Contract;
import com.gdis.database.model.Customer;
import com.gdis.database.service.ContractRepository;
import com.gdis.database.service.CustomerRepository;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/contracts")
public class ContractController {
	
	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllContracts() {
		
		Iterable<Contract> contractsIterable = contractRepository.findAll();
		
		if(contractsIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<Contract> contractsList = new ArrayList<Contract>();
		
		// Java 8 Method Reference is used here
		contractsIterable.forEach(contractsList::add);
		
		return new ResponseEntity<>(contractsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getContractByID(@PathVariable("id") final long id) {
		
		PreCondition.require(id >= 0, "Contract ID can't be negative!");
		
		Contract response = contractRepository.findByContractID(id);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST, 
			consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<?> createContract(@RequestBody Contract newContract) {
		
		if(newContract == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		
		// Check if the given policyOwner already exists in the customers table of the DB
		// If so, don't insert it as a new customer in the customers table
		Customer policyOwnerForNewContract = newContract.getPolicyOwner();
		
		List<Customer> customersWithSameLastNameAndBirthday = customerRepository.findByLastNameAndBirthday(
				policyOwnerForNewContract.getLastName(), policyOwnerForNewContract.getBirthday());
				
		long existingCustomerID = policyOwnerForNewContract.customerExistsInDB(customersWithSameLastNameAndBirthday);
				
		if(existingCustomerID > 0) {
			
			policyOwnerForNewContract = customerRepository.findByCustomerID(existingCustomerID);
					
			newContract.setPolicyOwner(policyOwnerForNewContract);;
		}
		
		
		customersWithSameLastNameAndBirthday.clear();
		
		if(!newContract.getPolicyOwner().toStringWithoutID().equals(newContract.getInsuredPerson().toStringWithoutID())) {
			// Check if the given customer already exists in the customers table of the DB
			// If so, don't insert the customer again in the customers table
			Customer insuredPersonForNewContract = newContract.getInsuredPerson();
							
			customersWithSameLastNameAndBirthday = customerRepository.findByLastNameAndBirthday(
					insuredPersonForNewContract.getLastName(), insuredPersonForNewContract.getBirthday());
							
			existingCustomerID = insuredPersonForNewContract.customerExistsInDB(customersWithSameLastNameAndBirthday);
							
			if(existingCustomerID > 0) {
						
				insuredPersonForNewContract = customerRepository.findByCustomerID(existingCustomerID);
								
				newContract.setInsuredPerson(insuredPersonForNewContract);;
			}
		} else {
			
			newContract.setInsuredPerson(newContract.getPolicyOwner());
		}
		
		
		
		contractRepository.save(newContract);
		
		return new ResponseEntity<>(newContract, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, 
			consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}) 
	public ResponseEntity<?> updateContract(@PathVariable("id") final long id, @RequestBody Contract updatedContract) {
		
		PreCondition.require(id >= 0, "Contract ID can't be negative!");
		
		if(updatedContract == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Contract currentContract = contractRepository.findByContractID(id);
		
		if(currentContract == null) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
		
		updatedContract.setContractID(id);
		
		contractRepository.save(updatedContract);
			
		return new ResponseEntity<>(currentContract, HttpStatus.ACCEPTED);
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteContract(@PathVariable("id") final long id) {
		
		PreCondition.require(id >= 0, "Contract ID can't be negative!");
		
		Contract toBeDeleted = contractRepository.findByContractID(id);
		
		if(toBeDeleted == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		contractRepository.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}


