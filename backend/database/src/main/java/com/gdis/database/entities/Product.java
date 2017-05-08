package com.gdis.database.entities;

//import javax.persistence.Basic;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

//@Entity(name = "com_gdis_database_entities_Product")
public class Product {

	//@Id()
	//@GeneratedValue(strategy = GenerationType.SEQUENCE) 
	private long id;
	
	//@Basic(optional = false)
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//@Override 
	public String toString() {
		return "Product " + " [id: " + getId() + "]" + " [name: " + getName() + "]";
	}
}
