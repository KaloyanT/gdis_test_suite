package com.gdis.importer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.importer.model.EntityImportModel;
import com.gdis.importer.util.CustomErrorType;
import com.gdis.importer.util.DBClient;

@RestController
@RequestMapping("/importer")
public class EntityRequestController {


	@Autowired
	private DBClient dbClient;
	
	@RequestMapping(value = "/i/entity", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleEntityImportRequest(@RequestBody EntityImportModel newEntity) {	
		
		if(newEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity JSON"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newEntity.getEntityName() == null) || (newEntity.getEntityName().isEmpty()) || (newEntity.getEntityName().trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryName"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newEntity.getTestEntityAttributes() == null) || (newEntity.getTestEntityAttributes().isEmpty()) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Scenarios for Story"), HttpStatus.BAD_REQUEST);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode json = objectMapper.createObjectNode();
		ArrayNode testEntityAttributes = objectMapper.createArrayNode();
		
		newEntity.getTestEntityAttributes().forEach(testEntityAttributes::add);
		
		json.put("entityName", newEntity.getEntityName());
		json.putArray("testEntityAttributes").addAll(testEntityAttributes);
		
		ResponseEntity<String> dbClientResponse = dbClient.importEntityInDB(json);
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
	
	@RequestMapping(value = "/i/entity/attribute", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleEntityAttributeImportRequest(@RequestBody EntityImportModel newEntity) {	
		
		if(newEntity == null) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity JSON"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newEntity.getEntityName() == null) || (newEntity.getEntityName().isEmpty()) || (newEntity.getEntityName().trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryName"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newEntity.getTestEntityAttributes() == null) || (newEntity.getTestEntityAttributes().isEmpty()) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Scenarios for Story"), HttpStatus.BAD_REQUEST);
		}
		
		// Take only the first element from the list and insert it as a new attribute for the entity. 
		// Other models or use of ObjectNode is not needed here. The attribute insertion will be 
		// the same as entity insertion with the difference that the list will contain only 1
		// element when inserting an entity attribute
		ResponseEntity<String> dbClientResponse = 
				dbClient.importEntityAttributeInDB(newEntity.getEntityName(), newEntity.getTestEntityAttributes().get(0));
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
}
