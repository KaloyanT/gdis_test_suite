package com.gdis.database.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "ModifyContract")
@Table(name = "modify_contract")
public class ModifyContract {
	
	@Id
	@GenericGenerator(name = "modifiedContractIdGenerator", strategy = "increment")
	@GeneratedValue(generator = "modifiedContractIdGenerator")
	private long modifiedContractID;
	
	@Basic(optional = false)
	@OneToOne(cascade = {CascadeType.ALL})
	private Contract contract;
	
	@Basic(optional = false)
	private String testName;
	
	@Basic(optional = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	// Saves the Date as dd/MM/yyyy in the DB instead of dd/MM/yyyy 01:00:00, but
	// sets the Date one day behind the actual one, because it cuts the time
	// @Type(type = "date") // hibernate annotation
	private Date newEndDate;
	
	@Basic(optional = false)
	private double changedMonthlyPremium;
	
	
	public long getModifiedContractID() {
		return modifiedContractID;
	}

	public void setModifiedContractID(long modifiedContractID) {
		this.modifiedContractID = modifiedContractID;
	}

	
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	
	public double getChangedMonthlyPremium(){
		return changedMonthlyPremium;
	}
	
	public void setChangedMonthlyPremium(double changedMonthlyPremium){
		this.changedMonthlyPremium = changedMonthlyPremium;
	}
	
	public String getTestName(){
		return testName;
	}
	
	public void setTestName(String newTestName){
		this.testName = newTestName;
	}
	
	public Date getNewEndDate() {
		return newEndDate;
	}

	public void setNewEndDate(Date newEndDate) {
		this.newEndDate = newEndDate;
	}
	
	
	@Override
	public String toString() {
		return "ContractRequest " + " [ID: " + getModifiedContractID() + "]" + " [ Contract: " + getContract() + "]"
				+ " [ChangedMonthlyPremium: " + getChangedMonthlyPremium() + "]";
	}
	
	public String toStringWithoutID() {
		return "ContractRequest " + " [ID: " + getModifiedContractID() + "]" + " [ Contract: " + getContract() + "]"
				+ " [ ChangedMonthlyPremium: " + getChangedMonthlyPremium() + "]" + "[ TestName: " + getTestName() + "]";
	}
	
	public long modifiedContractExistsInDB(List<ModifyContract> existingContracts) {
		
		if( (existingContracts == null) || (existingContracts.isEmpty()) ) {
			return -1L;
		}
		
		String newModifiedContractString = this.toStringWithoutID();
				
		for(ModifyContract c : existingContracts) {
						
			String temp = c.toStringWithoutID();
			
			if(newModifiedContractString.equals(temp)) {
				return c.getModifiedContractID();
			}
		}
		
		return -1L;
	}



}
