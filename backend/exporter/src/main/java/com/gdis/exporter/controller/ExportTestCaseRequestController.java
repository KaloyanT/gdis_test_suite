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
@RequestMapping("/exporter/e/testCase")
public class ExportTestCaseRequestController {
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTests() {
	
		DBClient dbClient = new DBClient();
		
		List<JSONResponse> response = dbClient.exportAllTestsFromDB();
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/byStoryName/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByStoryName(@PathVariable("storyName") String storyName) {
		
		//ObjectMapper objectMapper = new ObjectMapper();
		
		DBClient dbClient = new DBClient();
		
		List<JSONResponse> response = dbClient.exportTestsFromDBByStoryName(storyName);
		
		//System.out.println(response.size());
		
		//ObjectNode node = objectMapper.createObjectNode();
		
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/byTestName/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByTestName(@PathVariable("testName") String testName) {
		
		
		DBClient dbClient = new DBClient();
		
		JSONResponse response = dbClient.exportCTestFromDBByTestName(testName);
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
