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
	
	
	@RequestMapping(value = "/get/byStoryName/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getBasicStoryTestByStoryName(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<BasicStoryTest> storyList = basicStoryRepository.findByStoryName(storyName);
		
		if (storyList == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with name " + storyName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(storyList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/byTestName/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getBasicStoryTestByTestName(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		BasicStoryTest story = basicStoryRepository.findByTestName(testName);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Test with name " + testName
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
		
		long testExists = testExists(getStoryToSave());
	
		if(testExists == -1) {
			
			//newBasicStoryTest.setBasicStoryTestID(testExists);
			
			return new ResponseEntity<>(new CustomErrorType("A Test with the same name already exists"),  
					HttpStatus.CONFLICT);
		
		} else if (testExists == -2) {
			
			return new ResponseEntity<>(new CustomErrorType("This test already exists"),  
					HttpStatus.CONFLICT);
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
		currentTest.setData(updatedBasicStoryTest.getData());
		//currentTest.setAttributes(updatedBasicStoryTest.getAttributes());
		
		basicStoryRepository.save(currentTest);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBasicStoryTest(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		BasicStoryTest currentStory = basicStoryRepository.findByBasicStoryTestID(id);
		
		if (currentStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. BasicStoryTest with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
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
	
	/**
	 * 
	 * @param newBasicStoryTest
	 * @return
	 */
	private long testExists(BasicStoryTest newBasicStoryTest) {
		
		
		BasicStoryTest testWithSameName = basicStoryRepository.findByTestName(newBasicStoryTest.getTestName());
		
		// Return 1 if there is a test with the same testNamee
		if(testWithSameName != null) {
			return -1L;
			//return testWithSameName.getBasicStoryTestID();
		}
		
		
		List<BasicStoryTest> similarTests = basicStoryRepository.findByStoryNameAndTestName(
				newBasicStoryTest.getStoryName(), newBasicStoryTest.getTestName());
		
		long basicStoryTestID = newBasicStoryTest.storyExistsInDB(similarTests);
		
		
		// Return 2 if there is a test with the same storyName, testName, and attributes
		if(basicStoryTestID > 0) {
			return -2L;
		}
		
		
		//setStoryToSave(newBasicStoryTest);

		// Return 0 if the Test for this Story doesn't already exist
		return 0;
	} 
	
	
}
