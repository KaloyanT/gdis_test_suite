package com.gdis.database.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Story")
@Table(name = "story")
public class Story {
	
	@Id
	@GenericGenerator(name = "storyIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "storyIdGenerator")
	@Column(name = "storyID")
	private long storyID;
	
	@Basic(optional = false)
	private String storyName;
	
	@Basic(optional = false)
	private String description;

	@ElementCollection
	@CollectionTable(name = "story_scenarios", joinColumns = @JoinColumn(name = "story_storyID"))
	@Column(name = "scenarios")
	private List<String> scenarios = new ArrayList<String>();
	
	// Each story has a List of StoryTests. Every StoryTests consist of a list of columns (StoryTestElement)
	// and some additional data
	 @OneToMany(cascade = CascadeType.ALL, mappedBy = "story", orphanRemoval = true)
	 private List<StoryTest> testsForStory = new ArrayList<StoryTest>();
	
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

	public List<StoryTest> getTestsForStory() {
		return testsForStory;
	}

	public void setTestsForStory(List<StoryTest> testsForStory) {
		this.testsForStory = testsForStory;
	}
	
	public void addTestForStory(StoryTest storyTest) {
		testsForStory.add(storyTest);
		storyTest.setStory(this);
	}
	
	public void removeTestForStory(StoryTest storyTest) {
		testsForStory.remove(storyTest);
		storyTest.setStory(null);
	}
	
	public void clearTestsForStory() {
		
		for(StoryTest st : getTestsForStory()) {
			st.setStory(null);
		}
		testsForStory.clear();
	}
	
	/**
	 * Override the default equals method for consistency
	 */
	@Override
	public boolean equals(Object o) {
		
		if(this == o) {
			return true;
		}
		if(! (o instanceof Story) ) {
			return false;
		}
		
		return (this.storyID != 0) && 
				(this.storyID == ((Story) o).storyID);
	}
	
	/**
	 * Use this hashCode for consistency
	 */
	@Override
	public int hashCode() {
		return 1;
	}
}
