package com.gdis.importer.model;

import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONWrapper {
	
	private String testName;
	
	private String storyType;
	
	private String storyName;
	
	//private List<JSONSchema> testData;
	//private List<ObjectNode> testData;
	private Set<ObjectNode> testData = new HashSet<ObjectNode>();
	
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
	
	public Set<ObjectNode> getTestData() {
		return testData;
	}

	public void setTestData(Set<ObjectNode> testData) {
		this.testData = testData;
	}

	public String getStoryName() {
		return storyName;
	}

	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}

	
}
