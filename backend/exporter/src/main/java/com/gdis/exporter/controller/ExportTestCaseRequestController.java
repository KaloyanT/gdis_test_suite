package com.gdis.exporter.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.exporter.model.JSONResponse;
import com.gdis.exporter.util.DBClient;

@RestController
@RequestMapping("/exporter/e")
public class ExportTestCaseRequestController {
	
	
	private List<ObjectNode> elementsToExport;
	
	
	@RequestMapping(value = "/{storyType}/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTestsByStoryType(@PathVariable("storyType") String storyType) {
	
		DBClient dbClient = new DBClient();
		
		List<JSONResponse> response = dbClient.exportAllTestsFromDB(storyType);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response, storyType);
		
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{storyType}/by-story-name/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByStoryName(@PathVariable("storyType") String storyType, 
			@PathVariable("storyName") String storyName) {
		
		//ObjectMapper objectMapper = new ObjectMapper();
		
		DBClient dbClient = new DBClient();
		
		List<JSONResponse> response = dbClient.exportTestsFromDBByStoryName(storyType, storyName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response, storyType);
		
		//System.out.println(response.size());
		
		//ObjectNode node = objectMapper.createObjectNode();
		
		
		
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{storyType}/by-test-name/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByTestName(@PathVariable("storyType") String storyType, 
			@PathVariable("testName") String testName) {
		
		
		DBClient dbClient = new DBClient();
		
		List<JSONResponse> response = dbClient.exportTestFromDBByTestName(storyType, testName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response, storyType);
		
		
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	
	private void buildJsonElementsToExport(List<JSONResponse> dbEntries, String storyType) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<ObjectNode> res = new ArrayList<ObjectNode>();
		
		for(JSONResponse jResponse : dbEntries) {
			
			ObjectNode currentNode = mapper.createObjectNode();
			
			currentNode.put("storyType", storyType);
			currentNode.put("storyName", jResponse.getStoryName());
			currentNode.put("testName", jResponse.getTestName());
			
			ArrayNode arrayNode = mapper.createArrayNode();
			
			for(ObjectNode objN : jResponse.getData()) {
				arrayNode.add(objN.elements().next());	
			}
			
			currentNode.putArray("testData").addAll(arrayNode);
			res.add(currentNode);
		}
		
		setElementsToExport(res);
		
	}
	

	public List<ObjectNode> getElementsToExport() {
		return elementsToExport;
	}


	public void setElementsToExport(List<ObjectNode> elementsToExport) {
		this.elementsToExport = elementsToExport;
	}
}
