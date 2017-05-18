package com.gdis.database.models;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Contract")
public class Contract {
	
	@Id
	private long id;
	
	@Basic(optional = false)
	private Customer policyOwner;
	
	@Basic(optional = false)
	private Customer insuredPerson;
	
	@Basic(optional = false)
	private Product product;

	
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
	
	public Customer getInsuredPerson() {
		return insuredPerson;
	}
	
	public void setInsuredPerson(Customer newInsuredPerson) {
		if (insuredPerson != newInsuredPerson) {
			if (insuredPerson != null) {
				insuredPerson.removeFromInsuredBy(this);
			}
			insuredPerson = newInsuredPerson;
			if (insuredPerson != null) {
				insuredPerson.addToInsuredBy(this);
			}
		}
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product newProduct) {
		if (product != newProduct) {
			if (product != null) {
				product.removeFromContracts(this);
			}
			product = newProduct;
			if (product != null) {
				product.addToContracts(this);
			}
		}
	}	

	//@Override 
	public String toString() {
		return "Contract " + " [id: " + getId() + "]";
	}
}
