package com.gdis.database.model;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "Product")
@Table(name = "products")
public class Product {
	

	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@GenericGenerator(name = "customerIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "customerIdGenerator")
	private long productID;
	
	@Basic(optional = false)
	private String name;
	
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	// Saves the Date as dd/MM/yyyy in the DB instead of dd/MM/yyyy 01:00:00, but
	// sets the Date one day behind the actual one, because it cuts the time
	// @Type(type = "date") // hibernate annotation
	private Date productBegin;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	// Saves the Date as dd/MM/yyyy in the DB instead of dd/MM/yyyy 01:00:00, but
	// sets the Date one day behind the actual one, because it cuts the time
	// @Type(type = "date") // hibernate annotation
	private Date productEnd;

	@Basic(optional = false)
	private ProductType productType;
	
	public long getProductID() {
		return productID;
	}

	public void setProductID(long productID) {
		this.productID = productID;
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
		return "Product " + " [id: " + getProductID() + "]" + " [name: " + getName() + "]" +
	       " [Product Begin: "  + getProductBegin().toString() + "]"
				+ " [Contract End: " + getProductEnd().toString() + "]" 
	       + " [Product Type: " + getProductType() + "]";
	}
	 
	public String toStringWithoutID() {
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String begin = dateFormatter.format(getProductBegin());
		String end = dateFormatter.format(getProductEnd());
		
		return "Product " + " [name: " + getName() + "]" +
	       " [Begin: "  + begin + "]"
				+ " [End: " + end + "]" 
	       + " [Product Type: " + getProductType() + "]";
	}
	
	/**
	 * Checks if the THIS Product exists in the Database by searching in a Products List
	 * filtered by the controller
	 * @param existingProducts List with products which have the same name and product type
	 * @return The productID if the Product is contained in the existingProducts List, -1L else
	 */
	public long productExistsInDB(List<Product> existingProducts) {
		
		String newCustomerString = this.toStringWithoutID();
				
		for(Product p : existingProducts) {
						
			String temp = p.toStringWithoutID();
			
			if(newCustomerString.equals(temp)) {
				return p.getProductID();
			}
		}
		
		return -1L;
	}

}
