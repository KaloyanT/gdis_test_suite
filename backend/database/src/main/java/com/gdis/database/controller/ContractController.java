package com.gdis.database.controller;



import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.gdis.database.model.Contract;
import com.gdis.database.service.ContractRepository;

@RestController
@RequestMapping("/db/contracts")
public class ContractController {
	
	@Autowired
	private ContractRepository contractRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	//public Iterable<Contract> getAllContracts() {
	public ResponseEntity<?> getAllContracts() {
		
		Iterable<Contract> contractsIterable = contractRepository.findAll();
		
		List<Contract> contractsList = new ArrayList<Contract>();
		
		// Java 8 Method Reference is used here
		contractsIterable.forEach(contractsList::add);
		
		return new ResponseEntity<>(contractsList, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	//public Contract getContract(final long id) {
	public ResponseEntity<?> getContractByID(@RequestParam(value = "id") final long id) {
		
		Contract response = contractRepository.findById(id);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value="/insert", method = RequestMethod.POST, 
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> insertContract(@RequestBody Contract newContract) {
		
		contractRepository.save(newContract);
		
		return new ResponseEntity<>(newContract, HttpStatus.ACCEPTED);
	}
	
	
}


