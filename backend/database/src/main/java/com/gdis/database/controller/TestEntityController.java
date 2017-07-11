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
import com.gdis.database.model.TestEntity;
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
	
	
	@RequestMapping(value = "/update/{entityName}", method = RequestMethod.POST)
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
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
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
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. TestEntity with entityName "
					+ entityName + " not found."), HttpStatus.NOT_FOUND);
		}
		
		testEntityRepository.delete(testEntity);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateTestEntity(@PathVariable("id") long id, @RequestBody TestEntity newTestEntity) {
		
		PreCondition.require(id >= 0, "TestEntity ID can't be negative!");
		
		if(newTestEntity == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		TestEntity currentTestEntity = testEntityRepository.findByTestEntityID(id);
		
		if(currentTestEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to update. TestEntity with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		TestEntity entityWithTheSameName = testEntityRepository.findByEntityName(newTestEntity.getEntityName());
		
		if(entityWithTheSameName != null) {
			
			if(currentTestEntity.getTestEntityID() != entityWithTheSameName.getTestEntityID()) {
				return new ResponseEntity<>(new CustomErrorType("Unable to update. There is another TestEntity "
						+ "with this entityName."), HttpStatus.CONFLICT);
			}
		}
		
		// Begin the update process. This consist of clearing the attributes for the current entity
		// and inserting the ones of the new one. The Lists columnsContainingEntity and objectsForThis
		// Entity won't be touched. Further changes have to be made if a column (StoryTestElement)
		// already contains an entity with an attribute that has been deleted or changed 
		currentTestEntity.clearTestEntityAttribute();
		currentTestEntity.setEntityName(newTestEntity.getEntityName());
		newTestEntity.getTestEntityAttributes().forEach(currentTestEntity.getTestEntityAttributes()::add);
		
		testEntityRepository.save(currentTestEntity);
		
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
