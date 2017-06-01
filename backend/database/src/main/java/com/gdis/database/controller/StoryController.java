package com.gdis.database.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.gdis.database.model.Story;
import com.gdis.database.service.StoryRepository;
import com.gdis.database.util.PreCondition;

@RestController
@RequestMapping("/db/stories")
public class StoryController {

	@Autowired
	private StoryRepository storyRepository;
	
	private Story storyToSave;
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public ResponseEntity<?> createModifiedContract(@RequestBody Story newStory) {
	    
		
		if(newStory == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		setStoryToSave(newStory);
		
		/*
		boolean duplicateExists = duplicateModifiedContractFound(getModifiedContractToSave());
	
		if(duplicateExists == true) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}*/
		
		storyRepository.save(newStory);
			
		return new ResponseEntity<>(HttpStatus.CREATED);
		
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteModifyContract(@PathVariable("id") long id) {
		
		PreCondition.require(id >= 0, "Story ID can't be negative!");
		
		Story currentStory = storyRepository.findByStoryID(id);
		
		if (currentStory == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		storyRepository.delete(currentStory);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	public Story getStoryToSave() {
		return storyToSave;
	}

	public void setStoryToSave(Story storyToSave) {
		this.storyToSave = storyToSave;
	}
}
