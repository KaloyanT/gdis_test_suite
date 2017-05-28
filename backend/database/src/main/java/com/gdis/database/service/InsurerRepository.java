package com.gdis.database.service;

import org.springframework.data.repository.CrudRepository;

import com.gdis.database.model.Insurer;

public interface InsurerRepository extends CrudRepository<Insurer, Long> {

	public Insurer findByInsurerID(long insurerID);
	
	public Insurer findByName(String name);
}