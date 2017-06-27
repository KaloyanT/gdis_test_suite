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

@Entity(name = "StoryTest")
@Table(name = "storyTest")
public class StoryTest {
	
	@Id
	@GenericGenerator(name = "storyTestIdGenerator", strategy = "increment")
		
	@GeneratedValue(generator = "storyTestIdGenerator")
	@Column(name = "storyTestID")
	private long storyTestID;
	
	@Basic(optional = false)
	private String storyName;
	
	@Basic(optional = false)
	@Column(unique = true)
	private String testName;
		
	//@OneToMany(cascade = CascadeType.ALL, mappedBy = "storyTest", orphanRemoval = true)
	//private Map<String, TestEntity> mappings = new HashMap<String, TestEntity>();
	//@ManyToMany(mappedBy = "testsContainingEntity")
	//private List<TestEntity> testEntities = new ArrayList<TestEntity>();
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "storyTest", orphanRemoval = true)
	private List<StoryTestElement> data = new ArrayList<StoryTestElement>();
	
	
	public long getStoryTestID() {
		return storyTestID;
	}

	public void setStoryTestID(long storyTestID) {
		this.storyTestID = storyTestID;
	}
	
	public String getStoryName() {
		return storyName;
	}

	public void setStoryName(String storyName) {
		this.storyName = storyName;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}	
	
	@Override
	public String toString() {
		
		return "ContractRequest " + " [StoryTestID: " + getStoryTestID() + "]" 
				+ " [storyName: " + getStoryName() + "]" + " [testName: " + getTestName() + "]" 
				 + " [data: " + getData().toString() + "]";
	}
	
	
	public String toStringWithoutID() {
		
		return "ContractRequest " + " [storyName: " + getStoryName() + "]" 
				+ " [testName: " + getTestName() + "]" + " [data: " + getData().toString() + "]";
	}
	
	
	public long storyTestHashCodeNoID() {
		
		long hash = Long.MAX_VALUE;
		long mapHash = Long.MAX_VALUE;
		
		
		hash ^= getStoryName().hashCode();
		hash ^= getTestName().hashCode();
		
		
		for(StoryTestElement bste : getData()) {
			/*
			for(Map.Entry<String, String> entry : bste.getAttributes().entrySet()) {
				String keyValuePair = entry.getKey() + ":" + entry.getValue();
				mapHash ^= keyValuePair.hashCode();
			}*/
			
			for(String s : bste.getAttributes()) {
				mapHash ^= s.hashCode();
			}
		}
		
		hash ^= mapHash;
		
		return hash;
	}
	
	
	
	public long storyExistsInDB(List<StoryTest> existingStories) {
		
		if( (existingStories == null) || (existingStories.isEmpty()) ) {
			return -1L;
		}
		
		//String newBasicStoryTestString = this.toStringWithoutID();
		long newStoryTestHash = this.storyTestHashCodeNoID();
		
		for(StoryTest bst : existingStories) {
						
			long temp = bst.storyTestHashCodeNoID();
			
			if(newStoryTestHash == temp) {
				return bst.getStoryTestID();
			}
		}
		
		return -1L;
	}
	

	public List<StoryTestElement> getData() {
		return data;
	}

	public void setData(List<StoryTestElement> data) {
		this.data = data;
	} 
	
	public void addData(StoryTestElement dataElement) {
		data.add(dataElement);
		dataElement.setStoryTest(this);
	}
	
	public void removeData(StoryTestElement dataElement) {
		data.remove(dataElement);
		dataElement.setStoryTest(null);
	}
	
	public void clearData() {
		
		for(StoryTestElement ste : getData()) {
			ste.setStoryTest(null);
		}
		data.clear();
	}

	/*
	public Map<String, TestEntity> getMappings() {
		return mappings;
	}

	public void setMappings(Map<String, TestEntity> mappings) {
		this.mappings = mappings;
	}*/
	
	@Override
	public boolean equals(Object o) {
		
		if(this == o) {
			return true;
		}
		if(! (o instanceof StoryTest) ) {
			return false;
		}
		
		return (this.storyTestID != 0) && 
				(this.storyTestID == ((StoryTest) o).storyTestID);
	}
	
	/**
	 * Use this hashCode for consistency
	 */
	@Override
	public int hashCode() {
		return 1;
	}
}
