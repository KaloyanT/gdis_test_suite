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
import com.gdis.database.model.StoryTest;
import com.gdis.database.model.StoryTestElement;
import com.gdis.database.model.TestEntity;
import com.gdis.database.model.TestObject;
import com.gdis.database.service.StoryTestElementRepository;
import com.gdis.database.service.StoryTestRepository;
import com.gdis.database.service.TestEntityRepository;
import com.gdis.database.service.TestObjectRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/testEntity")
public class TestEntityController {
	
	@Autowired
	private TestEntityRepository testEntityRepository;
	
	@Autowired
	private StoryTestElementRepository storyTestElementRepository;
	
	@Autowired
	private TestObjectRepository testObjectRepository;
	
	@Autowired
	private StoryTestRepository storyTestRepository;
	
	private TestEntity entityToSave;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllTestEntities() {
		
		Iterable<TestEntity> testEntityIterable = testEntityRepository.findAll();
		
		if(testEntityIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<TestEntity> testEntitiesList = new ArrayList<TestEntity>();
		
		// Java 8 Method Reference is used here
		testEntityIterable.forEach(testEntitiesList::add);
		
		return new ResponseEntity<>(testEntitiesList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestEntityByID(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "TestEntity ID can't be negative!");
		
		TestEntity testEntity = testEntityRepository.findByTestEntityID(id);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with ID " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<TestEntity> testEntityAsList = new ArrayList<TestEntity>();
		testEntityAsList.add(testEntity);
		
		return new ResponseEntity<>(testEntityAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-entity-name/{entityName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestEntityByEntityName(@PathVariable("entityName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<TestEntity> testEntityAsList = new ArrayList<TestEntity>();
		testEntityAsList.add(testEntity);
		
		return new ResponseEntity<>(testEntityAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/attributes-for-entity/{entityName}", method = RequestMethod.GET)
	public ResponseEntity<?> getAttributesForTestEntity(@PathVariable("entityName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(testEntity.getTestEntityAttributes(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/story-tests-containing-entity/{entityName}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryTestsContainingEntity(@PathVariable("entityName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		Map<String, List<StoryTestElement>> groupedColumns = new HashMap<String, List<StoryTestElement>>();
		
		for(StoryTestElement ste : testEntity.getColumnsContainingEntity()) {
			
			if(groupedColumns.containsKey(ste.getStoryTest().getTestName())) {
				List<StoryTestElement> currentList = groupedColumns.get(ste.getStoryTest().getTestName());
				currentList.add(ste);
				groupedColumns.put(ste.getStoryTest().getTestName(), currentList);
			} else {
				List<StoryTestElement> newList = new ArrayList<StoryTestElement>();
				newList.add(ste);
				groupedColumns.put(ste.getStoryTest().getTestName(), newList);
			}
		}
		
		List<StoryTest> storyTestList = new ArrayList<StoryTest>();
		
		for(Map.Entry<String, List<StoryTestElement>> entry : groupedColumns.entrySet()) {
			storyTestList.add(storyTestRepository.findByTestName(entry.getKey()));
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
		
		
		return new ResponseEntity<>(storyTestList, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createTestEntity(@RequestBody TestEntity newTestEntity) {
	    
		
		if(newTestEntity == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setEntityToSave(newTestEntity);
		
		long entityExists = entityExists(getEntityToSave());
	
		if(entityExists == -1) {
			
			//newBasicStoryTest.setBasicStoryTestID(testExists);
			
			return new ResponseEntity<>(new CustomErrorType("An Entity with the same name already exists"),  
					HttpStatus.CONFLICT);
		
		}
		
		testEntityRepository.save(newTestEntity);
			
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}
	
	
	@RequestMapping(value = "/insert/attributes/{entityName}", method = RequestMethod.PUT)
	public ResponseEntity<?> addAttributeForEntity(@PathVariable("entityName") String entityName, 
			@RequestBody TestEntity updatedTestEntity) {
	    
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		if(updatedTestEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity"), HttpStatus.BAD_REQUEST);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		if(!updatedTestEntity.getEntityName().equals(entityName)) {
			return new ResponseEntity<>(new CustomErrorType("Can't insert attributes for different Entity"), HttpStatus.BAD_REQUEST);
		}
		
		for(String s : updatedTestEntity.getTestEntityAttributes()) {
			
			if(!testEntity.getTestEntityAttributes().contains(s)) {
				testEntity.addTestEntityAttribute(s);
			}
		}
		
		testEntityRepository.save(testEntity);
			
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	/*
	 * When a TestEnity is updated, for example from 
	 * "entityName": "Person",
	 * "testEntityAttributes": ["Name", "Age"]
	 * 
	 * to 
	 * "entityName": "Person2",
	 * "testEntityAttributes": ["FirstName", "LastName", "Birthday"]
	 * 
	 * one has couple of options. 
	 * First: Delete the objects for this TestEntity and also Delete the StoryTests and
	 * StoryTestElements which contain this entity since the mapping will be corrupted
	 * 
	 * Second: Delete The objects for this TestEntity, but don't Delete the StoryTests and
	 * StoryTestElements which contain this entity. Instead only change the entityName 
	 * attribute for each StoryTestElement, but keep the columnName the same. Here there is
	 * no point of building new objects, since they will have the same attributes as before, because
	 * the column names weren't changed
	 * 
	 * Change only the name of the TestEntity for now
	 */
	@RequestMapping(value = "/update/entity-name/{entityName}/{newEntityName}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTestEntityName(@PathVariable("entityName") String entityName, 
			@PathVariable("newEntityName") String newEntityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		if( (newEntityName == null) || (newEntityName.isEmpty()) || (newEntityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity New Name"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTestElement> columnsContainingOldEntity = storyTestElementRepository.getByTestEntity(testEntity);
		
		for(StoryTestElement ste : columnsContainingOldEntity) {
			ste.setEntityName(newEntityName);
			storyTestElementRepository.save(ste);
		}
		
		testEntity.setEntityName(newEntityName);
		
		testEntityRepository.save(testEntity);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/update/attribute/{entityName}/{oldAttributeName}/{updatedAttributeName}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTestEntityAttribute(@PathVariable("entityName") String entityName, 
			@PathVariable("oldAttributeName") String oldAttributeName, @PathVariable("updatedAttributeName") String updatedAttributeName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		if( (oldAttributeName == null) || (oldAttributeName.isEmpty()) || (oldAttributeName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Attribute"), HttpStatus.NOT_FOUND);
		}
		
		if( (updatedAttributeName == null) || (updatedAttributeName.isEmpty()) || (updatedAttributeName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Attribute"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTestElement> columnsContainingOldEntity = storyTestElementRepository.getByTestEntityAndColumnName(testEntity, oldAttributeName);
			
		for(StoryTestElement ste : columnsContainingOldEntity) {
			ste.setColumnName(updatedAttributeName);
			storyTestElementRepository.save(ste);
		}
		
		// Update Objects
		List<TestObject> objectsForEntity = testObjectRepository.findByEntityType(testEntity);
		for(TestObject to : objectsForEntity) {
			
			if(to.getObjectAttributes().containsKey(oldAttributeName)) {
				String value = to.getObjectAttributes().get(oldAttributeName);
				to.getObjectAttributes().remove(oldAttributeName);
				to.getObjectAttributes().put(updatedAttributeName, value);
				testObjectRepository.save(to);
			}
		}
		
		
		testEntity.removeTestEntityAttribute(oldAttributeName);
		testEntity.addTestEntityAttribute(updatedAttributeName);
		
		testEntityRepository.save(testEntity);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/delete/by-id/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTestEntityByID(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "TestEntity ID can't be negative!");
		
		TestEntity testEntity = testEntityRepository.findByTestEntityID(id);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. TestEntity with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		testEntityRepository.delete(testEntity);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/delete/by-entity-name/{entityName}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTestEntityByEntityName(@PathVariable("entityName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid EntityName"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. TestEntity with entityName "
					+ entityName + " not found."), HttpStatus.NOT_FOUND);
		}
		
		testEntityRepository.delete(testEntity);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/delete/attribute/{entityName}/{attribute}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTestEntityByEntityName(@PathVariable("entityName") String entityName, 
			@PathVariable("attribute") String attribute) {
		
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid EntityName"), HttpStatus.NOT_FOUND);
		}
		
		if( (attribute == null) || (attribute.isEmpty()) || (attribute.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Attribute"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. TestEntity with entityName "
					+ entityName + " not found."), HttpStatus.NOT_FOUND);
		}
		
		if(!testEntity.getTestEntityAttributes().contains(attribute)) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. TestEntity entityName "
					+ entityName + " doesn't have attribute " + attribute + "."), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTestElement> columnsContainingEntity = storyTestElementRepository.getByTestEntityAndColumnName(testEntity, attribute);
		
		for(StoryTestElement ste : columnsContainingEntity) {
			
			//if(ste.getColumnName().equals(attribute)) {
				testEntity.removeColumnsContainingEntity(ste);
				storyTestElementRepository.delete(ste);
			//}
		}
		
		List<TestObject> objectsContainingEntity = testObjectRepository.findByEntityType(testEntity);
		
		for(TestObject to : objectsContainingEntity) {
			
			if(to.getObjectAttributes().containsKey(attribute)) {
				to.getObjectAttributes().remove(attribute);
				testObjectRepository.save(to);
			}
		}
		
		testEntity.removeTestEntityAttribute(attribute);
		
		testEntityRepository.save(testEntity);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	public TestEntity getEntityToSave() {
		return entityToSave;
	}

	public void setEntityToSave(TestEntity entityToSave) {
		this.entityToSave = entityToSave;
	}
	
	private long entityExists(TestEntity newTestEntity) {
		
		
		TestEntity entityWithSameName = testEntityRepository.findByEntityName(newTestEntity.getEntityName());
		
		// Return 1 if there is a entity with the same entityName
		if(entityWithSameName != null) {
			return -1L;
		}
		

		// Return 0 if the Entity with this entityName doesn't already exist
		return 0;
	}
}
