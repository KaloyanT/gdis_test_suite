package com.gdis.database.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.database.model.Story;
import com.gdis.database.model.StoryTest;
import com.gdis.database.model.StoryTestElement;
import com.gdis.database.model.TestEntity;
import com.gdis.database.model.TestObject;
import com.gdis.database.service.StoryRepository;
import com.gdis.database.service.StoryTestRepository;
import com.gdis.database.service.TestEntityRepository;
import com.gdis.database.service.TestObjectRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/storyTest")
public class StoryTestController {

	@Autowired
	private StoryTestRepository storyTestRepository;
		
	@Autowired
	private TestEntityRepository testEntityRepository;
	
	@Autowired
	private TestObjectRepository testObjectRepository;
	
	@Autowired
	private StoryRepository storyRepository;
		
	private StoryTest storyTestToSave;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllStoryTests() {
		
		Iterable<StoryTest> storyTestIterable = storyTestRepository.findAll();
		
		if(storyTestIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<StoryTest> storyTestList = new ArrayList<StoryTest>();
		
		// Java 8 Method Reference is used here
		storyTestIterable.forEach(storyTestList::add);
		
		return new ResponseEntity<>(storyTestList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public ResponseEntity<?> getAllStoryTestsForExport() {
		
		Iterable<StoryTest> storyTestIterable = storyTestRepository.findAll();
		
		if(storyTestIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<StoryTest> storyTestList = new ArrayList<StoryTest>();
		
		// Java 8 Method Reference is used here
		storyTestIterable.forEach(storyTestList::add);
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode storyTestObjectNode = objectMapper.createObjectNode();
		ArrayNode storyTestDataArray = objectMapper.createArrayNode();
		List<ObjectNode> storyTestExportList = new ArrayList<ObjectNode>();
		
		for(StoryTest st : storyTestList) {
			
			storyTestObjectNode.put("testName", st.getTestName());
			
			for(StoryTestElement ste : st.getData()) {
				
				ObjectNode temp = objectMapper.createObjectNode();
				
				temp.put("columnName", ste.getColumnName());
				
				// Add the data to the ArrayNode and add the array node to the
				// temp ObjectNode
				ArrayNode storyTestElementRows = objectMapper.createArrayNode();	
				ste.getRows().forEach(storyTestElementRows::add);
				
				temp.putArray("rows").addAll(storyTestElementRows);
				
				// Now add this ObjectNode to the array with Columns
				storyTestDataArray.add(temp);	
			}
			
			// Finally, add the array with columns to the ObjectNode that has to be returned
			storyTestObjectNode.putArray("data").addAll(storyTestDataArray);
			
			storyTestExportList.add(storyTestObjectNode);
			
		}
		
		return new ResponseEntity<>(storyTestExportList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/testNames", method = RequestMethod.GET)
	public ResponseEntity<?> getTestNamesOfAllStoryTest() {
		
		Iterable<StoryTest> storyTestIterable = storyTestRepository.findAll();
		
		if(storyTestIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<String> storyTestTestNameList = new ArrayList<String>();
		
		for(StoryTest st : storyTestIterable) {
			storyTestTestNameList.add(st.getTestName());
		}
		
		return new ResponseEntity<>(storyTestTestNameList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByID(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "StoryTest ID can't be negative!");
		
		StoryTest storyTest = storyTestRepository.findByStoryTestID(id);
		
		if (storyTest == null) {
			return new ResponseEntity<>(new CustomErrorType("StoryTest with ID " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTest> storyTestAsList = new ArrayList<StoryTest>();
		storyTestAsList.add(storyTest);
		
		return new ResponseEntity<>(storyTestAsList, HttpStatus.OK);
	}
	
	
	/*
	 * The /get/.../export REST APIs are used only if the test data has to be exported
	 * to CSV File
	 */
	@RequestMapping(value = "/get/{id}/export", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByIDForExport(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "StoryTest ID can't be negative!");
		
		StoryTest storyTest = storyTestRepository.findByStoryTestID(id);
		
		if (storyTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with ID " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode storyTestObjectNode = objectMapper.createObjectNode();
		ArrayNode storyTestDataArray = objectMapper.createArrayNode();
				
		storyTestObjectNode.put("testName", storyTest.getTestName());
		
		for(StoryTestElement ste : storyTest.getData()) {
			
			ObjectNode temp = objectMapper.createObjectNode();
			
			temp.put("columnName", ste.getColumnName());
			
			// Add the data to the ArrayNode and add the array node to the
			// temp ObjectNode
			ArrayNode storyTestElementRows = objectMapper.createArrayNode();	
			ste.getRows().forEach(storyTestElementRows::add);
			
			temp.putArray("rows").addAll(storyTestElementRows);
			
			// Now add this ObjectNode to the array with Columns
			storyTestDataArray.add(temp);	
		}
		
		// Finally, add the array with columns to the ObjectNode that has to be returned
		storyTestObjectNode.putArray("data").addAll(storyTestDataArray);
		
		List<ObjectNode> storyTestAsList = new ArrayList<ObjectNode>();
		storyTestAsList.add(storyTestObjectNode);
		
		return new ResponseEntity<>(storyTestAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-story-name/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByStoryName(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTest> storyTestList = storyTestRepository.findByStoryName(storyName);
		
		if (storyTestList == null) {
			return new ResponseEntity<>(new CustomErrorType("StoryTest with name " + storyName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(storyTestList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-story-name/{storyName}/export", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByStoryNameForExport(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTest> storyTestList = storyTestRepository.findByStoryName(storyName);
		
		if (storyTestList == null) {
			return new ResponseEntity<>(new CustomErrorType("StoryTest with name " + storyName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode storyTestObjectNode = objectMapper.createObjectNode();
		ArrayNode storyTestDataArray = objectMapper.createArrayNode();
		List<ObjectNode> storyTestExportList = new ArrayList<ObjectNode>();
		
		for(StoryTest st : storyTestList) {
			
			storyTestObjectNode.put("testName", st.getTestName());
			
			for(StoryTestElement ste : st.getData()) {
				
				ObjectNode temp = objectMapper.createObjectNode();
				
				temp.put("columnName", ste.getColumnName());
				
				// Add the data to the ArrayNode and add the array node to the
				// temp ObjectNode
				ArrayNode storyTestElementRows = objectMapper.createArrayNode();	
				ste.getRows().forEach(storyTestElementRows::add);
				
				temp.putArray("rows").addAll(storyTestElementRows);
				
				// Now add this ObjectNode to the array with Columns
				storyTestDataArray.add(temp);	
			}
			
			// Finally, add the array with columns to the ObjectNode that has to be returned
			storyTestObjectNode.putArray("data").addAll(storyTestDataArray);
			
			storyTestExportList.add(storyTestObjectNode);
			
		}
		
		return new ResponseEntity<>(storyTestExportList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-test-name/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByTestName(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		StoryTest storyTest = storyTestRepository.findByTestName(testName);
		
		if (storyTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Test with name " + testName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTest> storyTestAsList = new ArrayList<StoryTest>();
		storyTestAsList.add(storyTest);
		
		return new ResponseEntity<>(storyTestAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-test-name/{testName}/export", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestByTestNameForExport(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		StoryTest storyTest = storyTestRepository.findByTestName(testName);
		
		if (storyTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Test with name " + testName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode storyTestObjectNode = objectMapper.createObjectNode();
		ArrayNode storyTestDataArray = objectMapper.createArrayNode();
				
		storyTestObjectNode.put("testName", storyTest.getTestName());
		
		for(StoryTestElement ste : storyTest.getData()) {
			
			ObjectNode temp = objectMapper.createObjectNode();
			
			temp.put("columnName", ste.getColumnName());
			
			// Add the data to the ArrayNode and add the array node to the
			// temp ObjectNode
			ArrayNode storyTestElementRows = objectMapper.createArrayNode();	
			ste.getRows().forEach(storyTestElementRows::add);
			
			temp.putArray("rows").addAll(storyTestElementRows);
			
			// Now add this ObjectNode to the array with Columns
			storyTestDataArray.add(temp);	
		}
		
		// Finally, add the array with columns to the ObjectNode that has to be returned
		storyTestObjectNode.putArray("data").addAll(storyTestDataArray);
		
		List<ObjectNode> storyTestAsList = new ArrayList<ObjectNode>();
		storyTestAsList.add(storyTestObjectNode);
		
		return new ResponseEntity<>(storyTestAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createStoryTest(@RequestBody StoryTest newStoryTest) {
		
		if(newStoryTest == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setStoryTestToSave(newStoryTest);
		
		long storyTestExists = storyTestExist(getStoryTestToSave());
	
		if(storyTestExists == -1) {			
			return new ResponseEntity<>(new CustomErrorType("A StoryTest with the same name already exists"),  
					HttpStatus.CONFLICT);
		}
		
		/*
		 * Turn the storyName to a storyObject
		 * Add StoryTest to the testsForStory List in Story
		 */
		Story story = storyRepository.findByStoryName(newStoryTest.getStoryName());
		
		if (story == null) {
				return new ResponseEntity<>(new CustomErrorType("Story with name " + newStoryTest.getStoryName()
						+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		newStoryTest.setStory(story);
		story.addTestForStory(newStoryTest);
		
		for(StoryTestElement ste : newStoryTest.getData()) {
			
			// Register TestEntity for StoryTestElement manually since Jackson can't map it, although the right annotations are used
			TestEntity entity = null;
			
			if(ste.getTestEntity() == null) {
				entity = testEntityRepository.findByEntityName(ste.getEntityName());
			} else {
				entity = testEntityRepository.findByEntityName(ste.getTestEntity().getEntityName());
			}
			
			
			if(entity != null) {
				ste.setTestEntity(entity);
			} else {
				return new ResponseEntity<>(new CustomErrorType("Such TestEntity doesn't exist!"),  
						HttpStatus.NOT_FOUND);
			}
			
			entity.addColumnsContainingEntity(ste);
			ste.setStoryTest(newStoryTest);
		}
		
		// First persist the current data and then build objects from it
		storyTestRepository.save(newStoryTest);
		
		
		// Get the already persisted test and create objects from the data
		StoryTest insertedStoryTest = storyTestRepository.findByTestName(newStoryTest.getTestName());
		
		// Build TestObjects
		buildObjectsForStoryTest(insertedStoryTest);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/update/by-id/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStoryTestByID(@PathVariable("id") long id, 
			@RequestBody StoryTest updatedStoryTest) {
		
		PreCondition.require(id >= 0, "StoryTestID can't be negative!");
		
		if(updatedStoryTest == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		StoryTest currentStoryTest = storyTestRepository.findByStoryTestID(id);

		if (currentStoryTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to update. StoryTest with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		
		return updateStoryTestByTestName(currentStoryTest.getTestName(), updatedStoryTest);
	}
		
	
	@RequestMapping(value = "/update/by-test-name/{testName}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStoryTestByTestName(@PathVariable("testName") String testName,
			@RequestBody StoryTest updatedStoryTest) {
		
		if(updatedStoryTest == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		StoryTest currentStoryTest = storyTestRepository.findByTestName(testName);

		if (currentStoryTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to update. StoryTest with testName "
					+ testName + " not found."), HttpStatus.NOT_FOUND);
		}
		
		StoryTest testWithSameName = storyTestRepository.findByTestName(updatedStoryTest.getTestName());
		
		if(testWithSameName != null) {
			
			if(currentStoryTest.getStoryTestID() != testWithSameName.getStoryTestID()) {
				return new ResponseEntity<>(new CustomErrorType("Unable to update. There is another StoryTest "
						+ "with this testName."), HttpStatus.CONFLICT);
			}
		}
		

		// Old Story shouldn't be null, but check anyways
		Story oldStory = storyRepository.findByStoryName(currentStoryTest.getStoryName());
		if (oldStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with name " + updatedStoryTest.getStoryName()
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		oldStory.removeTestForStory(currentStoryTest);
		
		currentStoryTest.setStoryName(updatedStoryTest.getStoryName());
		
		Story newStory = storyRepository.findByStoryName(updatedStoryTest.getStoryName());
				
		if (newStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with name " + updatedStoryTest.getStoryName()
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		currentStoryTest.setStory(newStory);
		newStory.addTestForStory(currentStoryTest);
		
		currentStoryTest.setTestName(updatedStoryTest.getTestName());
		
		for(StoryTestElement ste : currentStoryTest.getData()) {
			
			ste.getTestEntity().removeColumnsContainingEntity(ste);
		}
		
		/// remove the old columns and add the new ones
		currentStoryTest.clearData();
		
		for(StoryTestElement ste : updatedStoryTest.getData()) {
			
			// Register TestEntity for StoryTestElement manually since Jackson can't map it, although the right annotations are used
			TestEntity entity = null;
			
			if(ste.getTestEntity() == null) {
				entity = testEntityRepository.findByEntityName(ste.getEntityName());
			} 
			
			if(entity != null) {
				ste.setTestEntity(entity);
			} else {
				return new ResponseEntity<>(new CustomErrorType("Such TestEntity doesn't exist!"),  
						HttpStatus.NOT_FOUND);
			}
			
			currentStoryTest.addData(ste);
			// The StoryTestElements are inserted twice in the currentStoryTest.data List 
			// if the entity.addColumnsContainingEntity method is used, which shouldn't happen
			// The StoryTestElements are added automatically by Hibernate to the entity.columnsContainingEntity list
			//entity.addColumnsContainingEntity(ste);
		}
				
		storyTestRepository.save(currentStoryTest);
		
		// Get the already persisted test and create objects from the data
		StoryTest insertedStoryTest = storyTestRepository.findByTestName(currentStoryTest.getTestName());
		
		// Build TestObjects
		buildObjectsForStoryTest(insertedStoryTest);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@RequestMapping(value = "/delete/by-id/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStoryTestByID(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "StoryTest ID can't be negative!");
		
		StoryTest currentStory = storyTestRepository.findByStoryTestID(id);
		
		if (currentStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. StoryTest with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		storyTestRepository.delete(currentStory);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/delete/by-test-name/{testName}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStoryTestByTestName(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		StoryTest currentStory = storyTestRepository.findByTestName(testName);
		
		if (currentStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. StoryTest with testName "
					+ testName + " not found."), HttpStatus.NOT_FOUND);
		}
		
		storyTestRepository.delete(currentStory);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	public StoryTest getStoryTestToSave() {
		return storyTestToSave;
	}

	public void setStoryTestToSave(StoryTest storyToSave) {
		this.storyTestToSave = storyToSave;
	}
	
	/**
	 * 
	 * @param newBasicStoryTest
	 * @return
	 */
	private long storyTestExist(StoryTest newStoryTest) {
		
		StoryTest storyTestWithSameName = storyTestRepository.findByTestName(newStoryTest.getTestName());
		
		// Return 1 if there is a test with the same testNamee
		if(storyTestWithSameName != null) {
			return -1L;
			//return testWithSameName.getBasicStoryTestID();
		}
		
		// Return 0 if the Test for this Story doesn't already exist
		return 0;
	} 
	
	private void buildObjectsForStoryTest(StoryTest insertedStoryTest) {
				
		Map<String, List<StoryTestElement>> groupedColumns = new HashMap<String, List<StoryTestElement>>();
		
		for(StoryTestElement ste : insertedStoryTest.getData()) {
			
			// If the a column with this entityName is already in the Map, just add
			// the current column to the List with columns for this Entity
			if(groupedColumns.containsKey(ste.getEntityName())) {
				List<StoryTestElement> currentList = groupedColumns.get(ste.getEntityName());
				currentList.add(ste);
				groupedColumns.put(ste.getEntityName(), currentList);
			} else {
				List<StoryTestElement> newList = new ArrayList<StoryTestElement>();
				newList.add(ste);
				groupedColumns.put(ste.getEntityName(), newList);
				
			}
		}
		
		 /* Now, when the columns are grouped according to entityName, iterate through all the columns
		 * for a given Entity and create objects for it by getting the j-row of each column for every
		 * column that has been mapped to this TestEntity 
		 */
		
		for(Map.Entry<String, List<StoryTestElement>> entry : groupedColumns.entrySet()) {
			
			// Get the number of rows of a column. Checking just one column is enough
			// since all columns will have equal number of rows
			int numberOfRows = entry.getValue().get(0).getRows().size();
			
			for(int j = 0; j < numberOfRows; j++) {
				
				Map<String, String> attributesForCurrentObject = new HashMap<String, String>();
				TestObject currentObject = new TestObject();
								
				TestEntity entityOfObject = testEntityRepository.findByEntityName(entry.getValue().get(0).getTestEntity().getEntityName());
				currentObject.setEntityType(entityOfObject);
				
				for(StoryTestElement ste : entry.getValue()) {
					attributesForCurrentObject.put(ste.getColumnName(), ste.getRows().get(j));
				}
				
				currentObject.setObjectAttributes(attributesForCurrentObject);
				
				// After the attributes and the TestEntity are set, compute the hashCode
				currentObject.setHashCodeNoID(currentObject.hashCodeNoID());
				
				TestObject existingObject = testObjectRepository.findByHashCodeNoID(currentObject.getHashCodeNoID());
				
				// If the object doesn't exist, create it
				if(existingObject == null) {
					entityOfObject.addObjectContainingEntity(currentObject);
					testObjectRepository.save(currentObject);
				}
			}
		}
	}
	
}
