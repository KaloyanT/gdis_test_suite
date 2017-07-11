package com.gdis.exporter.model;

import java.util.ArrayList;
import java.util.List;

public class StoryExportModel {

	private long storyID;
	
	private String storyName;
	
	private String description;
	
	private List<String> scenarios = new ArrayList<String>();

	public long getStoryID() {
		return storyID;
	}

	public void setStoryID(long storyID) {
		this.storyID = storyID;
	}

	public String getStoryName() {
		return storyName;
	}

	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<String> scenarios) {
		this.scenarios = scenarios;
	}
	
}
