package com.gdis.exporter.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.exporter.util.CustomErrorType;
import com.gdis.exporter.util.DBClient;

@RestController
@RequestMapping("/exporter/e")
public class ExportObjectRequestController {
	
	@Autowired
	private DBClient dbClient;
	
	@RequestMapping(value = "/objects", method = RequestMethod.GET)
	public ResponseEntity<?> getObjects() {
		
		List<ObjectNode> objectsList = dbClient.exportAllObjectsFromDB();
		
		if(objectsList == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(objectsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/objects/by-entity-type/{entityType}", method = RequestMethod.GET)
	public ResponseEntity<?> getObjectsByEntityType(@PathVariable("entityType") String entityType) {
		
		if( (entityType == null) || (entityType.isEmpty()) || (entityType.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Type"), HttpStatus.NOT_FOUND);
		}
		
		List<ObjectNode> objectsList = dbClient.exportObjectsFromDBByEntityType(entityType);
		
		if(objectsList == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(objectsList, HttpStatus.OK);
	}
}
