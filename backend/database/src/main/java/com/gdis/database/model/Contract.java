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

	
	public long getContractID() {
		return contractID;
	}

	public void setContractID(long contractID) {
		this.contractID = contractID;
	}
	
	
	public Customer getPolicyOwner() {
		return policyOwner;
	}
	
	
	// This method is taken from the Car insurance example. 
	// The code makes sense only if a contract can change its owner. 
	
	public void setPolicyOwner(Customer newPolicyOwner) {
		/* if (policyOwner != newPolicyOwner) {
			if (policyOwner != null) {
				policyOwner.removeFromOwnedContracts(this);
			}
			policyOwner = newPolicyOwner;
			if (policyOwner != null) {
				policyOwner.addToOwnedContracts(this);
			}
		} */ 
		this.policyOwner = newPolicyOwner;
	}
	
	public Customer getInsuredPerson() {
		return insuredPerson;
	}
	
	public void setInsuredPerson(Customer newInsuredPerson) {
		/* if (insuredPerson != newInsuredPerson) {
			if (insuredPerson != null) {
				insuredPerson.removeFromInsuredBy(this);
			}
			insuredPerson = newInsuredPerson;
			if (insuredPerson != null) {
				insuredPerson.addToInsuredBy(this);
			}
		} */ 
		this.insuredPerson = newInsuredPerson;
	}

	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		
		this.product = product;
		
		/*if (product != newProduct) {
			if (product != null) {
				product.removeFromContracts(this);
			}
			product = newProduct;
			if (product != null) {
				product.addToContracts(this);
			}
		}*/
		
	}

	@Override 
	public String toString() {
		return "Contract " + " [id: " + getContractID() + "]";
	}
	
	
}
