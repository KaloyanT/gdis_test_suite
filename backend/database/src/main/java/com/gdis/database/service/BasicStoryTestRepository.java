package com.gdis.database.service;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.BasicStoryTest;

public interface BasicStoryTestRepository extends CrudRepository<BasicStoryTest, Long> {

	public BasicStoryTest findByStoryTestID(long id);
	
	public BasicStoryTest findByStoryID(long id);
	
	public List<BasicStoryTest> findByTestName(String testName);
	
	public List<BasicStoryTest> findByStoryName(String storyName);
	
}
