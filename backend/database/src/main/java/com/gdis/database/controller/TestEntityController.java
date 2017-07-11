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
import com.gdis.database.model.StoryTestElement;
import com.gdis.database.model.TestEntity;
import com.gdis.database.model.TestObject;
import com.gdis.database.service.StoryTestElementRepository;
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
	
	
	@RequestMapping(value = "/insert/attributes/{entityName}", method = RequestMethod.POST)
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
	@RequestMapping(value = "/update/{entityName}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTestEntity(@PathVariable("entityName") String entityName, 
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
		
		/*
		List<StoryTestElement> columnsContainingOldEntity = storyTestElementRepository.getByTestEntity(testEntity);
		Map<String, List<StoryTestElement>> groupedColumns = new HashMap<String, List<StoryTestElement>>();
		
		for(StoryTestElement ste : columnsContainingOldEntity) {
			ste.setEntityName(updatedTestEntity.getEntityName());
			storyTestElementRepository.save(ste);
			
			// Update Objects by recreating them
			// Group StoryTestElements to StoryTests and build objects for these story tests
			// Objects have to be built only for the columns that contain this entity
			// The rest of the columns in a StoryTest don't need a change
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
		
		// Delete old TestObjects
		testEntity.clearObjectsContainingEntity();
		List<TestObject> oldObjectsForEntity = testObjectRepository.findByEntityType(testEntity);
		for(TestObject to : oldObjectsForEntity) {
			testObjectRepository.delete(to);
		}
				
		*/
		testEntity.setEntityName(updatedTestEntity.getEntityName());
		//testEntity.clearTestEntityAttributes();
		//updatedTestEntity.getTestEntityAttributes().forEach(testEntity.getTestEntityAttributes()::add);
		
		testEntityRepository.save(testEntity);
		
		// Create the new objects
		/*
		for(Map.Entry<String, List<StoryTestElement>> entry : groupedColumns.entrySet()) {
			
			StoryTest current = storyTestRepository.findByTestName(entry.getKey());
			buildObjectsForStoryTest(current);
		}
		*/
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	

	@RequestMapping(value = "update/by-id/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTestEntity(@PathVariable("id") long id, @RequestBody TestEntity updatedTestEntity) {
		
		PreCondition.require(id >= 0, "TestEntity ID can't be negative!");
			
		if(updatedTestEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity"), HttpStatus.BAD_REQUEST);
		}
		
		TestEntity testEntity = testEntityRepository.findByTestEntityID(id);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		/*
		List<StoryTestElement> columnsContainingOldEntity = storyTestElementRepository.getByTestEntity(testEntity);
		Map<String, List<StoryTestElement>> groupedColumns = new HashMap<String, List<StoryTestElement>>();
		
		for(StoryTestElement ste : columnsContainingOldEntity) {
			ste.setEntityName(updatedTestEntity.getEntityName());
			storyTestElementRepository.save(ste);
			
			// Update Objects by recreating them
			// Group StoryTestElements to StoryTests and build objects for these story tests
			// Objects have to be built only for the columns that contain this entity
			// The rest of the columns in a StoryTest don't need a change
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
		
		// Delete old TestObjects
		testEntity.clearObjectsContainingEntity();
		List<TestObject> oldObjectsForEntity = testObjectRepository.findByEntityType(testEntity);
		for(TestObject to : oldObjectsForEntity) {
			testObjectRepository.delete(to);
		}
				
		*/
		testEntity.setEntityName(updatedTestEntity.getEntityName());
		//testEntity.clearTestEntityAttributes();
		//updatedTestEntity.getTestEntityAttributes().forEach(testEntity.getTestEntityAttributes()::add);
		
		testEntityRepository.save(testEntity);
		
		// Create the new objects
		/*
		for(Map.Entry<String, List<StoryTestElement>> entry : groupedColumns.entrySet()) {
			
			StoryTest current = storyTestRepository.findByTestName(entry.getKey());
			buildObjectsForStoryTest(current);
		}
		*/
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
		
		List<StoryTestElement> columnsContainingEntity = storyTestElementRepository.getByTestEntity(testEntity);
		
		for(StoryTestElement ste : columnsContainingEntity) {
			
			if(ste.getColumnName().equals(attribute)) {
				testEntity.removeColumnsContainingEntity(ste);
				//storyTestElementRepository.delete(ste);
			}
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
