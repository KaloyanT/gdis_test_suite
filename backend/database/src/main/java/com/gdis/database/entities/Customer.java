package com.gdis.database.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import javax.persistence.Basic;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;


//@Entity(name = "com_gdis_database_entities_Customer")
public class Customer {
	
	//@Id()
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	//@Basic(optional = false)
	private String firstName;
	
	//@Basic(optional = false)
	private String lastName;
	
	//@Basic(optional = false)
	private Date birthday;
	
	//@Basic(optional = false)
	private String address;
	
	private List<Contract> ownedContracts = new ArrayList<Contract>();
	
	//private List<Contract> activeContracts = new ArrayList<Contract>();
	
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
