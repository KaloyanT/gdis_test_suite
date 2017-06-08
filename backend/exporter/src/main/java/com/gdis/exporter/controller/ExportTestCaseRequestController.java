package com.gdis.exporter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.exporter.model.JSONResponse;
import com.gdis.exporter.util.DBClient;

@RestController
@RequestMapping("/exporter/e")
public class ExportTestCaseRequestController {
	
	@RequestMapping(value = "/{storyType}/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTests(@PathVariable("storyType") String storyType) {
	
		DBClient dbClient = new DBClient();
		
		List<JSONResponse> response = dbClient.exportAllTestsFromDB(storyType);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{storyType}/by-story-name/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByStoryName(@PathVariable("storyType") String storyType, @PathVariable("storyName") String storyName) {
		
		//ObjectMapper objectMapper = new ObjectMapper();
		
		DBClient dbClient = new DBClient();
		
		List<JSONResponse> response = dbClient.exportTestsFromDBByStoryName(storyType, storyName);
		
		//System.out.println(response.size());
		
		//ObjectNode node = objectMapper.createObjectNode();
		
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{storyType}/by-test-name/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByTestName(@PathVariable("storyType") String storyType, @PathVariable("testName") String testName) {
		
		
		DBClient dbClient = new DBClient();
		
		JSONResponse response = dbClient.exportCTestFromDBByTestName(storyType, testName);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
