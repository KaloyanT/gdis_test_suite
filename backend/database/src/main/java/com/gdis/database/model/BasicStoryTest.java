package com.gdis.database.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "BasicStoryTest")
@Table(name = "basicStoryTest")
public class BasicStoryTest {
	
	@Id
	@GenericGenerator(name = "basicStoryTestIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "basicStoryTestIdGenerator")
	@Column(name = "basicStoryTestID")
	private long basicStoryTestID;
	
	// Use this ID to associate a story, like New Contract, with id
	// @JsonIgnore
	// @Column(name = "storyID")
	// private long storyID;
	
	@Basic(optional = false)
	private String storyName;
	
	@Basic(optional = false)
	private String testName;

	/*
	@ElementCollection
	@MapKeyColumn(name = "attributes_key")
	@Column(name = "value")
	@CollectionTable(name = "basicStoryTest_attributes", joinColumns = @JoinColumn(name = "basicStoryTestID"))
	@JoinTable(name = "basicStoryTest_attributes", joinColumns = @JoinColumn(name = "basicStoryTestID"))
	private Map<String, String> attributes = new HashMap<String, String>();
	*/
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<BasicStoryTestElement> data = new ArrayList<BasicStoryTestElement>();
	
	
	public long getBasicStoryTestID() {
		return basicStoryTestID;
	}

	public void setBasicStoryTestID(long basicStoryTestID) {
		this.basicStoryTestID = basicStoryTestID;
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

	/*
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	*/
	
	@Override
	public String toString() {
		
		return "ContractRequest " + " [BasicStoryTestID: " + getBasicStoryTestID() + "]" 
				+ " [storyName: " + getStoryName() + "]" + " [testName: " + getTestName() + "]" 
				 + " [data: " + getData().toString() + "]";
	}
	
	
	public String toStringWithoutID() {
		
		return "ContractRequest " + " [storyName: " + getStoryName() + "]" 
				+ " [testName: " + getTestName() + "]" + " [data: " + getData().toString() + "]";
	}
	
	
	public long basicStoryTestHashCodeNoID() {
		
		long hash = Long.MAX_VALUE;
		long mapHash = Long.MAX_VALUE;
		
		
		hash ^= getStoryName().hashCode();
		hash ^= getTestName().hashCode();
		
		
		for(BasicStoryTestElement bste : getData()) {
			
			for(Map.Entry<String, String> entry : bste.getAttributes().entrySet()) {
				String keyValuePair = entry.getKey() + ":" + entry.getValue();
				mapHash ^= keyValuePair.hashCode();
			}
		}
		
		/*
		for(Map.Entry<String, String> entry : getAttributes().entrySet()) {
			String keyValuePair = entry.getKey() + ":" + entry.getValue();
			mapHash += keyValuePair.hashCode();
		}
		*/
		
		hash ^= mapHash;
		
		return hash;
	}
	
	
	
	public long storyExistsInDB(List<BasicStoryTest> existingBasicStories) {
		
		if( (existingBasicStories == null) || (existingBasicStories.isEmpty()) ) {
			return -1L;
		}
		
		//String newBasicStoryTestString = this.toStringWithoutID();
		long newBasicStoryTestHash = this.basicStoryTestHashCodeNoID();
		
		for(BasicStoryTest bst : existingBasicStories) {
						
			long temp = bst.basicStoryTestHashCodeNoID();
			
			if(newBasicStoryTestHash == temp) {
				return bst.getBasicStoryTestID();
			}
		}
		
		return -1L;
	}
	

	public List<BasicStoryTestElement> getData() {
		return data;
	}

	public void setData(List<BasicStoryTestElement> data) {
		this.data = data;
	} 
	
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
