package com.gdis.database.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.database.model.TestEntity;
import com.gdis.database.model.TestObject;
import com.gdis.database.service.TestEntityRepository;
import com.gdis.database.service.TestObjectRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/testObject")
public class TestObjectController {
	
	@Autowired
	private TestEntityRepository testEntityRepository;
	
	@Autowired
	private TestObjectRepository testObjectRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllTestObjects() {
		
		Iterable<TestObject> testObjectIterable = testObjectRepository.findAll();
		
		if(testObjectIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<TestObject> testObjectList = new ArrayList<TestObject>();
		
		// Java 8 Method Reference is used here
		testObjectIterable.forEach(testObjectList::add);
		
		return new ResponseEntity<>(testObjectList, HttpStatus.OK);
	}
	
	
	/*
	 *  /reduced REST APIs return only the most important features for an object
	 *  These are the ID, the EntityType and the attributes
	 */
	@RequestMapping(value = "/reduced", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTestObjectsReduced() {
	
		Iterable<TestObject> testObjectIterable = testObjectRepository.findAll();
		
		if(testObjectIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<TestObject> testObjectList = new ArrayList<TestObject>();
		
		// Java 8 Method Reference is used here
		testObjectIterable.forEach(testObjectList::add);
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<ObjectNode> objectsWithIDAndAttributes = new ArrayList<ObjectNode>();
		
		for(TestObject testObject : testObjectList) {
			
			ObjectNode currentObject = objectMapper.createObjectNode();
			currentObject.put("testObjectID", testObject.getTestObjectID());
			currentObject.put("entityName", testObject.getEntityType().getEntityName());
			
			for(Map.Entry<String, String> entry : testObject.getObjectAttributes().entrySet()) {
				currentObject.put(entry.getKey(), entry.getValue());
			}
				
			objectsWithIDAndAttributes.add(currentObject);
		}
		
		return new ResponseEntity<>(objectsWithIDAndAttributes, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestObjectByID(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "TestObject ID can't be negative!");
		
		TestObject testObject = testObjectRepository.findByTestObjectID(id);
		
		if (testObject == null) {
			return new ResponseEntity<>(new CustomErrorType("TestObject with ID " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<TestObject> testObjectAsList = new ArrayList<TestObject>();
		testObjectAsList.add(testObject);
		
		return new ResponseEntity<>(testObjectAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/{id}/reduced", method = RequestMethod.GET)
	public ResponseEntity<?> getTestObjectByIDReduced(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "TestObject ID can't be negative!");
		
		TestObject testObject = testObjectRepository.findByTestObjectID(id);
		
		if (testObject == null) {
			return new ResponseEntity<>(new CustomErrorType("TestObject with ID " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode currentObject = objectMapper.createObjectNode();
		currentObject.put("testObjectID", testObject.getTestObjectID());
		currentObject.put("entityName", testObject.getEntityType().getEntityName());
		
		for(Map.Entry<String, String> entry : testObject.getObjectAttributes().entrySet()) {
			currentObject.put(entry.getKey(), entry.getValue());
		}
		
		List<ObjectNode> currentObjectAsList = new ArrayList<ObjectNode>();
		currentObjectAsList.add(currentObject);
		
		return new ResponseEntity<>(currentObjectAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/objects-for-entity/{entityName}", method = RequestMethod.GET)
	public ResponseEntity<?> getObectsForTestEntity(@PathVariable("entityName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(testEntity.getObjectsForThisEntity(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/objects-for-entity/{entityName}/reduced", method = RequestMethod.GET)
	public ResponseEntity<?> getObectsForTestEntityReduced(@PathVariable("entityName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		TestEntity testEntity = testEntityRepository.findByEntityName(entityName);
		
		if (testEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("TestEntity with name " + entityName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<ObjectNode> objectsWithIDAndAttributes = new ArrayList<ObjectNode>();
		
		for(TestObject testObject : testEntity.getObjectsForThisEntity()) {
			
			ObjectNode currentObject = objectMapper.createObjectNode();
			currentObject.put("testObjectID", testObject.getTestObjectID());
			currentObject.put("entityName", testObject.getEntityType().getEntityName());
			
			for(Map.Entry<String, String> entry : testObject.getObjectAttributes().entrySet()) {
				currentObject.put(entry.getKey(), entry.getValue());
			}
				
			objectsWithIDAndAttributes.add(currentObject);
		}
		
		return new ResponseEntity<>(objectsWithIDAndAttributes, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteTestObject(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "TestObject ID can't be negative!");
		
		TestObject testObject = testObjectRepository.findByTestObjectID(id);
		
		if (testObject == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. TestObject with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		testObjectRepository.delete(testObject);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
