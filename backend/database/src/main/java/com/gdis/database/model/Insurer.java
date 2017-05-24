package com.gdis.database.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Insurer")
@Table(name = "insurers")
public class Insurer {

	@Id
	@GenericGenerator(name = "insurerIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "insurerIdGenerator")
	private long insurerID;
	
	@Basic(optional = false)
	private String name;
	
	@OneToMany
	private List<Product> offeredProducts = new ArrayList<Product>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public long getInsurerID() {
		return insurerID;
	}

	public void setInsurerID(long insurerID) {
		this.insurerID = insurerID;
	}
	
	
	
	public List<Product> getOfferedProducts() {
		return offeredProducts;
	}

	public boolean addToOfferedProducts(Product offeredProductValue) {
		if (!offeredProducts.contains(offeredProductValue)) {
			boolean result = offeredProducts.add(offeredProductValue);
			return result;
		}
		return false;
	}

	public boolean removeFromOfferedProducts(Product offeredProductValue) {
		if (offeredProducts.contains(offeredProductValue)) {
			boolean result = offeredProducts.remove(offeredProductValue);
			return result;
		}
		return false;
	}

	public void clearOfferedProducts() {
		while (!offeredProducts.isEmpty()) {
			removeFromOfferedProducts(offeredProducts.iterator().next());
		}
	}

	public void setOfferedProducts(List<Product> newOfferedProducts) {
		clearOfferedProducts();
		for (Product value : newOfferedProducts) {
			addToOfferedProducts(value);
		}
	}
	
}
