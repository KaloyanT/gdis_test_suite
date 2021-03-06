package com.gdis.importer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
			return new ResponseEntity<>(new CustomErrorType("Invalid EntityName"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newEntity.getTestEntityAttributes() == null) || (newEntity.getTestEntityAttributes().isEmpty()) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Attributes for Entity"), HttpStatus.BAD_REQUEST);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode json = objectMapper.createObjectNode();
		ArrayNode testEntityAttributes = objectMapper.createArrayNode();
		
		newEntity.getTestEntityAttributes().forEach(testEntityAttributes::add);
		
		json.put("entityName", newEntity.getEntityName());
		json.putArray("testEntityAttributes").addAll(testEntityAttributes);
		
		ResponseEntity<String> dbClientResponse = dbClient.importTestEntityInDB(json);
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
	
	
	@RequestMapping(value = "/u/entity/entit-name/{entityName}/{newEntityName}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleEntityNameUpdateRequest(@PathVariable("entityName") String entityName, @PathVariable("newEntityName") String newEntityName) {	
		
				
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid entityName to update"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newEntityName == null) || (newEntityName.isEmpty()) || (newEntityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid newEntityName"), HttpStatus.BAD_REQUEST);
		}
				
		ResponseEntity<String> dbClientResponse = dbClient.updateTestEntityNameInDB(entityName, newEntityName);
				
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		return dbClientResponse;
	}
	
	
	@RequestMapping(value = "/u/entity/attribute/{entityName}/{oldAttributeName}/{newAttributeName}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleEntityAttributeUpdateRequest(@PathVariable("entityName") String entityName, 
			@PathVariable("oldAttributeName") String oldAttributeName, @PathVariable("newAttributeName") String newAttributeName) {	
		
				
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid entityName to update"), HttpStatus.BAD_REQUEST);
		}
		
		if( (oldAttributeName == null) || (oldAttributeName.isEmpty()) || (oldAttributeName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid newEntityName"), HttpStatus.BAD_REQUEST);
		}
				
		ResponseEntity<String> dbClientResponse = dbClient.updateTestEntityAttributeInDB(entityName, oldAttributeName, newAttributeName);
				
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		return dbClientResponse;
	}
	
	
	@RequestMapping(value = "/d/entity/{entityName}", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleEntityDeleteRequest(@PathVariable("entityName") String entityName) {
			
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid entityName to Update"), HttpStatus.BAD_REQUEST);
		}
		
		ResponseEntity<String> dbClientResponse = dbClient.deleteTestEntityFromDB(entityName);
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
}
