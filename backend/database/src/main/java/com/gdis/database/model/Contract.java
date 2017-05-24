package com.gdis.database.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Contract")
@Table(name = "contracts")
public class Contract {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GenericGenerator(name = "contractIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "contractIdGenerator")
	private long contractID;

	@Basic(optional = false)
	@OneToOne(cascade = {CascadeType.ALL})
	private Customer policyOwner;
	
	@Basic(optional = false)
	@OneToOne(cascade = {CascadeType.ALL})
	private Customer insuredPerson;
	
	@Basic(optional = false)
	@OneToOne(cascade = {CascadeType.ALL})
	private Product product;

	@Basic(optional = false)
	private double monthlyPremium;
	
	public long getContractID() {
		return contractID;
	}

	public void setContractID(long contractID) {
		this.contractID = contractID;
	}
	
	
	public Customer getPolicyOwner() {
		return policyOwner;
	}
	
	
	public void setPolicyOwner(Customer newPolicyOwner) {
		this.policyOwner = newPolicyOwner;
	}
	
	public Customer getInsuredPerson() {
		return insuredPerson;
	}
	
	public void setInsuredPerson(Customer newInsuredPerson) {
		this.insuredPerson = newInsuredPerson;
	}

	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getMonthlyPremium() {
		return monthlyPremium;
	}

	public void setMonthlyPremium(double monthlyPremium) {
		this.monthlyPremium = monthlyPremium;
	}
	
	
	@Override 
	public String toString() {
		return "Contract " + " [id: " + getContractID() + "]";
	}
	
}
