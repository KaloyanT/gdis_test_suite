package com.gdis.database.service;

import java.util.Date;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.gdis.database.model.Product;
import com.gdis.database.model.ProductType;

public interface ProductRepository extends CrudRepository <Product, Long> {
	
	List<Product> findByName(String name);
	
	public Product findByProductID(long productID);
	
	List<Product> findByNameAndProductBeginAndProductEndAndProductType(String name, Date productBegin,
			Date productEnd, ProductType type);
}
