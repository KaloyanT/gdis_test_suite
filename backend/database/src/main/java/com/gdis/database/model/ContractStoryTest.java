package com.gdis.database.model;


import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity(name = "ContractStory")
@Table(name = "contractStories")
public class ContractStoryTest extends BasicStoryTest {
	
	
	@Basic(optional = false)
	@OneToOne(cascade = {CascadeType.ALL})
	private Contract contract;
	
	
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	
	@Override
	public String toString() {
		
		return "ContractRequest " + " [BasicStoryTestID: " + getStoryTestID() + "]"
				+ " [contract: " + getContract().toString() + " ]"
				+ " [storyName: " + getStoryName() + "]" + " [testName: " + getTestName() + "]"
				+ " [attributes: " + getAttributes().toString() + "]";
	}
	
	@Override
	public String toStringWithoutID() {
		
		return "ContractRequest " + " [contract: " + getContract().toString() + " ]"
				+ " [storyName: " + getStoryName() + "]" + " [testName: " + getTestName() + "]"
				+ " [attributes: " + getAttributes().toString() + "]";
	}

}
