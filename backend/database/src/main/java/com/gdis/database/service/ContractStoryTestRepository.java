package com.gdis.database.service;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.ContractStoryTest;

public interface ContractStoryTestRepository extends CrudRepository<ContractStoryTest, Long> {

	public ContractStoryTest findByBasicStoryTestID(long id);
		
	public List<ContractStoryTest> findByTestName(String testName);
	
	public List<ContractStoryTest> findByStoryName(String storyName); 
	
	public List<ContractStoryTest> findByStoryNameAndTestName(String storyName, String testnaName);
}
