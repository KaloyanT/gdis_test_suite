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
import com.gdis.database.model.BasicStoryTest;
import com.gdis.database.service.BasicStoryTestRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/basicStoryTest")
public class BasicStoryTestController {

	@Autowired
	private BasicStoryTestRepository basicStoryRepository;
	
	private BasicStoryTest storyToSave;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllBasicStoryTests() {
		
		Iterable<BasicStoryTest> storyIterable = basicStoryRepository.findAll();
		
		if(storyIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<BasicStoryTest> storyList = new ArrayList<BasicStoryTest>();
		
		// Java 8 Method Reference is used here
		storyIterable.forEach(storyList::add);
		
		return new ResponseEntity<>(storyList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getBasicStoryTestByID(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		BasicStoryTest story = basicStoryRepository.findByBasicStoryTestID(id);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(story, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createBasicStoryTest(@RequestBody BasicStoryTest newBasicStoryTest) {
	    
		
		if(newBasicStoryTest == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setStoryToSave(newBasicStoryTest);
		
		
		boolean testExists = testExists(getStoryToSave());
	
		if(testExists == true) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		basicStoryRepository.save(newBasicStoryTest);
			
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBasicStoryTestByID(@PathVariable("id") long id, 
			@RequestBody BasicStoryTest updatedBasicStoryTest) {
		
		PreCondition.require(id >= 0, "BasicStoryTestID can't be negative!");
		
		if(updatedBasicStoryTest == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		BasicStoryTest currentTest = basicStoryRepository.findByBasicStoryTestID(id);

		if (currentTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to update. BasicStoryTest with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
	
		currentTest.setStoryName(updatedBasicStoryTest.getStoryName());
		currentTest.setTestName(updatedBasicStoryTest.getTestName());
		currentTest.setAttributes(updatedBasicStoryTest.getAttributes());
		
		basicStoryRepository.save(currentTest);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBasicStoryTest(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		BasicStoryTest currentStory = basicStoryRepository.findByBasicStoryTestID(id);
		
		if (currentStory == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		basicStoryRepository.delete(currentStory);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	public BasicStoryTest getStoryToSave() {
		return storyToSave;
	}

	public void setStoryToSave(BasicStoryTest storyToSave) {
		this.storyToSave = storyToSave;
	}
	
	
	private boolean testExists(BasicStoryTest newBasicStoryTest) {
		
		
		List<BasicStoryTest> similarTests = basicStoryRepository.findByStoryNameAndTestName(
				newBasicStoryTest.getStoryName(), newBasicStoryTest.getTestName());
		
		long basicStoryTestID = newBasicStoryTest.storyExistsInDB(similarTests);
		
		
		if(basicStoryTestID > 0) {
			return true;
		}
				
		
		//setStoryToSave(newBasicStoryTest);
		
		return false;
	} 
	
	
}
