package com.gdis.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity(name = "Customer")
@Table(name = "customers")
public class Customer {
	
	@Id
	@GenericGenerator(name = "customerIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "customerIdGenerator")
	private long customerID;
	
	@Basic(optional = false)
	private String firstName;
	
	@Basic(optional = false)
	private String lastName;
	
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Type(type = "date") // hibernate annotation
	private Date birthday;
	
	private String address;
	
	private String job;
	
	//@ElementCollection(targetClass = Contract.class)
	@OneToMany
	private List<Contract> ownedContracts = new ArrayList<Contract>();
	
	public long getCustomerID() {
		return customerID;
	}

	public void setCustomerID(long customerID) {
		this.customerID = customerID;
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
	
	
	@Override
	public String toString() {
		return "Customer " + " [id: " + getCustomerID() + "]" + " [firstName: " + getFirstName() + "]" + " [lastName: "
				+ getLastName() + "]" + " [birthday: " + getBirthday() + "]" + " [address: " + getAddress() + "]";
	}
	
}
