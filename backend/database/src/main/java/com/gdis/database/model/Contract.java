package com.gdis.database.model;

import java.util.List;
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
		return "Contract " + " [id: " + getContractID() + "]" + " [insuredPerson: "
				+ getInsuredPerson().toString() + "]" + " [product: " + getProduct().toString() + "]" 
				+ " [monthlyPremium: " + getMonthlyPremium() + "]";
	}
	
	public String toStringWithoutID() {
				
		return "Contract " + " [policyOwner: " + getPolicyOwner().toString() + "]" + " [insuredPerson: "
				+ getInsuredPerson().toString() + "]" + " [product: " + getProduct().toString() + "]"
				+ " [monthlyPremium: " + getMonthlyPremium() + "]";
	}
	
	/**
	 * Checks if the THIS Contract exists in the Database by searching in a Contracts 
	 * List with the same policyOwner, insuredPerson and Product
	 * @param existingContracts List with contracts which have the same policyOwner, 
	 * insuredPerson and Product
	 * @return The contractID if the Contract is contained in the existingContracts List, -1L else
	 */
	public long contractExistsInDB(List<Contract> existingContracts) {
		
		if( (existingContracts == null) || (existingContracts.isEmpty()) ) {
			return -1L;
		}
		
		String newContractString = this.toStringWithoutID();
				
		for(Contract c : existingContracts) {
						
			String temp = c.toStringWithoutID();
			
			if(newContractString.equals(temp)) {
				return c.getContractID();
			}
		}
		
		return -1L;
	}
	
}
