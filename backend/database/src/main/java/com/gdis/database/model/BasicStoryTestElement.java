package com.gdis.database.model;

import java.util.HashMap;
import java.util.Map;

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
	
	
	@OneToOne(cascade = CascadeType.ALL)
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
}
