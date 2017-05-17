package com.gdis.database.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gdis.database.models.Contract;
import com.gdis.database.services.ContractRepository;

@RestController
@RequestMapping("/contracts")
public class ContractController {
	
	@Autowired
	private ContractRepository contractRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Contract> getAllContracts() {
		return contractRepository.findAll();		
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public Contract getContract(final long id) {
		return contractRepository.findById(id);
	}
}
