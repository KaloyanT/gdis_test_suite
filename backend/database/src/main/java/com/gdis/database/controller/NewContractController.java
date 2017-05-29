package com.gdis.database.controller;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.database.model.Customer;
import com.gdis.database.model.NewContract;
import com.gdis.database.service.CustomerRepository;
import com.gdis.database.service.NewContractRepository;
import com.gdis.database.util.PreCondition;
import com.gdis.database.util.CustomErrorType;

@RestController
@RequestMapping("/db/newContract")
public class NewContractController {

	@Autowired
	private NewContractRepository newContractRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	private NewContract contractToSave;

	// retrieve all contacts
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllNeContracts() {
		
		Iterable<NewContract> newContractsIterable = newContractRepository.findAll();
		
		List<NewContract> contracts = new ArrayList<NewContract>();
		
		for (NewContract nc : newContractsIterable) {
			contracts.add(nc);
		}
		
		return new ResponseEntity<>(contracts, HttpStatus.OK);
	}

	
	// retrieve single new contract
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getNewContract(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "New Contract ID can't be negative!");
		
		NewContract newContract = newContractRepository.findByNewContractID(id);
		
		if (newContract == null) {
			return new ResponseEntity<>(new CustomErrorType("New Contract with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(newContract, HttpStatus.OK);
	}
	
	
	//create new contract in the database
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createNewContract(@RequestBody NewContract newContract) {
	    
		
		if(newContract == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setContractToSave(newContract);
		
		boolean customerExists = false;
		
		Customer customerNC = newContract.getCustomer();
		
		List<Customer> similarCustomers = customerRepository.findByFirstNameAndLastNameAndBirthdayAndAddress(
			customerNC.getFirstName(), customerNC.getLastName(), customerNC.getBirthday(), customerNC.getAddress());
		
		long existingCustomerID = customerNC.customerExistsInDB(similarCustomers);
		
		if(existingCustomerID > 0) {	
			customerExists = true;
			customerNC = customerRepository.findByCustomerID(existingCustomerID);
			newContract.setCustomer(customerNC);
			setContractToSave(newContract);
		}
				
		// Check if the given customer already exists in the customers table of the DB
		// If so, don't insert the customer again in the customers table
		
		boolean duplicateContract = false;
		
		if(customerExists == true) {
			duplicateContract = duplicateContractFound(getContractToSave());
		}
				
		if(duplicateContract == true) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		/*
		if(newContractRepository.existsById(newContract.getNewContractID())) {
			//return new ResponseEntity<>(new CustomErrorType("Unable to create a contract with id " +
			//     newContract.getId() + " already exist."),HttpStatus.CONFLICT);
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		
		} */
			
		newContractRepository.save(getContractToSave());
			
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}

	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateNewContract(@PathVariable("id") long id, @RequestBody NewContract newContract) {
		
		PreCondition.require(id >= 0, "New Contract ID can't be negative!");
		
		NewContract currentContract = newContractRepository.findByNewContractID(id);

		if (currentContract == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to update. Contract with id "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}

		currentContract.setCustomer(newContract.getCustomer());
		currentContract.setProductType(newContract.getProductType());
		currentContract.setContractBegin(newContract.getContractBegin());

		newContractRepository.save(currentContract);
		
		return new ResponseEntity<NewContract>(currentContract, HttpStatus.OK);
	}

	
	// delete a new contract
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteNewContract(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "New Contract ID can't be negative!");
		
		NewContract currentContract = newContractRepository.findByNewContractID(id);
		
		if (currentContract == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		newContractRepository.delete(currentContract);
		
		return new ResponseEntity<NewContract>(HttpStatus.NO_CONTENT);
	}

	
	// delete all new contracts
	@RequestMapping(value = "/delete/all", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAllNewContracts() {
		
		newContractRepository.deleteAll();
		
		return new ResponseEntity<NewContract>(HttpStatus.NO_CONTENT);
	}


	public NewContract getContractToSave() {
		return contractToSave;
	}


	public void setContractToSave(NewContract contractToSave) {
		this.contractToSave = contractToSave;
	}

	
	private boolean duplicateContractFound(NewContract newContract) {
		
		
		List<NewContract> newContractsFromSameTest = newContractRepository.findByTestName(newContract.getTestName());
		
		if(newContract.newContractExistsInDB(newContractsFromSameTest) > 0) {
			return true;
		}
		
		//setContractToSave(newContract);
		
		return false;
	}
}
