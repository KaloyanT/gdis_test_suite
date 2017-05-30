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

import com.gdis.database.model.Contract;
import com.gdis.database.model.Customer;
import com.gdis.database.model.ModifyContract;
import com.gdis.database.model.NewContract;
import com.gdis.database.model.Product;
import com.gdis.database.service.ContractRepository;
import com.gdis.database.service.CustomerRepository;
import com.gdis.database.service.ModifyContractRepository;
import com.gdis.database.service.ProductRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/modifyContract")
public class ModifyContractController {
	
	@Autowired
	private ModifyContractRepository modifyContractRepository;
	
	@Autowired
	private ContractRepository contractRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
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
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateModifyContract(@PathVariable("id") long id, 
			@RequestBody ModifyContract modifiedContract) {
		
		PreCondition.require(id >= 0, "New Contract ID can't be negative!");
		
		ModifyContract currentContract = modifyContractRepository.findByModifiedContractID(id);

		if (currentContract == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to update. ModifyContract with id "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}

		currentContract.setContract(modifiedContract.getContract());
		currentContract.setTestName(modifiedContract.getTestName());
		currentContract.setNewEndDate(modifiedContract.getNewEndDate());
		currentContract.setChangedMonthlyPremium(modifiedContract.getChangedMonthlyPremium());
		
		modifyContractRepository.save(currentContract);
		
		return new ResponseEntity<>(currentContract, HttpStatus.OK);
	}

	
	// delete a new contract
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteModifyContract(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Modify Contract ID can't be negative!");
		
		ModifyContract currentContract = modifyContractRepository.findByModifiedContractID(id);
		
		if (currentContract == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		modifyContractRepository.delete(currentContract);
		
		return new ResponseEntity<NewContract>(HttpStatus.NO_CONTENT);
	}
	
	
	private boolean duplicateModifiedContractFound(ModifyContract newModifiedContract) {
		
		Contract contract = newModifiedContract.getContract();
		Product product = contract.getProduct();
		Customer policyOwner = contract.getPolicyOwner();
		Customer insuredPerson = contract.getInsuredPerson();
		
		boolean contractExists = false;
		boolean productExists = false;
		boolean policyOwnerExists = false;
		boolean insuredPersonExists = false;
		
		
		List<Product> similarProducts = productRepository.findByNameAndProductBeginAndProductEndAndProductType(
				product.getName(), product.getProductBegin(), product.getProductEnd(), product.getProductType());
		
		long productID = product.productExistsInDB(similarProducts);
		
		
		if(productID > 0) {
			productExists = true;
			product = productRepository.findByProductID(productID);
			contract.setProduct(product);
		}
		
		
		List<Customer> similarCustomers = customerRepository.findByFirstNameAndLastNameAndBirthdayAndAddress(
				policyOwner.getFirstName(), policyOwner.getLastName(), policyOwner.getBirthday(), policyOwner.getAddress());
			
		long existingCustomerID = policyOwner.customerExistsInDB(similarCustomers);
			
		if(existingCustomerID > 0) {	
			policyOwnerExists = true;
			policyOwner = customerRepository.findByCustomerID(existingCustomerID);
			contract.setPolicyOwner(policyOwner);;
		}
			
			
		similarCustomers.clear();
			
		if(!contract.getPolicyOwner().toStringWithoutID().equals(contract.getInsuredPerson().toStringWithoutID())) {
				// Check if the given customer already exists in the customers table of the DB
				// If so, don't insert the customer again in the customers table
			
								
			similarCustomers = customerRepository.findByFirstNameAndLastNameAndBirthdayAndAddress(
					insuredPerson.getFirstName(), insuredPerson.getLastName(), insuredPerson.getBirthday(), 
					insuredPerson.getAddress());
								
			existingCustomerID = insuredPerson.customerExistsInDB(similarCustomers);
								
			if(existingCustomerID > 0) {
					
				insuredPersonExists = true;	
				insuredPerson = customerRepository.findByCustomerID(existingCustomerID);				
				contract.setInsuredPerson(insuredPerson);;
			}
			
		} else {
			insuredPersonExists = true;
			contract.setInsuredPerson(contract.getPolicyOwner());
		}	
		
		
		if( (productExists && policyOwnerExists && insuredPersonExists) == true) {
			
			List<Contract> similarContracts = contractRepository.findByPolicyOwnerAndInsuredPersonAndProduct(
					contract.getPolicyOwner(), contract.getInsuredPerson(), contract.getProduct());
			
			long contractID = contract.contractExistsInDB(similarContracts);
			
			if(contractID > 0) {
				contractExists = true;
				contract = contractRepository.findByContractID(contractID);
				newModifiedContract.setContract(contract);
			}
		}
		
		if(contractExists == true ) {
			
			List<ModifyContract> similarModifiedContracts = 
				modifyContractRepository.findByContractAndTestNameAndNewEndDateAndChangedMonthlyPremium(
				newModifiedContract.getContract(), newModifiedContract.getTestName(), newModifiedContract.getNewEndDate(), 
				newModifiedContract.getChangedMonthlyPremium());
		
		
			if(newModifiedContract.modifiedContractExistsInDB(similarModifiedContracts) > 0) {
				return true;
			}
		}
		
		setModifiedContractToSave(newModifiedContract);
		
		return false;
	} 
		 

	public ModifyContract getModifiedContractToSave() {
		return modifiedContractToSave;
	}


	public void setModifiedContractToSave(ModifyContract modifiedContractToSave) {
		this.modifiedContractToSave = modifiedContractToSave;
	}

}
