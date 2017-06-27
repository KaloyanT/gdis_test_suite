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
import com.gdis.database.model.StoryTest;
import com.gdis.database.model.StoryTestElement;
import com.gdis.database.model.TestEntity;
import com.gdis.database.service.StoryTestRepository;
import com.gdis.database.service.TestEntityRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/basicStoryTest")
public class StoryTestController {

	@Autowired
	private StoryTestRepository storyTestRepository;
	
	@Autowired
	private TestEntityRepository testEntityRepository;
	
	private StoryTest storyToSave;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllStoryTests() {
		
		Iterable<StoryTest> storyIterable = storyTestRepository.findAll();
		
		if(storyIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<StoryTest> storyList = new ArrayList<StoryTest>();
		
		// Java 8 Method Reference is used here
		storyIterable.forEach(storyList::add);
		
		return new ResponseEntity<>(storyList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByID(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		StoryTest story = storyTestRepository.findByStoryTestID(id);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(story, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-story-name/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByStoryName(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTest> storyList = storyTestRepository.findByStoryName(storyName);
		
		if (storyList == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with name " + storyName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(storyList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-test-name/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByTestName(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		StoryTest story = storyTestRepository.findByTestName(testName);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Test with name " + testName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTest> storyList = new ArrayList<StoryTest>();
		storyList.add(story);
		
		return new ResponseEntity<>(storyList, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createStoryTest(@RequestBody StoryTest newStoryTest) {
	    
		
		if(newStoryTest == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setStoryToSave(newStoryTest);
		
		long testExists = testExists(getStoryToSave());
	
		if(testExists == -1) {
			
			//newBasicStoryTest.setBasicStoryTestID(testExists);
			
			return new ResponseEntity<>(new CustomErrorType("A Test with the same name already exists"),  
					HttpStatus.CONFLICT);
		
		} else if (testExists == -2) {
			
			return new ResponseEntity<>(new CustomErrorType("This test already exists"),  
					HttpStatus.CONFLICT);
		}
		
		/*
		System.out.println(newStoryTest.getTestEntities().toString());
		
		for(TestEntity te : newStoryTest.getTestEntities()) {
			
			TestEntity entity = testEntityRepository.findByEntityName(te.getEntityName());
			
			if(entity != null) {
				te = entity;
			} else {
				return new ResponseEntity<>(new CustomErrorType("Such TestEntity doesn't exist!"),  
						HttpStatus.NOT_FOUND);
			}
			
			entity.addTestContainingEntitiy(newStoryTest);
		}*/
		
		
		
		for(StoryTestElement ste : newStoryTest.getData()) {
			
			
			TestEntity entity = testEntityRepository.findByEntityName(ste.getTestEntity().getEntityName());
			
			if(entity != null) {
				ste.setTestEntity(entity);
			} else {
				return new ResponseEntity<>(new CustomErrorType("Such TestEntity doesn't exist!"),  
						HttpStatus.NOT_FOUND);
			}
			
			/*
			 *  Add the StoryTestElement to the TestEntities list
			 */
			entity.addColumnsContainingEntity(ste);
			
			
			ste.setStoryTest(newStoryTest);
		}
		
		storyTestRepository.save(newStoryTest);
			
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStoryTestByID(@PathVariable("id") long id, 
			@RequestBody StoryTest updatedStoryTest) {
		
		PreCondition.require(id >= 0, "BasicStoryTestID can't be negative!");
		
		if(updatedStoryTest == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		StoryTest currentTest = storyTestRepository.findByStoryTestID(id);

		if (currentTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to update. BasicStoryTest with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		StoryTest testWithSameName = storyTestRepository.findByTestName(updatedStoryTest.getTestName());
		
		if(testWithSameName != null) {
			
			if(currentTest.getStoryTestID() != testWithSameName.getStoryTestID()) {
				return new ResponseEntity<>(new CustomErrorType("Unable to update. There is another test"
						+ "with this testName."), HttpStatus.CONFLICT);
			}
		}
		
		currentTest.clearData();
	
		currentTest.setStoryName(updatedStoryTest.getStoryName());
		currentTest.setTestName(updatedStoryTest.getTestName());
		//currentTest.setData(updatedBasicStoryTest.getData());
		//currentTest.setAttributes(updatedBasicStoryTest.getAttributes());
		
		for(StoryTestElement bste : updatedStoryTest.getData()) {
			currentTest.addData(bste);
		}
		
		storyTestRepository.save(currentTest);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStoryTest(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		StoryTest currentStory = storyTestRepository.findByStoryTestID(id);
		
		if (currentStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. StoryTest with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		storyTestRepository.delete(currentStory);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	public StoryTest getStoryToSave() {
		return storyToSave;
	}

	public void setStoryToSave(StoryTest storyToSave) {
		this.storyToSave = storyToSave;
	}
	
	/**
	 * 
	 * @param newBasicStoryTest
	 * @return
	 */
	private long testExists(StoryTest newStoryTest) {
		
		
		StoryTest testWithSameName = storyTestRepository.findByTestName(newStoryTest.getTestName());
		
		// Return 1 if there is a test with the same testNamee
		if(testWithSameName != null) {
			return -1L;
			//return testWithSameName.getBasicStoryTestID();
		}
		
		
		List<StoryTest> similarTests = storyTestRepository.findByStoryNameAndTestName(
				newStoryTest.getStoryName(), newStoryTest.getTestName());
		
		long basicStoryTestID = newStoryTest.storyExistsInDB(similarTests);
		
		
		// Return 2 if there is a test with the same storyName, testName, and attributes
		if(basicStoryTestID > 0) {
			return -2L;
		}
		
		
		//setStoryToSave(newBasicStoryTest);

		// Return 0 if the Test for this Story doesn't already exist
		return 0;
	} 
	
	
}
