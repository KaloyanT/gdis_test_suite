package com.gdis.database.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
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

@Table(name = "TestObject")
@Entity(name = "testObject")
public class TestObject {
	
	@Id
	@GenericGenerator(name = "testObjectIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "testObjectIdGenerator")
	@Column(name = "testObjectID")
	private long testObjectID;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_entity_testEntityID")
	private TestEntity entityType;
	
	private long hashCodeNoID;

	@ElementCollection
	@MapKeyColumn(name = "objectAttributes_key")
	@Column(name = "value")
	@CollectionTable(name = "testObject_attributes", joinColumns = @JoinColumn(name = "test_object_testObjectID"))
	@JoinTable(name = "testObject_attributes", joinColumns = @JoinColumn(name = "test_object_testObjectID"))
	private Map<String, String> objectAttributes = new HashMap<String, String>();
	
	
	public long getTestObjectID() {
		return testObjectID;
	}

	public void setTestObjectID(long testObjectID) {
		this.testObjectID = testObjectID;
	}

	public TestEntity getEntityType() {
		return entityType;
	}

	public void setEntityType(TestEntity entityType) {
		this.entityType = entityType;
	}

	public Map<String, String> getObjectAttributes() {
		return objectAttributes;
	}

	public void setObjectAttributes(Map<String, String> objectAttributes) {
		this.objectAttributes = objectAttributes;
	}

	@Override
	public boolean equals(Object o) {
		
		if(this == o) {
			return true;
		}
		if(! (o instanceof TestObject) ) {
			return false;
		}
		
		return (this.testObjectID != 0) && 
				(this.testObjectID == ((TestObject) o).testObjectID);
	}
	
	/**
	 * Use this hashCode for consistency
	 */
	@Override
	public int hashCode() {
		return 1;
	}
	
	/*
	 * Use this hash code to see if there is another object in the database
	 * which has exactly the same attributes and exactly the same values
	 */
	public long hashCodeNoID() {
		
		long hashCode = Long.MAX_VALUE;
		long mapHash = Long.MAX_VALUE;
		
		// Sort the Map by using a TreeMap
		// This is done in order to insure that the hashCode for every object is the same
		Map<String, String> sortedMap = new TreeMap<String, String>(getObjectAttributes());
		
		hashCode ^= getEntityType().getEntityName().hashCode();
		
		for(Map.Entry<String, String> entry : sortedMap.entrySet()) {
			
			String keyValuePair = entry.getKey() + ": " + entry.getValue();
			mapHash ^= keyValuePair.hashCode();
		}
		
		hashCode ^= mapHash;
		
		return hashCode;
	}

	public long getHashCodeNoID() {
		return hashCodeNoID;
	}

	public void setHashCodeNoID(long hashCodeNoID) {
		this.hashCodeNoID = hashCodeNoID;
	}
}
