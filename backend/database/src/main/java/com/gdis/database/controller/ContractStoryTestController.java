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
import com.gdis.database.model.ContractStoryTest;
import com.gdis.database.service.ContractStoryTestRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/contractStories")
public class ContractStoryTestController {
	
	@Autowired
	private ContractStoryTestRepository contractStoryRepository;
	
	private ContractStoryTest storyToSave;

	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllContractStories() {
		
		Iterable<ContractStoryTest> storyIterable = contractStoryRepository.findAll();
		
		if(storyIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<ContractStoryTest> storyList = new ArrayList<ContractStoryTest>();
		
		// Java 8 Method Reference is used here
		storyIterable.forEach(storyList::add);
		
		return new ResponseEntity<>(storyList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getContractStory(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		ContractStoryTest story = contractStoryRepository.findByBasicStoryTestID(id);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(story, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createContractStory(@RequestBody ContractStoryTest newStory) {
	    
		
		if(newStory == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setStoryToSave(newStory);
		
		/*
		boolean duplicateExists = duplicateModifiedContractFound(getModifiedContractToSave());
	
		if(duplicateExists == true) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}*/
		
		contractStoryRepository.save(newStory);
			
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteContractStory(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		ContractStoryTest currentStory = contractStoryRepository.findByBasicStoryTestID(id);
		
		if (currentStory == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		contractStoryRepository.delete(currentStory);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	public ContractStoryTest getStoryToSave() {
		return storyToSave;
	}

	public void setStoryToSave(ContractStoryTest storyToSave) {
		this.storyToSave = storyToSave;
	}

}
