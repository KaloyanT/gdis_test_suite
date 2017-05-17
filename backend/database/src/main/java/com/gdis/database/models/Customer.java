package com.gdis.database.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Customer {
	private long id;
	private String firstName;
	private String lastName;
	private Date birthday;
	private String address;
	private String job;
	private List<Contract> insuredBy = new ArrayList<Contract>();
	private List<Contract> ownedContracts = new ArrayList<Contract>();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getJob(){ 
		return job;
	}
	
	public void setJob(String job) {
		this.job = job;
	}

	public List<Contract> getInsuredBy() {
		return insuredBy;
	}

	public boolean addToInsuredBy(Contract insuredByValue) {
		if (!insuredBy.contains(insuredByValue)) {
			boolean result = insuredBy.add(insuredByValue);
			insuredByValue.setInsuredPerson(this);
			return result;
		}
		return false;
	}

	public boolean removeFromInsuredBy(Contract insuredByValue) {
		if (insuredBy.contains(insuredByValue)) {
			boolean result = insuredBy.remove(insuredByValue);
			insuredByValue.setInsuredPerson(null);
			return result;
		}
		return false;
	}

	public void clearInsuredBy() {
		while (!insuredBy.isEmpty()) {
			removeFromInsuredBy(insuredBy.iterator().next());
		}
	}

	public void setInsuredBy(List<Contract> newInsuredBy) {
		clearInsuredBy();
		for (Contract value : newInsuredBy) {
			addToInsuredBy(value);
		}
	}
	public List<Contract> getOwnedContracts() {
		return ownedContracts;
	}

	public boolean addToOwnedContracts(Contract ownedContractsValue) {
		if (!ownedContracts.contains(ownedContractsValue)) {
			boolean result = ownedContracts.add(ownedContractsValue);
			ownedContractsValue.setPolicyOwner(this);
			return result;
		}
		return false;
	}

	public boolean removeFromOwnedContracts(Contract ownedContractsValue) {
		if (ownedContracts.contains(ownedContractsValue)) {
			boolean result = ownedContracts.remove(ownedContractsValue);
			ownedContractsValue.setPolicyOwner(null);
			return result;
		}
		return false;
	}

	public void clearOwnedContracts() {
		while (!ownedContracts.isEmpty()) {
			removeFromOwnedContracts(ownedContracts.iterator().next());
		}
	}

	public void setOwnedContracts(List<Contract> newOwnedContracts) {
		clearOwnedContracts();
		for (Contract value : newOwnedContracts) {
			addToOwnedContracts(value);
		}
	}
	
	//@Override
	public String toString() {
		return "Customer " + " [id: " + getId() + "]" + " [firstName: " + getFirstName() + "]" + " [lastName: "
				+ getLastName() + "]" + " [birthday: " + getBirthday() + "]" + " [address: " + getAddress() + "]";
	}
	
}
