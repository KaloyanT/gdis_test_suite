package com.gdis.database.service;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.ContractStoryTest;

public interface ContractStoryTestRepository extends CrudRepository<ContractStoryTest, Long> {

	public ContractStoryTest findByStoryTestID(long id);
	
	public ContractStoryTest findByStoryID(long id);
	
	public List<ContractStoryTest> findByTestName(String testName);
	
	public List<ContractStoryTest> findByStoryName(String storyName);
}
