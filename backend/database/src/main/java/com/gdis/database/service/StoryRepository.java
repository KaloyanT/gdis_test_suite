package com.gdis.database.service;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.Story;

public interface StoryRepository extends CrudRepository<Story, Long> {

	public Story findByStoryID(long storyID);
	
	public Story findByStoryName(String storyName);
	
	public Story findByDescription(String description);
}
