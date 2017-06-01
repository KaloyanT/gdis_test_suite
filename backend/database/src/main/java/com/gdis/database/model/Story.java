package com.gdis.database.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Story")
@Table(name = "stories")
public class Story {
	
	@Id
	@GenericGenerator(name = "storyIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "storyIdGenerator")
	@Column(name = "storyID")
	private long storyID;
	
	@Basic(optional = false)
	private String testName;
	
	@Basic(optional = false)
	@OneToOne(cascade = {CascadeType.ALL})
	private Contract contract;
	
	@ElementCollection
	@MapKeyColumn(name = "attributes_key")
	@Column(name = "value")
	@CollectionTable(name = "story_attributes", joinColumns = @JoinColumn(name = "storyID"))
	@JoinTable(name = "story_attributes", joinColumns = @JoinColumn(name = "storyID"))
	private Map<String, String> attributes = new HashMap<String, String>();
	 
	public long getStoryID() {
		return storyID;
	}

	public void setStoryID(long storyID) {
		this.storyID = storyID;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	/*
	@Override
	public String toString() {
		
		//SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		//String newEndDate = dateFormatter.format(getNewEndDate());
		
		return "ContractRequest " + " [ID: " + getStoryID() + "]" + " [ Contract: " + getContract() + "]"
				+ " [testName: ]" + getTestName() + "]" + " [attributes: " + getAttributes().toString() + "]";
	}
	
	public String toStringWithoutID() {
	
		// SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		// String newEndDate = dateFormatter.format(getNewEndDate());
		
		return "ContractRequest " + " [ Contract: " + getContract() + "]" + " [testName: ]" + getTestName() + "]" 
				+ " [attributes: " + getAttributes().toString() + "]";
	}
	
	
	
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
