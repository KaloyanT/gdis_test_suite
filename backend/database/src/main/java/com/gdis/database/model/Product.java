package com.gdis.database.model;


import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "Product")
@Table(name = "products")
public class Product {
	

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@GenericGenerator(name = "customerIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "customerIdGenerator")
	private long id;
	
	@Basic(optional = false)
	private String name;
	
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Type(type = "date")
	private Date productBegin;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Type(type = "date")
	private Date productEnd;

	@Basic(optional = false)
	private ProductType productType;
	
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
	
	public Date getProductBegin() {
		return productBegin;
	}

	public void setProductBegin(Date contractBegin) {
		this.productBegin = contractBegin;
	}

	public Date getProductEnd() {
		return productEnd;
	}

	public void setProductEnd(Date contractEnd) {
		this.productEnd = contractEnd;
	}
	
	
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	@Override 
	public String toString() {
		return "Product " + " [id: " + getId() + "]" + " [name: " + getName() + "]" +
	       " [Product Begin: "  + getProductBegin().toString() + "]"
				+ " [Contract End: " + getProductEnd().toString() + "]";
	}

}
