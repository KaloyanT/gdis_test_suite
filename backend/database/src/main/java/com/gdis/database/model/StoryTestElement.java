package com.gdis.database.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity(name = "StoryTestElement")
@Table(name = "storyTestElement")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StoryTestElement {

	@Id
	@GenericGenerator(name = "storyTestElementIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "storyTestElementIdGenerator")
	@Column(name = "storyTestElementID")
	private long storyTestElementID;
	
	@Basic(optional = false)
	private String columnName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "story_test_storyTestID")
	private StoryTest storyTest;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_entity_testEntityID")
	//@JsonProperty(value = "testEntity")
	//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "entityName", scope = TestEntity.class)
	//@JsonIdentityReference(alwaysAsId = true) 
	//@JsonUnwrapped
	private TestEntity testEntity;
	
	
	// Use this only to make the Incoming JSON simpler. Just Find the TestEntity with the given name here during import
	@Basic(optional = false)
	private String entityName;
	
	
	@ElementCollection
	@CollectionTable(name = "storyTestElement_attributes", joinColumns = @JoinColumn(name = "story_test_element_storyTestElementID"))
	@Column(name = "rows")
	private List<String> attributes = new ArrayList<String>();	
	
	@JsonIgnore
	public long getStoryTestElementID() {
		return storyTestElementID;
	}

	public void setStoryTestElementID(long storyTestElementID) {
		this.storyTestElementID = storyTestElementID;
	}
	
	/*
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	 */
	
	@JsonIgnore
	public StoryTest getStoryTest() {
		return storyTest;
	}

	public void setStoryTest(StoryTest storyTest) {
		this.storyTest = storyTest;
	}
	
	/**
	 * Override the default equals method for consistency
	 */
	@Override
	public boolean equals(Object o) {
		
		if(this == o) {
			return true;
		}
		if(! (o instanceof StoryTestElement) ) {
			return false;
		}
		
		return (this.storyTestElementID != 0) && 
				(this.storyTestElementID == ((StoryTestElement) o).storyTestElementID);
	}
	
	/**
	 * Use this hashCode for consistency
	 */
	@Override
	public int hashCode() {
		return 1;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/*
	 * Make the JSON Look like this: 
	 * "testName": "...", 
	 * "storyName": "...",
	 * "data": [
	 * 		{
	 * 			"columnName": "...",
	 * 			"testEntity": "...",
	 * 			"attributes": ["..."]
	 * 		}
	 * ]
	 * 
	 * And not like this: 
	 * "testName": "...", 
	 * "storyName": "...",
	 * "data": [
	 * 		{
	 * 			"columnName": "...",
	 * 			"testEntity": {"entityName": "..."},
	 * 			"attributes": ["..."]
	 * 		}
	 * ]
	 * Note that the second version still works
	 */
	//@JsonProperty(value = "testEntity")
	//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "entityName", scope = TestEntity.class)
	//@JsonIdentityReference(alwaysAsId = true) 
	// @JsonIgnore
	public TestEntity getTestEntity() {
		return testEntity;
	}

	
	public void setTestEntity(TestEntity testEntity) {
		this.testEntity = testEntity;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntity(String entityName) {
		this.entityName = entityName;
	}
}
