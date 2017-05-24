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
import com.gdis.database.service.ContractRepository;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/contracts")
public class ContractController {
	
	@Autowired
	private ContractRepository contractRepository;
	
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


