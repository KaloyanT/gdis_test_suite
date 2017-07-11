package com.gdis.exporter.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.exporter.model.EntityExportModel;
import com.gdis.exporter.util.DBClient;
import com.gdis.exporter.util.CustomErrorType;

@RestController
@RequestMapping("/exporter/e")
public class ExportEntityRequestController {

	@Autowired
	private DBClient dbClient;
	
	@RequestMapping(value = "/entities", method = RequestMethod.GET)
	public ResponseEntity<?> getEntities() {
		
		List<EntityExportModel> entitiesList = dbClient.exportAllTestEntitiesFromDB();
		
		if(entitiesList == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(entitiesList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/entities/by-name/{entityName}", method = RequestMethod.GET)
	public ResponseEntity<?> getEntityByName(@PathVariable("entityName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid entityName to get"), HttpStatus.BAD_REQUEST);
		}
		
		List<EntityExportModel> entitiesList = dbClient.exportTestEntityByNameFromDB(entityName);
		
		if(entitiesList == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(entitiesList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/entities/as-mappings", method = RequestMethod.GET)
	public ResponseEntity<?> getEntitiesAsMapping() {
		
		List<EntityExportModel> entitiesList = dbClient.exportAllTestEntitiesFromDB();
		
		if(entitiesList == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// After we have the entities like they are saved in the database, i.e. 
		// with entityName and a List of attributes, turn them into 
		// entityName.attribute form
		List<String> exportList = new ArrayList<String>();
		
		for(EntityExportModel eem : entitiesList) {
			
			for(String s : eem.getTestEntityAttributes()) {
				exportList.add(eem.getEntityName() + "." + s);
			}
		}
		
		return new ResponseEntity<>(exportList, HttpStatus.OK);
	}
}
