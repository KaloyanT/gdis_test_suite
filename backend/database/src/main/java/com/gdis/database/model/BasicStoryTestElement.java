package com.gdis.database.model;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "BasicStoryTestElement")
@Table(name = "basicStoryTestElement")
public class BasicStoryTestElement {

	@Id
	@GenericGenerator(name = "basicStoryTestElementIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "basicStoryTestElementIdGenerator")
	@Column(name = "basicStoryTestElementID")
	private long basicStoryTestElementID;
	
	@ElementCollection
	@MapKeyColumn(name = "attributes_key")
	@Column(name = "value")
	@CollectionTable(name = "basicStoryTestElement_attributes", joinColumns = @JoinColumn(name = "basicStoryTestElementID"))
	@JoinTable(name = "basicStoryTestElement_attributes", joinColumns = @JoinColumn(name = "basicStoryTestElementID"))
	private Map<String, String> attributes = new HashMap<String, String>();
	
	// @OneToOne(cascade = CascadeType.ALL)
	//@ManyToOne(cascade = CascadeType.ALL)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "basic_story_test_basicStoryTestID")
	private BasicStoryTest basicStoryTest;
	
	@JsonIgnore
	public long getBasicStoryTestElementID() {
		return basicStoryTestElementID;
	}

	public void setBasicStoryTestElementID(long basicStoryTestElementID) {
		this.basicStoryTestElementID = basicStoryTestElementID;
	}
	
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@JsonIgnore
	public BasicStoryTest getBasicStoryTest() {
		return basicStoryTest;
	}

	public void setBasicStoryTest(BasicStoryTest basicStoryTest) {
		this.basicStoryTest = basicStoryTest;
	}
	
	/**
	 * Override the default equals method for consistency
	 */
	@Override
	public boolean equals(Object o) {
		
		if(this == o) {
			return true;
		}
		if(! (o instanceof BasicStoryTestElement) ) {
			return false;
		}
		
		return (this.basicStoryTestElementID != 0) && 
				(this.basicStoryTestElementID == ((BasicStoryTestElement) o).basicStoryTestElementID);
	}
	
	/**
	 * Use this hashCode for consistency
	 */
	@Override
	public int hashCode() {
		return 1;
	}
}
