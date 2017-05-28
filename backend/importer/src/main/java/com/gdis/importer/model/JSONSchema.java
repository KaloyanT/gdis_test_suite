package com.gdis.importer.model;

import java.util.HashMap;
import java.util.Map;

public class JSONSchema {
	
	private long testCaseCustomerID;
	
	private Map<String, Object> customerData = new HashMap<String, Object>();
	
	
	public long getTestCaseCustomerID() {
		return testCaseCustomerID;
	}

	public void setTestCaseCustomerID(long testCaseCustomerID) {
		this.testCaseCustomerID = testCaseCustomerID;
	}
	
	public Map<String, Object> getCustomerData() {
		return customerData;
	}

	public void setCustomerData(Map<String, Object> customerData) {
		this.customerData = customerData;
	}
	
}
