package com.gdis.importer.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.importer.model.JSONWrapper;
import com.gdis.importer.util.PreCondition;
import com.gdis.importer.util.DBClient;

@RestController
@RequestMapping("/importer/i/testCase")
public class ImportTestCaseRequestController {
	
	private JSONWrapper testCaseToImport;

	private List<ObjectNode> chunksToImport;

	private String storyTypeImport;
	
	@RequestMapping(method = RequestMethod.POST, consumes = 
	{ MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleImportRequest(@RequestBody JSONWrapper jsonWrapper) {	

		//Precondition.require(!json.equals("{}"), "JSON length == 0!");
		PreCondition.notNull(jsonWrapper, "JSON is empty!");
		
		
		setTestCaseToImport(jsonWrapper);
		
		// Return HTTP 400 if the JSON is missing the Story Type or The test data
		if(missingStory(testCaseToImport) == true) {
			testCaseToImport = null;
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			setStoryTypeImport(testCaseToImport.getStoryType());
		}
		
		if(missingTestData(testCaseToImport) == true) {
			testCaseToImport = null;
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		checkTestName(testCaseToImport);
		
		chunksToImport = new ArrayList<ObjectNode>();
		chunkJSON(testCaseToImport);
		
		DBClient dbClient = new DBClient();
		
		dbClient.importChunksInDB(chunksToImport, getStoryTypeImport());
		
		
		return new ResponseEntity<>(HttpStatus.OK);
		//return new ResponseEntity<JSONWrapper>(HttpStatus.OK);
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
	
	
	private boolean missingStory(JSONWrapper jsonWrapper) {
		
		// No Story Type. Can't import
		if( (jsonWrapper.getStoryType() == null) || (jsonWrapper.getStoryType().trim().length() == 0)) {
			return true;
		}
		return false;
	}
	
	private boolean missingTestData(JSONWrapper jsonWrapper) {
		
		// No Test Data. Nothing to export
		 if(jsonWrapper.getTestData() == null) {
			return true;
		} else if(jsonWrapper.getTestData().isEmpty()) {
			return true;
		}	
		
		
		return false;
	}
	
	 private void checkTestName(JSONWrapper jsonWrapper) {
		
		// If there was no name specified for the exported test, just
		// generate one
		if(jsonWrapper.getTestName() == null) {
			jsonWrapper.setTestName(new String());
		}
				
				
		boolean testNameExportEmpty = (jsonWrapper.getTestName().trim().length() == 0) 
			|| (jsonWrapper.getTestName().isEmpty());
				
		if(testNameExportEmpty == true)  {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyy-HH:mm:ss");
			String date = dtf.format(LocalDateTime.now());
					
			String generatedTestName = jsonWrapper.getStoryType().toString()
					+ "-TEST-" + date;
			jsonWrapper.setTestName(generatedTestName);
		}
	}
	
	

	
	/**
	 * Chunk the received JSON into individual JSONs, that will be sent to the 
	 * Global Variable for all the chunks
	 * JSON Format: 
	 * "storyType": "new contract",
	 * "testName": "name",
	 * "exampleCustomerID": "1",
	 * "customerData": {"name": "jack", "age": "20"}
	 */
	 private void chunkJSON(JSONWrapper jsonWrapper) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode chunk;
		
		for(ObjectNode j : jsonWrapper.getTestData()) {
			
			chunk = objectMapper.createObjectNode();
			
			//chunk.put("storyType", jsonWrapper.getStoryType());
			chunk.put("testName", jsonWrapper.getTestName());
			chunk.setAll(j);
			//chunk.put("exampleCustomerID", j.getTestCaseCustomerID());
			
			/**
			 *  Get Every Key Value Pair so at the end the JSON looks like this: 
			 *  "testName": "name",
			 *  "name": "jack",
			 *  "age": "20"
			 */
			chunksToImport.add(chunk);
		}	
	}	
	
	
	
	public JSONWrapper getTestCaseToImport() {
		return testCaseToImport;
	}

	public void setTestCaseToImport(JSONWrapper objectToExport) {
		this.testCaseToImport = objectToExport;
	}


	public String getStoryTypeImport() {
		return storyTypeImport;
	}


	public void setStoryTypeImport(String storyTypeImport) {
		this.storyTypeImport = storyTypeImport;
	}
	
}
