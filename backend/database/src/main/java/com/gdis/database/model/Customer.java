package com.gdis.database.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	
	// Saves the Date as dd/MM/yyyy in the DB instead of dd/MM/yyyy 01:00:00, but
	// sets the Date one day behind the actual one, because it cuts the time
	// @Type(type = "date") // hibernate annotation
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private Date birthday;
	
	@Basic(optional = false)
	private String address;
	
	@Basic(optional = false)
	private String job;
	
	//@ElementCollection(targetClass = Contract.class)
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
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

	@JsonIgnore
	public List<Contract> getOwnedContracts() {
		return ownedContracts;
	}

	
	@JsonProperty
	public void setOwnedContracts(List<Contract> newOwnedContracts) {
		clearOwnedContracts();
		for (Contract value : newOwnedContracts) {
			addToOwnedContracts(value);
		}
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


	@Override
	public String toString() {
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String birthday = dateFormatter.format(getBirthday());
		
		return "Customer " + " [id: " + getCustomerID() + "]" + " [firstName: " 
		+ getFirstName() + "]" + " [lastName: " + getLastName() + "]"  
		+ " [birthday: " + birthday + "]" + " [address: " + getAddress() + "]"  
		+ " [job: " + getJob() + "]" ; // + " [ownedContracts: " + getOwnedContracts().toString() + "]";
	}
	
	public String toStringWithoutID() {
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String birthday = dateFormatter.format(getBirthday());
		
		return "Customer " + " [firstName: " + getFirstName() + "]" + " [lastName: "
				+ getLastName() + "]" + " [birthday: " + birthday + "]" + " [address: " + getAddress() + "]" + 
				" [job: " + getJob() + "]" + " ]"; //" [ownedContracts: " + getOwnedContracts().toString() + "]";
	}
	
	public long customerHashCodeNoID() {
		
		long res = 0L;
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String birthday = dateFormatter.format(getBirthday());
		
		res += getFirstName().toString().hashCode();
		res += getLastName().toString().hashCode();
		res += birthday.hashCode();
		res += getAddress().toString().hashCode();
		res += getJob().toString().hashCode();
		res += getOwnedContracts().toString().hashCode();
		
		return res;
	}
	
	
	/**
	 * Checks if the THIS Customer exists in the Database by searching in a Customers 
	 * List with the same lastName and birthday
	 * @param existingCustomers List with customers who have the same lastName and were born
	 * on the same date (same birthday)
	 * @return The customerID if the Customer is contained in the existingCustomers List, -1L else
	 */
	public long customerExistsInDB(List<Customer> existingCustomers) {
		
		if( (existingCustomers == null) || (existingCustomers.isEmpty()) ) {
			return -1L;
		}
		
		String newCustomerString = this.toStringWithoutID();
				
		for(Customer c : existingCustomers) {
						
			String temp = c.toStringWithoutID();
			
			if(newCustomerString.equals(temp)) {
				return c.getCustomerID();
			}
		}
		
		return -1L;
	}
}
