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
public class ExportTestCaseRequestController {
	
	
	private List<ObjectNode> elementsToExport;
	
	@Autowired
	private DBClient dbClient;
	
	@RequestMapping(value = "/{storyType}/all", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTestsByStoryType(@PathVariable("storyType") String storyType) {
	
		if( (storyType == null) || (storyType.isEmpty()) || (storyType.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Type"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTestExportModel> response = dbClient.exportAllTestsFromDB(storyType);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response, storyType);
		
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{storyType}/all/export", method = RequestMethod.GET)
	public ResponseEntity<?> getAllTestsByStoryTypeForExport(@PathVariable("storyType") String storyType) {
	
		if( (storyType == null) || (storyType.isEmpty()) || (storyType.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Type"), HttpStatus.NOT_FOUND);
		}
		
		List<ObjectNode> response = dbClient.exportAllTestsFromDBForExport(storyType);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{storyType}/by-story-name/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByStoryName(@PathVariable("storyType") String storyType, 
			@PathVariable("storyName") String storyName) {
		
		if( (storyType == null) || (storyType.isEmpty()) || (storyType.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Type"), HttpStatus.NOT_FOUND);
		}
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTestExportModel> response = dbClient.exportTestsFromDBByStoryName(storyType, storyName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response, storyType);
				
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{storyType}/by-story-name/{storyName}/export", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByStoryNameForExport(@PathVariable("storyType") String storyType, 
			@PathVariable("storyName") String storyName) {
		
		if( (storyType == null) || (storyType.isEmpty()) || (storyType.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Type"), HttpStatus.NOT_FOUND);
		}
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<ObjectNode> response = dbClient.exportTestsFromDBByStoryNameForExport(storyType, storyName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{storyType}/by-test-name/{testName}", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByTestName(@PathVariable("storyType") String storyType, 
			@PathVariable("testName") String testName) {
		
		if( (storyType == null) || (storyType.isEmpty()) || (storyType.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Type"), HttpStatus.NOT_FOUND);
		}
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryTestExportModel> response = dbClient.exportTestFromDBByTestName(storyType, testName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		buildJsonElementsToExport(response, storyType);
		
		
		return new ResponseEntity<>(getElementsToExport(), HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/{storyType}/by-test-name/{testName}/export", method = RequestMethod.GET)
	public ResponseEntity<?> getTestsByTestNameForExport(@PathVariable("storyType") String storyType, 
			@PathVariable("testName") String testName) {
		
		if( (storyType == null) || (storyType.isEmpty()) || (storyType.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Type"), HttpStatus.NOT_FOUND);
		}
		
		if( (testName == null) || (testName.isEmpty()) || (testName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<ObjectNode> response = dbClient.exportTestFromDBByTestNameForExport(storyType, testName);
		
		if(response == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	private void buildJsonElementsToExport(List<StoryTestExportModel> dbEntries, String storyType) {
		
		ObjectMapper mapper = new ObjectMapper();
		List<ObjectNode> res = new ArrayList<ObjectNode>();
		
		for(StoryTestExportModel jResponse : dbEntries) {
			
			ObjectNode currentNode = mapper.createObjectNode();
			
			currentNode.put("storyTestID", jResponse.getStoryTestID());
			currentNode.put("storyType", storyType);
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
