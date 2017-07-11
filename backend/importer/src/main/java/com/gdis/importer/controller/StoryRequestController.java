package com.gdis.importer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.importer.util.CustomErrorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.importer.model.StoryImportModel;
import com.gdis.importer.util.DBClient;

@RestController
@RequestMapping("/importer")
public class StoryRequestController {

	@Autowired
	private DBClient dbClient;
	
	@RequestMapping(value = "/i/story", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
		public ResponseEntity<?> handleStoryImportRequest(@RequestBody StoryImportModel newStory) {	
		
		if(newStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story JSON"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newStory.getStoryName() == null) || (newStory.getStoryName().isEmpty()) || (newStory.getStoryName().trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryName"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newStory.getScenarios() == null) || (newStory.getScenarios().isEmpty()) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Scenarios for Story"), HttpStatus.BAD_REQUEST);
		}
		
		if( (newStory.getDescription() == null) || (newStory.getDescription().isEmpty()) || (newStory.getDescription().trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryDescription"), HttpStatus.BAD_REQUEST);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode json = objectMapper.createObjectNode();
		ArrayNode scenarios = objectMapper.createArrayNode();
		
		newStory.getScenarios().forEach(scenarios::add);
		
		json.put("storyName", newStory.getStoryName());
		json.put("description", newStory.getDescription());
		json.putArray("scenarios").addAll(scenarios);
				
		ResponseEntity<String> dbClientResponse = dbClient.importStoryInDB(json);
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
	
	
	@RequestMapping(value = "/u/story/{storyName}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleStoryUpdateRequest(@PathVariable("storyName") String storyName, 
			@RequestBody StoryImportModel updatedStory) {	
		
		if(updatedStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story JSON"), HttpStatus.BAD_REQUEST);
		}
		
		if( (updatedStory.getStoryName() == null) || (updatedStory.getStoryName().isEmpty()) || (updatedStory.getStoryName().trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryName for Updated Story"), HttpStatus.BAD_REQUEST);
		}
		
		if( (updatedStory.getScenarios() == null) || (updatedStory.getScenarios().isEmpty()) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Scenarios for Updated Story"), HttpStatus.BAD_REQUEST);
		}
		
		if( (updatedStory.getDescription() == null) || (updatedStory.getDescription().isEmpty()) || (updatedStory.getDescription().trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid StoryDescription for Updated Story"), HttpStatus.BAD_REQUEST);
		}
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid storyName to Update"), HttpStatus.BAD_REQUEST);
		}
		
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode json = objectMapper.createObjectNode();
		ArrayNode scenarios = objectMapper.createArrayNode();
		
		updatedStory.getScenarios().forEach(scenarios::add);
		
		json.put("storyName", updatedStory.getStoryName());
		json.put("description", updatedStory.getDescription());
		json.putArray("scenarios").addAll(scenarios);
				
		ResponseEntity<String> dbClientResponse = dbClient.updateStoryInDB(storyName, json);
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
	
	

	@RequestMapping(value = "/d/story/{storyName}", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})		
	public ResponseEntity<?> handleStoryDeleteRequest(@PathVariable("storyName") String storyName) {
		
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid storyName to Update"), HttpStatus.BAD_REQUEST);
		}
		
		
		ResponseEntity<String> dbClientResponse = dbClient.deleteStoryFromDB(storyName);
		
		if(dbClientResponse == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return dbClientResponse;
	}
}
