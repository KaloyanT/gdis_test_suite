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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.database.model.Contract;
import com.gdis.database.model.Customer;
import com.gdis.database.service.CustomerRepository;

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
	
	@RequestMapping(value= "/get", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomer(@RequestParam(value = "id") final long id) {
				
		Customer response = customerRepository.findById(id);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value= "/byLastName/{lastName}", method = RequestMethod.GET)
	public ResponseEntity<?> findCustomerByLastName(@PathVariable("lastName") String lastName) {
		
		List<Customer> response = customerRepository.findByLastName(lastName);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST, 
			consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<?> createCustomer(@RequestBody Customer newCustomer) {
				

		customerRepository.save(newCustomer);
		
		return new ResponseEntity<>(newCustomer, HttpStatus.ACCEPTED);
	}
		
	
	
	@RequestMapping(value = "/{id}/ownedContracts", method = RequestMethod.GET)
	public ResponseEntity<?> getOwnedContractsOfCustomer(@PathVariable("id") long id) {
		//Precondition.require(id > 0, "id must be greater than 0");

		final Customer customer = customerRepository.findById(id);
		final List<Contract> contracts = customer.getOwnedContracts();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_XML)
				.body(contracts);
	}
	
	
}
