package com.gdis.database.model;

import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
	// Saves the Date as dd/MM/yyyy in the DB instead of dd/MM/yyyy 01:00:00, but
	// sets the Date one day behind the actual one, because it cuts the time
	// @Type(type = "date") // hibernate annotation
	private Date contractBegin = null;
	
	@Basic(optional = false)
	private double monthlyPremium;
	
	private double changedMonthlyPremium;

	@Basic(optional = false)
	private String testName;
	
	public long getId() {
		return id;
	}

	public void setId(long newId) {
		this.id = newId;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType newProductType) {
		this.productType = newProductType;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer newCustomer) {
		this.customer = newCustomer;
	}

	public Date getContractBegin() {
		return contractBegin;
	}

	public void setContractBegin(Date newContractBegin) {
		this.contractBegin = newContractBegin;
	}
	
	public double getMonthlyPremium(){
		return monthlyPremium;
	}
	
	public void setMonthlyPremium(double monthlyPremium){
		this.monthlyPremium = monthlyPremium;
	}
	
	public double getChangedMonthlyPremium(){
		return changedMonthlyPremium;
	}
	
	public void setChangedMonthlyPremium(double changedMonthlyPremium){
		this.changedMonthlyPremium = changedMonthlyPremium;
	}
	
	public String getTestName(){
		return testName;
	}
	
	public void setTestNameName(String newTestName){
		this.testName = newTestName;
	}
	
	@Override
	public String toString() {
		return "ContractRequest " + " [id: " + getId() + "]" + " [productType: " + getProductType() + "]"
				+ " [contractBegin: " + getContractBegin() + "]";
	}
}
