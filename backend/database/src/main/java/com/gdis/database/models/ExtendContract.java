package com.gdis.database.models;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "ExtendContract")
public class ExtendContract {
	
	@Id
	private long id;
	
	@Basic(optional = false)
	private ProductType productType = null;
	
	@Basic(optional = false)
	private Customer customer = null;
	
	@Basic(optional = false)
	private Date contractBegin = null;

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
