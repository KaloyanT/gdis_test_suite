package com.gdis.database.entities;

import java.util.Date;
//import javax.persistence.Basic;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;

//@Entity(name = "com_gdis_database_entities_Contract")
public class Contract {
	
	//@Id()
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	//@Basic(optional = false)
	private Customer policyOwner;
	
	//@Basic(optional = false)
	private Date contractBegin;
	
	private Date contractEnd;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Customer getPolicyOwner() {
		return policyOwner;
	}
	
	/*
	 * This method is taken from the Car insurance example. 
	 * The code makes sense only if a contract can change its owner. 
	 */
	public void setPolicyOwner(Customer newPolicyOwner) {
		if (policyOwner != newPolicyOwner) {
			if (policyOwner != null) {
				policyOwner.removeFromOwnedContracts(this);
			}
			policyOwner = newPolicyOwner;
			if (policyOwner != null) {
				policyOwner.addToOwnedContracts(this);
			}
		}
	}

	public Date getContractBegin() {
		return contractBegin;
	}

	public void setContractBegin(Date contractBegin) {
		this.contractBegin = contractBegin;
	}

	public Date getContractEnd() {
		return contractEnd;
	}

	public void setContractEnd(Date contractEnd) {
		this.contractEnd = contractEnd;
	}

	//@Override 
	public String toString() {
		return "Contract " + " [id: " + getId() + "]" + " [Contract Begin: " + getContractBegin().toString() + "]"
				+ " [Contract End: " + getContractEnd().toString() + "]";
	}
}
