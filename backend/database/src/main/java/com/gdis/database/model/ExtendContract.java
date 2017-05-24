package com.gdis.database.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "ExtendContract")
@Table(name = "extend_contract")
public class ExtendContract {
	
	@Id
	private long id;
	
	@Basic(optional = false)
	private ProductType productType = null;
	
	@Basic(optional = false)
	@OneToOne
	private Customer customer = null;
	
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Type(type = "date")
	private Date contractBegin = null;
	
	@Basic(optional = false)
	private double payment;
	
	private double changedPayment;

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
	
	public double getPayment(){
		return payment;
	}
	
	public void setPayment(double newPayment){
		payment = newPayment;
	} 
	
	public double getChangedPayment(){
		return changedPayment;
	}
	
	public void setChangedPayment(double newChangedPayment){
		changedPayment = newChangedPayment;
	}
	
	@Override
	public String toString() {
		return "ContractRequest " + " [id: " + getId() + "]" + " [productType: " + getProductType() + "]"
				+ " [contractBegin: " + getContractBegin() + "]";
	}
}
