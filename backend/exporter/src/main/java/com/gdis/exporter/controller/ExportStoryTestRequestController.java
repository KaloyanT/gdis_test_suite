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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.exporter.util.CustomErrorType;
import com.gdis.exporter.model.StoryTestExportModel;
import com.gdis.exporter.util.DBClient;

@RestController
@RequestMapping("/exporter/e")
public class ExportStoryTestRequestController {
	
	private List<ObjectNode> elementsToExport;
	
	@Autowired
	private DBClient dbClient;
	
	@RequestMapping(value = "/storyTests/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTestsByStoryType() {
		
		List<StoryTestExportModel> response = dbClient.exportAllStoryTestsFromDB();
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response);
		
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/storyTests/all/export", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTestsByStoryTypeForExport() {
	
		List<ObjectNode> response = dbClient.exportAllStoryTestsFromDBForExport();
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}	
	
	@RequestMapping(value = "/storyTests/all/testNames", method = RequestMethod.GET)
	public ResponseEntity<?> getTestNamesOfAllStoryTests() {
		
		List<String> response = dbClient.exportTestNamesOfAllStoryTestsFromDB();
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/storyTests/by-story-name/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByStoryName(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTestExportModel> response = dbClient.exportStoryTestsFromDBByStoryName(storyName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response);
				
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/storyTests/by-story-name/{storyName}/export", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByStoryNameForExport(@PathVariable("storyName") String storyName) {
				
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<ObjectNode> response = dbClient.exportStoryTestsFromDBByStoryNameForExport(storyName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/storyTests/by-test-name/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByTestName(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTestExportModel> response = dbClient.exportStoryTestFromDBByTestName(testName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response);
		
		
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/storyTests/by-test-name/{testName}/export", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByTestNameForExport(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		List<ObjectNode> response = dbClient.exportStoryTestFromDBByTestNameForExport(testName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
		
	@RequestMapping(value = "/storyTests/by-entity-name/{entityName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByEntityName(@PathVariable("entityName") String entityName) {
		
		if( (entityName == null) || (entityName.isEmpty()) || (entityName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Entity Name"), HttpStatus.NOT_FOUND);
		}
		
		List<ObjectNode> response = dbClient.exportStoryTestFromDBByEntityName(entityName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/storyTests/entity-attributes/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getEntityAttributesForTestByTestName(@PathVariable("testName") String testName) {
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Test Name"), HttpStatus.NOT_FOUND);
		}
		
		List<String> response = dbClient.exportEntityAttributesForStoryTestFromDBByTestName(testName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}	
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	private void buildJsonElementsToExport(List<StoryTestExportModel> dbEntries) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<ObjectNode> res = new ArrayList<ObjectNode>();
		
		for(StoryTestExportModel jResponse : dbEntries) {
			
			ObjectNode currentNode = mapper.createObjectNode();
			
			currentNode.put("storyTestID", jResponse.getStoryTestID());
			currentNode.put("storyName", jResponse.getStoryName());
			currentNode.put("testName", jResponse.getTestName());
			
			ArrayNode arrayNode = mapper.createArrayNode();
			
			for(ObjectNode objN : jResponse.getData()) {
				//arrayNode.add(objN.elements().next());
				arrayNode.add(objN);
			}
			
			currentNode.putArray("data").addAll(arrayNode);
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
