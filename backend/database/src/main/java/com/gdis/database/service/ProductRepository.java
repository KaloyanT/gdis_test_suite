package com.gdis.database.service;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.gdis.database.model.Product;

public interface ProductRepository extends CrudRepository <Product, Long> {
	
	List<Product> findByName(String name);
	
	public Product findByProductID(long productID);
}
