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
import com.gdis.database.service.TestEntityRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/testEntity")
public class TestEntityController {
	
	@Autowired
	private TestEntityRepository testEntityRepository;
	
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
			return new ResponseEntity<>(new CustomErrorType("TestEntity with id " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(testEntity, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-entity-name/{entityName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestEntityByEntityName(@PathVariable("storyName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(testEntity, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createTestEntity(@RequestBody TestEntity newTestEntity) {
	    
		
		if(newTestEntity == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setEntityToSave(newTestEntity);
		
		long testExists = entityExists(getEntityToSave());
	
		if(testExists == -1) {
			
			//newBasicStoryTest.setBasicStoryTestID(testExists);
			
			return new ResponseEntity<>(new CustomErrorType("An Entity with the same name already exists"),  
					HttpStatus.CONFLICT);
		
		}
		
		
		testEntityRepository.save(newTestEntity);
			
		return new ResponseEntity<>(HttpStatus.CREATED);
		
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
