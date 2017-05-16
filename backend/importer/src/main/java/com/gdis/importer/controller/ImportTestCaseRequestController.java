package com.gdis.importer.controller;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.importer.model.JSONSchema;
import com.gdis.importer.model.JSONWrapper;
import com.gdis.importer.util.Precondition;

@RestController
@RequestMapping("/importer/import/testcase")
public class ImportTestCaseRequestController {
	
	private JSONWrapper testCaseToImport;
	
	private List<ObjectNode> chunksToImport;

	@RequestMapping(method = RequestMethod.POST, consumes = 
	{ MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleImportRequest(@RequestBody JSONWrapper jsonWrapper) {	
		//Precondition.require(!json.equals("{}"), "JSON length == 0!");
		Precondition.notNull(jsonWrapper, "JSON is empty!");
		
		setTestCaseToImport(jsonWrapper);
		
		// Return HTTP 400 if the JSON is missing the Story Type or The test data
		if(missingStory(testCaseToImport) == true) {
			testCaseToImport = null;
			return new ResponseEntity<JSONWrapper>(HttpStatus.NO_CONTENT);
		}
		
		if(missingTestData(testCaseToImport) == true) {
			testCaseToImport = null;
			return new ResponseEntity<JSONWrapper>(HttpStatus.NO_CONTENT);
		}
		
		checkTestNameImport(testCaseToImport);
		
		chunksToImport = new ArrayList<ObjectNode>();
		chunkJSON(testCaseToImport);
		
		//printChunks(chunksToExport);
		importChunksInDB(chunksToImport);
		
		return new ResponseEntity<JSONWrapper>(jsonWrapper, HttpStatus.OK);
		//return new ResponseEntity<JSONWrapper>(HttpStatus.OK);
	}
	
	private boolean missingStory(JSONWrapper jsonWrapper) {
		// No Story Type. Can't export
		if(jsonWrapper.getStoryType() == null) {
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
	
	private void checkTestNameImport(JSONWrapper jsonWrapper) {
		
		// If there was no name specified for the exported test, just
		// generate one
		if(jsonWrapper.getTestNameImport() == null) {
			jsonWrapper.setTestNameImport(new String());
		}
				
				
		boolean testNameExportEmpty = (jsonWrapper.getTestNameImport().trim().length() == 0) 
			|| (jsonWrapper.getTestNameImport().isEmpty());
				
		if(testNameExportEmpty == true)  {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyy-HH:mm:ss");
			String date = dtf.format(LocalDateTime.now());
					
			String generatedTestName = jsonWrapper.getStoryType().toString()
					+ "-TEST-" + date;
			jsonWrapper.setTestNameImport(generatedTestName);
		}
	}

	
	/**
	 * Chunk the received JSON into individual JSONs, that will be send to the 
	 * Global Variable for all the chunks
	 * JSON Format: 
	 * "storyType": "new contract",
	 * "testNameExport": "name",
	 * "exampleCustomerID": "1",
	 * "customerData": {"name": "jack", "age": "20"}
	 */
	private void chunkJSON(JSONWrapper jsonWrapper) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ObjectNode chunk;
		
		for(JSONSchema j : jsonWrapper.getTestData()) {
			
			chunk = objectMapper.createObjectNode();
			
			chunk.put("storyType", jsonWrapper.getStoryType());
			chunk.put("testNameImport", jsonWrapper.getTestNameImport());
			chunk.put("exampleCustomerID", j.getExampleCustomerID());
			
			/**
			 *  Get Every Key Value Pair so at the end the JSON looks like this: 
			 *  "storyType": "new contract",
			 *  "testNameExport": "name",
			 *  "exampleCustomerID": "1",
			 *  "name": "jack",
			 *  "age": "20"
			 */
			for(Map.Entry<String, String> entry : j.getCustomerData().entrySet()) {
				
				String key = entry.getKey();
				String value = entry.getValue();
				chunk.put(key, value);		
			}
			chunksToImport.add(chunk);
		}	
	}	
	
	private void importChunksInDB(List<ObjectNode> chunks) {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		
		// Debug DB POST Request Simulator
		final String url = "http://localhost:8083/importer/database/" + getTestCaseToImport().getStoryType();
		
		for(ObjectNode i : chunks) {
			
			HttpEntity<String> entity = new HttpEntity<String>(i.toString(), headers);
			//ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			System.out.println(response.getBody());
		}
	}
	
	
	
	public JSONWrapper getTestCaseToImport() {
		return testCaseToImport;
	}

	public void setTestCaseToImport(JSONWrapper objectToExport) {
		this.testCaseToImport = objectToExport;
	}
	
}
