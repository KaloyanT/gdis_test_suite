package com.gdis.database.model;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "NewContract")
@Table(name = "new_contract")
public class NewContract {
	
	@Id
	@GenericGenerator(name = "newContractIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "newContractIdGenerator")
	private long newContractID;
	
	@Basic(optional = false)
	@OneToOne(cascade = {CascadeType.ALL})
	private Customer customer;
	
	@Basic(optional = false)
	private ProductType productType;
	
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	//@Type(type = "date")
	private Date contractBegin;
	
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date contractEnd;

	@Basic(optional = false)
	private String partnerName;

	@Basic(optional = false)
	private double monthlyPremium;
	
	@Basic(optional = false)
	private String testName;

	public long getNewContractID() {
		return newContractID;
	}

	public void setNewContractID(long newContractID) {
		this.newContractID = newContractID;
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
	
	public Date getContractEnd() {
		return contractEnd;
	}

	public void setContractEnd(Date newContractEnd) {
		this.contractEnd = newContractEnd;
	}
	
	public double getMonthlyPremium(){
		return monthlyPremium;
	}
	
	public void setMonthlyPremium(double monthlyPremium){
		this.monthlyPremium = monthlyPremium;
	}
	
	public String getTestName(){
		return testName;
	}
	
	public void setTestName(String newTestName){
		this.testName = newTestName;
	}
	
	
	public String getPartnerName(){
		return partnerName;
	}
	
	public void setPartnerName(String newPartnerName){
		partnerName = newPartnerName;
	}
	
	@Override
	public String toString() {
		return "ContractRequest " + " [id: " + getNewContractID() + "]" + " [Customer: " + getCustomer().toString() + "]"
				+ " [productType: " + getProductType() + "]"+ " [contractBegin: " + getContractBegin() + "]" 
				+ " [contractEnd: " + getContractEnd() + "]" + "[testName: " + getTestName() + "]" + 
				" [monthlyPremium: " + getMonthlyPremium() + "]";
	}
	
	public String toStringWithoutID() {
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String begin = dateFormatter.format(getContractBegin());
		String end = dateFormatter.format(getContractEnd());
		
		return "ContractRequest " + " [Customer: " + getCustomer().toString() + "]"
				+ " [productType: " + getProductType() + "]"+ " [contractBegin: " + begin + "]" 
				+ " [contractEnd: " + end + "]" + "[testName: " + getTestName() + "]" 
				+ " [monthlyPremium: " + getMonthlyPremium() + "]";
	}
	
	public long newContractExistsInDB(List<NewContract> existingNewContracts) {
		
		if( (existingNewContracts == null) || (existingNewContracts.isEmpty()) ) {
			return -1L;
		}
		
		String newContractString = this.toStringWithoutID();
		
		for(NewContract nc : existingNewContracts) {
						
			String temp = nc.toStringWithoutID();
			
			if(newContractString.equals(temp)) {
				return nc.getNewContractID();
			}
		}
		
		return -1L;
	}
}
