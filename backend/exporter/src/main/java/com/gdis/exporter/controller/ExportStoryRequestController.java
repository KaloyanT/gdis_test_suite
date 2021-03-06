package com.gdis.exporter.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.exporter.util.CustomErrorType;
import com.gdis.exporter.model.StoryExportModel;
import com.gdis.exporter.util.DBClient;

@RestController
@RequestMapping("/exporter/e")
public class ExportStoryRequestController {

	@Autowired
	private DBClient dbClient;
	
	/**
	 * Returns all the stories with all of their details 
	 * @return A List which contains all stories
	 */
	@RequestMapping(value = "/stories", method = RequestMethod.GET)
	public ResponseEntity<?> getStories() {
		
		List<StoryExportModel> storiesList = dbClient.exportAllStoriesFromDB();
		
		if(storiesList == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(storiesList, HttpStatus.OK);
	}
	
	
	/**
	 * Returns a simple list with the names of all Stories
	 * @return A list with the names of all Stories
	 */
	@RequestMapping(value = "/stories/list", method = RequestMethod.GET)
	public ResponseEntity<?> getStoriesList() {
		
		List<String> storiesList = dbClient.exportListOfStoryNamesFromDB();
		
		if(storiesList == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(storiesList, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param storyName The name of the Story which has to be returned
	 * @return Returns all the information about the Story with the name storyName
	 */
	@RequestMapping(value = "/stories/by-story-name/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryByStoryName(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		List<StoryExportModel> storiesList = dbClient.exportStoryFromDBByStoryName(storyName);
		
		if(storiesList == null) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(storiesList, HttpStatus.OK);
	}
	
}
