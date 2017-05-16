package com.gdis.importer.model;

import java.util.List;

public class JSONWrapper {
	
	private String testNameImport;
	
	private String storyType;
	
	private List<JSONSchema> testData;
	
	public String getTestNameImport() {
		return testNameImport;
	}

	public void setTestNameImport(String testNameImport) {
		this.testNameImport = testNameImport;
	}
	

	public String getStoryType() {
		return storyType;
	}

	public void setStoryType(String storyType) {
		this.storyType = storyType;
	}
	
	public List<JSONSchema> getTestData() {
		return testData;
	}

	public void setTestData(List<JSONSchema> testData) {
		this.testData = testData;
	}

	
}
