package com.gdis.database.models;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "NewContract")
public class NewContract {
	
	@Id
	private long id;
	
	@Basic(optional = false)
	private ProductType productType;
	
	@Basic(optional = false)
	private Customer customer;
	
	@Basic(optional = false)
	private Date contractBegin;

	public long getId() {
		return id;
	}

	public void setId(long newId) {
		id = newId;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType newProductType) {
		productType = newProductType;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer newCustomer) {
		customer = newCustomer;
	}

	public Date getContractBegin() {
		return contractBegin;
	}

	public void setContractBegin(Date newContractBegin) {
		contractBegin = newContractBegin;
	}
	
	@Override
	public String toString() {
		return "ContractRequest " + " [id: " + getId() + "]" + " [productType: " + getProductType() + "]"
				+ " [contractBegin: " + getContractBegin() + "]";
	}
}
