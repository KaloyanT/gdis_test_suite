package com.gdis.database.service;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.TestEntity;

public interface TestEntityRepository extends CrudRepository<TestEntity, Long> {

	public TestEntity findByTestEntityID(long testEntityID);
	
	public TestEntity findByEntityName(String entityName);
}
