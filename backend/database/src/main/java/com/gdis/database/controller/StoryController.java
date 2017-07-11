package com.gdis.database.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gdis.database.model.Story;
import com.gdis.database.service.StoryRepository;
import com.gdis.database.util.CustomErrorType;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/story")
public class StoryController {
	
	@Autowired
	private StoryRepository storyRepository;

	private Story storyToSave;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllStories() {
		
		Iterable<Story> storyIterable = storyRepository.findAll();
		
		if(storyIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<Story> storyList = new ArrayList<Story>();
		
		// Java 8 Method Reference is used here
		storyIterable.forEach(storyList::add);
		
		return new ResponseEntity<>(storyList, HttpStatus.OK);
	}
	
	
	/*
	 * Get all the stories without the list with storyTest
	 */
	@RequestMapping(value = "/reduced", method = RequestMethod.GET)
	public ResponseEntity<?> getAllStoriesReduced() {
		
		Iterable<Story> storyIterable = storyRepository.findAll();
		
		if(storyIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<Story> storyList = new ArrayList<Story>();
		
		// Java 8 Method Reference is used here
		storyIterable.forEach(storyList::add);
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<ObjectNode> storyObjectsWithoutStoryTests = new ArrayList<ObjectNode>();
		
		for(Story s : storyList) {
			
			ObjectNode currentObject = objectMapper.createObjectNode();
			currentObject.put("storyID", s.getStoryID());
			currentObject.put("storyName", s.getStoryName());
			currentObject.put("description", s.getDescription());
			
			ArrayNode scenariosArray = objectMapper.createArrayNode();
			s.getScenarios().forEach(scenariosArray::add);
			
			currentObject.putArray("scenarios").addAll(scenariosArray);
			
			storyObjectsWithoutStoryTests.add(currentObject);
			
		}
		
		return new ResponseEntity<>(storyObjectsWithoutStoryTests, HttpStatus.OK);
	}
	
	
	/*
	 * Get all the stories as a list of storyNames
	 */
	@RequestMapping(value = "/story-name-list", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryNameOfAllStories() {
		
		Iterable<Story> storyIterable = storyRepository.findAll();
		
		if(storyIterable == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		List<Story> storyList = new ArrayList<Story>();
		
		// Java 8 Method Reference is used here
		storyIterable.forEach(storyList::add);
		
		List<String> storyObjectsWithoutStoryTests = new ArrayList<String>();
		
		for(Story s : storyList) {
			
			storyObjectsWithoutStoryTests.add(s.getStoryName());
		}
		
		return new ResponseEntity<>(storyObjectsWithoutStoryTests, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryByID(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		Story story = storyRepository.findByStoryID(id);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with ID " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<Story> storyAsList = new ArrayList<Story>();
		storyAsList.add(story);
		
		return new ResponseEntity<>(storyAsList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get/{id}/reduced", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryByIDReduced(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		Story story = storyRepository.findByStoryID(id);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with ID " + id
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<ObjectNode> storyObjectWithoutStoryTestsAsList = new ArrayList<ObjectNode>();
		
			
		ObjectNode currentObject = objectMapper.createObjectNode();
		currentObject.put("storyID", story.getStoryID());
		currentObject.put("storyName", story.getStoryName());
		currentObject.put("description", story.getDescription());
			
		ArrayNode scenariosArray = objectMapper.createArrayNode();
		story.getScenarios().forEach(scenariosArray::add);
			
		currentObject.putArray("scenarios").addAll(scenariosArray);
			
		storyObjectWithoutStoryTestsAsList.add(currentObject);
		
		return new ResponseEntity<>(storyObjectWithoutStoryTestsAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-story-name/{storyName}", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryByStoryName(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		Story story = storyRepository.findByStoryName(storyName);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with name " + storyName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		List<Story> storyAsList = new ArrayList<Story>();
		storyAsList.add(story);
		
		return new ResponseEntity<>(storyAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/get/by-story-name/{storyName}/reduced", method = RequestMethod.GET)
	public ResponseEntity<?> getStoryByStoryNameReduced(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		Story story = storyRepository.findByStoryName(storyName);
		
		if (story == null) {
			return new ResponseEntity<>(new CustomErrorType("Story with name " + storyName
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		List<ObjectNode> storyObjectWithoutStoryTestsAsList = new ArrayList<ObjectNode>();
		
			
		ObjectNode currentObject = objectMapper.createObjectNode();
		currentObject.put("storyID", story.getStoryID());
		currentObject.put("storyName", story.getStoryName());
		currentObject.put("description", story.getDescription());
			
		ArrayNode scenariosArray = objectMapper.createArrayNode();
		story.getScenarios().forEach(scenariosArray::add);
			
		currentObject.putArray("scenarios").addAll(scenariosArray);
			
		storyObjectWithoutStoryTestsAsList.add(currentObject);
		
		return new ResponseEntity<>(storyObjectWithoutStoryTestsAsList, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createStory(@RequestBody Story newStory) {
	
		if(newStory == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setStoryToSave(newStory);
		
		long storyExists = storyExist(getStoryToSave());
		
		if(storyExists == -1) {			
			return new ResponseEntity<>(new CustomErrorType("A Story with the same name already exists"),  
					HttpStatus.CONFLICT);
		}
		
		storyRepository.save(newStory);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStory(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		Story currentStory = storyRepository.findByStoryID(id);
		
		if (currentStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. Story with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		storyRepository.delete(currentStory);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/delete/by-story-name/{storyName}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStoryByStoryName(@PathVariable("storyName") String storyName) {
		
		if( (storyName == null) || (storyName.isEmpty()) || (storyName.trim().length() == 0) ) {
			return new ResponseEntity<>(new CustomErrorType("Invalid Story Name"), HttpStatus.NOT_FOUND);
		}
		
		Story currentStory = storyRepository.findByStoryName(storyName);
		
		if (currentStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to delete. Story with storyName "
					+ storyName + " not found."), HttpStatus.NOT_FOUND);
		}
		
		storyRepository.delete(currentStory);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStoryByID(@PathVariable("id") long id, 
			@RequestBody Story updatedStory) {
		
		PreCondition.require(id >= 0, "StoryID can't be negative!");
		
		if(updatedStory == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Story currentStory = storyRepository.findByStoryID(id);

		if (currentStory == null) {
			return new ResponseEntity<>(new CustomErrorType("Unable to update. StoryTest with ID "
					+ id + " not found."), HttpStatus.NOT_FOUND);
		}
		
		Story storyWithSameName = storyRepository.findByStoryName(updatedStory.getStoryName());
		
		if(storyWithSameName != null) {
			
			if(currentStory.getStoryID() != storyWithSameName.getStoryID()) {
				return new ResponseEntity<>(new CustomErrorType("Unable to update. There is another Story "
						+ "with this testName."), HttpStatus.CONFLICT);
			}
		}
		
		currentStory.getScenarios().clear();
		currentStory.setStoryName(updatedStory.getStoryName());
		currentStory.setDescription(updatedStory.getDescription());
		currentStory.setScenarios(updatedStory.getScenarios());
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public Story getStoryToSave() {
		return storyToSave;
	}


	public void setStoryToSave(Story storyToSave) {
		this.storyToSave = storyToSave;
	}
	
	private long storyExist(Story newStory) {
		
		Story storyWithSameName = storyRepository.findByStoryName(newStory.getStoryName());
		
		// Return 1 if there is a test with the same testNamee
		if(storyWithSameName != null) {
			return -1L;
		}
		
		// Return 0 if the Test for this Story doesn't already exist
		return 0;
	} 
}
