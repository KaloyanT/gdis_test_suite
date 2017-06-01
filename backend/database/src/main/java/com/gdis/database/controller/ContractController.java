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
import com.gdis.database.model.Product;
import com.gdis.database.service.ContractRepository;
import com.gdis.database.service.CustomerRepository;
import com.gdis.database.service.ProductRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/contracts")
public class ContractController {
	
	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	private Contract contractToSave;
	
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
			return new ResponseEntity<>(new CustomErrorType("Contract with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST, 
			consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<?> createContract(@RequestBody Contract newContract) {
		
		if(newContract == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setContractToSave(newContract);
		
		boolean contractExists = duplicateContractFound(getContractToSave());
		
		if(contractExists == true) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		// Make sure that the correct value is saved
		// Check if the global variable is changed in the duplicateContractFound method
		contractRepository.save(getContractToSave());
		
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
			return new ResponseEntity<>(new CustomErrorType("Contract with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		updatedContract.setContractID(id);
		
		/*
		Customer policyOwner = currentContract.getPolicyOwner();
		policyOwner.removeFromOwnedContracts(currentContract);
		policyOwner.addToOwnedContracts(updatedContract);
		*/
		contractRepository.save(updatedContract);
			
		return new ResponseEntity<>(currentContract, HttpStatus.ACCEPTED);
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteContract(@PathVariable("id") final long id) {
		
		PreCondition.require(id >= 0, "Contract ID can't be negative!");
		
		Contract toBeDeleted = contractRepository.findByContractID(id);
		
		if(toBeDeleted == null) {
			return new ResponseEntity<>(new CustomErrorType("Contract with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		
		Customer policyOwner = toBeDeleted.getPolicyOwner();
		policyOwner.removeFromOwnedContracts(toBeDeleted);
		
		contractRepository.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * This method performs a step-by-step or an element-by-element check to see if 
	 * the contract already exists
	 * @param newContract The new contract received from the POST Request
	 * @return True if the contract already exists
	 */
	private boolean duplicateContractFound(Contract newContract) {
		
		boolean policyOwnerExists = false;
		boolean insuredPersonExists = false;
		boolean productExists = false;
		
		// Check if the given policyOwner already exists in the customers table of the DB
		// If so, don't insert it as a new customer in the customers table
		Customer policyOwner = newContract.getPolicyOwner();
		Customer insuredPerson = newContract.getInsuredPerson();
		
		
		List<Customer> similarCustomers = customerRepository.findByFirstNameAndLastNameAndBirthdayAndAddress(
				policyOwner.getFirstName(), policyOwner.getLastName(), policyOwner.getBirthday(), 
				policyOwner.getAddress());
				
		long policyOwnerID = policyOwner.customerExistsInDB(similarCustomers);
		long insuredPersonID = 0L;
		
		
		if(policyOwnerID > 0) {
			
			policyOwnerExists = true;
			policyOwner = customerRepository.findByCustomerID(policyOwnerID);
			newContract.setPolicyOwner(policyOwner);
		}
		
		
		similarCustomers.clear();
		
		if(!newContract.getPolicyOwner().toStringWithoutID().equals(newContract.getInsuredPerson().toStringWithoutID())) {
			// Check if the given customer already exists in the customers table of the DB
			// If so, don't insert the customer again in the customers table
						
			similarCustomers = customerRepository.findByFirstNameAndLastNameAndBirthdayAndAddress(
					insuredPerson.getFirstName(),	insuredPerson.getLastName(), insuredPerson.getBirthday(), 
					insuredPerson.getAddress());
							
			insuredPersonID = insuredPerson.customerExistsInDB(similarCustomers);
							
			if(insuredPersonID > 0) {
				
				insuredPersonExists = true;
				insuredPerson = customerRepository.findByCustomerID(insuredPersonID);				
				newContract.setInsuredPerson(insuredPerson);;
			}
			
		} else {
			insuredPersonID = policyOwnerID;
			insuredPersonExists = true;
			newContract.setInsuredPerson(newContract.getPolicyOwner());
		}
		
		Product productNC = newContract.getProduct();
		
		List<Product> similarProducts = productRepository.findByNameAndProductBeginAndProductEndAndProductType(
				productNC.getName(), productNC.getProductBegin(), productNC.getProductEnd(), productNC.getProductType());
		
		long existingProductID = productNC.productExistsInDB(similarProducts);
		
		if(existingProductID > 0) {
			
			productExists = true;
			
			productNC = productRepository.findByProductID(existingProductID);
					
			newContract.setProduct(productNC);
		}
				
		// Save the Contract only if it is a new unique contract
		if( (policyOwnerExists && insuredPersonExists && productExists) == true) {
			
			List<Contract> similarContracts = contractRepository.findByPolicyOwnerAndInsuredPersonAndProduct(
					newContract.getPolicyOwner(), newContract.getInsuredPerson(), newContract.getProduct());
			
			if(newContract.contractExistsInDB(similarContracts) > 0) {
				return true;
			}
			
		}
		
		policyOwner.addToOwnedContracts(newContract);
		newContract.setPolicyOwner(policyOwner);
		
		
		setContractToSave(newContract);
		
		return false;
	}


	public Contract getContractToSave() {
		return contractToSave;
	}


	public void setContractToSave(Contract contractToSave) {
		this.contractToSave = contractToSave;
	}
}


