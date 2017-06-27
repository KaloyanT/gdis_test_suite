package com.gdis.database.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity(name = "TestEntity")
@Table(name = "testEntity")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TestEntity {

	@Id
	@GenericGenerator(name = "testEntityIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "testEntityIdGenerator")
	@Column(name = "testEntityID")
	private long testEntityID;
	
	@Basic(optional = false)
	private String entityName;
	
	//@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	//@JoinTable(name = "story_test_entities", joinColumns = @JoinColumn(name = "test_entity_testEntityID"), 
	//inverseJoinColumns = @JoinColumn(name = "story_teststoryTestID"))
	//private List<StoryTest> testsContainingEntity = new ArrayList<StoryTest>();
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "testEntity", orphanRemoval = true)
	private List<StoryTestElement> columnsContainingEntity = new ArrayList<StoryTestElement>();

	@JsonIgnore
	public long getTestEntityID() {
		return testEntityID;
	}


	public void setTestEntityID(long testEntityID) {
		this.testEntityID = testEntityID;
	}


	public String getEntityName() {
		return entityName;
	}


	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	
	@Override
	public boolean equals(Object o) {
		
		if(this == o) {
			return true;
		}
		if(! (o instanceof TestEntity) ) {
			return false;
		}
		
		return (this.testEntityID != 0) && 
				(this.testEntityID == ((TestEntity) o).testEntityID);
	}
	
	/**
	 * Use this hashCode for consistency
	 */
	@Override
	public int hashCode() {
		return 1;
	}

	public List<StoryTestElement> getColumnsContainingEntity() {
		return columnsContainingEntity;
	}


	public void setColumnsContainingEntity(List<StoryTestElement> columnsContainingEntity) {
		this.columnsContainingEntity = columnsContainingEntity;
	}
	
	public void addColumnsContainingEntity(StoryTestElement testElement) {
		columnsContainingEntity.add(testElement);
		testElement.setTestEntity(this);
	}
	
	public void removeColumnsContainingEntity(StoryTestElement testElement) {
		columnsContainingEntity.remove(testElement);
		testElement.setTestEntity(null);
	}
	
	public void clearColumnsContainingEntity() {
		
		for(StoryTestElement ste : getColumnsContainingEntity()) {
			ste.setTestEntity(null);
		}
		columnsContainingEntity.clear();
	}
}
