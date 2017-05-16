package com.gdis.importer.model;

import java.util.HashMap;
import java.util.Map;

public class JSONSchema {
	
	
	private long exampleCustomerID;
	
	private Map<String, String> customerData = new HashMap<String, String>();
	
	
	public long getExampleCustomerID() {
		return exampleCustomerID;
	}

	public void setExampleCustomerID(long exampleCustomerID) {
		this.exampleCustomerID = exampleCustomerID;
	}
	
	public Map<String, String> getCustomerData() {
		return customerData;
	}

	public void setCustomerData(Map<String, String> customerData) {
		this.customerData = customerData;
	}
	
}
