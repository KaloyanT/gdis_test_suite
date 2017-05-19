package com.gdis.database.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gdis.database.model.Contract;
import com.gdis.database.model.Customer;
import com.gdis.database.service.CustomerRepository;

import com.gdis.database.util.PreCondition;
@RestController
@RequestMapping("/customers")

public class CustomerController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}
	
	@RequestMapping(value= "/{id}", method = RequestMethod.GET)
	public Customer getCustomer(@PathVariable long id) {
		return customerRepository.findById(id);
	}
	
	@RequestMapping(value= "/by-last-name/{lastName}", method = RequestMethod.GET)
	public Iterable<Customer> findCustomerByLastName(String lastName) {
		return customerRepository.findByLastName(lastName);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Customer createCustomer(Customer customer) {
		PreCondition.notNull(customer, Customer.class.getSimpleName());

		customerRepository.save(customer);
		
		return customer;
	}
		
	@RequestMapping(value = "/{id}/owned-contracts", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<Iterable<Contract>> getOwnedContractsOfCustomer(@PathVariable long id) {
		//Precondition.require(id > 0, "id must be greater than 0");

		final Customer customer = customerRepository.findById(id);
		final List<Contract> contracts = customer.getOwnedContracts();
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_XML)
				.body(contracts);
	}
	
	
}
