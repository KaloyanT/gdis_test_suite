package com.gdis.database.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity(name = "NewContract")
public class NewContract {
	
	@Id
	private long id;
	
	@Basic(optional = false)
	private ProductType productType;
	
	@Basic(optional = false)
	@OneToOne
	private Customer customer;
	
	@Basic(optional = false)
	private Date contractBegin;
	
	@Basic(optional = false)
	private Date contractEnd;
	
	@Basic(optional = false)
	private double payment;
	
	private String partnerName;

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
	
	public Date getContractEnd() {
		return contractEnd;
	}

	public void setContractEnd(Date newContractEnd) {
		contractBegin = newContractEnd;
	}
	
	public double getPayment(){
		return payment;
	}
	
	public void setPayment(double newPayment){
		payment = newPayment;
	}
	
	public String getPartnerName(){
		return partnerName;
	}
	
	public void setPartnerName(String newPartnerName){
		partnerName = newPartnerName;
	}
	
	@Override
	public String toString() {
		return "ContractRequest " + " [id: " + getId() + "]" + " [productType: " + getProductType() + "]"
				+ " [contractBegin: " + getContractBegin() + "]";
	}
}
