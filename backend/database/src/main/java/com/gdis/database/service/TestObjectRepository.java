package com.gdis.database.service;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.TestEntity;
import com.gdis.database.model.TestObject;

public interface TestObjectRepository extends CrudRepository<TestObject, Long> {
	
	public TestObject findByTestObjectID(long id);
	
	public List<TestObject> findByEntityType(TestEntity entityType);
	
	public TestObject findByHashCodeNoID(long hashCodeNoID);

}
