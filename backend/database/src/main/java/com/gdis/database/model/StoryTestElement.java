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
	@JsonIgnore
	private TestEntity testEntity;
	
	// Use this only to make the Incoming JSON simpler. Just Find the TestEntity with the given name during import
	@Basic(optional = false)
	private String entityName;
	
	@ElementCollection
	@CollectionTable(name = "storyTestElement_rows", joinColumns = @JoinColumn(name = "story_test_element_storyTestElementID"))
	@Column(name = "rows")
	private List<String> rows = new ArrayList<String>();	
	
	//@JsonIgnore
	public long getStoryTestElementID() {
		return storyTestElementID;
	}

	public void setStoryTestElementID(long storyTestElementID) {
		this.storyTestElementID = storyTestElementID;
	}
	
	@JsonIgnore
	public StoryTest getStoryTest() {
		return storyTest;
	}

	public void setStoryTest(StoryTest storyTest) {
		this.storyTest = storyTest;
	}
	
	public List<String> getRows() {
		return rows;
	}

	public void setRows(List<String> rows) {
		this.rows = rows;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public TestEntity getTestEntity() {
		return testEntity;
	}

	public void setTestEntity(TestEntity testEntity) {
		this.testEntity = testEntity;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
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
}
