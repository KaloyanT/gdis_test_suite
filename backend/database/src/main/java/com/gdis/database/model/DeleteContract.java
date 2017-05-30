package com.gdis.database.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "DeleteContract")

public class DeleteContract{
    	@Id
	private long id;
	
	@Basic(optional = false)
	private ProductType productType = null;
	
	@Basic(optional = false)
	@OneToOne
	private Customer customer = null;
	
	@Basic(optional = false)
	private Date contractBegin = null;

	public long getId() {
		return id;
	}
    
    public void removeId(){
        
    }
    
    public void removeCustomer(){
        
    }
    
    public void removeProductType(){
        
    }
    
    public void removeContractBegin(){
        
    }
}