package com.gdis.importer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class StoryTestImportModel {
	
	private String testName;
	
	private String storyName;
	
	private Map<String, String> mappings = new HashMap<String, String>();
	
	private List<ObjectNode> data = new ArrayList<ObjectNode>();
	
	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}
	
	public List<ObjectNode> getData() {
		return data;
	}

	public void setData(List<ObjectNode> data) {
		this.data = data;
	}

	public Map<String, String> getMappings() {
		return mappings;
	}

	public void setMappings(Map<String, String> mappings) {
		this.mappings = mappings;
	}

	
	public String getStoryName() {
		return storyName;
	}

	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}

	
	
}
