package com.gdis.database.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "BasicStory")
@Table(name = "basicStories")
public class BasicStoryTest {
	
	@Id
	@GenericGenerator(name = "basicStoryTestIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "basicStoryTestIdGenerator")
	@Column(name = "storyTestID")
	private long storyTestID;
	
	// Use this ID to associate a story, like New Contract, with id
	//@JsonIgnore
	@Column(name = "storyID")
	private long storyID;
	
	@Basic(optional = false)
	private String storyName;
	
	@Basic(optional = false)
	private String testName;

	@ElementCollection
	@MapKeyColumn(name = "attributes_key")
	@Column(name = "value")
	@CollectionTable(name = "basicStory_attributes", joinColumns = @JoinColumn(name = "storyTestID"))
	@JoinTable(name = "basicStory_attributes", joinColumns = @JoinColumn(name = "storyTestID"))
	private Map<String, String> attributes = new HashMap<String, String>();
	 
	
	public long getStoryTestID() {
		return storyTestID;
	}

	public void setStoryTestID(long storyTestID) {
		this.storyTestID = storyTestID;
	}
	
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

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}


	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	
	@Override
	public String toString() {
		
		return "ContractRequest " + " [BasicStoryTestID: " + getStoryTestID() + "]" 
				+ " [storyName: " + getStoryName() + "]" + " [testName: " + getTestName() + "]"
				+ " [attributes: " + getAttributes().toString() + "]";
	}
	
	
	public String toStringWithoutID() {
		
		return "ContractRequest " + " [storyName: " + getStoryName() + "]" 
				+ " [testName: " + getTestName() + "]" + " [attributes: " + getAttributes().toString() + "]";
	}
	
	
	/*
	public long storyExistsInDB(List<Story> existingStories) {
		
		if( (existingStories == null) || (existingStories.isEmpty()) ) {
			return -1L;
		}
		
		String newStoryString = this.toStringWithoutID();
		
		for(Story s : existingStories) {
						
			String temp = s.toStringWithoutID();
			
			if(newStoryString.equals(temp)) {
				return s.getStoryID();
			}
		}
		
		return -1L;
	} */
	
	/*
	private void formatDates() {
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		
		Map<String, Object> hashMap = this.getAttributes();
		
		for(Map.Entry<String, Object> entry : hashMap.entrySet()) {
			
			if(entry.getValue().getClass().isInstance(Date.class)) {
				
				String temp = dateFormatter.format(entry.getValue());
				Date newDate = null;
				
				try {
					newDate = dateFormatter.parse(temp);
				} catch (ParseException e) {
					
				}
				
				if(newDate != null) {
					hashMap.replace(entry.getKey(), newDate);
				}
			}
			
		}
		
		setAttributes(hashMap);
	}*/

}
