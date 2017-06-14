package com.gdis.exporter.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JSONResponse {
	
	private long basicStoryTestID;
	
	private String testName;
	
	private String storyName;
	
	private List<ObjectNode> data = new ArrayList<ObjectNode>();

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getStoryName() {
		return storyName;
	}

	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}

	public List<ObjectNode> getData() {
		return data;
	}

	public void setData(List<ObjectNode> data) {
		this.data = data;
	}

	public long getBasicStoryTestID() {
		return basicStoryTestID;
	}

	public void setBasicStoryTestID(long basicStoryTestID) {
		this.basicStoryTestID = basicStoryTestID;
	}

}
