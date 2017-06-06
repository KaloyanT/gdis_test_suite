package com.gdis.importer.model;

import java.util.List;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONWrapper {
	
	private String testName;
	
	private String storyType;
	
	private String storyName;
	
	//private List<JSONSchema> testData;
	private List<ObjectNode> testData;
	
	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
	

	public String getStoryType() {
		return storyType;
	}

	public void setStoryType(String storyType) {
		this.storyType = storyType;
	}
	
	public List<ObjectNode> getTestData() {
		return testData;
	}

	public void setTestData(List<ObjectNode> testData) {
		this.testData = testData;
	}

	public String getStoryName() {
		return storyName;
	}

	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}

	
}
