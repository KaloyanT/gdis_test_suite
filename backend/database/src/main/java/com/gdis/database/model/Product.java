package com.gdis.database.model;

//import java.util.ArrayList;
import java.util.Date;
//import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "Product")
public class Product {
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Basic(optional = false)
	private String name;
	
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date productBegin;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date productEnd;
	
	//private List<Contract> contracts = new ArrayList<Contract>();
	
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
	
	/*
	public List<Contract> getContracts() {
		return contracts;
	}

	public boolean addToContracts(Contract contractsValue) {
		if (!contracts.contains(contractsValue)) {
			boolean result = contracts.add(contractsValue);
			contractsValue.setProduct(this);
			return result;
		}
		return false;
	}

	public boolean removeFromContracts(Contract contractsValue) {
		if (contracts.contains(contractsValue)) {
			boolean result = contracts.remove(contractsValue);
			contractsValue.setProduct(null);
			return result;
		}
		return false;
	}

	public void clearContracts() {
		while (!contracts.isEmpty()) {
			removeFromContracts(contracts.iterator().next());
		}
	}

	public void setContracts(List<Contract> newContracts) {
		clearContracts();
		for (Contract value : newContracts) {
			addToContracts(value);
		}
	}
	*/
	
	@Override 
	public String toString() {
		return "Product " + " [id: " + getId() + "]" + " [name: " + getName() + "]" +
	       " [Product Begin: "  + getProductBegin().toString() + "]"
				+ " [Contract End: " + getProductEnd().toString() + "]";
	}
}
