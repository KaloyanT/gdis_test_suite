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
import com.gdis.database.service.CustomerRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/customers")
public class CustomerController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllCustomers() {
		
		Iterable<Customer> customerIterable = customerRepository.findAll();
		
		if(customerIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<Customer> customerList = new ArrayList<Customer>();
	
		// Java 8 Method Reference is used here
		customerIterable.forEach(customerList::add);
	
		return new ResponseEntity<>(customerList, HttpStatus.OK);
	}
	
	@RequestMapping(value= "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomer(@PathVariable("id") final long id) {

		PreCondition.require(id >= 0, "Customer ID can't be negative!");
		
		Customer response = customerRepository.findByCustomerID(id);
		
		if(response == null) {
			return new ResponseEntity<>(new CustomErrorType("Customer with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value= "/byLastName/{lastName}", method = RequestMethod.GET)
	public ResponseEntity<?> findCustomerByLastName(@PathVariable("lastName") String lastName) {
		
		List<Customer> response = customerRepository.findByLastName(lastName);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{id}/ownedContracts", method = RequestMethod.GET)
	public ResponseEntity<?> getOwnedContractsOfCustomer(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Customer ID can't be negative!");

		final Customer customer = customerRepository.findByCustomerID(id);
		
		if(customer == null) {
			return new ResponseEntity<>(new CustomErrorType("Customer with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		final List<Contract> contracts = customer.getOwnedContracts();
		
		return new ResponseEntity<>(contracts, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST, 
			consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<?> createCustomer(@RequestBody Customer newCustomer) {
				
		if(newCustomer == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		// Check if the given customer already exists in the customers table of the DB
		// If so, don't insert the customer again in the customers table
		List<Customer> similarCustomers = 
			customerRepository.findByFirstNameAndLastNameAndBirthdayAndAddress(newCustomer.getFirstName(), 
			newCustomer.getLastName(), newCustomer.getBirthday(), newCustomer.getAddress());
		
		if(newCustomer.customerExistsInDB(similarCustomers) > 0) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		customerRepository.save(newCustomer);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, 
			consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}) 
	public ResponseEntity<?> updateCustomer(@PathVariable("id") final long id, @RequestBody Customer updatedCustomer) {
		
		PreCondition.require(id >= 0, "Customer ID can't be negative!");
		
		if(updatedCustomer == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Customer currentCustomer = customerRepository.findByCustomerID(id);
		
		if(currentCustomer == null) {
			return new ResponseEntity<>(new CustomErrorType("Customer with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
	
		updatedCustomer.setCustomerID(id);
		
		customerRepository.save(updatedCustomer);
		
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomer(@PathVariable("id") final long id) {
		
		PreCondition.require(id >= 0, "Customer ID can't be negative!");
		
		Customer toBeDeleted = customerRepository.findByCustomerID(id);
		
		if(toBeDeleted == null) {
			return new ResponseEntity<>(new CustomErrorType("Customer with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
				
		customerRepository.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	

	
	
}
