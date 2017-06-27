package com.gdis.database.service;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.StoryTest;

public interface StoryTestRepository extends CrudRepository<StoryTest, Long> {

	public StoryTest findByStoryTestID(long id);
	
	public StoryTest findByTestName(String testName);
		
	public List<StoryTest> findByStoryName(String storyName);
	
	public List<StoryTest> findByStoryNameAndTestName(String storyName, String testnaName);
	
}
