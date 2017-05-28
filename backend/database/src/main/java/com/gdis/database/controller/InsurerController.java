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
import com.gdis.database.model.Insurer;
import com.gdis.database.service.InsurerRepository;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/insurers")
public class InsurerController {

	@Autowired
	private InsurerRepository insurerRepository;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllInsurers() {
		
		Iterable<Insurer> insurerIterable = insurerRepository.findAll();
		
		if(insurerIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<Insurer> insurerList = new ArrayList<Insurer>();
	
		// Java 8 Method Reference is used here
		insurerIterable.forEach(insurerList::add);
	
		return new ResponseEntity<>(insurerList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value= "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getInsurer(@PathVariable("id") final long id) {

		PreCondition.require(id >= 0, "Insurer ID can't be negative!");
		
		Insurer response = insurerRepository.findByInsurerID(id);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value= "/byName/{name}", method = RequestMethod.GET)
	public ResponseEntity<?> findInsurerByName(@PathVariable("name") String name) {
		
		Insurer response = insurerRepository.findByName(name);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST, 
			consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<?> createInsurer(@RequestBody Insurer newInsurer) {
				
		if(newInsurer == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		insurerRepository.save(newInsurer);
		
		return new ResponseEntity<>(newInsurer, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT, 
			consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE}) 
	public ResponseEntity<?> updateInsurer(@PathVariable("id") final long id, @RequestBody Insurer updatedInsurer) {
		
		PreCondition.require(id >= 0, "Insurer ID can't be negative!");
		
		if(updatedInsurer == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Insurer currentInsurer = insurerRepository.findByInsurerID(id);
		
		if(currentInsurer == null) {
			return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
		}
	
		updatedInsurer.setInsurerID(id);
		
		insurerRepository.save(updatedInsurer);
		
		return new ResponseEntity<>(currentInsurer, HttpStatus.ACCEPTED);
	}
	

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteInsurer(@PathVariable("id") final long id) {
		
		PreCondition.require(id >= 0, "Insurer ID can't be negative!");
		
		Insurer toBeDeleted = insurerRepository.findByInsurerID(id);
		
		if(toBeDeleted == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		insurerRepository.deleteById(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
}
