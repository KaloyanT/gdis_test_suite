package com.gdis.database.service;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.Story;

public interface StoryRepository extends CrudRepository<Story, Long> {

	public Story findByStoryID(long id);
	
	public Story findByTestName(String testName);
}
