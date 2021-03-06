package com.gdis.importer.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.importer.model.StoryTestImportModel;
import com.gdis.importer.util.CustomErrorType;
import com.gdis.importer.util.DBClient;

@RestController
@RequestMapping("/importer")
public class StoryTestRequestController {
	
	private StoryTestImportModel storyTestToImport;
	
	private ObjectNode jsonToImport;
	
	@Autowired
	private DBClient dbClient;
	
	@RequestMapping(value = "/i/test-case", method = RequestMethod.POST, consumes = 
	{ MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleStoryTestImportRequest(@RequestBody StoryTestImportModel newStoryTest) {	

		if(newStoryTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryTest JSON"), HttpStatus.NOT_FOUND);
		}
		
		setStoryTestToImport(newStoryTest);
		
		if(missingStoryName(getStoryTestToImport()) == true) {
			setStoryTestToImport(null);
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryName"), HttpStatus.BAD_REQUEST);
		}
		
		if(missingTestData(getStoryTestToImport()) == true) {
			setStoryTestToImport(null);
			return new ResponseEntity<>(new CustomErrorType("Invalid TestData"), HttpStatus.BAD_REQUEST);
		}
		
		if(missingMappings(getStoryTestToImport()) == true) {
			setStoryTestToImport(null);
			return new ResponseEntity<>(new CustomErrorType("Invalid Mappings"), HttpStatus.BAD_REQUEST);
		}
		
		checkTestName(getStoryTestToImport());
		
		addMappingsToColumns(getStoryTestToImport());
		
		ObjectMapper mapper = new ObjectMapper();
		jsonToImport = mapper.createObjectNode();
		
		buildJsonToImport(getJsonToImport(), getStoryTestToImport());
		
		ResponseEntity<String> dbClientResponse = dbClient.importStoryTestInDB(getJsonToImport());
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
	
	
	@RequestMapping(value = "/u/test-case/{testName}", method = RequestMethod.PUT, consumes = 
		{ MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<?> handleStoryTestUpdateRequest(@PathVariable("testName") String testName, 
			@RequestBody StoryTestImportModel updatedStoryTest) {
		
		if(updatedStoryTest == null) {
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryTest JSON"), HttpStatus.NOT_FOUND);
		}
	 
		setStoryTestToImport(updatedStoryTest);
		
		if(missingStoryName(getStoryTestToImport()) == true) {
			setStoryTestToImport(null);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(missingTestData(getStoryTestToImport()) == true) {
			setStoryTestToImport(null);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(missingMappings(getStoryTestToImport()) == true) {
			setStoryTestToImport(null);
			return new ResponseEntity<>(new CustomErrorType("Invalid Mappings"), HttpStatus.BAD_REQUEST);
		}
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid testName for StoryTest"), HttpStatus.BAD_REQUEST);
		}
		
		checkTestName(getStoryTestToImport());
				
		addMappingsToColumns(getStoryTestToImport());
		
		ObjectMapper mapper = new ObjectMapper();
		jsonToImport = mapper.createObjectNode();
		
		buildJsonToImport(getJsonToImport(), getStoryTestToImport());
		
				
		ResponseEntity<String> dbClientResponse = dbClient.updateStoryTestInDB(getJsonToImport(), testName);
				
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
	
	
	@RequestMapping(value = "/d/test-case/{testName}", method = RequestMethod.DELETE)
	public ResponseEntity<?> handleDeleteRequest(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid testName for StoryTest"), HttpStatus.BAD_REQUEST);
		}
		
		ResponseEntity<String> dbClientResponse = dbClient.deleteStoryTestFromDB(testName);
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
	
	
	@ExceptionHandler({HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<?> resolveException() {
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode response = mapper.createObjectNode();
		
		response.put("reason", "Empty Body");
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	

	private boolean missingStoryName(StoryTestImportModel jsonWrapper) {
		
		// No Story Name. Can't import
		if( (jsonWrapper.getStoryName() == null) || (jsonWrapper.getStoryName().trim().length() == 0) ) {
			return true;
		}
		return false;
	}
	
	private boolean missingTestData(StoryTestImportModel storyTest) {
		
		// No Test Data. Nothing to export
		 if(storyTest.getData() == null) {
			return true;
		} else if(storyTest.getData().isEmpty()) {
			return true;
		}	
		
		return false;
	}
	
	private boolean missingMappings(StoryTestImportModel storyTest) {
		
		if(storyTest.getMappings() == null) {
			return true;
		} else if (storyTest.getMappings().isEmpty()) {
			return true;
		} else if (storyTest.getData().size() > storyTest.getMappings().size()) {
			return true;
		}
		
		return false;
	}
	
	 private void checkTestName(StoryTestImportModel storyTest) {
		
		// If there was no name specified for the exported test, just
		// generate one
		if(storyTest.getTestName() == null) {
			storyTest.setTestName(new String());
		}
				
				
		boolean testNameExportEmpty = (storyTest.getTestName().trim().length() == 0) 
			|| (storyTest.getTestName().isEmpty());
				
		if(testNameExportEmpty == true)  {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyy-HH:mm:ss");
			String date = dtf.format(LocalDateTime.now());
					
			String generatedTestName = storyTest.getStoryName().toString()
					+ "-TEST-" + date;
			storyTest.setTestName(generatedTestName);
		}
	}
	
	 
	 private void addMappingsToColumns(StoryTestImportModel storyTest) {
		 
		 for(ObjectNode objN : storyTest.getData()) {
			 
			 if(objN.has("columnName") && objN.has("rows")) {
				 				 
				 String mappedColumn = storyTest.getMappings().get(objN.get("columnName").asText());
				 
				 //System.out.println(mappedColumn);
				 //System.out.println(objN.get("columnName"));
				 //System.out.println(storyTest.getMappings().get("Name"));
				 
				 String[] splittedMapping = mappedColumn.split("\\.");
				 
				 String entityName = splittedMapping[0];
				 String columnName = splittedMapping[1];			 
				 
				 objN.put("columnName", columnName);
				 objN.put("entityName", entityName);
			 }
		 }
		 
		 setStoryTestToImport(storyTest);
	 }
	 
	 
	private void buildJsonToImport(ObjectNode json, StoryTestImportModel storyTest) {
		
		json.put("testName", storyTest.getTestName());
		json.put("storyName", storyTest.getStoryName());
		
		ObjectMapper mapper = new ObjectMapper();
		
		//ArrayNode arrayNode = json.putArray("data");
		ArrayNode arrayNode = mapper.createArrayNode();
		
		for(ObjectNode j : storyTest.getData()) {
			arrayNode.add(j);
		}
		
		json.putArray("data").addAll(arrayNode);
		
		setJsonToImport(json);
	}
	
	
	public StoryTestImportModel getStoryTestToImport() {
		return storyTestToImport;
	}

	public void setStoryTestToImport(StoryTestImportModel storyTestToImport) {
		this.storyTestToImport = storyTestToImport;
	}


	public ObjectNode getJsonToImport() {
		return jsonToImport;
	}


	public void setJsonToImport(ObjectNode jsonToImport) {
		this.jsonToImport = jsonToImport;
	}
	
}
