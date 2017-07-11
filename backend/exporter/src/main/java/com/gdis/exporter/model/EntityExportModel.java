package com.gdis.exporter.model;

import java.util.ArrayList;
import java.util.List;

public class EntityExportModel {
	
	private String entityName;
	
	private List<String> testEntityAttributes = new ArrayList<String>();

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public List<String> getTestEntityAttributes() {
		return testEntityAttributes;
	}

	public void setTestEntityAttributes(List<String> testEntityAttributes) {
		this.testEntityAttributes = testEntityAttributes;
	}
}
