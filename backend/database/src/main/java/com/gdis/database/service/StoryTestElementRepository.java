package com.gdis.database.service;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.gdis.database.model.StoryTestElement;
import com.gdis.database.model.TestEntity;

public interface StoryTestElementRepository extends CrudRepository<StoryTestElement, Long> {
	
	public List<StoryTestElement> getByTestEntity(TestEntity testEntity);
}
